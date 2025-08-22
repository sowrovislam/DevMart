package com.example.devmart

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.devmart.userdata.RetrofitClient
import com.example.devmart.userdata.UploadResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DetailsActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var etName: EditText
    private lateinit var etNumber: EditText
    private lateinit var etAmount: EditText
    private lateinit var etDescription: EditText
    private var selectedImageUri: Uri? = null
    private val REQUEST_STORAGE_PERMISSION = 100

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            selectedImageUri = result.data?.data
            selectedImageUri?.let { uri ->
                imageView.setImageURI(uri)
                Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_details)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }




        imageView = findViewById(R.id.profile_image)
        etName = findViewById(R.id.etName)
        etNumber = findViewById(R.id.etNumber)
        etAmount = findViewById(R.id.etAmount)
        etDescription = findViewById(R.id.description)
        val btnChooseImage = findViewById<Button>(R.id.btnSelectImage)
        val btnUpload = findViewById<Button>(R.id.btnUpload)

        btnChooseImage.setOnClickListener {
            checkStoragePermission()
        }

        btnUpload.setOnClickListener {
            uploadData()
        }
    }

    private fun checkStoragePermission() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Requesting permission to access images", Toast.LENGTH_SHORT).show()
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_MEDIA_IMAGES), REQUEST_STORAGE_PERMISSION)
                } else {
                    openImagePicker()
                }
            }
            else -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Requesting permission to access storage", Toast.LENGTH_SHORT).show()
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSION)
                } else {
                    openImagePicker()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_STORAGE_PERMISSION && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker()
            } else {
                val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    Toast.makeText(this, "Please enable image access in app settings", Toast.LENGTH_LONG).show()
                    val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:$packageName")
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Permission denied. Cannot access images.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openImagePicker() {
        try {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageLauncher.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "No app available to pick images", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadData() {
        val name = etName.text.toString().trim()
        val number = etNumber.text.toString().trim()
        val amount = etAmount.text.toString().trim()
        val description = etDescription.text.toString().trim()

        if (name.isEmpty() || number.isEmpty() || amount.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedImageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        val file = getFileFromUri(selectedImageUri!!)
        if (file == null) {
            Toast.makeText(this, "Failed to get file", Toast.LENGTH_SHORT).show()
            return
        }

        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

        val nameRequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val numberRequestBody = number.toRequestBody("text/plain".toMediaTypeOrNull())
        val amountRequestBody = amount.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionRequestBody = description.toRequestBody("text/plain".toMediaTypeOrNull())

        val apiService = RetrofitClient.instance
        apiService.uploadData(imagePart, nameRequestBody, numberRequestBody, amountRequestBody, descriptionRequestBody)
            .enqueue(object : Callback<UploadResponse> {
                override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                    if (response.isSuccessful) {
                        val uploadResponse = response.body()
                        uploadResponse?.let {
                            Toast.makeText(this@DetailsActivity, it.message, Toast.LENGTH_LONG).show()
                            etName.text.clear()
                            etNumber.text.clear()
                            etAmount.text.clear()
                            etDescription.text.clear()
                            imageView.setImageDrawable(null)
                            selectedImageUri = null
                        }
                    } else {
                        Toast.makeText(this@DetailsActivity, "Upload failed: ${response.message()}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    Toast.makeText(this@DetailsActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun getFileFromUri(uri: Uri): File? {
        try {
            val fileName = getFileName(uri)
            val file = File(cacheDir, fileName)
            contentResolver.openInputStream(uri)?.use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            return file
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun getFileName(uri: Uri): String {
        var name = ""
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    name = cursor.getString(nameIndex)
                }
            }
        }
        return name.ifEmpty { "temp_image.jpg" }
    }
}