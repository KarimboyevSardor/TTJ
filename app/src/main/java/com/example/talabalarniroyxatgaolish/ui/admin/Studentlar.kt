package com.example.talabalarniroyxatgaolish.ui.admin

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
import com.example.talabalarniroyxatgaolish.adapter.StudentsAdapter
import com.example.talabalarniroyxatgaolish.data.StudentDataItem
import com.example.talabalarniroyxatgaolish.databinding.FragmentStudentlarAdminBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.studentlarList
import com.example.talabalarniroyxatgaolish.utils.Utils.xonalarList
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import com.example.talabalarniroyxatgaolish.vm.Resource
import com.example.talabalarniroyxatgaolish.vm.StudentlarVm
import kotlinx.coroutines.launch
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Studentlar : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var binding: FragmentStudentlarAdminBinding? = null
    lateinit var studentlarVm: StudentlarVm
    lateinit var liveDates: LiveDates
    lateinit var studentsAdapter: StudentsAdapter
    private val TAG = "STUDENTLAR"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentlarAdminBinding.inflate(layoutInflater)
        studentlarVm = ViewModelProvider(requireActivity())[StudentlarVm::class.java]
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class.java]
        studentsAdapter = StudentsAdapter(studentlarList,
            onClick = { student ->
                val bundle = Bundle()
                bundle.putParcelable("student", student)
                val fr = EditStudent()
                fr.arguments = bundle
                requireActivity().supportFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container_admin, fr)
                    .commit()
            },
            deleteStudent = { student ->
                studentlarVm.deleteStudent(requireContext(), student.id)
            }
        )
        studentlarVm._deleteStudent.observe(viewLifecycleOwner) {
            if (isAdded) {
                Log.d(TAG, "onCreateView: $it")
                when (it) {
                    "O'chirildi." -> {
                        liveDates.studentlarLiveData.value = studentlarList

                    }
                    else -> {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        liveDates.getStudentlar().observe(viewLifecycleOwner) {
            studentsAdapter.updateList(it)
        }
        getXonalar()
        getStudents()
        binding!!.apply {
            isEmptyInclude.btnRetry.setOnClickListener {
                getStudents()
            }
            addStudent.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container_admin, AddStudent())
                    .commit()
            }
            studentsRv.adapter = studentsAdapter
            studentSearchview.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
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

        return binding!!.root
    }

    private fun searchStudent(text: String) {
        var students: MutableList<StudentDataItem> = mutableListOf()
        for (i in 0 until studentlarList.size) {
            if (studentlarList[i].name.toLowerCase(Locale.ROOT).contains(text.toLowerCase())) {
                students.add(studentlarList[i])
            }
        }
        studentsAdapter.updateList(students)
    }

    private fun getXonalar() {
        if (isAdded) {
            studentlarVm.getXona(requireContext())
            lifecycleScope.launch {
                try {
                    studentlarVm._stateXona.collect {
                        when(it) {
                            is Resource.Error -> {
                                Log.d(TAG, "getXonalar: ${it.e.message}")
                            }
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                xonalarList = it.data
                                liveDates.xonalarLiveData.value = xonalarList
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "getXonalar: ${e.message}")
                }
            }
        }
    }

    private fun getStudents() {
        studentlarVm.getStudents(requireContext())
        studentlarVm._getStudentlar.observe(viewLifecycleOwner) {
            if (isAdded) {
                when (it) {
                    "topildi" -> {
                        binding!!.studentsRv.visibility = View.VISIBLE
                        binding!!.isEmptyLayout.visibility = View.GONE
                        liveDates.studentlarLiveData.value = studentlarList
                    }
                    else -> {
                        binding!!.studentsRv.visibility = View.GONE
                        binding!!.isEmptyLayout.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Studentlar().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}