package com.example.talabalarniroyxatgaolish.ui.admin

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.talabalarniroyxatgaolish.data.AdminDataItem
import com.example.talabalarniroyxatgaolish.data.AuthDataItem
import com.example.talabalarniroyxatgaolish.databinding.FragmentEditAdminBinding
import com.example.talabalarniroyxatgaolish.vm.EditAdminVm
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import com.example.talabalarniroyxatgaolish.vm.Resource
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditAdmin : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var binding: FragmentEditAdminBinding? = null
    lateinit var liveDates: LiveDates
    lateinit var admin: AdminDataItem
    lateinit var editAdminVm: EditAdminVm
    private val TAG = "EDITADMIN"
    private var auth: AuthDataItem? = null
    private var uri: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditAdminBinding.inflate(layoutInflater)
        admin = arguments?.getParcelable("admin")!!
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class]
        editAdminVm = ViewModelProvider(requireActivity())[EditAdminVm::class]
        getAuth()
        binding?.apply {
            xonaSave.setOnClickListener {
                saveInfo(loginEt.text.toString(), parolEt.text.toString(), adminNameEt.text.toString())
            }
            rasmJoylashTv.setOnClickListener {
                checkPermissionAndOpenGallery()
            }
            yigilishImage.setOnClickListener {
                checkPermissionAndOpenGallery()
            }
        }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val selectedImageUri: Uri? = result.data?.data
                if (selectedImageUri != null) {
                    uri = selectedImageUri
                    binding!!.yigilishImage.setImageURI(selectedImageUri)
                    binding!!.rasmJoylashTv.visibility = View.GONE
                    binding!!.yigilishImage.visibility = View.VISIBLE
                } else {
                    Toast.makeText(requireContext(), "Rasm tanlashda xatolik yuz berdi!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding?.root
    }

    fun createMultipartFromUri(): MultipartBody.Part? {
        try {
            val fileName = requireContext().contentResolver.query(uri!!, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndexOrThrow("_display_name")
                cursor.moveToFirst()
                cursor.getString(nameIndex)
            } ?: "temp_file"
            val tempFile = File(requireContext().cacheDir, fileName)
            val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri!!)
            val outputStream = FileOutputStream(tempFile)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), tempFile)
            return MultipartBody.Part.createFormData("image", tempFile.name, requestBody)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(requireContext(), "Ruxsat berildi!", Toast.LENGTH_SHORT).show()
            openGallery()
        } else {
            Toast.makeText(requireContext(), "Ruxsat rad etildi!", Toast.LENGTH_SHORT).show()
        }
    }
    private fun checkPermissionAndOpenGallery() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        when {
            ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }
            shouldShowRequestPermissionRationale(permission) -> {
                Toast.makeText(
                    requireContext(),
                    "Ruxsat zarur! Iltimos, ruxsatga rozilik bering.",
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {
                permissionLauncher.launch(permission)
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        galleryLauncher.launch(intent)
    }

    private fun saveInfo(login: String, password: String, adminName: String) {
        if (login != "" && password != "" && adminName != "") {
            saved(login, password, adminName)
        }
    }

    private fun saved(login: String, password: String, adminName: String) {
        lifecycleScope.launch {
            if (isAdded) {
                try {
                    var imagePart = createMultipartFromUri()
                    if (imagePart == null) {
                        imagePart = null
                    }
                    val auth_id = (admin.auth_id).toString().toRequestBody("text/plain".toMediaTypeOrNull())
                    val name = adminName.toRequestBody("text/plain".toMediaTypeOrNull())
                    editAdminVm.editAdmin(requireContext(), requireActivity(), admin.id, name = name, image = imagePart, auth_id = auth_id)
                    Log.d(TAG, "saved: $admin")
                    auth!!.password = password
                    auth!!.login = login
                    editAdminVm.editAuth(requireContext(), auth!!, requireActivity())
                } catch (e: Exception) {
                    Log.d(TAG, "saved: ${e.message}")
                }
            }
        }
    }

    private fun getAuth() {
        lifecycleScope.launch {
            if (isAdded) {
                try {
                    editAdminVm.getLogins(requireContext(), admin.auth_id)
                    editAdminVm._logins.collect {
                        when (it) {
                            is Resource.Error -> {
                                Toast.makeText(requireContext(), "${it.e.message}", Toast.LENGTH_SHORT).show()
                            }
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                auth = it.data
                                if (admin.image_url != null) {
                                    binding!!.rasmJoylashTv.visibility = View.GONE
                                    Glide.with(requireContext())
                                        .load(admin.image_url)
                                        .into(binding!!.yigilishImage)
                                } else {
                                    binding!!.rasmJoylashTv.visibility = View.VISIBLE
                                }
                                binding!!.adminNameEt.setText(admin.name)
                                binding!!.loginEt.setText(it.data.login)
                                binding!!.parolEt.setText(it.data.password)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "getAuth: ${e.message}")
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditAdmin().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}