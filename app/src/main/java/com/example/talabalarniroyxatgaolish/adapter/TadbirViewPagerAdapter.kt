package com.example.talabalarniroyxatgaolish.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.talabalarniroyxatgaolish.ui.admin.BaxolashViewPager
import com.example.talabalarniroyxatgaolish.ui.admin.TarbirYangilashOchirishViewPager

class TadbirViewPagerAdapter(fr: Fragment, val yigilishId: Long) : FragmentStateAdapter(fr) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> {
                BaxolashViewPager.newInstance(yigilishId)
            }
            1 -> {
                TarbirYangilashOchirishViewPager.newInstance(yigilishId)
            }
            else -> {
                Fragment()
            }
        }
    }

}