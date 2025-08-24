package com.example.devmart.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devmart.DetailsActivity
import com.example.devmart.R
import com.example.devmart.databinding.FragmentAccountBinding
import com.example.devmart.getuserdata.UserAdapter
import com.example.devmart.getuserdata.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val viewModel: UserViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize views
        recyclerView = binding.recyclerView
        progressBar = binding.progressBar // Ensure progressBar is in fragment_account.xml
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Fetch data for the logged-in user
        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            progressBar.visibility = View.VISIBLE
            viewModel.fetchUserData(userId)
        } else {
            progressBar.visibility = View.GONE
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            // Optionally navigate to a login screen
            // findNavController().navigate(R.id.action_accountFragment_to_loginFragment)
        }

        // Observe ViewModel data
        viewModel.userData.observe(viewLifecycleOwner) { response ->
            progressBar.visibility = View.GONE
            if (response.success == true && response.data != null) {
                recyclerView.adapter = UserAdapter(response.data.filterNotNull())
            } else {
                Toast.makeText(
                    requireContext(),
                    response.message ?: "Error fetching data",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Navigation
        binding.profileImage.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_profileFragment)
        }

        binding.fabAddPeople.setOnClickListener {
            startActivity(Intent(requireActivity(), DetailsActivity::class.java))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle back press to exit the app
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finishAffinity() // Exit the app
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }
}