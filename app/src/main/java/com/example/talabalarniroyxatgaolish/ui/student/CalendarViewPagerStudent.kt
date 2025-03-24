package com.example.talabalarniroyxatgaolish.ui.student

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.talabalarniroyxatgaolish.R
import com.example.talabalarniroyxatgaolish.adapter.CalendarAdapter
import com.example.talabalarniroyxatgaolish.data.Date
import com.example.talabalarniroyxatgaolish.databinding.FragmentCalendarViewPagerStudentBinding
import com.example.talabalarniroyxatgaolish.ui.admin.Davomat
import com.example.talabalarniroyxatgaolish.utils.Utils.davomatList
import com.example.talabalarniroyxatgaolish.utils.Utils.myInfo
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.LocalDate
import java.time.YearMonth
import java.util.Calendar

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CalendarViewPagerStudent : Fragment() {
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

    private var binding: FragmentCalendarViewPagerStudentBinding? = null
    lateinit var calendarAdapter: CalendarAdapter
    lateinit var dayList: MutableList<Date>
    private val TAG = "CALENDARVIEWPAGERSTUDENT"
    lateinit var liveDates: LiveDates
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCalendarViewPagerStudentBinding.inflate(layoutInflater)
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class]
        val yearMonth = YearMonth.of(yil!!, oy!!)
        dayCount = yearMonth.lengthOfMonth()
        setMonthYear()
        dayList = generateMonthDays()
        calendarAdapter = CalendarAdapter(dayList) {}
        calendarAdapter.filter(dayList)
        liveDates.getDavomat().observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                setColor()
                calendarAdapter.filter(dayList)
            }
        }
        binding!!.apply {
            calendarView.adapter = calendarAdapter
            oyTv.setOnClickListener {
                showMonthYearPicker()
            }
        }

        return binding!!.root
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

    fun formatDate(inputDate: String): String {
        val parts = inputDate.split("-")
        if (parts.size != 3) return "Noto'g'ri format"
        val year = parts[0]
        val month = parts[1].padStart(2, '0') // Oy oldiga 0 qo'shish
        val day = parts[2].padStart(2, '0')   // Kun oldiga 0 qo'shish
        return "$year-$month-$day"
    }


    private fun getDayHafta() : Int {
        val date = LocalDate.of(yil!!, oy!!, 1)
        return date.dayOfWeek.value
    }

    private fun generateMonthDays(): MutableList<Date> {
        val oq = ContextCompat.getColor(requireContext(), R.color.white)
        var daysList: MutableList<Date> = mutableListOf()
        val haftaKuni = getDayHafta()
        var i = 1
        var i1 = 2
        var weekcounter = 1
        var dayCounter = 1
        var isAdd = false
        while (i < 42) {
            if (dayCounter - 1 == dayCount) {
                dayCounter = 1
                daysList.add(Date(oy!! + 1, yil!!, weekcounter, dayCounter, false, color = oq))
                dayCounter++
                break
            }
            if (isAdd) {
                daysList.add(Date(oy!!, yil!!, weekcounter, dayCounter, true, color = oq))
                dayCounter++
            } else {
                if (weekcounter == haftaKuni) {
                    isAdd = true
                    daysList.add(Date(oy!!, yil!!, weekcounter, dayCounter, true, color = oq))
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
                    daysList.add(Date(oy1, yil1, weekcounter, day, false, color = oq))
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
            daysList.add(Date(oy!! + 1, yil!!, weekcounter, dayCounter, false, color = oq))
            if (weekcounter == 7) {
                weekcounter = 1
            }
            dayCounter++
            weekcounter++
        }
        return daysList
    }

    private fun setColor() {
        val qizil = ContextCompat.getColor(requireContext(), R.color.red)
        val yashil = ContextCompat.getColor(requireContext(), R.color.light_green)
        val qidiriluvchiList = davomatList.filter { !it.is_there && it.student_id == myInfo!!.id}.map { it.date }.toHashSet()
        for (item in dayList) {
            if (item.isThisMonth) {
                if (formatDate("${item.yil}-${item.oy}-${item.kun}") in qidiriluvchiList) {
                    item.color = qizil
                } else {
                    item.color = yashil
                }
            }
        }
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
            CalendarViewPagerStudent().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, oy)
                    putInt(ARG_PARAM2, yil)
                }
            }
    }
}