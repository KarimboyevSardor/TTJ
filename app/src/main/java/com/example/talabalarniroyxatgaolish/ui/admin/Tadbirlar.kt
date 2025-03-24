package com.example.talabalarniroyxatgaolish.ui.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.talabalarniroyxatgaolish.R
import com.example.talabalarniroyxatgaolish.adapter.TadbirlarAdapter
import com.example.talabalarniroyxatgaolish.data.TadbirlarDataItem
import com.example.talabalarniroyxatgaolish.databinding.FragmentTadbirlarAdminBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.rateList
import com.example.talabalarniroyxatgaolish.utils.Utils.studentlarList
import com.example.talabalarniroyxatgaolish.utils.Utils.tadbirlarList
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import com.example.talabalarniroyxatgaolish.vm.Resource
import com.example.talabalarniroyxatgaolish.vm.YigilishlarAdminVm
import kotlinx.coroutines.launch
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Tadbirlar : Fragment() {
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
    lateinit var tadbirlarAdapter: TadbirlarAdapter
    lateinit var yigilishlarAdminVm: YigilishlarAdminVm
    lateinit var liveDates: LiveDates
    private var binding: FragmentTadbirlarAdminBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTadbirlarAdminBinding.inflate(layoutInflater)

        yigilishlarAdminVm = ViewModelProvider(requireActivity())[YigilishlarAdminVm::class]
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class]
        getYigilishlar()
        getStudents()

        val bottomNavigation: FrameLayout = requireActivity().findViewById(R.id.bottom_navigation_admin)
        val toolbar: Toolbar = requireActivity().findViewById(R.id.bosh_toolbar_admin)
        bottomNavigation.visibility = View.VISIBLE
        toolbar.visibility = View.VISIBLE

        tadbirlarAdapter = TadbirlarAdapter(tadbirlarList, rateList, requireContext()) {
            val bundle = Bundle()
            bundle.putLong("yigilish", it.id)
            val fr = Tadbir()
            fr.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_admin, fr)
                .addToBackStack(null)
                .commit()
            bottomNavigation.visibility = View.GONE
            toolbar.visibility = View.GONE
        }
        liveDates.getYigilish().observe(requireActivity()) {
            tadbirlarAdapter.filterYigilish(it)
        }
        liveDates.getRate().observe(requireActivity()) {
            tadbirlarAdapter.filterRate(it)
        }

        binding!!.apply {
            yigilishRv.adapter = tadbirlarAdapter
            addYigilish.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_admin, TadbirQoshish())
                    .addToBackStack(null)
                    .commit()
                bottomNavigation.visibility = View.GONE
                toolbar.visibility = View.GONE
            }
            yigilishSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        searchTadbir(query)
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        searchTadbir(newText)
                    }
                    return false
                }
            })
        }

        return binding!!.root
    }

    private fun searchTadbir(query: String) {
        val tadbirlar: MutableList<TadbirlarDataItem> = mutableListOf()
        for (i in 0 until tadbirlarList.size) {
            if (tadbirlarList[i].name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                tadbirlar.add(tadbirlarList[i])
            }
        }
        tadbirlarAdapter.filterYigilish(tadbirlar)
        tadbirlarAdapter.filterRate(rateList)
    }

    private fun getYigilishlar() {
        lifecycleScope.launch {
            if (isAdded) {
                if (tadbirlarList.isEmpty()) {
                    yigilishlarAdminVm.getYigilishlar(requireContext())
                    yigilishlarAdminVm._stateYigilishlar.collect {
                        when (it) {
                            is Resource.Error -> {
                                Log.d(TAG, "getYigilishlar: ${it.e.message}")
                            }

                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                tadbirlarList = it.data
                                liveDates.tarbirlarLiveData.value = tadbirlarList
                            }
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
            Tadbirlar().apply {
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

    fun getStudents() {
        lifecycleScope.launch {
            if (isAdded) {
                try {
                    yigilishlarAdminVm.getStudents(requireContext())
                    yigilishlarAdminVm._students.collect{
                        when (it) {
                            is Resource.Error -> {
                                Log.d(TAG, "getStudents: ${it.e.message}")
                            }
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                studentlarList = it.data
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "getStudents: ${e.message}")
                }
            }
        }
    }
}