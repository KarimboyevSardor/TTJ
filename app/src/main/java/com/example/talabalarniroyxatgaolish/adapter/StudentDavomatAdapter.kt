package com.example.talabalarniroyxatgaolish.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.talabalarniroyxatgaolish.data.DavomatDataItem
import com.example.talabalarniroyxatgaolish.data.StudentDataItem
import com.example.talabalarniroyxatgaolish.databinding.StudentAddDavomatRvItemBinding

class StudentDavomatAdapter(var studentList: MutableList<DavomatDataItem>, val onClick: (position: Int, CheckBox) -> Unit) : RecyclerView.Adapter<StudentDavomatAdapter.StudentDavomatAdapterVM>() {

    fun filter(studentList: MutableList<DavomatDataItem>) {
        val diffCallback = BaholashDiffUtil(this.studentList, studentList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.studentList = studentList
        diffResult.dispatchUpdatesTo(this)
    }

    class BaholashDiffUtil(
        private val oldList: List<DavomatDataItem>,
        private val newList: List<DavomatDataItem>
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

    inner class StudentDavomatAdapterVM(val binding: StudentAddDavomatRvItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentDavomatAdapterVM {
        return StudentDavomatAdapterVM(StudentAddDavomatRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    override fun onBindViewHolder(holder: StudentDavomatAdapterVM, position: Int) {
        val student = studentList[position]
        holder.binding.tvStudentName.text = student.name
        holder.binding.fakultetTv.text = student.course
        holder.binding.kursTv.text = "${student.course_count} - kurs talabasi"
        holder.binding.switchAttendance.isChecked = student.is_there
        holder.binding.switchAttendance.setOnClickListener {
            onClick(position, holder.binding.switchAttendance)
        }
    }


}