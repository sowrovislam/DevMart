package com.example.devmart.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.devmart.R
import com.example.devmart.databinding.FragmentSignupBinding


import com.example.devmart.reprository.AuthRepository
import com.example.devmart.reprository.AuthViewModel
import com.example.devmart.reprository.AuthViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class Signup : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)

        // Initialize ViewModel with FirebaseAuth
        val repo = AuthRepository(FirebaseAuth.getInstance())
        viewModel = ViewModelProvider(this, AuthViewModelFactory(repo))[AuthViewModel::class.java]

        // Click on "Already have account? Login"
        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signup_to_login)
        }

        // Click on SignUp button
        binding.btnSignup.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirm = binding.etConfirmPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirm) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.signup(email, password)
            findNavController().navigate(R.id.action_signup_to_login)
        }

        // Observe result
        viewModel.authState.observe(viewLifecycleOwner) { state ->
            Toast.makeText(requireContext(), state, Toast.LENGTH_SHORT).show()

            if (state == "Signup Success") {
                // Navigate to Login fragment after successful signup
                findNavController().navigate(R.id.action_signup_to_login)
            }
        }

        return binding.root
    }
}
