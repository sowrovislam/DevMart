package com.example.devmart.Fragment

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.transition.Visibility
import com.example.devmart.R
import com.example.devmart.databinding.FragmentHomeBinding

class Home : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

      binding= FragmentHomeBinding.inflate(inflater,container,false)





        binding.progressBar.visibility = View.VISIBLE
         // Navigate after 3 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_home2_to_login)
                binding.progressBar.visibility = View.GONE

            },300)





        return binding.root
    }







}