package com.example.talabalarniroyxatgaolish.ui.admin

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
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
import com.example.talabalarniroyxatgaolish.adapter.StudentBiriktirishAdapter
import com.example.talabalarniroyxatgaolish.adapter.TadbirStudentAdapter
import com.example.talabalarniroyxatgaolish.data.AddRateReq
import com.example.talabalarniroyxatgaolish.data.AddedRate
import com.example.talabalarniroyxatgaolish.data.StudentDataItem
import com.example.talabalarniroyxatgaolish.databinding.FragmentStudentUpdateRoomBottomSheetDialogAdminBinding
import com.example.talabalarniroyxatgaolish.databinding.FragmentYigilishQoshishAdminBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.addedTadbirStudentList
import com.example.talabalarniroyxatgaolish.utils.Utils.studentlarList
import com.example.talabalarniroyxatgaolish.vm.AddYigilishAdminVm
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import com.example.talabalarniroyxatgaolish.vm.Resource
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Yigilish_qoshish : Fragment(), View.OnClickListener {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private val TAG = "YIGILISHQOSHISHADMIN"
    private lateinit var addYigilishAdminVm: AddYigilishAdminVm
    private var uri: Uri? = null
    private var binding: FragmentYigilishQoshishAdminBinding? = null
    private lateinit var studentBiriktirishAdapter: StudentBiriktirishAdapter
    private lateinit var liveDates: LiveDates
    private lateinit var tadbirStudentAdapter: TadbirStudentAdapter
    var addTadbirStudents: MutableList<StudentDataItem> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentYigilishQoshishAdminBinding.inflate(layoutInflater)
        addYigilishAdminVm = ViewModelProvider(requireActivity())[AddYigilishAdminVm::class]
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class]
        addTadbirStudents = mutableListOf()
        liveDates.getAddedTadbirStudent().observe(requireActivity()) {
            if (it.isNotEmpty()) {
                binding!!.tadbirTv.visibility = View.VISIBLE
            } else {
                binding!!.tadbirTv.visibility = View.GONE
            }
        }
        liveDates.addedTadbirStudentLiveData.value = addedTadbirStudentList
        
        tadbirStudentAdapter = TadbirStudentAdapter(addedTadbirStudentList) { student ->
            addedTadbirStudentList.remove(student)
            addTadbirStudents.add(student)
            liveDates.addedTadbirStudentLiveData.value = addedTadbirStudentList
            liveDates.addTadbirStudentLiveData.value = addTadbirStudents
        }
        liveDates.getAddedTadbirStudent().observe(requireActivity()) {
            tadbirStudentAdapter.filter(it)
        }
        binding!!.apply {
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
            rasmJoylashTv.setOnClickListener {
                openGallery()
            }
            dateTv.setOnClickListener {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
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
            addStudent.setOnClickListener {
                showBottomSheetDialog()
            }
            for (i in 0 until chipgroup.childCount) {
                val chip = chipgroup.getChildAt(i) as Chip
                chip.setOnCloseIconClickListener {
                    chipgroup.removeView(chip)
                }
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
        return binding!!.root
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val bottomSheetDialogBinding = FragmentStudentUpdateRoomBottomSheetDialogAdminBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomSheetDialogBinding.root)
        bottomSheetDialogBinding.apply {
            addTadbirStudents = studentlarList
            liveDates.addTadbirStudentLiveData.value = addTadbirStudents
            studentBiriktirishAdapter = StudentBiriktirishAdapter(addTadbirStudents) { student ->
                addedTadbirStudentList.add(student)
                addTadbirStudents.remove(student)
                liveDates.addTadbirStudentLiveData.value = addTadbirStudents
                liveDates.addedTadbirStudentLiveData.value = addedTadbirStudentList
                val chip = Chip(requireContext())
                chip.text = student.name
                chip.tag = student.id
                chip.isCloseIconEnabled = true
                chip.setOnClickListener(this@Yigilish_qoshish)
                chip.setOnCloseIconClickListener {
                    binding!!.chipgroup.removeView(chip)
                    addTadbirStudents.add(addedTadbirStudentList.filter { it.id == chip.tag.toString().toLong() }[0])
                    liveDates.addTadbirStudentLiveData.value = addTadbirStudents
                    addedTadbirStudentList.remove(addedTadbirStudentList.filter { it.id == chip.tag.toString().toLong() }[0])
                    liveDates.addedTadbirStudentLiveData.value = addedTadbirStudentList
                }
                binding!!.chipgroup.addView(chip)
            }
            liveDates.getAddTadbirStudent().observe(requireActivity()) {
                studentBiriktirishAdapter.filter(it)
            }
            studentBiriktirishAdapter.filter(addTadbirStudents)
            studentRoomRv.adapter = studentBiriktirishAdapter
            studentSearch.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchStudent(query!!)
                    return false
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    searchStudent(newText!!)
                    return false
                }
            })
        }
        bottomSheetDialog.show()
    }

    private fun searchStudent(query: String) {
        var students: MutableList<StudentDataItem> = mutableListOf()
        for (i in 0 until studentlarList.size) {
            if (studentlarList[i].name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                students.add(studentlarList[i])
            }
        }
        studentBiriktirishAdapter.filter(students)
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


    private fun saveYigilish(yigilishName: String, yigilishMaqsadi: String, date: String, time: String, yigilishJoyi: String) {
        var imagePart = createMultipartFromUri()
        if (imagePart == null) {
            imagePart = null
        }
        val name = yigilishName.toRequestBody("text/plain".toMediaTypeOrNull())
        val time = "$date $time".toRequestBody("text/plain".toMediaTypeOrNull())
        val description = yigilishMaqsadi.toRequestBody("text/plain".toMediaTypeOrNull())
        val meetingPlace = yigilishJoyi.toRequestBody("text/plain".toMediaTypeOrNull())
        lifecycleScope.launch {
            if (isAdded) {
                addYigilishAdminVm.addYigilish(
                    time = time,
                    name = name,
                    description = description,
                    meetingPlace = meetingPlace,
                    image = imagePart,
                    context = requireContext()
                )
                addYigilishAdminVm._stateAddYigilish.collect{
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
                            val rateStudents: MutableList<AddedRate> = mutableListOf()
                            for (i in 0 until addedTadbirStudentList.size) {
                                rateStudents.add(AddedRate(it.data.meeting.id, "0", addedTadbirStudentList[i].id))
                            }
                            val addRate = AddRateReq(rateStudents)
                            addYigilishAdminVm.addRate(requireContext(), addRate)
                            requireActivity().onBackPressed()
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Yigilish_qoshish().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick(view: View) {
        if (view is Chip) {
            Toast.makeText(requireContext(), view.text, Toast.LENGTH_SHORT).show()
        }
    }
}
