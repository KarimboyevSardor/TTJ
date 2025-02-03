package com.example.talabalarniroyxatgaolish.ui.admin

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.ColoredDate
import com.ak.EventObjects
import com.example.talabalarniroyxatgaolish.R
import com.example.talabalarniroyxatgaolish.adapter.DavomatAdapter
import com.example.talabalarniroyxatgaolish.data.CalendarDateData
import com.example.talabalarniroyxatgaolish.data.DateColor
import com.example.talabalarniroyxatgaolish.databinding.FragmentEskiDavomatAdminBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.davomatList
import com.example.talabalarniroyxatgaolish.vm.EskiDavomatAdminVm
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import com.example.talabalarniroyxatgaolish.vm.Resource
import kotlinx.coroutines.launch


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EskiDavomat : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var binding: FragmentEskiDavomatAdminBinding? = null
    lateinit var eskiDavomatAdminVm: EskiDavomatAdminVm
    lateinit var liveDates: LiveDates
    lateinit var davomatAdapter: DavomatAdapter
    private val TAG = "ESKIDAVOMATADMIN"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEskiDavomatAdminBinding.inflate(layoutInflater)
        eskiDavomatAdminVm = ViewModelProvider(requireActivity())[EskiDavomatAdminVm::class]
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class]
        val bottomNavigation: FrameLayout = requireActivity().findViewById(R.id.bottom_navigation_admin)
        val toolbar1: Toolbar = requireActivity().findViewById(R.id.bosh_toolbar_admin)
        toolbar1.visibility = View.VISIBLE
        bottomNavigation.visibility = View.VISIBLE
        getDavomat()
        davomatAdapter = DavomatAdapter(davomatList.filter { !it.is_there }.toMutableList(), requireContext())
        liveDates.getDavomat().observe(requireActivity()) { it ->
            davomatAdapter.filter(it.filter { !it.is_there }.toMutableList())
        }

        binding!!.apply {
            studentDavomatRv.adapter = davomatAdapter
            calenarView.setOnDateChangeListener { p0, p1, p2, p3 ->
                val date = "$p1-${p2 + 1}-$p3"
                val bundle = Bundle()
                bundle.putString("date", date)
                val fr = Davomat()
                fr.arguments = bundle
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_admin, fr)
                    .addToBackStack(null)
                    .commit()
                toolbar1.visibility = View.GONE
                bottomNavigation.visibility = View.GONE
            }
        }

        return binding!!.root
    }

    private fun getDavomat() {
        lifecycleScope.launch {
            if (isAdded) {
                try {
                    if (davomatList.isEmpty()) {
                        eskiDavomatAdminVm.getDavomat(requireContext())
                        eskiDavomatAdminVm._davomat.collect {
                            when (it) {
                                is Resource.Error -> {
                                    Toast.makeText(
                                        requireContext(),
                                        "Server bilan bog'lanib bo'lmadi.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                is Resource.Loading -> {}
                                is Resource.Success -> {
                                    davomatList = it.data
                                    liveDates.davomatLiveData.value = davomatList
                                }
                            }
                        }
                    }
                } catch (e: Exception) {

                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EskiDavomat().apply {
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