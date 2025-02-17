package com.example.talabalarniroyxatgaolish.ui.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.talabalarniroyxatgaolish.R
import com.example.talabalarniroyxatgaolish.adapter.TadbirViewPagerAdapter
import com.example.talabalarniroyxatgaolish.data.TadbirlarDataItem
import com.example.talabalarniroyxatgaolish.databinding.FragmentTadbirAdminBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.tadbirlarList
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import com.example.talabalarniroyxatgaolish.vm.Resource
import com.example.talabalarniroyxatgaolish.vm.TadbirAdminVm
import com.example.talabalarniroyxatgaolish.vm.YigilishlarniYangilashOchirishAdminVm
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class Tadbir : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var binding: FragmentTadbirAdminBinding? = null
    private var tadbirId: Long = 0
    private var tadbir: TadbirlarDataItem? = null
    lateinit var tadbirViewPagerAdapter: TadbirViewPagerAdapter
    lateinit var yigilishlarniYangilashOchirishAdminVm: YigilishlarniYangilashOchirishAdminVm
    lateinit var liveDates: LiveDates
    lateinit var tadbirAdminVm: TadbirAdminVm
    private val TAG = "TADBIRADMIN"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTadbirAdminBinding.inflate(layoutInflater)
        yigilishlarniYangilashOchirishAdminVm = ViewModelProvider(requireActivity())[YigilishlarniYangilashOchirishAdminVm::class]
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class]
        tadbirAdminVm = ViewModelProvider(requireActivity())[TadbirAdminVm::class]
        tadbirId = arguments?.getLong("yigilish")!!
//        getBaholash(tadbirId)
        tadbir = tadbirlarList.filter { it.id == tadbirId }[0]
        binding!!.apply {
            toolbar.title = tadbir!!.name
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
                deleteYigilish(tadbirId)
                true
            }
            tablayout.addTab(tablayout.newTab().setText("Qatnashuvchilarni baholash"))
            tablayout.addTab(tablayout.newTab().setText("Ma'lumotlarini o'zgartirish"))
            tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        viewpager2.currentItem = tab.position
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
            viewpager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tablayout.selectTab(tablayout.getTabAt(position))
                }
            })
            tadbirViewPagerAdapter = TadbirViewPagerAdapter(this@Tadbir, tadbirId)
            viewpager2.adapter = tadbirViewPagerAdapter
        }

        return binding!!.root
    }

    private fun deleteYigilish(param1: Long) {
        lifecycleScope.launch {
            yigilishlarniYangilashOchirishAdminVm.deleteYigilish(param1, requireContext())
            try {
                yigilishlarniYangilashOchirishAdminVm._stateDeleteYigilish.collect{
                    when (it) {
                        is Resource.Error -> {
                            Toast.makeText(requireContext(), "${it.e.message}", Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            requireActivity().onBackPressed()
                            tadbirlarList.remove(tadbirlarList.filter { it.id == param1 }[0])
                            liveDates.tarbirlarLiveData.value = tadbirlarList
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "deleteYigilish: ${e.message}")
            }
        }
    }

//    private fun getBaholash(param1: Long) {
//        lifecycleScope.launch {
//            if (isAdded) {
//                try {
//                    tadbirAdminVm.getRateMeetingId(requireContext(), param1, requireActivity())
//                } catch (e: Exception) {
//                    Log.d(TAG, "getBaholash: ${e.message}")
//                }
//            }
//        }
//    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Tadbir().apply {
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