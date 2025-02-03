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
import com.example.talabalarniroyxatgaolish.databinding.FragmentDavomatAdminBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.davomatList
import com.example.talabalarniroyxatgaolish.vm.DavomatAdminVm
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import com.example.talabalarniroyxatgaolish.vm.Resource
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Davomat : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var binding: FragmentDavomatAdminBinding? = null
    lateinit var davomatAdminVm: DavomatAdminVm
    lateinit var liveDates: LiveDates
    private val TAG = "DAVOMATADMIN"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDavomatAdminBinding.inflate(layoutInflater)
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class]
        davomatAdminVm = ViewModelProvider(requireActivity())[DavomatAdminVm::class]
        val date = arguments?.getString("date")
        Toast.makeText(requireContext(), date, Toast.LENGTH_SHORT).show()
        binding!!.apply {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            val backIcon = toolbar.navigationIcon
            backIcon?.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            toolbar.navigationIcon = backIcon
            toolbar.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
        }

        return binding!!.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Davomat().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}