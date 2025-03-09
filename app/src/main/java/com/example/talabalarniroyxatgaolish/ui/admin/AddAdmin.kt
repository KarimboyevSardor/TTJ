package com.example.talabalarniroyxatgaolish.ui.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.talabalarniroyxatgaolish.R
import com.example.talabalarniroyxatgaolish.adapter.CourseAdapter
import com.example.talabalarniroyxatgaolish.data.AdminDataItem
import com.example.talabalarniroyxatgaolish.databinding.BottomSheetDialogSelectCourseBinding
import com.example.talabalarniroyxatgaolish.databinding.FragmentAdminQoshishAdminBinding
import com.example.talabalarniroyxatgaolish.vm.AddAdminVm
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import kotlin.math.log

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
    private var isAdmin = ""
    lateinit var courseAdapter: CourseAdapter
    lateinit var courses: MutableList<String>
    lateinit var course_count: MutableList<String>
    private var position = -1
    private var courseName = ""
    private var courseCount = ""
    private var isCourse = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminQoshishAdminBinding.inflate(layoutInflater)
        addAdminVm = ViewModelProvider(requireActivity())[AddAdminVm::class.java]
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class.java]

        binding?.apply {
            addAdmin.setOnClickListener {
                checkInfo(adminNameEt.text.toString(), loginEt.text.toString(), parolEt.text.toString())
            }
            addAdmin.setOnClickListener {
                checkInfo(adminNameEt.text.toString(), loginEt.text.toString(), parolEt.text.toString())
            }
        }
        return binding?.root
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
        val bottomSheetBinding: BottomSheetDialogSelectCourseBinding = BottomSheetDialogSelectCourseBinding.inflate(layoutInflater)
        bottomSheet.setContentView(bottomSheetBinding.root)
        bottomSheetBinding.apply {
            courseAdapter = CourseAdapter(list, position) { text, position ->
                this@AddAdmin.position = position
                Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
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

    private fun checkInfo(adminName: String, login: String, parol: String) {
        if (adminName.isNotEmpty() && login.isNotEmpty() && parol.isNotEmpty()) {
            savedInfo(adminName, login, parol)
        }
    }

    private fun savedInfo(adminName: String, login: String, parol: String) {
        lifecycleScope.launch {
            if (isAdded) {
                try {
                    addAdminVm.addAdmin(requireContext(), requireActivity(), AdminDataItem(0, 0, adminName, login, parol, isAdmin))
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