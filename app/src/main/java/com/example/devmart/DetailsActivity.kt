package com.example.devmart

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.devmart.databinding.ActivityDetailsBinding

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database


class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private var selectedImageUri: Uri? = null

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedImageUri = result.data?.data
                selectedImageUri?.let {
                    binding.ivPreview.setImageURI(it)
                }
            }
        }

    private val pickContact =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { contactUri ->
                    contentResolver.query(
                        contactUri,
                        arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                        null, null, null
                    )?.use { cursor ->
                        if (cursor.moveToFirst()) {
                            val numberIndex =
                                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            val phoneNumber = cursor.getString(numberIndex)
                            binding.etNumber.setText(phoneNumber)
                        }
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth and Database
        auth = Firebase.auth
        database = Firebase.database.reference

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupClickListeners()
        checkAuthentication()
    }

    private fun checkAuthentication() {
        if (auth.currentUser == null) {
            // Sign in anonymously or redirect to login
            auth.signInAnonymously().addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                    Log.e("Auth", "Authentication failed", task.exception)
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            pickImage.launch(intent)
        }

        binding.btnSelectContact.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            }
            pickContact.launch(intent)
        }

        binding.btnUpload.setOnClickListener {



        }
            



        }
    private fun clearFields() {
        binding.etName.text.clear()
        binding.etNumber.text.clear()
        binding.etAmount.text.clear()
        binding.ivPreview.setImageURI(null)
        selectedImageUri = null
    }
}