package com.example.talabalarniroyxatgaolish.ui

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
import com.example.talabalarniroyxatgaolish.ui.admin.Bosh
import com.example.talabalarniroyxatgaolish.vm.LoginVm
import com.example.talabalarniroyxatgaolish.vm.Resource
// o'z paket nomingizni qo'llang
import kotlinx.coroutines.launch

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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)

        loginVm = ViewModelProvider(requireActivity())[LoginVm::class.java]

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun auth(login: String, password: String) {
        lifecycleScope.launch {
            loginVm.auth(login, password, requireContext())
            if (isAdded) {
                loginVm._stateLogin.collect {
                    when (it) {
                        is Resource.Error -> {
                            Toast.makeText(requireContext(), "${it.e.message}", Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            Log.d(TAG, "auth: ${it.data}")
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainerView, Bosh()) // MainFragmentni yuklaydi
                                .commit()
                        }
                    }
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