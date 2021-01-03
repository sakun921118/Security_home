package com.sakun.security_home

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.navigation.Navigation
import com.sakun.security_home.databinding.FragmentMainPageBinding
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class Main_page : Fragment(), View.OnClickListener {

    private lateinit var userLocalData: UserLocalData
    private lateinit var communicator: Communicator

    private var _binding: FragmentMainPageBinding? = null
    private val binding get() = _binding!!

    private var closeStatus = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainPageBinding.inflate(inflater, container, false)
        val view = binding.root
        communicator = activity as Communicator

        communicator.actionBarShow()

        userLocalData = communicator.getUserLocalData()!!

        binding.aboutUsBtn.setOnClickListener(this)
        binding.doorLockBtn.setOnClickListener(this)
        binding.powerUsedBtn.setOnClickListener(this)
        binding.roomControlBtn.setOnClickListener(this)
        binding.settingBtn.setOnClickListener(this)

        requireActivity().onBackPressedDispatcher.addCallback(this){
            closeActivity()
        }

        return view
    }

    override fun onClick(v: View?) {
        val fragment = when(v?.id){
            R.id.aboutUs_btn -> R.id.action_main_page_to_aboutUs_page
            R.id.powerUsed_btn -> R.id.action_main_page_to_powerUsed_page
            R.id.roomControl_btn -> R.id.action_main_page_to_roomControl_page
            R.id.setting_btn -> R.id.action_main_page_to_setting_page
            R.id.doorLock_btn ->{
                // Lock door
                null
            }
            else -> null
        }
        if(fragment != null){
            view.let {
                if (it != null) {
                    Navigation.findNavController(it).navigate(fragment)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(communicator.loginTimeout()){
            view.let {
                if(it != null){
                    Navigation.findNavController(it).navigate(R.id.action_main_page_to_fastLogin_page)
                }
            }
        }
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
}