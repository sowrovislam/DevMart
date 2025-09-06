package com.example.devmart.Fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.SearchView
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
    private var progressDialog: Dialog? = null
    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 🔹 Initialize adapter once with empty list
        adapter = UserAdapter(mutableListOf())
        recyclerView.adapter = adapter
        adapter.reverseItems()
        recyclerView.scrollToPosition(0)

        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            showProgressDialog()
            viewModel.fetchUserData(userId)
        } else {
            hideProgressDialog()
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
        }






        // 🔹 Observe LiveData
        viewModel.userData.observe(viewLifecycleOwner) { response ->
            hideProgressDialog()
            if (response.success == true && response.data != null) {
                adapter.updateData(response.data.filterNotNull())

                val total = adapter.getTotalAmount()
                binding.balanceAmount.text = "${String.format("%.2f", total)}"

                val totaldue = adapter.getTotalAmountdue()
                binding.dueAmount.text = "${String.format("%.2f", totaldue)}"

            } else {
                Toast.makeText(
                    requireContext(),
                    response.message ?: "Error fetching data",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // 🔹 SearchView filter
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText.orEmpty())
                return true
            }
        })

        // 🔹 Navigate to Profile
        binding.profileImage.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_profileFragment)
        }

        // 🔹 Open DetailsActivity to add new user
        binding.fabAddPeople.setOnClickListener {
            startActivity(Intent(requireActivity(), DetailsActivity::class.java))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finishAffinity()
                }
            }
        )
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showProgressDialog() {

        if (progressDialog == null) {
            progressDialog = Dialog(requireContext())
            progressDialog?.setContentView(R.layout.progress_dialog_recycilerview)
            progressDialog?.setCancelable(false) // User can’t dismiss by back press
            progressDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
        progressDialog?.show()
    }

    private fun hideProgressDialog() {
        progressDialog?.dismiss()
    }



}
