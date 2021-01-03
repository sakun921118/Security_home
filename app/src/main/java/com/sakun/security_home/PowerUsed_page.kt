package com.sakun.security_home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

class PowerUsed_page : Fragment() {

    private lateinit var communicator: Communicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        communicator = activity as Communicator
        return inflater.inflate(R.layout.fragment_power_used_page, container, false)
    }

    override fun onResume() {
        super.onResume()
    }
}