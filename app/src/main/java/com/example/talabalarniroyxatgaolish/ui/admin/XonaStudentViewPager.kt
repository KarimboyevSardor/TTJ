package com.example.talabalarniroyxatgaolish.ui.admin

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.talabalarniroyxatgaolish.R
import com.example.talabalarniroyxatgaolish.adapter.StudentRoomUpdateAdapter
import com.example.talabalarniroyxatgaolish.adapter.StudentsAdapter
import com.example.talabalarniroyxatgaolish.data.StudentDataItem
import com.example.talabalarniroyxatgaolish.data.XonaDataItem
import com.example.talabalarniroyxatgaolish.databinding.FragmentStudentUpdateRoomBottomSheetDialogAdminBinding
import com.example.talabalarniroyxatgaolish.databinding.FragmentXonaStudentViewPagerAdminBinding
import com.example.talabalarniroyxatgaolish.databinding.StudentRemoveRoomBottomSheetDialogBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.studentlarList
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import com.example.talabalarniroyxatgaolish.vm.Resource
import com.example.talabalarniroyxatgaolish.vm.XonaAdminVm
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class XonaStudentViewPager : Fragment() {
    private var param1: XonaDataItem? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getParcelable(ARG_PARAM1)
        }
    }

    lateinit var liveDates: LiveDates
    lateinit var xonaAdminVm: XonaAdminVm
    lateinit var studentsAdapter: StudentsAdapter
    private val TAG = "XONASTUDENTVIEWPAGER"
    private var binding: FragmentXonaStudentViewPagerAdminBinding? = null
    lateinit var studentRoomUpdateAdapter: StudentRoomUpdateAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentXonaStudentViewPagerAdminBinding.inflate(layoutInflater)
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class.java]
        xonaAdminVm = ViewModelProvider(requireActivity())[XonaAdminVm::class.java]
        studentsAdapter = StudentsAdapter(studentlarList.filter { it.room_id == param1!!.id }.toMutableList()) {
            showUpdateStudentRoomDeleteDialog(it)
        }
        getStudents()

        liveDates.getStudentlar().observe(requireActivity()) { it ->
            studentsAdapter.filter(it.filter { it.room_id == param1!!.id }.toMutableList())
        }

        binding!!.apply {
            studentRv.adapter = studentsAdapter
            addStudentRoom.setOnClickListener {
                showUpdateStudentRoomDialog()
            }
        }

        return binding!!.root
    }

    private fun showUpdateStudentRoomDeleteDialog(studentDataItem: StudentDataItem) {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val bottomSheetDialogBinding = StudentRemoveRoomBottomSheetDialogBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomSheetDialogBinding.root)
        bottomSheetDialogBinding.apply {
            cancelButton.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            removeButton.setOnClickListener {
                updateStudentRoom(-1, studentDataItem)
                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetDialog.show()
    }

    private fun showUpdateStudentRoomDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val bottomSheetDialogBinding = FragmentStudentUpdateRoomBottomSheetDialogAdminBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomSheetDialogBinding.root)

        bottomSheetDialogBinding.apply {
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
            studentRoomUpdateAdapter = StudentRoomUpdateAdapter(studentlarList.filter { it.room_id == -1L }.toMutableList()) {
                updateStudentRoom(param1!!.id, it)
            }
            liveDates.getStudentlar().observe(requireActivity()) { it ->
                studentRoomUpdateAdapter.filter(it.filter { it.room_id == -1L }.toMutableList())
            }
            studentRoomRv.adapter = studentRoomUpdateAdapter
        }
        bottomSheetDialog.show()
    }

    private fun updateStudentRoom(roomId: Long, studentDataItem: StudentDataItem) {
        lifecycleScope.launch {
            if (isAdded) {
                try {
                    studentDataItem.room_id = roomId
                    xonaAdminVm.updateStudent(requireContext(), requireActivity(), studentDataItem)
                } catch (e: Exception) {
                    Log.d(TAG, "updateStudentRoom: ${e.message}")
                }
            }
        }
    }

    private fun searchStudent(text: String) {
        var students: MutableList<StudentDataItem> = mutableListOf()
        for (i in 0 until studentlarList.size) {
            if (studentlarList[i].name.toLowerCase(Locale.ROOT).contains(text.toLowerCase())) {
                students.add(studentlarList[i])
            }
        }
        studentRoomUpdateAdapter.filter(students.filter { it.room_id == -1L }.toMutableList())
    }

    private fun getStudents() {
        lifecycleScope.launch {
            if (isAdded) {
                try {
                    if (studentlarList.isEmpty()) {
                        xonaAdminVm.getStudentRoomId(requireContext())
                        xonaAdminVm._students.collect {
                            when (it) {
                                is Resource.Error -> {
                                    Toast.makeText(
                                        requireContext(),
                                        "Server bilan bog'lanib bo'lmadi.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d(TAG, "getStudents: ${it.e.message}")
                                }

                                is Resource.Loading -> {}
                                is Resource.Success -> {
                                    studentlarList = it.data
                                    liveDates.studentlarLiveData.value = studentlarList
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "getStudents: ${e.message}")
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: XonaDataItem) =
            XonaStudentViewPager().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)
                }
            }
    }
}