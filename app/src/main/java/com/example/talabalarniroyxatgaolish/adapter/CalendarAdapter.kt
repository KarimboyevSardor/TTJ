package com.example.talabalarniroyxatgaolish.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.talabalarniroyxatgaolish.data.Date
import com.example.talabalarniroyxatgaolish.databinding.CalendarDayRvItemBinding
import com.example.talabalarniroyxatgaolish.databinding.UncalendarDayRvItemBinding
import com.google.android.material.card.MaterialCardView

class CalendarAdapter(var calendarList: MutableList<Date>, val onClick: (Date) -> Unit) : Adapter<ViewHolder>() {

    companion object {
        private const val TYPE_FIRST = 0
        private const val TYPE_SECOND = 1
    }

    override fun getItemViewType(position: Int): Int {
        val calendar = calendarList[position]
        return if (calendar.isThisMonth) TYPE_FIRST else TYPE_SECOND
    }

    fun filter(calendarList: MutableList<Date>) {
        this.calendarList = calendarList
        notifyDataSetChanged()
    }

    inner class FirstViewHolder(val firstBinding: CalendarDayRvItemBinding) : ViewHolder(firstBinding.root) {
        fun bind(date: Date) {
            firstBinding.calendarDayTv.text = date.kun.toString()
        }
    }

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
        }
    }
}