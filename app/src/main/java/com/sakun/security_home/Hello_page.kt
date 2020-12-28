package com.sakun.security_home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

class Hello_page : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hello_page, container, false)

        Handler(Looper.getMainLooper()).postDelayed({
            view?.let { Navigation.findNavController(it).navigate(R.id.action_hello_page_to_fastLogin_page) }
        }, 1500)

        return view
    }
}