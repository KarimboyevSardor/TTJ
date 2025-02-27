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
import com.example.talabalarniroyxatgaolish.adapter.XonaAdapter
import com.example.talabalarniroyxatgaolish.data.XonaDataItem
import com.example.talabalarniroyxatgaolish.databinding.FragmentDavomatAdminBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.davomatList
import com.example.talabalarniroyxatgaolish.utils.Utils.xonalarList
import com.example.talabalarniroyxatgaolish.vm.DavomatAdminVm
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import com.example.talabalarniroyxatgaolish.vm.Resource
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale

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
    lateinit var xonaAdapter: XonaAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDavomatAdminBinding.inflate(layoutInflater)
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class]
        davomatAdminVm = ViewModelProvider(requireActivity())[DavomatAdminVm::class]
        val date = arguments?.getString("date")
        getRoom()
        xonaAdapter = XonaAdapter(xonalarList) { xona ->
            val bundle = Bundle()
            val fr = XonaDavomat()
            bundle.putLong("room_id", xona.id)
            bundle.putString("date", date)
            fr.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_admin, fr)
                .addToBackStack(null)
                .commit()
        }
        liveDates.getXonalar().observe(requireActivity()) {
            xonaAdapter.filter(it)
        }
        binding!!.apply {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            val backIcon = toolbar.navigationIcon
            backIcon?.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            toolbar.navigationIcon = backIcon
            toolbar.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
            roomRv.adapter = xonaAdapter
            searchview.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
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

    private fun xonaQidirish(query: String) {
        var xonaList: MutableList<XonaDataItem> = mutableListOf()
        for (i in 0 until xonalarList.size) {
            if (xonalarList[i].room_count.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                xonaList.add(xonalarList[i])
            }
        }
        xonaAdapter.filter(xonaList)
    }

    private fun getRoom() {
        if (xonalarList.isEmpty()) {
            lifecycleScope.launch {
                if (isAdded) {
                    try {
                        davomatAdminVm.getXona(requireContext())
                        davomatAdminVm._xona.collect {
                            when (it) {
                                is Resource.Error -> {
                                    Toast.makeText(requireContext(), "Server bilan bog'lanib bo'lmadi.", Toast.LENGTH_SHORT).show()
                                }
                                is Resource.Loading -> {}
                                is Resource.Success -> {
                                    xonalarList = it.data
                                    liveDates.xonalarLiveData.value = xonalarList
                                    Log.d(TAG, "getRoom: $xonalarList")
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.d(TAG, "getRoom: ${e.message}")
                    }
                }
            }
        }
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