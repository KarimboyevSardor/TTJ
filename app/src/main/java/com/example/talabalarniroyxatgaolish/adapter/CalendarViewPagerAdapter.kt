package com.example.talabalarniroyxatgaolish.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.talabalarniroyxatgaolish.data.Oy
import com.example.talabalarniroyxatgaolish.ui.admin.CalendarViewPager

class CalendarViewPagerAdapter(fr: Fragment, val count: Int, var oy: Int, var yil: Int) : FragmentStateAdapter(fr) {
    override fun getItemCount(): Int {
        return count
    }

    override fun createFragment(position: Int): Fragment {
        if (position != 0) {
            oy -= 1
            if (oy == 0) {
                oy = 12
                yil -= 1
            }
        }
        return CalendarViewPager.newInstance(oy, yil)
    }

}