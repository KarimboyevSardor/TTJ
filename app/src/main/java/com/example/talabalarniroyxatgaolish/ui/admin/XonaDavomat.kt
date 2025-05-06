package com.example.talabalarniroyxatgaolish.ui.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.talabalarniroyxatgaolish.R
import com.example.talabalarniroyxatgaolish.adapter.StudentDavomatAdapter
import com.example.talabalarniroyxatgaolish.data.DavomatDataItem
import com.example.talabalarniroyxatgaolish.data.Rate
import com.example.talabalarniroyxatgaolish.data.StudentDataItem
import com.example.talabalarniroyxatgaolish.databinding.FragmentDavomatAdminBinding
import com.example.talabalarniroyxatgaolish.databinding.FragmentXonaDavomatAdminBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.currentDateDavomat
import com.example.talabalarniroyxatgaolish.utils.Utils.xonalarList
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import com.example.talabalarniroyxatgaolish.vm.Resource
import com.example.talabalarniroyxatgaolish.vm.XonaDavomatAdminVm
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class XonaDavomat : Fragment(), StudentDavomatAdapter.AdapterListener {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var binding: FragmentXonaDavomatAdminBinding? = null
    lateinit var liveDates: LiveDates
    private val TAG = "XONADAVOMATADMIN"
    lateinit var xonaDavomatAdminVm: XonaDavomatAdminVm
    private var room_id: Long = 0
    private var date: String = ""
    lateinit var studentDavomatAdapter: StudentDavomatAdapter
    lateinit var qoshilganStudent: MutableList<DavomatDataItem>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentXonaDavomatAdminBinding.inflate(layoutInflater)
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class.java]
        xonaDavomatAdminVm = ViewModelProvider(requireActivity())[XonaDavomatAdminVm::class]
        room_id = arguments?.getLong("room_id")!!
        date = arguments?.getString("date")!!
        qoshilganStudent = mutableListOf()
        qoshilganStudent = currentDateDavomat.filter { it.room_id == room_id }.toMutableList()
        if (qoshilganStudent.isEmpty()) {
            getStudents()
        } else {
            setAdapter()
        }
        binding!!.apply {
            toolbar.title = xonalarList.filter { it.id == room_id }[0].room_count + " - xona"
            setHasOptionsMenu(true)
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            val backIcon = toolbar.navigationIcon
            backIcon?.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            toolbar.navigationIcon = backIcon
            toolbar.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
            saqlash.setOnClickListener {
                setDavomat()
            }
        }
        return binding!!.root
    }

    private fun setAdapter() {
        studentDavomatAdapter = StudentDavomatAdapter(qoshilganStudent, this)
        binding!!.rvStudentList.adapter = studentDavomatAdapter
    }

    private fun setDavomat() {
        if (qoshilganStudent.isNotEmpty()) {
            lifecycleScope.launch {
                if (isAdded) {
                    try {
                        xonaDavomatAdminVm.setDavomat(requireContext(), qoshilganStudent, requireActivity())
                        requireActivity().onBackPressed()
                    } catch (e: Exception) {
                        Log.d(TAG, "setDavomat: ${e.message}")
                    }
                }
            }
        }
    }

    fun formatDate(inputDate: String): String {
        val parts = inputDate.split("-")
        if (parts.size != 3) return "Noto'g'ri format"
        val year = parts[0]
        val month = parts[1].padStart(2, '0') // Oy oldiga 0 qo'shish
        val day = parts[2].padStart(2, '0')   // Kun oldiga 0 qo'shish
        return "$year-$month-$day"
    }

    private fun setStudent(students: MutableList<StudentDataItem>) {
        for (i in 0 until students.size) {
            qoshilganStudent.add(
                DavomatDataItem(
                    date = formatDate(date),
                    id = 0,
                    is_there = false,
                    room_id = room_id,
                    student_id = students[i].id,
                    name = students[i].name,
                    course = students[i].course,
                    course_count = students[i].course_count.toLong()
                )
            )
        }
        setAdapter()
    }

//    private fun isCheckEmpty() {
//        binding!!.roomsShimmer.stopShimmer()
//        binding!!.rvStudentList.visibility = View.VISIBLE
//        binding!!.roomsShimmer.visibility = View.GONE
//    }

    private fun getStudents() {
        lifecycleScope.launch {
            if (isAdded) {
                try {
                    xonaDavomatAdminVm.getStudentRoom(requireContext(), room_id)
                    xonaDavomatAdminVm._roomStudent.collect{
                        when (it) {
                            is Resource.Error -> {
                                Log.d(TAG, "getStudents: ${it.e}")
                            }
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                setStudent(it.data)
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
        fun newInstance(param1: String, param2: String) =
            XonaDavomat().apply {
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

    override fun onStart() {
        super.onStart()
        qoshilganStudent = mutableListOf()
        Log.d(TAG, "onStart: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    override fun onAdapterFunctionCalled(position: Int, switch: CheckBox) {
        if (switch.isChecked) {
            qoshilganStudent[position].is_there = true
        } else {
            qoshilganStudent[position].is_there = false
        }
    }
}