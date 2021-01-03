package com.sakun.security_home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

class Setting_page : Fragment() {

    private lateinit var communicator: Communicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        communicator = activity as Communicator

        return inflater.inflate(R.layout.fragment_setting_page, container, false)
    }

    override fun onResume() {
        super.onResume()
    }
}