package com.example.talabalarniroyxatgaolish.ui.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.talabalarniroyxatgaolish.R
import com.example.talabalarniroyxatgaolish.adapter.StudentDavomatAdapter
import com.example.talabalarniroyxatgaolish.data.DavomatDataItem
import com.example.talabalarniroyxatgaolish.data.Rate
import com.example.talabalarniroyxatgaolish.data.StudentDataItem
import com.example.talabalarniroyxatgaolish.databinding.FragmentDavomatAdminBinding
import com.example.talabalarniroyxatgaolish.databinding.FragmentXonaDavomatAdminBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.xonalarList
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import com.example.talabalarniroyxatgaolish.vm.Resource
import com.example.talabalarniroyxatgaolish.vm.XonaDavomatAdminVm
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class XonaDavomat : Fragment() {
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
        getRoomDavomat()
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

//    private fun getRoomDavomat1() {
//        lifecycleScope.launch {
//            if (isAdded) {
//                try {
//                    xonaDavomatAdminVm.getRoomDate1(requireContext(), formatDate(date), room_id, requireActivity())
//                    if (liveDates.davomatLiveData.value!!.isEmpty()) {
//                        getStudents1()
//                    } else {
//                        setAdapter(liveDates.davomatLiveData.value!!)
//                    }
//                } catch (e: Exception) {
//                    Log.d(TAG, "getStudents: ${e.message}")
//                }
//            }
//        }
//    }
//
//    private fun getStudents1() {
//        lifecycleScope.launch {
//            if (isAdded) {
//                try {
//                    xonaDavomatAdminVm.getStudentRoom1(requireContext(), room_id, requireActivity())
//                    if (liveDates.xonaStudentLiveDate.value!!.isNotEmpty()) {
//                        setStudent(liveDates.xonaStudentLiveDate.value!!)
//                    } else {
//                        setAdapter(mutableListOf())
//                    }
//                } catch (e: Exception) {
//                    Log.d(TAG, "getStudents: ${e.message}")
//                }
//            }
//        }
//    }

    private fun setDavomat() {
        if (qoshilganStudent.isNotEmpty()) {
            lifecycleScope.launch {
                if (isAdded) {
                    try {
                        xonaDavomatAdminVm.setDavomat(requireContext(), qoshilganStudent, requireActivity())
                    } catch (e: Exception) {
                        Log.d(TAG, "setDavomat: ${e.message}")
                    }
                }
            }
        }
    }

    private fun setAdapter(qoshilganStudent: MutableList<DavomatDataItem>) {
        if (qoshilganStudent.isEmpty()) {
            studentDavomatAdapter = StudentDavomatAdapter(mutableListOf()) { position, checkBox ->
                if (checkBox.isChecked) {
                    qoshilganStudent[position].is_there = true
                    liveDates.xonaDavomatLiveData.value = qoshilganStudent
                } else {
                    qoshilganStudent[position].is_there = false
                    liveDates.xonaDavomatLiveData.value = qoshilganStudent
                }
                liveDates.xonaDavomatLiveData.observe(requireActivity()) {
                    studentDavomatAdapter.filter(it)
                }
            }
        } else {
            studentDavomatAdapter = StudentDavomatAdapter(qoshilganStudent) { position, checkBox ->
                if (checkBox.isChecked) {
                    qoshilganStudent[position].is_there = true
                    liveDates.xonaDavomatLiveData.value = qoshilganStudent
                } else {
                    qoshilganStudent[position].is_there = false
                    liveDates.xonaDavomatLiveData.value = qoshilganStudent
                }
                liveDates.xonaDavomatLiveData.observe(requireActivity()) {
                    studentDavomatAdapter.filter(it)
                }
            }
        }
        binding?.rvStudentList?.adapter = studentDavomatAdapter
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
        val qoshilganStudent: MutableList<DavomatDataItem> = mutableListOf()
        for (i in 0 until students.size) {
            qoshilganStudent.add(
                DavomatDataItem(
                    date = date,
                    id = 0,
                    is_there = false,
                    room_id = room_id,
                    student_id = students[i].id,
                    name = students[i].name,
                    course = students[i].course,
                    course_count = students[i].course_count.toLong()
                )
            )
            liveDates.xonaDavomatLiveData.value = qoshilganStudent
            setAdapter(qoshilganStudent)
        }
    }

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
                                if (it.data.isEmpty()) {
                                    setAdapter(mutableListOf())
                                } else {
                                    setStudent(it.data)
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

    private fun getRoomDavomat() {
        lifecycleScope.launch {
            xonaDavomatAdminVm.getRoomDate(requireContext(), formatDate(date), room_id)
            if (isAdded) {
                try {
                    xonaDavomatAdminVm._roomDavomat.collect {
                        when (it) {
                            is Resource.Error -> {
                                Log.d(TAG, "getStudents: ${it.e.message}")
                            }
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                delay(100)
                                if (it.data.isEmpty()) {
                                    getStudents()
                                } else {
                                    liveDates.xonaDavomatLiveData.value = it.data
                                    setAdapter(it.data)
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
        liveDates.xonaDavomatLiveData.value = mutableListOf()
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
}