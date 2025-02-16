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
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.talabalarniroyxatgaolish.R
import com.example.talabalarniroyxatgaolish.adapter.StudentBiriktirishAdapter
import com.example.talabalarniroyxatgaolish.data.AddRateReq
import com.example.talabalarniroyxatgaolish.data.AddedRate
import com.example.talabalarniroyxatgaolish.data.Rate
import com.example.talabalarniroyxatgaolish.data.StudentDataItem
import com.example.talabalarniroyxatgaolish.data.TadbirlarDataItem
import com.example.talabalarniroyxatgaolish.databinding.FragmentStudentUpdateRoomBottomSheetDialogAdminBinding
import com.example.talabalarniroyxatgaolish.databinding.FragmentYigilishlarTadbirOchirishViewPagerAdminBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.addedTadbirStudentList
import com.example.talabalarniroyxatgaolish.utils.Utils.rateList
import com.example.talabalarniroyxatgaolish.utils.Utils.studentlarList
import com.example.talabalarniroyxatgaolish.utils.Utils.tadbirlarList
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import com.example.talabalarniroyxatgaolish.vm.Resource
import com.example.talabalarniroyxatgaolish.vm.YigilishlarniYangilashOchirishAdminVm
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.squareup.picasso.Picasso
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

class TarbirYangilashOchirishViewPager : Fragment(), View.OnClickListener {
    private var param1: Long? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getLong(ARG_PARAM1)
        }
    }

    private val TAG = "YIGILISHLARYANGILASHOCHIRISH"
    private var uri: Uri? = null
    private var yigilishDataId: Long = 0
    private lateinit var yigilishData: TadbirlarDataItem
    lateinit var yigilishlarniYangilashOchirishAdminVm: YigilishlarniYangilashOchirishAdminVm
    private var binding: FragmentYigilishlarTadbirOchirishViewPagerAdminBinding? = null
    lateinit var liveDates: LiveDates
    private var rates: MutableList<Rate> = mutableListOf()
    private var addTadbirStudents: MutableList<StudentDataItem> = mutableListOf()
    lateinit var studentBiriktirishAdapter: StudentBiriktirishAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentYigilishlarTadbirOchirishViewPagerAdminBinding.inflate(layoutInflater)
        yigilishlarniYangilashOchirishAdminVm = ViewModelProvider(requireActivity())[YigilishlarniYangilashOchirishAdminVm::class]
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class]

        yigilishData = tadbirlarList.filter { it.id == param1!!.toLong() }[0]
        addTadbirStudents = studentlarList
        binding!!.apply {
            rates = rateList.filter { it.meeting_id == param1!! }.toMutableList()
            if (rates.isNotEmpty()) {
                tadbirTv.visibility = View.VISIBLE
                for (i in 0 until rates.size) {
                    for (j in 0 until studentlarList.size) {
                        if (studentlarList[j].id == rates[i].student_id) {
                            addTadbirStudents.removeAt(j)
                            break
                        }
                    }
                }
            } else {
                tadbirTv.visibility = View.GONE
            }
            addedChip(yigilishEditDeleteChip)
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

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val bottomSheetDialogBinding = FragmentStudentUpdateRoomBottomSheetDialogAdminBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomSheetDialogBinding.root)
        bottomSheetDialogBinding.apply {
            liveDates.addTadbirStudentLiveData.value = addTadbirStudents
            studentBiriktirishAdapter = StudentBiriktirishAdapter(addTadbirStudents) { student ->
                addedStudent(student,param1!!)
                addedTadbirStudentList.add(student)
                addTadbirStudents.remove(student)
                liveDates.addTadbirStudentLiveData.value = addTadbirStudents
                liveDates.addedTadbirStudentLiveData.value = addedTadbirStudentList
                val chip = Chip(requireContext())
                chip.text = student.name
                chip.tag = student.id
                chip.isCloseIconEnabled = true
                chip.setOnClickListener(this@TarbirYangilashOchirishViewPager)
                chip.setOnCloseIconClickListener {
                    binding!!.yigilishEditDeleteChip.removeView(chip)
                    addTadbirStudents.add(addedTadbirStudentList.filter { it.id == chip.tag.toString().toLong() }[0])
                    liveDates.addTadbirStudentLiveData.value = addTadbirStudents
                    addedTadbirStudentList.remove(addedTadbirStudentList.filter { it.id == chip.tag.toString().toLong() }[0])
                    liveDates.addedTadbirStudentLiveData.value = addedTadbirStudentList
                }
                binding!!.yigilishEditDeleteChip.addView(chip)
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

    private fun addedStudent(student: StudentDataItem, tadbir_id: Long) {
        val addRate: MutableList<AddedRate> = mutableListOf()
        addRate.add(AddedRate(meeting_id = tadbir_id, rate = "", student_id = student.id))
        val addRateReq = AddRateReq(addRate)
        yigilishlarniYangilashOchirishAdminVm.addRate(requireContext(), addRateReq, requireActivity())
    }

    private fun searchStudent(query: String) {
        var students: MutableList<StudentDataItem> = mutableListOf()
        for (i in 0 until addTadbirStudents.size) {
            if (addTadbirStudents[i].name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                students.add(addTadbirStudents[i])
            }
        }
        studentBiriktirishAdapter.filter(students)
    }


    private fun addedChip(chip_group: ChipGroup) {
        for (i in 0 until rates.size) {
            val chip = Chip(requireContext())
            chip.text = rates[i].name
            chip.tag = rates[i].id
            chip.isCloseIconEnabled = true
            chip.setOnClickListener {
                Toast.makeText(requireContext(), "${chip.text}", Toast.LENGTH_SHORT).show()
            }
            chip.setOnCloseIconClickListener {
                deleteStudent(chip.tag.toString().toLong(), chip)
            }
            chip_group.addView(chip)
        }
    }

    private fun deleteStudent(id: Long, chip: Chip) {
        yigilishlarniYangilashOchirishAdminVm.deleteRate(requireContext(), id, chip, binding!!.yigilishEditDeleteChip)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.ochirish_menu, menu)
    }

    @Deprecated("Deprecated in Java")
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
                    yigilishlarniYangilashOchirishAdminVm._stateDeleteYigilish.collect{ it1 ->
                        when (it1) {
                            is Resource.Error -> {
                                Toast.makeText(requireContext(), "Server bilan bog'lanib bo'lmadi.", Toast.LENGTH_SHORT).show()
                            }
                            is Resource.Loading -> {

                            }
                            is Resource.Success -> {
                                tadbirlarList.remove(tadbirlarList.filter { it.id == id }[0])
                                liveDates.tarbirlarLiveData.value = tadbirlarList
                                Toast.makeText(requireContext(), it1.data.message, Toast.LENGTH_SHORT).show()
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
        fun newInstance(param1: Long) =
            TarbirYangilashOchirishViewPager().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PARAM1, param1)
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
                yigilishlarniYangilashOchirishAdminVm._stateYigilish.collect{ it1 ->
                    when (it1) {
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
                            val yigilish = TadbirlarDataItem(
                                description = it1.data.meeting.description,
                                id = it1.data.meeting.id,
                                image_base64 = it1.data.meeting.image_base64,
                                meeting_place = it1.data.meeting.meeting_place,
                                name = it1.data.meeting.name,
                                time = it1.data.meeting.time,
                                image_name = "",
                                image_path = ""
                            )
                            tadbirlarList[tadbirlarList.indexOf(tadbirlarList.filter { it.id == yigilish.id }[0])] = yigilish
                            liveDates.tarbirlarLiveData.value = tadbirlarList
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

    override fun onClick(p0: View?) {
        if (p0 is Chip) {
            Toast.makeText(requireContext(), p0.text.toString(), Toast.LENGTH_SHORT).show()
        }
    }

}