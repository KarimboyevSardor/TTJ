package com.example.talabalarniroyxatgaolish.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.talabalarniroyxatgaolish.data.XonaDataItem
import com.example.talabalarniroyxatgaolish.ui.admin.XonaOzgaritirishViewPager
import com.example.talabalarniroyxatgaolish.ui.admin.XonaStudentViewPager

class XonaViewPagerAdapter(fm: Fragment, val  xona: XonaDataItem) : FragmentStateAdapter(fm) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> {
                XonaOzgaritirishViewPager.newInstance(xona)
            }
            1 -> {
                XonaStudentViewPager.newInstance(xona)
            }
            else -> {Fragment()}
        }
    }
}