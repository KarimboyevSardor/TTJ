package com.example.talabalarniroyxatgaolish.ui.admin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.talabalarniroyxatgaolish.adapter.LoginAdapter
import com.example.talabalarniroyxatgaolish.data.AdminDataItem
import com.example.talabalarniroyxatgaolish.data.AuthDataItem
import com.example.talabalarniroyxatgaolish.databinding.FragmentEditAdminBinding
import com.example.talabalarniroyxatgaolish.vm.EditAdminVm
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import com.example.talabalarniroyxatgaolish.vm.Resource
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditAdmin : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var binding: FragmentEditAdminBinding? = null
    lateinit var loginAdapter: LoginAdapter
    lateinit var liveDates: LiveDates
    lateinit var admin: AdminDataItem
    lateinit var editAdminVm: EditAdminVm
    private val TAG = "EDITADMIN"
    private var auth: AuthDataItem? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditAdminBinding.inflate(layoutInflater)
        admin = arguments?.getParcelable("admin")!!
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class]
        editAdminVm = ViewModelProvider(requireActivity())[EditAdminVm::class]
        getAuth()
        binding?.apply {
            passwordEt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val counter = 8
                    val currentLength = s?.length ?: 0

                    if (currentLength < counter) {
                        passwordIl.error = "Parol 8 ta belgidan iborat bo'lishi kerak."
                    } else {
                        passwordIl.error = null
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
            xonaSave.setOnClickListener {
                saveInfo(loginEt.text.toString(), passwordEt.text.toString(), adminNomiEt.text.toString())
            }
        }

        return binding?.root
    }

    private fun saveInfo(login: String, password: String, adminName: String) {
        if (login != "" && password != "" && adminName != "") {
            saved(login, password, adminName)
        }
    }

    private fun saved(login: String, password: String, adminName: String) {
        lifecycleScope.launch {
            if (isAdded) {
                try {
                    admin.name = adminName
                    editAdminVm.editAdmin(requireContext(), requireActivity(), admin)
                    auth!!.password = password
                    auth!!.login = login
                    editAdminVm.editAuth(requireContext(), auth!!, requireActivity())
                } catch (e: Exception) {
                    Log.d(TAG, "saved: ${e.message}")
                }
            }
        }
    }

    private fun getAuth() {
        lifecycleScope.launch {
            if (isAdded) {
                try {
                    editAdminVm.getLogins(requireContext(), admin.auth_id)
                    editAdminVm._logins.collect {
                        when (it) {
                            is Resource.Error -> {
                                Toast.makeText(requireContext(), "${it.e.message}", Toast.LENGTH_SHORT).show()
                            }
                            is Resource.Loading -> {}
                            is Resource.Success -> {
//                                auth!!.login = it.data.login
//                                auth!!.password = it.data.password
//                                auth!!.id = it.data.id
//                                auth!!.role = it.data.role
                                auth = it.data
                                binding!!.adminNomiEt.setText(admin.name)
                                binding!!.loginEt.setText(it.data.login)
                                binding!!.passwordEt.setText(it.data.password)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "getAuth: ${e.message}")
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditAdmin().apply {
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