package com.example.talabalarniroyxatgaolish.ui.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.talabalarniroyxatgaolish.R
import com.example.talabalarniroyxatgaolish.adapter.YigilishlarAdapter
import com.example.talabalarniroyxatgaolish.databinding.FragmentYigilishlarAdminBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.rateList
import com.example.talabalarniroyxatgaolish.utils.Utils.yigilishlarList
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import com.example.talabalarniroyxatgaolish.vm.Resource
import com.example.talabalarniroyxatgaolish.vm.YigilishlarAdminVm
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Yigilishlar : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private val TAG = "YIGILISHLARADMIN"
    lateinit var yigilishlarAdapter: YigilishlarAdapter
    lateinit var yigilishlarAdminVm: YigilishlarAdminVm
    lateinit var liveDates: LiveDates
    private var binding: FragmentYigilishlarAdminBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentYigilishlarAdminBinding.inflate(layoutInflater)

        yigilishlarAdminVm = ViewModelProvider(requireActivity())[YigilishlarAdminVm::class]
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class]
        getYigilishlar()

        val bottomNavigation: FrameLayout = requireActivity().findViewById(R.id.bottom_navigation_admin)
        val toolbar: Toolbar = requireActivity().findViewById(R.id.bosh_toolbar_admin)
        bottomNavigation.visibility = View.VISIBLE
        toolbar.visibility = View.VISIBLE

        yigilishlarAdapter = YigilishlarAdapter(yigilishlarList, rateList, requireContext()) {
            val bundle = Bundle()
            bundle.putLong("yigilish", it.id)
            val fr = YigilishlarYangilashOchirish()
            fr.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_admin, fr)
                .addToBackStack(null)
                .commit()
            bottomNavigation.visibility = View.GONE
            toolbar.visibility = View.GONE
        }
        liveDates.getYigilish().observe(requireActivity()) {
            yigilishlarAdapter.filterYigilish(it)
        }
        liveDates.getRate().observe(requireActivity()) {
            yigilishlarAdapter.filterRate(it)
            Log.d(TAG, "onCreateView: ${it.size}")
        }

        binding!!.apply {
            yigilishRv.adapter = yigilishlarAdapter
            addYigilish.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_admin, Yigilish_qoshish())
                    .addToBackStack(null)
                    .commit()
                bottomNavigation.visibility = View.GONE
                toolbar.visibility = View.GONE
            }
        }

        return binding!!.root
    }

    private fun getYigilishlar() {
        lifecycleScope.launch {
            if (isAdded) {
                yigilishlarAdminVm.getYigilishlar(requireContext())
                yigilishlarAdminVm._stateYigilishlar.collect{
                    when (it) {
                        is Resource.Error -> {
                            Toast.makeText(requireContext(), "Yig'ilishlarni olib bo'lmadi.", Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            yigilishlarList = it.data
                            liveDates.yigilishlarLiveData.value = yigilishlarList
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            if (isAdded) {
                yigilishlarAdminVm.getRates(requireContext())
                yigilishlarAdminVm._rates.collect {
                    when (it) {
                        is Resource.Error -> {
                            Log.d(TAG, "getYigilishlar: ${it.e.message}")
                        }
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            rateList = it.data
                            liveDates.rateLiveData.value = rateList
                        }
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Yigilishlar().apply {
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