package com.example.talabalarniroyxatgaolish.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.talabalarniroyxatgaolish.R
import com.example.talabalarniroyxatgaolish.data.StudentDataItem
import com.example.talabalarniroyxatgaolish.databinding.StudentsRvItemBinding

class StudentsAdapter(var studentsList: MutableList<StudentDataItem>, val onDeleteClick: (StudentDataItem) -> Unit) : Adapter<StudentsAdapter.StudentAdapterVh>() {

    inner class StudentAdapterVh(val binding: StudentsRvItemBinding) : ViewHolder(binding.root)

    fun filter(studentsList: MutableList<StudentDataItem>) {
        this.studentsList = studentsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentAdapterVh {
        return StudentAdapterVh(StudentsRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return studentsList.size
    }

    override fun onBindViewHolder(holder: StudentAdapterVh, position: Int) {
        val student = studentsList[position]
        holder.binding.tvStudentName.text = student.name
        holder.binding.tvUniversity.text = student.course
        holder.binding.tvCourseYear.text = "${student.course_count} - kurs talabasi"
        holder.binding.btnDelete.setOnClickListener {
            onDeleteClick(student)
        }
    }


}