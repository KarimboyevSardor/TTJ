package com.example.talabalarniroyxatgaolish.ui.admin

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
import com.example.talabalarniroyxatgaolish.data.YigilishlarDataItem
import com.example.talabalarniroyxatgaolish.databinding.FragmentYigilishlarYangilashOchirishAdminBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.yigilishlarList
import com.example.talabalarniroyxatgaolish.vm.Resource
import com.example.talabalarniroyxatgaolish.vm.YigilishlarniYangilashOchirishAdminVm
import com.squareup.picasso.Picasso
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

class YigilishlarYangilashOchirish : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var uri: Uri? = null
    private var yigilishDataId: Long = 0
    private lateinit var yigilishData: YigilishlarDataItem
    lateinit var yigilishlarniYangilashOchirishAdminVm: YigilishlarniYangilashOchirishAdminVm
    private var binding: FragmentYigilishlarYangilashOchirishAdminBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentYigilishlarYangilashOchirishAdminBinding.inflate(layoutInflater)
        yigilishlarniYangilashOchirishAdminVm = ViewModelProvider(requireActivity())[YigilishlarniYangilashOchirishAdminVm::class]

        yigilishDataId = requireArguments().getLong("yigilish")
        yigilishData = yigilishlarList.filter { it.id == yigilishDataId }[0]
        Log.d("TAG", "onCreateView: $yigilishData")

        binding!!.apply {
            if (yigilishData.image_base64 != null) {
                Picasso.get().load(yigilishData.image_base64).into(yigilishImage)
            } else {
                rasmJoylashTv.visibility = View.VISIBLE
            }
            yigilishNameEt.setText(yigilishData.name)
            yigilishMaqsadEt.setText(yigilishData.description)
            yigilishJoyiEt.setText(yigilishData.meeting_place)
            val date = yigilishData.time.split(" ")
            timeTv.text = date[1]
            dateTv.text = date[0]
            setHasOptionsMenu(true)
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            val backIcon = toolbar.navigationIcon
            backIcon?.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            toolbar.navigationIcon = backIcon
            toolbar.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
            yigilishSave.setOnClickListener {
                if (isCheck(yigilishNameEt.text.toString(), yigilishMaqsadEt.text.toString(), yigilishJoyiEt.text.toString(),  dateTv.text.toString(), timeTv.text.toString())) {
                    saveYigilish(yigilishNameEt.text.toString(), yigilishMaqsadEt.text.toString(), dateTv.text.toString(), timeTv.text.toString(), yigilishJoyiEt.text.toString())
                }
            }
            yigilishImage.setOnClickListener {
                openGallery()
            }
            rasmJoylashTv.setOnClickListener {
                openGallery()
            }
            dateTv.setOnClickListener {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                    // Foydalanuvchi tanlagan sanani ko'rsatamiz
                    val date = "$selectedDay-${selectedMonth + 1}-$selectedYear"
                    dateTv.text = date
                }, year, month, day)

                datePickerDialog.show()
            }
            timeTv.setOnClickListener {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                    val time = String.format("%02d:%02d", selectedHour, selectedMinute)
                    timeTv.text = time
                }, hour, minute, true)
                timePickerDialog.show()
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

        return binding!!.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.ochirish_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.yigilish_ochirish -> {
                deleteYigilish(yigilishData.id)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteYigilish(id: Long) {
        lifecycleScope.launch {
            try {
                if (isAdded) {
                    yigilishlarniYangilashOchirishAdminVm.deleteYigilish(id = id, requireContext())
                    yigilishlarniYangilashOchirishAdminVm._stateDeleteYigilish.collect{
                        when (it) {
                            is Resource.Error -> {
                                Toast.makeText(requireContext(), "Server bilan bog'lanib bo'lmadi.", Toast.LENGTH_SHORT).show()
                            }
                            is Resource.Loading -> {

                            }
                            is Resource.Success -> {
                                Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
                                requireActivity().supportFragmentManager.popBackStack()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    private fun base64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            YigilishlarYangilashOchirish().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun isCheck(yigilishName: String, yigilishMaqsadi: String, yigilishJoyi: String, date: String, time: String): Boolean {
        var isCheck = false
        if (yigilishMaqsadi.isNotEmpty() && yigilishName.isNotEmpty() && date != "kun/oy/yil" && time != "soat:minut" && yigilishJoyi.isNotEmpty()) {
            isCheck = true
        } else {
            Toast.makeText(requireContext(), "Iltimos maydonlarni to'ldiring.", Toast.LENGTH_SHORT).show()
        }
        return isCheck
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

    private fun saveYigilish(yigilishName: String, yigilishMaqsadi: String, date: String, time: String, yigilishJoyi: String) {
        var imagePart = createMultipartFromUri()
        if (imagePart == null) {
            imagePart = null
        }
        val name = yigilishName.toRequestBody("text/plain".toMediaTypeOrNull())
        val time = ("$date $time").toRequestBody("text/plain".toMediaTypeOrNull())
        val description = yigilishMaqsadi.toRequestBody("text/plain".toMediaTypeOrNull())
        val meetingPlace = yigilishJoyi.toRequestBody("text/plain".toMediaTypeOrNull())
        lifecycleScope.launch {
            if (isAdded) {
                yigilishlarniYangilashOchirishAdminVm.editYigilish(
                    id = yigilishData.id,
                    name = name,
                    time = time,
                    description = description,
                    meeting_place = meetingPlace,
                    image = imagePart,
                    context = requireContext()
                )
                yigilishlarniYangilashOchirishAdminVm._stateYigilish.collect{
                    when (it) {
                        is Resource.Error -> {
                            Toast.makeText(
                                requireContext(),
                                "Ma'lumotni saqlab bo'lmadi.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is Resource.Loading -> {

                        }
                        is Resource.Success -> {
                            Toast.makeText(
                                requireContext(),
                                "Ma'lumot saqlandi.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
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
                // Agar ruxsat berilgan bo'lsa, galereyani oching
                openGallery()
            }
            shouldShowRequestPermissionRationale(permission) -> {
                // Agar foydalanuvchi ruxsatni rad etsa
                Toast.makeText(
                    requireContext(),
                    "Ruxsat zarur! Iltimos, ruxsatga rozilik bering.",
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {
                // Ruxsatni so'rang
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}