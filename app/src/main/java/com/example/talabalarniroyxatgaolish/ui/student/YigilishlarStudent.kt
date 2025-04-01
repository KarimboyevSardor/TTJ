package com.example.talabalarniroyxatgaolish.ui.student

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.talabalarniroyxatgaolish.adapter.TadbirlarAdapter
import com.example.talabalarniroyxatgaolish.data.TadbirlarDataItem
import com.example.talabalarniroyxatgaolish.databinding.FragmentTadbirlarStudentBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.rateList
import com.example.talabalarniroyxatgaolish.utils.Utils.studentlarList
import com.example.talabalarniroyxatgaolish.utils.Utils.tadbirlarList
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import com.example.talabalarniroyxatgaolish.vm.Resource
import com.example.talabalarniroyxatgaolish.vm.TadbirlarStudentVm
import kotlinx.coroutines.launch
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class YigilishlarStudent : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var tadbirlarAdapter: TadbirlarAdapter
    lateinit var yigilishlarStudentVm: TadbirlarStudentVm
    lateinit var liveDates: LiveDates
    private var binding: FragmentTadbirlarStudentBinding? = null
    private val TAG = "TADBIRLARSTUDENT"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTadbirlarStudentBinding.inflate(layoutInflater)
        yigilishlarStudentVm = ViewModelProvider(requireActivity())[TadbirlarStudentVm::class]
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class]
        getYigilishlar()
        getStudents()
        isCheckEmpty()
        tadbirlarAdapter = TadbirlarAdapter(tadbirlarList, rateList, requireContext()) {}
        liveDates.getYigilish().observe(requireActivity()) {
            tadbirlarAdapter.filterYigilish(it)
        }
        liveDates.getRate().observe(requireActivity()) {
            tadbirlarAdapter.filterRate(it)
        }
        binding!!.apply {
            shimmerYigilish.startShimmer()
            yigilishRv.adapter = tadbirlarAdapter
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

    private fun isCheckEmpty() {
        if (tadbirlarList.isNotEmpty()) {
            binding!!.shimmerYigilish.stopShimmer()
            binding!!.shimmerYigilish.visibility = View.GONE
            binding!!.yigilishRv.visibility = View.VISIBLE
        }
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
                    yigilishlarStudentVm.getYigilishlar(requireContext())
                    yigilishlarStudentVm._stateYigilishlar.collect {
                        when (it) {
                            is Resource.Error -> {
                                Log.d(TAG, "getYigilishlar: ${it.e.message}")
                                binding!!.shimmerYigilish.stopShimmer()
                                binding!!.shimmerYigilish.visibility = View.GONE
                            }

                            is Resource.Loading -> {

                            }
                            is Resource.Success -> {
                                tadbirlarList = it.data
                                liveDates.tarbirlarLiveData.value = tadbirlarList
                                isCheckEmpty()
                            }
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            if (isAdded) {
                yigilishlarStudentVm.getRates(requireContext())
                yigilishlarStudentVm._rates.collect {
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

    fun getStudents() {
        lifecycleScope.launch {
            if (isAdded) {
                try {
                    yigilishlarStudentVm.getStudents(requireContext())
                    yigilishlarStudentVm._students.collect{
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            YigilishlarStudent().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}