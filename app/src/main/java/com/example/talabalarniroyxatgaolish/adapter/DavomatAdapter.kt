package com.example.talabalarniroyxatgaolish.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.talabalarniroyxatgaolish.R
import com.example.talabalarniroyxatgaolish.data.DavomatDataItem
import com.example.talabalarniroyxatgaolish.databinding.StudentDavomatRvItemBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.studentlarList

class DavomatAdapter(var davomatList: MutableList<DavomatDataItem>, val context: Context) : Adapter<DavomatAdapter.DavomatVh>() {
    inner class DavomatVh(var binding: StudentDavomatRvItemBinding) : ViewHolder(binding.root)

    fun filter(davomatList: MutableList<DavomatDataItem>) {
        this.davomatList = davomatList
        notifyDataSetChanged()
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