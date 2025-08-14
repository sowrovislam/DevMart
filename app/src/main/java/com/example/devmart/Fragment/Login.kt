package com.example.devmart.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.devmart.DetailsActivity
import com.example.devmart.R
import com.example.devmart.databinding.FragmentLoginBinding

import com.example.devmart.reprository.AuthRepository
import com.example.devmart.reprository.AuthViewModel
import com.example.devmart.reprository.AuthViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class Login : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: AuthViewModel
    private lateinit var auth: FirebaseAuth  // You forgot to initialize this before

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Initialize ViewModel
        val repo = AuthRepository(auth)
        viewModel = ViewModelProvider(this, AuthViewModelFactory(repo))[AuthViewModel::class.java]

        // Navigate to Signup
        binding.tvSignup.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_signup)
        }

        // Navigate to Forgot Password (if enabled)
        // binding.tvForgotPassword.setOnClickListener {
        //     findNavController().navigate(R.id.action_login_to_forgotPassword)
        // }

        // Handle login
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please enter both email and password",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            viewModel.login(email, password)
        }

        // Observe login result
        viewModel.authState.observe(viewLifecycleOwner) { state ->
            Toast.makeText(requireContext(), state, Toast.LENGTH_SHORT).show()
            if (state == "Login Success") {
                findNavController().navigate(R.id.action_login_to_accountFragment)


//                startActivity(Intent(requireContext(), DetailsActivity::class.java))
//                requireActivity().finish()  // Prevents back navigation to login
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // Auto-login if already authenticated
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {

          findNavController().navigate(R.id.action_login_to_accountFragment)

//            startActivity(Intent(requireContext(), DetailsActivity::class.java))
//            requireActivity().finish()  // Prevents back navigation to login
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            onBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finishAffinity()// Exit the app

                }
            }
        )
    }



}
