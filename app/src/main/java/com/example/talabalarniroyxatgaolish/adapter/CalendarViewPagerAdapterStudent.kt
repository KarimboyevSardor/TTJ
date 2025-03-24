package com.example.talabalarniroyxatgaolish.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.talabalarniroyxatgaolish.ui.admin.CalendarViewPager
import com.example.talabalarniroyxatgaolish.ui.student.CalendarViewPagerStudent
import com.example.talabalarniroyxatgaolish.utils.Utils.oyList

class CalendarViewPagerAdapterStudent(fr: Fragment, private val count: Int) : FragmentStateAdapter(fr) {
    override fun getItemCount(): Int {
        return count
    }

    override fun createFragment(position: Int): Fragment {
        return CalendarViewPagerStudent.newInstance(oyList[position].oyCount, oyList[position].yil)
    }
}