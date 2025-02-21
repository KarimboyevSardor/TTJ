package com.example.talabalarniroyxatgaolish.adapter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.talabalarniroyxatgaolish.ui.admin.CalendarViewPager
import com.example.talabalarniroyxatgaolish.utils.Utils.oyList
import java.time.YearMonth

class CalendarViewPagerAdapter(fr: Fragment, val count: Int) : FragmentStateAdapter(fr) {
    override fun getItemCount(): Int {
        return count
    }

    override fun createFragment(position: Int): Fragment {
        return CalendarViewPager.newInstance(oyList[position].oyCount, oyList[position].yil)
    }

}