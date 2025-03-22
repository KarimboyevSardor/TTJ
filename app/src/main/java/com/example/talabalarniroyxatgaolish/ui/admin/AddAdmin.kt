package com.example.talabalarniroyxatgaolish.ui.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
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
    lateinit var course_count: MutableList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminQoshishAdminBinding.inflate(layoutInflater)
        addAdminVm = ViewModelProvider(requireActivity())[AddAdminVm::class.java]
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class.java]

        binding?.apply {
            val activity: AppCompatActivity = requireActivity() as AppCompatActivity
            activity.setSupportActionBar(toolbar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            val backIcon = toolbar.navigationIcon
            backIcon?.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            toolbar.navigationIcon = backIcon
            toolbar.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
            addAdmin.setOnClickListener {
                checkInfo(adminNameEt.text.toString(), loginEt.text.toString(), parolEt.text.toString())
            }
            addAdmin.setOnClickListener {
                checkInfo(adminNameEt.text.toString(), loginEt.text.toString(), parolEt.text.toString())
            }
        }
        return binding?.root
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
                    addAdminVm.addAdmin(requireContext(), requireActivity(), AdminDataItem(0, 0, adminName, login, parol, "admin"))
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