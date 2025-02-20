package com.example.talabalarniroyxatgaolish.ui.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.talabalarniroyxatgaolish.databinding.FragmentCalendarViewPagerAdminBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CalendarViewPager : Fragment() {
    private var oy: Int? = null
    private var yil: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            oy = it.getInt(ARG_PARAM1)
            yil = it.getInt(ARG_PARAM2)
        }
    }

    private var binding: FragmentCalendarViewPagerAdminBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarViewPagerAdminBinding.inflate(layoutInflater)
        setCalendar()
        binding!!.apply {

        }

        return binding!!.root
    }

    private fun setCalendar() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(oy: Int, yil: Int) =
            CalendarViewPager().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, oy)
                    putInt(ARG_PARAM2, yil)
                }
            }
    }
}