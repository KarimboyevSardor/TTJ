package com.example.talabalarniroyxatgaolish.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.talabalarniroyxatgaolish.data.DavomatDataItem
import com.example.talabalarniroyxatgaolish.data.Rate
import com.example.talabalarniroyxatgaolish.databinding.StudentDavomatRvItemBinding
import java.util.Calendar

class DavomatAdapter(var davomatList: MutableList<DavomatDataItem>, val context: Context) : Adapter<DavomatAdapter.DavomatVh>() {
    inner class DavomatVh(var binding: StudentDavomatRvItemBinding) : ViewHolder(binding.root)

    fun updateList(davomatList: MutableList<DavomatDataItem>) {
        val diffCallback = DavomatDiffUtil(this.davomatList, davomatList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.davomatList = davomatList
        diffResult.dispatchUpdatesTo(this)
    }

    class DavomatDiffUtil(
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DavomatVh {
        return DavomatVh(StudentDavomatRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return davomatList.size
    }

    override fun onBindViewHolder(holder: DavomatVh, position: Int) {
        val davomat = davomatList[position]
        holder.binding.studentName.text = davomat.name
        val sana = davomat.date.split("T")[0]
        holder.binding.attendanceDate.text = sana
        holder.binding.studentDetails.text = davomat.course
        holder.binding.studentRoom.text = davomat.room_count + " - xona"
    }

}