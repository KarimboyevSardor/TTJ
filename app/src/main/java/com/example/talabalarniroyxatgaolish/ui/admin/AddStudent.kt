package com.example.talabalarniroyxatgaolish.ui.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.talabalarniroyxatgaolish.R
import com.example.talabalarniroyxatgaolish.adapter.CourseAdapter
import com.example.talabalarniroyxatgaolish.data.AddedStudentDataItem
import com.example.talabalarniroyxatgaolish.databinding.BottomSheetDialogSelectCourseBinding
import com.example.talabalarniroyxatgaolish.databinding.FragmentAddStudentAdminBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.studentlarList
import com.example.talabalarniroyxatgaolish.utils.Utils.xonalarList
import com.example.talabalarniroyxatgaolish.vm.AddStudentVm
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlin.math.truncate

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddStudent : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var binding: FragmentAddStudentAdminBinding? = null
    lateinit var courseAdapter: CourseAdapter
    lateinit var courses: MutableList<String>
    lateinit var course_count: MutableList<String>
    lateinit var rooms: MutableList<String>
    private var position = -1
    private var courseName = ""
    private var courseCount = ""
    private var isCourse = false
    lateinit var addStudentVm: AddStudentVm
    lateinit var liveDates: LiveDates
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddStudentAdminBinding.inflate(layoutInflater)
        addStudentVm = ViewModelProvider(requireActivity())[AddStudentVm::class]
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class]

        addStudentVm.uiState.observe(viewLifecycleOwner) {
            if (isAdded) {
                when (it) {
                    "Qo'shildi" -> {
                        liveDates.studentlarLiveData.value = studentlarList
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding!!.apply {
            selectCourseBtn.setOnClickListener {
                setCourses()
                showBottomSheetDialogCourse(courses, isCourse, selectCourseBtn)
            }
            selectCourseCountBtn.setOnClickListener {
                setCourseCount()
                showBottomSheetDialogCourse(course_count, isCourse, selectCourseCountBtn)
            }
            selectRoomBtn.setOnClickListener {
                setRooms()
                showBottomSheetDialogCourse(rooms, isCourse, selectRoomBtn)
            }

            addStudent.setOnClickListener {
                if (checkAll(studentName, selectCourseBtn.text.toString(),
                        selectCourseCountBtn.text.toString(), selectRoomBtn.text.toString(),
                        loginEt, parolEt)) {
                    addedStudent(studentName.text.toString(), selectCourseBtn.text.toString(),
                        selectCourseCountBtn.text.toString(), selectRoomBtn.text.toString(),
                        loginEt.text.toString(), parolEt.text.toString())
                }
            }
        }

        return binding!!.root
    }

    private fun addedStudent(studentName: String, course: String, courseCount: String, room: String, login: String, password: String) {
        val room_id = xonalarList.filter { it.room_count == room.substring(0, room.indexOf(" - xona")) }[0].id
        addStudentVm.addStudent(context = requireContext(), addedStudentDataItem = AddedStudentDataItem(
                course = course,
                course_count = courseCount.substring(0, 1).toInt(),
                id = 0,
                name = studentName,
                room_id = room_id,
                login = login,
                password = password,
                role = "student",
            )
        )
    }

    private fun checkAll(studentName: EditText, course: String, courseCount: String, room: String, login: EditText, password: EditText): Boolean {
        if (studentName.text.toString().isEmpty()) {
            studentName.error = "Maydonni to'ldiring."
            return false
        } else if (course == "Fakultetni tanlang") {
            Toast.makeText(requireContext(), "Fakultetni tanlamadingiz.", Toast.LENGTH_SHORT).show()
            return false
        } else if (courseCount == "Kursini tanlang") {
            Toast.makeText(requireContext(), "Kursini tanlamadingiz.", Toast.LENGTH_SHORT).show()
            return false
        } else if (room == "Xonasini tanlang") {
            Toast.makeText(requireContext(), "Xonani tanlamadingiz.", Toast.LENGTH_SHORT).show()
            return false
        } else if (login.text.toString().isEmpty()) {
            login.error = "Maydonni to'ldiring."
            return false
        } else if (password.text.toString().isEmpty()) {
            password.error = "Maydonni to'ldiring."
            return false
        } else {
            return true
        }
    }

    private fun setRooms() {
        rooms = mutableListOf()
        for (i in 0 until xonalarList.size) {
            rooms.add(xonalarList[i].room_count + " - xona")
        }
    }

    private fun setCourseCount() {
        course_count = mutableListOf()
        course_count.add("1-KURS")
        course_count.add("2-KURS")
        course_count.add("3-KURS")
        course_count.add("4-KURS")
    }

    private fun setCourses() {
        courses = mutableListOf()
        courses.add("TELEKOMMUNIKATSIYA TEXNOLOGIYALARI YO'NALISHI")
        courses.add("DASTURIY INJINIRING YO'NALISHI")
        courses.add("AXBOROT XAVFSIZLIGI YO'NALISHI")
        courses.add("KOMPYUTER INJINIRING (KOMPYUTER INJINIRING, IT-SERVIS) YO'NALISHI")
        courses.add("AXBOROT-KOMMUNIKATSIYALAR SOHASIDA KASB TA'LIMI YO'NALISHI")
        courses.add("POCHTA ALOQASI TEXNOLOGIYASI YO'NALISHI")
        courses.add("KUTUBXONA-AXBOROT FAOLIYATI (FAOLIYAT TURLARI BO'YICHA) YO'NALISHI")
    }

    private fun showBottomSheetDialogCourse(list: MutableList<String>, isCourse: Boolean, course: AppCompatButton) {
        val bottomSheet = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val bottomSheetBinding: BottomSheetDialogSelectCourseBinding =
            BottomSheetDialogSelectCourseBinding.inflate(layoutInflater)
        bottomSheet.setContentView(bottomSheetBinding.root)
        bottomSheetBinding.apply {
            courseAdapter = CourseAdapter(list, position) { _, position ->
                this@AddStudent.position = position
                if (isCourse) {
                    courseName = list[position]
                    course.text = courseName
                } else {
                    courseCount = list[position]
                    course.text = courseCount
                }
                bottomSheet.dismiss()
            }
            selectCourseRv.adapter = courseAdapter
        }
        bottomSheet.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddStudent().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}