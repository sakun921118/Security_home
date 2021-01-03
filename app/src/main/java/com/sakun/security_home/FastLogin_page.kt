package com.sakun.security_home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.sakun.security_home.databinding.FragmentFastLoginPageBinding
import java.util.concurrent.Executor

class FastLogin_page : Fragment(), View.OnClickListener {

    private lateinit var passwordImg: Array<ImageView>
    private lateinit var communicator: Communicator
    private lateinit var userLocalData: UserLocalData
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private var _binding: FragmentFastLoginPageBinding? = null
    private val binding get() = _binding!!

    private var closeStatus = false
    private var password = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFastLoginPageBinding.inflate(inflater, container, false)
        val view = binding.root

        communicator = activity as Communicator
        communicator.actionBarHide()
        passwordImg = arrayOf(binding.num1Img, binding.num2Img, binding.num3Img, binding.num4Img, binding.num5Img, binding.num6Img)

        userLocalData = communicator.getUserLocalData()!!

        binding.num0Btn.setOnClickListener(this)
        binding.num1Btn.setOnClickListener(this)
        binding.num2Btn.setOnClickListener(this)
        binding.num3Btn.setOnClickListener(this)
        binding.num4Btn.setOnClickListener(this)
        binding.num5Btn.setOnClickListener(this)
        binding.num6Btn.setOnClickListener(this)
        binding.num7Btn.setOnClickListener(this)
        binding.num8Btn.setOnClickListener(this)
        binding.num9Btn.setOnClickListener(this)
        binding.num0Btn.setOnClickListener(this)
        binding.numBackBtn.setOnClickListener(this)
        binding.biometricBtn.setOnClickListener(this)

        executor = ContextCompat.getMainExecutor(view.context)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)

                    if(userLocalData.fastLoginAcPassword == password){
                        messageBox("Use biometric login?", "Did you want to use biometric login?")
                    }
                    else if(errorCode != 10 && errorCode != 13) {
                        Toast.makeText(view.context, "$errorCode $errString", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)

                    if(!userLocalData.biometricAsk){
                        userLocalData.biometricAsk = true
                        userLocalData.biometricStatus = true
                        communicator.updateUserLocalData(userLocalData)
                    }

                    communicator.resetAppTimeout()
                    changeFragment()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }
            })

        if(userLocalData.biometricStatus) {
            useBiometricLogin()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this){
            closeActivity()
        }

        return view
    }

    private fun changeFragment() {
        view.let {
            if (it != null) {
                Toast.makeText(view?.context, "Hi ${userLocalData.userName}!", Toast.LENGTH_LONG).show()
                Navigation.findNavController(it)
                    .navigate(R.id.action_fastLogin_page_to_main_page)
            }
        }
    }

    private fun useBiometricLogin() {
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for Security_home")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()
        biometricPrompt.authenticate(promptInfo)
    }

    private fun closeActivity(){
        if(closeStatus){
            activity?.finish()
        }
        else{
            closeStatus = true
            Handler(Looper.getMainLooper()).postDelayed({
                closeStatus = false
            }, 1000)
            Toast.makeText(view?.context, "Press one more time to close!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.numBack_btn -> passwordBack()
                R.id.biometric_btn -> useBiometricLogin()
                else -> {
                    val btn = v as Button
                    passwordAdd(btn.text.toString())
                }
            }
        }
    }

    private fun passwordAdd(num: String) {
        if(num.length == 1 && password.length != 6){
            passwordImg[password.length].setImageResource(R.drawable.sample_picture)
            password += num
        }
        if(password.length == 6){
            login()
        }
    }

    private fun login() {
        if(password == userLocalData.fastLoginAcPassword){
            if(!userLocalData.biometricAsk){
                messageBox("Use biometric login?", "Did you want to use biometric login?")
            }
            else {
                communicator.resetAppTimeout()
                changeFragment()
            }
        }
        else{
            Toast.makeText(view?.context, "Please Enter Correct Password", Toast.LENGTH_LONG).show()
            passwordClean()
        }
    }

    private fun passwordClean() {
        for (img in passwordImg) {
            img.setImageResource(R.drawable.ic_launcher_background)
        }
        password = ""
    }

    private fun passwordBack() {
        if(password.isNotEmpty()) {
            passwordImg[password.length-1].setImageResource(R.drawable.ic_launcher_background)
            password = password.dropLast(1)
        }
    }

    private fun messageBox(title: String, message: String){
        val alertDialog = view?.let {
            AlertDialog.Builder(it.context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.mipmap.ic_launcher)
                .setNegativeButton("No"){ _, _ ->
                    if (title == "Use biometric login?"){
                        userLocalData.biometricAsk = true
                        communicator.updateUserLocalData(userLocalData)
                        changeFragment()
                    }
                }
                .setPositiveButton("Yes"){ _, _ ->
                    if (title == "Use biometric login?"){
                        useBiometricLogin()
                    }
                }
        }
        alertDialog?.show()
    }
}