package com.example.talabalarniroyxatgaolish.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.talabalarniroyxatgaolish.data.MeetingX
import com.example.talabalarniroyxatgaolish.databinding.StudentProfileTadbirRvItemBinding

class TadbirProfileStudentAdapter(var tadbirsList: MutableList<MeetingX>, val context: Context) : Adapter<TadbirProfileStudentAdapter.tadbirProfileVh>() {

    fun updateList(tadbirsList: MutableList<MeetingX>) {
        this.tadbirsList = tadbirsList
        notifyDataSetChanged()
    }

    inner class tadbirProfileVh(val binding: StudentProfileTadbirRvItemBinding) : ViewHolder(binding.root) {
        fun onBind(meetingX: MeetingX) {
            binding.tadbirNameRvItem.text = meetingX.meeting_name
            binding.rateStudent.text = "Baho: ${meetingX.rate}/5"
            if (meetingX.image_url != null) {
                Glide.with(context).load(meetingX.image_url).into(binding.tadbirRvItemImg)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): tadbirProfileVh {
        return tadbirProfileVh(StudentProfileTadbirRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return tadbirsList.size
    }

    override fun onBindViewHolder(holder: tadbirProfileVh, position: Int) {
        holder.onBind(tadbirsList[position])
    }
}