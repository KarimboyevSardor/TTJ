package com.example.talabalarniroyxatgaolish.ui.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.talabalarniroyxatgaolish.adapter.TadbirViewPagerAdapter
import com.example.talabalarniroyxatgaolish.data.TadbirlarDataItem
import com.example.talabalarniroyxatgaolish.databinding.FragmentTadbirAdminBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.tadbirlarList
import com.google.android.material.tabs.TabLayout

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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTadbirAdminBinding.inflate(layoutInflater)
        tadbirId = arguments?.getLong("yigilish")!!
        tadbir = tadbirlarList.filter { it.id == tadbirId }[0]
        binding!!.apply {
            toolbar.title = tadbir!!.name
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
}