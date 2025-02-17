package com.example.talabalarniroyxatgaolish.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.talabalarniroyxatgaolish.data.StudentDataItem
import com.example.talabalarniroyxatgaolish.databinding.StudentUpdateRoomRvItemBinding

class StudentBiriktirishAdapter(var studentList: MutableList<StudentDataItem>, val onClickListener: (student: StudentDataItem) -> Unit) : Adapter<StudentBiriktirishAdapter.StudentBiriktirishAdapterViewHolder>() {

    inner class StudentBiriktirishAdapterViewHolder(val binding: StudentUpdateRoomRvItemBinding) : ViewHolder(binding.root)

    fun filter(studentList: MutableList<StudentDataItem>) {
        val diffCallback = StudentBiriktirishDiffUtil(this.studentList, studentList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.studentList = studentList
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    class StudentBiriktirishDiffUtil(
        private val oldList: List<StudentDataItem>,
        private val newList: List<StudentDataItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudentBiriktirishAdapterViewHolder {
        return StudentBiriktirishAdapterViewHolder(StudentUpdateRoomRvItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    override fun onBindViewHolder(holder: StudentBiriktirishAdapterViewHolder, position: Int) {
        val student = studentList[position]
        holder.binding.btnDelete.text = "Tadbirga biriktirish"
        holder.binding.tvStudentName.text = student.name
        holder.binding.tvCourseYear.text = "${student.course_count} - kurs talabasi"
        holder.binding.tvUniversity.text = student.course
        holder.binding.btnDelete.setOnClickListener {
            onClickListener(student)
        }
    }


}