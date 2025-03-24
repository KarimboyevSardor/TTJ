package com.example.talabalarniroyxatgaolish.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.talabalarniroyxatgaolish.data.StudentDataItem
import com.example.talabalarniroyxatgaolish.databinding.StudentRvItemBinding

class StudentsAdapter(var studentsList: MutableList<StudentDataItem>, val onClick: (student: StudentDataItem) -> Unit, val deleteStudent: (student: StudentDataItem) -> Unit) : Adapter<StudentsAdapter.StudentsAdapterVh>() {

    fun updateList(studentsList: MutableList<StudentDataItem>) {
        this.studentsList = studentsList
        notifyDataSetChanged()
    }

    inner class StudentsAdapterVh(val binding: StudentRvItemBinding) : ViewHolder(binding.root) {
        fun onBind(student: StudentDataItem) {
            binding.tvStudentName.text = student.name
            binding.tvCourse.text = "${student.course_count} - kurs talabasi"
            binding.tvFaculty.text = student.course
            binding.root.setOnClickListener {
                onClick(student)
            }
            binding.btnDelete.setOnClickListener{
                deleteStudent(student)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentsAdapterVh {
        return StudentsAdapterVh(StudentRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return studentsList.size
    }

    override fun onBindViewHolder(holder: StudentsAdapterVh, position: Int) {
        holder.onBind(studentsList[position])
    }
}