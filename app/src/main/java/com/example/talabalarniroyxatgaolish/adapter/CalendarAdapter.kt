package com.example.talabalarniroyxatgaolish.adapter

import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.talabalarniroyxatgaolish.R
import com.example.talabalarniroyxatgaolish.data.Date
import com.example.talabalarniroyxatgaolish.databinding.CalendarDayRvItemBinding
import com.example.talabalarniroyxatgaolish.databinding.UncalendarDayRvItemBinding
import java.util.Calendar

class CalendarAdapter(var calendarList: MutableList<Date>, val onClick: (Date) -> Unit) : Adapter<ViewHolder>() {
    private val TAG = "CALENDARADAPTER"
    private val calendar: Calendar = Calendar.getInstance()
    private val currentD = calendar.get(Calendar.DAY_OF_MONTH)
    private val currentM = calendar.get(Calendar.MONTH) + 1
    private val currentY = calendar.get(Calendar.YEAR)
    private val currentDay = "${currentY}-${currentM}-${currentD}"
    companion object {
        private const val TYPE_FIRST = 0
        private const val TYPE_SECOND = 1
        private const val TYPE_CURRENT_DAY = 3
    }

    override fun getItemViewType(position: Int): Int {
        val days = calendarList[position]
        return if (days.isThisMonth) {
            TYPE_FIRST
        } else {
            TYPE_SECOND
        }
    }

    fun filter(calendarList: MutableList<Date>) {
        this.calendarList = calendarList
        notifyDataSetChanged()
    }

    inner class FirstViewHolder(val firstBinding: CalendarDayRvItemBinding) : ViewHolder(firstBinding.root) {
        fun bind(date: Date) {
            val currentDayToList = "${date.yil}-${date.oy}-${date.kun}"
            if ((currentD <= date.kun && currentM <= date.oy && currentY <= date.yil)) {} else {
                firstBinding.calendarCardviewRvItem.setCardBackgroundColor(date.color)
            }
            if (currentDay == currentDayToList) {
                firstBinding.calendarDayTv.setBackgroundResource(R.drawable.current_day_background)
            }
            firstBinding.calendarDayTv.text = date.kun.toString()
            firstBinding.calendarCardviewRvItem.setOnClickListener {
                if (currentD < date.kun && currentM <= date.oy && currentY <= date.yil) {} else {
                    onClick(date)
                }
            }
        }
    }

//    inner class CurrentDayViewHolder(val currentDayBinding: CurrentDayRvItemBinding) : ViewHolder(currentDayBinding.root) {
//        fun bind(date: Date) {
//            currentDayBinding.calendarDayTv.text = date.kun.toString()
//            currentDayBinding.calendarCardviewRvItem.setOnClickListener {
//                onClick(date)
//            }
//        }
//    }


    inner class SecondViewHolder(val secondBinding: UncalendarDayRvItemBinding) : ViewHolder(secondBinding.root) {
        fun bind(date: Date) {
            secondBinding.calendarDayTv.text = date.kun.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_FIRST -> {
                val binding = CalendarDayRvItemBinding.inflate(layoutInflater, parent, false)
                FirstViewHolder(binding)
            }
            TYPE_SECOND -> {
                val binding = UncalendarDayRvItemBinding.inflate(layoutInflater, parent, false)
                SecondViewHolder(binding)
            }
//            TYPE_CURRENT_DAY -> {
//                val binding = CurrentDayRvItemBinding.inflate(layoutInflater, parent, false)
//                CurrentDayViewHolder(binding)
//            }
            else -> {
                throw  IllegalArgumentException("Unknown view type")
            }
        }
    }

    override fun getItemCount() = calendarList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is FirstViewHolder -> holder.bind(calendarList[position])
            is SecondViewHolder -> holder.bind(calendarList[position])
//            is CurrentDayViewHolder -> holder.bind(calendarList[position])
        }
    }
}