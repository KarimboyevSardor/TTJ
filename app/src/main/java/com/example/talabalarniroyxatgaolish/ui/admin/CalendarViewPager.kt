package com.example.talabalarniroyxatgaolish.ui.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.talabalarniroyxatgaolish.adapter.CalendarAdapter
import com.example.talabalarniroyxatgaolish.data.Date
import com.example.talabalarniroyxatgaolish.databinding.FragmentCalendarViewPagerAdminBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.dates
import java.time.LocalDate
import java.time.YearMonth
import java.util.Calendar
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

class CalendarViewPager : Fragment() {
    private var oy: Int? = null
    private var yil: Int? = null
    private var dayCount: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            oy = it.getInt(ARG_PARAM1)
            yil = it.getInt(ARG_PARAM2)
//            dayCount = it.getInt(ARG_PARAM3)

        }
    }

    private var binding: FragmentCalendarViewPagerAdminBinding? = null
    private val TAG = "CALENDARVIEWPAGERADMIN"
    lateinit var calendarAdapter: CalendarAdapter
    lateinit var dayList: MutableList<Date>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarViewPagerAdminBinding.inflate(layoutInflater)
        val yearMonth = YearMonth.of(yil!!, oy!!)
        dayCount = yearMonth.lengthOfMonth()
        setMonthYear()
        dayList = generateMonthDays()
        //setCalendar()
        calendarAdapter = CalendarAdapter(dayList) {
            Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
        }
        calendarAdapter.filter(dayList)
        binding!!.apply {
//            val gridLayoutManager = GridLayoutManager(requireContext(), 7, RecyclerView.VERTICAL, true)
//            calendarView.layoutManager = gridLayoutManager
            calendarView.adapter = calendarAdapter
        }

        return binding!!.root
    }

    private fun getDayHafta() : Int {
        val date = LocalDate.of(yil!!, oy!!, 1)
        return date.dayOfWeek.value
    }

    private fun generateMonthDays(): MutableList<Date> {
        var daysList: MutableList<Date> = mutableListOf()
        val haftaKuni = getDayHafta()
        var i = 1
        var i1 = 2
        var weekcounter = 1
        var dayCounter = 1
        var isAdd = false
        var isNextMonth = false
        while (i < 42) {
            if (dayCounter - 1 == dayCount) {
                dayCounter = 1
                daysList.add(Date(oy!! + 1, yil!!, weekcounter, dayCounter, false))
                dayCounter++
                break
            }
            if (isAdd) {
                daysList.add(Date(oy!!, yil!!, weekcounter, dayCounter, true))
                dayCounter++
            } else {
                if (weekcounter == haftaKuni) {
                    isAdd = true
                    daysList.add(Date(oy!!, yil!!, weekcounter, dayCounter, true))
                    dayCounter++
                } else {
                    isAdd = false
                    var oy1: Int
                    var yil1 = 0
                    if (oy == 1) {
                        oy1 = 12
                        yil1 = yil!! - 1
                    } else {
                        yil1 = yil!!
                        oy1 = oy!! - 1
                    }
                    val day = getDayCount(yil1, oy1) - (haftaKuni - i1)
                    daysList.add(Date(oy1, yil1, weekcounter, day, false))
                }
            }
            if (weekcounter == 7) {
                weekcounter = 0
            }
            weekcounter++
            i++
            i1++
        }
        while (daysList.size < 42) {
            weekcounter++
            daysList.add(Date(oy!! + 1, yil!!, weekcounter, dayCounter, false))
            if (weekcounter == 7) {
                weekcounter = 1
            }
            dayCounter++
            weekcounter++
        }
        return daysList
    }

    private fun setMonthYear() {
        var oyName = ""
        when (oy) {
            1 -> oyName = "Yanvar"
            2 -> oyName = "Fevral"
            3 -> oyName = "Mart"
            4 -> oyName = "Aprel"
            5 -> oyName = "May"
            6 -> oyName = "Iyun"
            7 -> oyName = "Iyul"
            8 -> oyName = "Avgust"
            9 -> oyName = "Sentyabr"
            10 -> oyName = "Oktyabr"
            11 -> oyName = "Noyabr"
            12 -> oyName = "Dekabr"
        }
        binding!!.oyTv.text = "$oyName $yil"
    }

    private fun getDayCount(yil1: Int, oy1: Int) : Int {
        val yearMonth = YearMonth.of(yil1, oy1)
        return yearMonth.lengthOfMonth()
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
//                    putInt(ARG_PARAM3, dayCount)
                }
            }
    }
}