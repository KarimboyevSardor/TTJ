package com.example.talabalarniroyxatgaolish.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.talabalarniroyxatgaolish.databinding.SelectCourceBinding

class CourseAdapter(var courses: MutableList<String>, val position1: Int, val onClick: (text: String, position: Int) -> Unit) : Adapter<CourseAdapter.CourseAdapterVH>() {

    inner class CourseAdapterVH(val binding: SelectCourceBinding) : ViewHolder(binding.root) {
        fun onBind(text: String, position: Int) {
            binding.courseTv.text = text
            binding.courseTv.setOnClickListener {
                onClick(text, position)
            }

            if (position1 == position && position1 != -1) {
                binding.courseTv.isChecked = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseAdapterVH {
        return CourseAdapterVH(SelectCourceBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = courses.size

    override fun onBindViewHolder(holder: CourseAdapterVH, position: Int) {
        holder.onBind(courses[position], position)
    }

}