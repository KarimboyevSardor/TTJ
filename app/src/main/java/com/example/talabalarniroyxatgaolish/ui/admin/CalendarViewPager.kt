package com.example.talabalarniroyxatgaolish.ui.admin

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.talabalarniroyxatgaolish.R
import com.example.talabalarniroyxatgaolish.adapter.CalendarAdapter
import com.example.talabalarniroyxatgaolish.data.Date
import com.example.talabalarniroyxatgaolish.databinding.FragmentCalendarViewPagerAdminBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.davomatList
import com.example.talabalarniroyxatgaolish.utils.Utils.studentlarList
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.LocalDate
import java.time.YearMonth
import java.util.Calendar

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
        setColor()
        //setCalendar()
        val bottomNavigation: FrameLayout = requireActivity().findViewById(R.id.bottom_navigation_admin)
        val toolbar1: Toolbar = requireActivity().findViewById(R.id.bosh_toolbar_admin)
        calendarAdapter = CalendarAdapter(dayList) {
            val date = "${it.yil}-${it.oy}-${it.kun}"
                val bundle = Bundle()
                bundle.putString("date", date)
                val fr = Davomat()
                fr.arguments = bundle
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_admin, fr)
                    .addToBackStack(null)
                    .commit()
                toolbar1.visibility = View.GONE
                bottomNavigation.visibility = View.GONE
        }
        calendarAdapter.filter(dayList)
        binding!!.apply {
//            val gridLayoutManager = GridLayoutManager(requireContext(), 7, RecyclerView.VERTICAL, true)
//            calendarView.layoutManager = gridLayoutManager
            calendarView.adapter = calendarAdapter
            oyTv.setOnClickListener {
                showMonthYearPicker()
            }
        }

        return binding!!.root
    }

    private fun setColor() {
        val yashil = ContextCompat.getColor(requireContext(), R.color.light_green)
        for (i in 0 until dayList.size) {
            val currentDayAttendance = davomatList.filter { formatDate(it.date) == "${dayList[i].yil}-${dayList[i].oy}-${dayList[i].kun}" }
            Log.d(TAG, "setColor: ${currentDayAttendance.size}, ${dayList[i].yil}-${dayList[i].oy}-${dayList[i].kun}")
            if (currentDayAttendance.size == studentlarList.size) {
                dayList[i].color = yashil
            }
        }
    }

    private fun showMonthYearPicker() {
        val constraintsBuilder = CalendarConstraints.Builder()

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Oy va yilni tanlang")
            .setCalendarConstraints(constraintsBuilder.build())
            .build()
        datePicker.show(parentFragmentManager, "MONTH_YEAR_PICKER")

        datePicker.addOnPositiveButtonClickListener { selection ->
            val calendar = Calendar.getInstance().apply { timeInMillis = selection as Long }
            val month = calendar.get(Calendar.MONTH) + 1
            val year = calendar.get(Calendar.YEAR)
            Toast.makeText(requireContext(), "Tanlangan: $month/$year", Toast.LENGTH_SHORT).show()
        }
    }

    fun formatDate(date: String): String {
        val parts = date.split("-")
        if (parts.size != 3) return date
        val year = parts[0]
        val month = parts[1].toInt().toString()
        val day = parts[2].toInt().toString()
        return "$year-$month-$day"
    }


    private fun getDayHafta() : Int {
        val date = LocalDate.of(yil!!, oy!!, 1)
        return date.dayOfWeek.value
    }

    private fun generateMonthDays(): MutableList<Date> {
        val yashil = ContextCompat.getColor(requireContext(), R.color.light_green)
        val qizil = ContextCompat.getColor(requireContext(), R.color.red_light_coral)
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
                daysList.add(Date(oy!! + 1, yil!!, weekcounter, dayCounter, false, color = qizil))
                dayCounter++
                break
            }
            if (isAdd) {
                daysList.add(Date(oy!!, yil!!, weekcounter, dayCounter, true, color = qizil))
                dayCounter++
            } else {
                if (weekcounter == haftaKuni) {
                    isAdd = true
                    daysList.add(Date(oy!!, yil!!, weekcounter, dayCounter, true, color = qizil))
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
                    daysList.add(Date(oy1, yil1, weekcounter, day, false, color = qizil))
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
            daysList.add(Date(oy!! + 1, yil!!, weekcounter, dayCounter, false, color = qizil))
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

    fun helloWorld() {
        fun helloWorld1() {
            Log.d(TAG, "helloWorld: onDestroyView")
        }
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