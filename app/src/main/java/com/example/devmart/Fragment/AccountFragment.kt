package com.example.devmart.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.devmart.DetailsActivity
import com.example.devmart.R
import com.example.devmart.databinding.FragmentAccountBinding
import com.example.devmart.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth


class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAccountBinding.inflate(layoutInflater)
//
//        binding.btnLogout.setOnClickListener {
//            FirebaseAuth.getInstance().signOut()
//            findNavController().navigate(R.id.action_accountFragment_to_login)
//        }
//


        binding.profileImage.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_profileFragment)
        }

        binding.fabAddPeople.setOnClickListener {

            startActivity(Intent(requireActivity(), DetailsActivity::class.java))


        }


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finishAffinity()// Exit the app

                }
            }
        )
    }


}