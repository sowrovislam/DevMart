package com.example.devmart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.devmart.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Show user info
        val user = auth.currentUser
        if (user != null) {
            binding.tvEmail.text = user.email  // Assuming you have a TextView with id = tvEmail
        } else {
            binding.tvEmail.text = "No user logged in"
        }




        // Logout
        binding.btnLogout.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_profileFragment_to_login)
        }

        return binding.root
    }
}
