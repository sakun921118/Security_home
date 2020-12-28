package com.sakun.security_home

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import com.google.gson.Gson
import com.sakun.security_home.databinding.ActivityMainBinding
import java.lang.Exception

class MainActivity : AppCompatActivity(), Communicator {

    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(getUserLocalData() == null) {
            updateUserLocalData(
                UserLocalData(
                "sakun", "921118", false,
                false, 0, 15000, "", "")
            )
        }

        getBiometricPermission()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            val userLocalData = getUserLocalData()
            if (userLocalData != null) {
                userLocalData.appPauseTime = System.currentTimeMillis()
                updateUserLocalData(userLocalData)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun getBiometricPermission(){
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                messageBox("Need Permission!", "This app need some important permission, please open that.")
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                }
                startActivityForResult(enrollIntent, 87)
            }
        }
    }

    private fun messageBox(title: String, message: String){
        val alertDialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setIcon(R.mipmap.ic_launcher)
            .setPositiveButton("OK"){ _, _ -> }
        alertDialog.show()
    }

    override fun getUserLocalData(): UserLocalData? {
        return try {
            val sharedPref = getPreferences(Context.MODE_PRIVATE)
            val json = sharedPref.getString("UserLocalData", "")
            Gson().fromJson(json, UserLocalData::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override fun loginTimeout(): Boolean {

        val userLocalData = getUserLocalData()!!
        val resumeTime = System.currentTimeMillis();
        return if(userLocalData.appPauseTime != "0".toLong()) {
            resumeTime - userLocalData.appPauseTime > userLocalData.loginTimeout
        } else{
            false
        }
    }

    override fun updateUserLocalData(userLocalData: UserLocalData) {
        val prefsEditor = getPreferences(Context.MODE_PRIVATE).edit()
        val json = Gson().toJson(userLocalData)
        prefsEditor.putString("UserLocalData", json)
        prefsEditor.apply()
    }

    override fun resetAppTimeout() {
        val userLocalData = getUserLocalData()
        if(userLocalData != null) {
            userLocalData.appPauseTime = 0
            updateUserLocalData(userLocalData)
        }
    }
}