package com.example.talabalarniroyxatgaolish.ui.student

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.talabalarniroyxatgaolish.adapter.TadbirProfileStudentAdapter
import com.example.talabalarniroyxatgaolish.databinding.FragmentProfileStudentBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.myInfo
import com.example.talabalarniroyxatgaolish.utils.Utils.studentInfoData
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import com.example.talabalarniroyxatgaolish.vm.ProfileStudentVm
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileStudent : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var binding: FragmentProfileStudentBinding? = null
    lateinit var profileStudentVm: ProfileStudentVm
    lateinit var liveDates: LiveDates
    lateinit var profileStudentAdapter: TadbirProfileStudentAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileStudentBinding.inflate(layoutInflater)
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class]
        profileStudentVm = ViewModelProvider(requireActivity())[ProfileStudentVm::class]
        isCheckEmpty()
        liveDates.getStudentInfoData().observe(viewLifecycleOwner) {
            if (it.student.id != 0L) {
                profileStudentAdapter = TadbirProfileStudentAdapter(
                    studentInfoData!!.meetings.toMutableList(),
                    requireContext()
                )
                binding!!.tadbirlarRv.adapter = profileStudentAdapter
                binding!!.studentNameProfil.text = studentInfoData!!.student.name
                binding!!.studentXonaProfile.text = studentInfoData!!.rooms.room_count
                binding!!.studentFakultetProfil.text = studentInfoData!!.student.course
                binding!!.studentKursCountProfile.text = studentInfoData!!.student.course_count.toString()
                binding!!.studentMeetingCountProfile.text = studentInfoData!!.meetings.size.toString()
            }
        }
        binding!!.apply {
            lifecycleScope.launch {
                if (isAdded) {
                    profileStudentVm.getStudentInfo(requireContext(), myInfo!!.id)
                    profileStudentVm._studentInfo.observe(viewLifecycleOwner) {
                        when (it) {
                            "qoshildi" -> {
                                liveDates.studentInfo.value = studentInfoData
                                isCheckEmpty()
                            }
                            else -> {
                                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                                binding!!.profileRvShimmer.visibility = GONE
                                binding!!.profileRvShimmer.startShimmer()
                            }
                        }
                    }
                }
            }
        }

        liveDates.getStudentInfoData().observe(viewLifecycleOwner) {
            profileStudentAdapter.updateList(it.meetings.toMutableList())
        }
        return binding!!.root
    }

    private fun isCheckEmpty() {
        if (studentInfoData != null) {
            binding!!.profileRvShimmer.visibility = GONE
            binding!!.profileRvShimmer.startShimmer()
            binding!!.tadbirlarRv.visibility = VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileStudent().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}