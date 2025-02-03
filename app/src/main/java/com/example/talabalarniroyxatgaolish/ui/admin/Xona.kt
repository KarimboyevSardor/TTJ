package com.example.talabalarniroyxatgaolish.ui.admin

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.talabalarniroyxatgaolish.R
import com.example.talabalarniroyxatgaolish.adapter.XonaViewPagerAdapter
import com.example.talabalarniroyxatgaolish.data.XonaDataItem
import com.example.talabalarniroyxatgaolish.databinding.FragmentXonaAdminBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.studentlarList
import com.example.talabalarniroyxatgaolish.utils.Utils.xonalarList
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import com.example.talabalarniroyxatgaolish.vm.Resource
import com.example.talabalarniroyxatgaolish.vm.XonaAdminVm
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.log

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Xona : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var TAG = "XONAADMIN"
    lateinit var liveDates: LiveDates
    lateinit var xonaAdminVm: XonaAdminVm
    private var binding: FragmentXonaAdminBinding? = null
    lateinit var viewPagerAdapter: XonaViewPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentXonaAdminBinding.inflate(layoutInflater)

        val xona: XonaDataItem = arguments?.getParcelable("xona")!!
        xonaAdminVm = ViewModelProvider(requireActivity())[XonaAdminVm::class]
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class]

        binding!!.apply {
            toolbar.title = xona.room_count + " - xona ma'lumotlari"
            setHasOptionsMenu(true)
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            val backIcon = toolbar.navigationIcon
            backIcon?.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            toolbar.navigationIcon = backIcon
            toolbar.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
            toolbar.setOnMenuItemClickListener {
                showDeleteDialog(xona)
                true
            }
            tabLayout.addTab(tabLayout.newTab().setText("Xona ma'lumotlarini o'zgartirish"))
            tabLayout.addTab(tabLayout.newTab().setText("Xonada yashovchi studentlar"))
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: Tab?) {
                    if (tab != null) {
                        viewPager2.currentItem = tab.position
                    }
                }
                override fun onTabUnselected(tab: Tab?) {}
                override fun onTabReselected(tab: Tab?) {}
            })
            viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tabLayout.selectTab(tabLayout.getTabAt(position))
                }
            })
            viewPagerAdapter = XonaViewPagerAdapter(this@Xona, xona)
            viewPager2.adapter = viewPagerAdapter
        }

        return binding!!.root
    }

    private fun showDeleteDialog(xona: XonaDataItem) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Xona ma'lumotlarini o'chirish")
            .setMessage("Agarda bu xonani o'chirsangiz xonaga oid ma'lumotlar barchasi o'chib ketadi.")
            .setNegativeButton("Bekor qilish"
            ) { dialog, id ->
                dialog.dismiss()
            }
            .setPositiveButton("O'chirish"
            ) { dialog, id ->
                deleteRoom(xona)
            }
            .create()
        dialog.show()
    }

    private fun deleteRoom(xonaDataItem: XonaDataItem) {
        lifecycleScope.launch {
            val updateStudent = studentlarList.filter { it.room_id == xonaDataItem.id }.toMutableList()
            for (i in 0 until updateStudent.size) {
                updateStudent[i].room_id = -1
                delay(100)
                xonaAdminVm.updateStudent(requireContext(), requireActivity(), updateStudent[i])
            }
            if (isAdded) {
                try {
                    xonaAdminVm.xonaDelete(requireContext(), xonaDataItem.id)
                    xonaAdminVm._xonaDelete.collect{
                        when (it) {
                            is Resource.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    "Server bilan bog'lanib bo'lmadi.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d(TAG, "deleteRoom: ${it.e.message}")
                            }
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                xonalarList.remove(xonaDataItem)
                                liveDates.xonalarLiveData.value = xonalarList
                                requireActivity().onBackPressed()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "deleteRoom: ${e.message}")
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Xona().apply {
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.ochirish_menu, menu)
    }
}