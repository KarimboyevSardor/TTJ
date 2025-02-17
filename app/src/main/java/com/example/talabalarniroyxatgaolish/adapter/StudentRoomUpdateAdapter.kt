package com.example.talabalarniroyxatgaolish.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.talabalarniroyxatgaolish.adapter.StudentBiriktirishAdapter.StudentBiriktirishDiffUtil
import com.example.talabalarniroyxatgaolish.data.StudentDataItem
import com.example.talabalarniroyxatgaolish.databinding.StudentUpdateRoomRvItemBinding

class StudentRoomUpdateAdapter(var studentList: MutableList<StudentDataItem>, val onSelect: (StudentDataItem) -> Unit) : Adapter<StudentRoomUpdateAdapter.StudentRoomUpdateAdapterVh>() {

    inner class StudentRoomUpdateAdapterVh(val binding: StudentUpdateRoomRvItemBinding) : ViewHolder(binding.root)

    fun filter(studentList: MutableList<StudentDataItem>) {
        val diffCallback = StudentRoomUpdateDiffUtil(this.studentList, studentList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.studentList = studentList
        diffResult.dispatchUpdatesTo(this)
    }

    class StudentRoomUpdateDiffUtil(
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentRoomUpdateAdapterVh {
        return StudentRoomUpdateAdapterVh(StudentUpdateRoomRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    override fun onBindViewHolder(holder: StudentRoomUpdateAdapterVh, position: Int) {
        val student = studentList[position]
        holder.binding.tvStudentName.text = student.name
        holder.binding.tvUniversity.text = student.course
        holder.binding.tvCourseYear.text = "${student.course_count} - kurs talabasi"
        holder.binding.btnDelete.setOnClickListener {
            onSelect(student)
        }
    }
}