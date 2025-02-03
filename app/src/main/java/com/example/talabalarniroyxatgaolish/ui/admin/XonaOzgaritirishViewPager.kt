package com.example.talabalarniroyxatgaolish.ui.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.talabalarniroyxatgaolish.data.StudentDataItem
import com.example.talabalarniroyxatgaolish.data.XonaDataItem
import com.example.talabalarniroyxatgaolish.databinding.FragmentXonaOzgartirishViewpagerAdminBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.studentlarList
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import com.example.talabalarniroyxatgaolish.vm.XonaAdminVm
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"

class XonaOzgaritirishViewPager : Fragment() {
    private var param1: XonaDataItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getParcelable(ARG_PARAM1)
        }
    }

    private var binding: FragmentXonaOzgartirishViewpagerAdminBinding? = null
    lateinit var liveDates: LiveDates
    lateinit var xonaAdminVm: XonaAdminVm
    private val TAG = "XONAOZGARTIRISHVIEWPAGER"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentXonaOzgartirishViewpagerAdminBinding.inflate(layoutInflater)
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class.java]
        xonaAdminVm = ViewModelProvider(requireActivity())[XonaAdminVm::class.java]

        binding!!.apply {
            xonaNameEt.setText(param1!!.room_count)
            xonaSave.setOnClickListener {
                if (xonaNameEt.text.toString().isNotEmpty()) {
                    saveXona(xonaNameEt.text.toString())
                } else {
                    xonaNameEt.error = "Xona nomi bo'sh holatda bo'lishi mumkin emas!"
                }
            }
        }

        return binding!!.root
    }

    private fun saveXona(xona_name: String) {
        lifecycleScope.launch {
            if (isAdded) {
                try {
                    xonaAdminVm.xonaEdit(requireContext(), XonaDataItem(param1!!.id, xona_name))
                } catch (e: Exception) {
                    Log.d(TAG, "saveXona: ${e.message}")
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(xona: XonaDataItem) =
            XonaOzgaritirishViewPager().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, xona)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}