package com.example.talabalarniroyxatgaolish.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.talabalarniroyxatgaolish.data.DavomatDataItem
import com.example.talabalarniroyxatgaolish.databinding.StudentAddDavomatRvItemBinding
import java.util.Calendar

class StudentDavomatAdapter(var studentList: MutableList<DavomatDataItem>, val listener: AdapterListener) : RecyclerView.Adapter<StudentDavomatAdapter.StudentDavomatAdapterVM>() {

    private val TAG = "STUDENTDAVOMATADAPTER"
    interface AdapterListener {
        fun onAdapterFunctionCalled(position: Int, switch: CheckBox)
    }

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
            listener.onAdapterFunctionCalled(position, holder.binding.switchAttendance)
        }
        if (student.date != getCurrentDay()) {
            holder.binding.switchAttendance.isEnabled = false
        } else if (student.room_count.isNotEmpty() && student.date == getCurrentDay()) {
            holder.binding.switchAttendance.isEnabled = false
        }
    }

    private fun formatDate(inputDate: String): String {
        val parts = inputDate.split("-")
        if (parts.size != 3) return "Noto'g'ri format"
        val year = parts[0]
        val month = parts[1].padStart(2, '0')
        val day = parts[2].padStart(2, '0')
        return "$year-$month-$day"
    }

    private fun getCurrentDay() : String {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        return formatDate("$year-$month-$day")
    }

}