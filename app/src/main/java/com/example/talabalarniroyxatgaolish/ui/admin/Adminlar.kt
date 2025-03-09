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
import com.example.talabalarniroyxatgaolish.adapter.AdminlarAdapter
import com.example.talabalarniroyxatgaolish.databinding.FragmentAdminlarBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.adminsList
import com.example.talabalarniroyxatgaolish.vm.AdminVm
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import com.example.talabalarniroyxatgaolish.vm.Resource
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Adminlar : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var binding: FragmentAdminlarBinding? = null
    lateinit var adminVm: AdminVm
    lateinit var liveDates: LiveDates
    private val TAG = "ADMINLAR"
    lateinit var adminlarAdapter: AdminlarAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminlarBinding.inflate(layoutInflater)
        adminVm = ViewModelProvider(requireActivity())[AdminVm::class.java]
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class.java]
        val bottomNavigation: FrameLayout = requireActivity().findViewById(R.id.bottom_navigation_admin)
        val toolbar1: Toolbar = requireActivity().findViewById(R.id.bosh_toolbar_admin)
        bottomNavigation.visibility = View.VISIBLE
        toolbar1.visibility = View.VISIBLE
        adminlarAdapter = AdminlarAdapter(
            adminsList,
            editOnClick = { admin ->
                val bundle = Bundle()
                val fr = EditAdmin()
                bundle.putParcelable("admin", admin)
                fr.arguments = bundle
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_admin, fr)
                    .addToBackStack(null)
                    .commit()
                bottomNavigation.visibility = View.GONE
                toolbar1.visibility = View.GONE
            }
        ) { id ->
            adminVm.deleteAdmin(requireContext(), requireActivity(), id)
        }
        getAdmins()
        liveDates.getAdminlar().observe(requireActivity()) {
            adminlarAdapter.updateList(it)
        }
        binding?.apply {
            recyclerViewAdmins.adapter = adminlarAdapter
            addAdmin.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_admin, AddAdmin())
                    .addToBackStack(null)
                    .commit()
                bottomNavigation.visibility = View.GONE
                toolbar1.visibility = View.GONE
            }
        }

        return binding?.root
    }

    private fun getAdmins() {
        lifecycleScope.launch {
            if (isAdded) {
                try {
                    adminVm.getAdmins(requireContext())
                    adminVm._adminlar.collect {
                        when (it) {
                            is Resource.Error -> {
                                Toast.makeText(requireContext(), "${it.e.message}", Toast.LENGTH_SHORT).show()
                            }
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                adminsList = it.data
                                liveDates.adminlarLiveData.value = adminsList
                                Log.d(TAG, "getAdmins: ${it.data}")
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "getAdmins: ${e.message}")
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Adminlar().apply {
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