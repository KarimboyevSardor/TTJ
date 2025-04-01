package com.example.talabalarniroyxatgaolish.ui.admin

import android.app.AlertDialog
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
import com.example.talabalarniroyxatgaolish.adapter.XonaAdapter
import com.example.talabalarniroyxatgaolish.data.XonaDataItem
import com.example.talabalarniroyxatgaolish.databinding.AddXonaDialogBinding
import com.example.talabalarniroyxatgaolish.databinding.FragmentXonalarAdminBinding
import com.example.talabalarniroyxatgaolish.network.ApiClient
import com.example.talabalarniroyxatgaolish.network.ApiService
import com.example.talabalarniroyxatgaolish.utils.Utils.xonalarList
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import com.example.talabalarniroyxatgaolish.vm.Resource
import com.example.talabalarniroyxatgaolish.vm.XonalarAdminVm
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Xonalar : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var TAG = "XONALARADMIN"
    lateinit var liveDates: LiveDates
    lateinit var xonalarAdminVm: XonalarAdminVm
    lateinit var xonaAdapter: XonaAdapter
    private var addedCount = 0
    private var binding: FragmentXonalarAdminBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentXonalarAdminBinding.inflate(layoutInflater)
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class]
        xonalarAdminVm = ViewModelProvider(requireActivity())[XonalarAdminVm::class]
        val bottomNavigation: FrameLayout = requireActivity().findViewById(R.id.bottom_navigation_admin)
        val toolbar1: Toolbar = requireActivity().findViewById(R.id.bosh_toolbar_admin)
        toolbar1.visibility = View.VISIBLE
        bottomNavigation.visibility = View.VISIBLE
        isCheckEmpty()
        xonaAdapter = XonaAdapter(xonalarList) {
            val bundle = Bundle()
            val fm = Xona()
            bundle.putParcelable("xona", it)
            fm.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_admin, fm)
                .addToBackStack(null)
                .commit()
            bottomNavigation.visibility = View.GONE
            toolbar1.visibility = View.GONE
        }

        liveDates.getXonalar().observe(requireActivity()) {
            xonaAdapter.filter(it)
        }
        getXona()

        binding!!.apply {
            roomsShimmer.startShimmer()
            xonaRv.adapter = xonaAdapter
            addRoom.setOnClickListener {
                showDialog()
            }

            xonaSearch.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        xonaQidirish(query)
                    }
                    return false
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        xonaQidirish(newText)
                    }
                    return false
                }
            })
        }

        return binding!!.root
    }

    private fun isCheckEmpty() {
        if (xonalarList.isNotEmpty()) {
            binding!!.roomsShimmer.stopShimmer()
            binding!!.xonaRv.visibility = View.VISIBLE
            binding!!.roomsShimmer.visibility = View.GONE
        }
    }

    private fun xonaQidirish(query: String) {
        var xonaList: MutableList<XonaDataItem> = mutableListOf()
        for (i in 0 until xonalarList.size) {
            if (xonalarList[i].room_count.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                xonaList.add(xonalarList[i])
            }
        }
        xonaAdapter.filter(xonaList)
    }

    private fun showDialog() {
        addedCount = 0
        val dialog = AlertDialog.Builder(requireContext()).create()
        val dialogBinding = AddXonaDialogBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)
        dialogBinding.apply {
            xonaQoshishBtn.setOnClickListener {
                if (xonalarList.any { it.room_count == xonaNameEt.text.toString() }) {
                    xonaNameEt.error = "Bunday xona mavjud!"
                } else if (xonaNameEt.text.toString().isNotEmpty()) {
                    addXona(xonaNameEt.text.toString())
                    dialog.dismiss()
                } else if (xonaNameEt.text.toString().isEmpty()) {
                    xonaNameEt.error = "Xona nomi bo'sh!"
                }
            }
        }
        dialog.show()
    }

    private fun addXona(xona: String) {
        val apiService = ApiClient.getRetrofit(requireContext()).create(ApiService::class.java)
        apiService.addXona(XonaDataItem(0, xona)).enqueue(object : Callback<XonaDataItem>{
            override fun onResponse(call: Call<XonaDataItem>, response: Response<XonaDataItem>) {
                if (response.isSuccessful) {
                    xonalarList.add(response.body()!!)
                    liveDates.xonalarLiveData.value = xonalarList
                    Toast.makeText(requireContext(), "Xona qo'shildi.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<XonaDataItem>, t: Throwable) {
                Toast.makeText(requireContext(), "Server bilan bog'lanib bo'lmadi.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getXona() {
        lifecycleScope.launch {
            if (isAdded) {
                try {
                    if (xonalarList.isEmpty()) {
                        xonalarAdminVm.getXona(requireContext())
                        xonalarAdminVm._stateXona.collect {
                            when (it) {
                                is Resource.Error -> {
                                    Toast.makeText(
                                        requireContext(),
                                        "Server bilan bog'lanib bo'lmadi.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d(TAG, "getXona: ${it.e.message}")
                                    binding!!.roomsShimmer.stopShimmer()
                                    binding!!.roomsShimmer.visibility = View.GONE
                                }

                                is Resource.Loading -> {

                                }

                                is Resource.Success -> {
                                    liveDates.xonalarLiveData.value = it.data
                                    xonalarList = it.data
                                    isCheckEmpty()
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    throw e
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Xonalar().apply {
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