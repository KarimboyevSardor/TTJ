package com.example.talabalarniroyxatgaolish.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.talabalarniroyxatgaolish.data.StudentDataItem
import com.example.talabalarniroyxatgaolish.databinding.StudentUpdateRoomRvItemBinding

class TadbirStudentAdapter(var studentList: MutableList<StudentDataItem>, val onClick: (student: StudentDataItem) -> Unit) : Adapter<TadbirStudentAdapter.TadbirStudentAdapterViewHolder>() {

    inner class TadbirStudentAdapterViewHolder(val binding: StudentUpdateRoomRvItemBinding) : ViewHolder(binding.root)

    fun filter(studentList: MutableList<StudentDataItem>) {
        this.studentList = studentList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TadbirStudentAdapterViewHolder {
        return TadbirStudentAdapterViewHolder(StudentUpdateRoomRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    override fun onBindViewHolder(holder: TadbirStudentAdapterViewHolder, position: Int) {
        val student = studentList[position]
        holder.binding.btnDelete.text = "O'chirish"
        holder.binding.tvStudentName.text = student.name
        holder.binding.tvUniversity.text = student.course
        holder.binding.tvCourseYear.text = "${student.course_count} - kurs talabasi"
        holder.binding.btnDelete.setOnClickListener {
            onClick(student)
        }
    }
}