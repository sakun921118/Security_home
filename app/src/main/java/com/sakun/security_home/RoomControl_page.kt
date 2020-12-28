package com.sakun.security_home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

class RoomControl_page : Fragment() {

    private lateinit var communicator: Communicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        communicator = activity as Communicator

        return inflater.inflate(R.layout.fragment_room_control_page, container, false)
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(view?.context, communicator.loginTimeout().toString(), Toast.LENGTH_SHORT).show()
    }
}