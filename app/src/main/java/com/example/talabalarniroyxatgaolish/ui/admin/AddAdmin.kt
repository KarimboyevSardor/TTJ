package com.example.talabalarniroyxatgaolish.ui.admin

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.talabalarniroyxatgaolish.R
import com.example.talabalarniroyxatgaolish.databinding.FragmentAdminQoshishAdminBinding
import com.example.talabalarniroyxatgaolish.vm.AddAdminVm
import com.example.talabalarniroyxatgaolish.vm.LiveDates
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

class AddAdmin : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var binding: FragmentAdminQoshishAdminBinding? = null
    lateinit var liveDates: LiveDates
    lateinit var addAdminVm: AddAdminVm
    private val TAG = "ADDADMIN"
    lateinit var course_count: MutableList<String>
    private var uri: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminQoshishAdminBinding.inflate(layoutInflater)
        addAdminVm = ViewModelProvider(requireActivity())[AddAdminVm::class.java]
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class.java]

        binding?.apply {
            val activity: AppCompatActivity = requireActivity() as AppCompatActivity
            activity.setSupportActionBar(toolbar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            val backIcon = toolbar.navigationIcon
            backIcon?.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            toolbar.navigationIcon = backIcon
            toolbar.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
            addAdmin.setOnClickListener {
                checkInfo(adminNameEt.text.toString(), loginEt.text.toString(), parolEt.text.toString())
            }
            addAdmin.setOnClickListener {
                checkInfo(adminNameEt.text.toString(), loginEt.text.toString(), parolEt.text.toString())
            }
            rasmJoylashTv.setOnClickListener {
                openGallery()
            }
        }
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val selectedImageUri: Uri? = result.data?.data
                if (selectedImageUri != null) {
                    uri = selectedImageUri
                    binding!!.yigilishImage.setImageURI(uri)
                    binding!!.rasmJoylashTv.visibility = View.GONE
                    binding!!.yigilishImage.visibility = View.VISIBLE
                } else {
                    Toast.makeText(requireContext(), "Rasm tanlashda xatolik yuz berdi!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return binding?.root
    }

    private fun createMultipartFromUri(): MultipartBody.Part? {
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

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        galleryLauncher.launch(intent)
    }

    private fun checkInfo(adminName: String, login: String, parol: String) {
        if (adminName.isNotEmpty() && login.isNotEmpty() && parol.isNotEmpty() && uri != null) {
            savedInfo(adminName, login, parol)
        }
    }

    private fun savedInfo(adminName: String, login: String, parol: String) {
        lifecycleScope.launch {
            if (isAdded) {
                try {
                    var imagePart = createMultipartFromUri()
                    if (imagePart == null) {
                        imagePart = null
                    }
                    val adminName = adminName.toRequestBody("text/plain".toMediaTypeOrNull())
                    val login = login.toRequestBody("text/plain".toMediaTypeOrNull())
                    val parol = parol.toRequestBody("text/plain".toMediaTypeOrNull())
                    val role = "admin".toRequestBody("text/plain".toMediaTypeOrNull())
                    addAdminVm.addAdmin(requireContext(), requireActivity(), name = adminName, login = login, password = parol, role = role, image = imagePart)
                } catch (e: Exception) {
                    Log.d(TAG, "savedInfo: ${e.message}")
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddAdmin().apply {
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