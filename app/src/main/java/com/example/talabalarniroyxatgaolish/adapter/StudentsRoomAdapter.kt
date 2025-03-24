package com.example.talabalarniroyxatgaolish.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.talabalarniroyxatgaolish.data.StudentDataItem
import com.example.talabalarniroyxatgaolish.databinding.StudentsRvItemBinding

class StudentsRoomAdapter(var studentsList: MutableList<StudentDataItem>, val onDeleteClick: (StudentDataItem) -> Unit) : Adapter<StudentsRoomAdapter.StudentAdapterVh>() {

    inner class StudentAdapterVh(val binding: StudentsRvItemBinding) : ViewHolder(binding.root)

    fun filter(studentList: MutableList<StudentDataItem>) {
        val diffCallback = StudentsDiffUtil(this.studentsList, studentList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.studentsList = studentList
        diffResult.dispatchUpdatesTo(this)
    }

    class StudentsDiffUtil(
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