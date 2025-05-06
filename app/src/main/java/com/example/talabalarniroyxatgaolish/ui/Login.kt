package com.example.talabalarniroyxatgaolish.ui

import android.os.Build
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
import com.example.talabalarniroyxatgaolish.databinding.FragmentLoginBinding
import com.example.talabalarniroyxatgaolish.db.MyDatabase
import com.example.talabalarniroyxatgaolish.ui.admin.BoshAdmin
import com.example.talabalarniroyxatgaolish.ui.student.BoshStudent
import com.example.talabalarniroyxatgaolish.utils.Utils.myInfo
import com.example.talabalarniroyxatgaolish.vm.LoginVm
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
// o'z paket nomingizni qo'llang
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Login : Fragment() {
    private var TAG = "Login"
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var binding: FragmentLoginBinding? = null
    lateinit var loginVm: LoginVm
    lateinit var myDatabase: MyDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)

        loginVm = ViewModelProvider(requireActivity())[LoginVm::class.java]
        myDatabase = MyDatabase(requireContext())
        if (myDatabase.getAuth().isNotEmpty()) {
            myInfo = myDatabase.getAuth()[0]
            Log.d(TAG, "onCreateView: $myInfo")
            if (myInfo!!.role == "admin") {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, BoshAdmin())
                    .commit()
            } else if (myInfo!!.role == "student") {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, BoshStudent())
                    .commit()
            }
        }

        binding!!.apply {
            kirish.setOnClickListener {
                if (login.text.toString().isNotEmpty() && password.text.isNotEmpty()) {
                    auth(login.text.toString(), password.text.toString())
                } else {
                    Toast.makeText(requireContext(), "Maydonlarni to'ldiring!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return binding!!.root
    }

    private fun auth(login: String, password: String) {
        lifecycleScope.launch {
            if (isAdded) {
                try {
                    val manufacturer = Build.MANUFACTURER
                    val model = Build.MODEL
                    val deviceInfo = "$manufacturer $model"
                    val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                        Date()
                    )
                    Log.d(TAG, "auth: $deviceInfo, $currentTime")
                    loginVm.auth(login, password, deviceInfo, currentTime, requireContext(), requireActivity())
                } catch (e: Exception) {
                    Log.d(TAG, "auth: ${e.message}")
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Login().apply {
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