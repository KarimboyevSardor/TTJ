package com.example.talabalarniroyxatgaolish.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.talabalarniroyxatgaolish.data.XonaDataItem
import com.example.talabalarniroyxatgaolish.databinding.RoomRvItemBinding

class XonaAdapter(var xonaList: MutableList<XonaDataItem>, val xonaOnClick: (XonaDataItem) -> Unit) : Adapter<XonaAdapter.XonaVh>() {
    inner class XonaVh(val binding: RoomRvItemBinding) : ViewHolder(binding.root)

    fun filter(xonaList: MutableList<XonaDataItem>) {
        this.xonaList = xonaList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): XonaVh {
        return XonaVh(RoomRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return xonaList.size
    }

    override fun onBindViewHolder(holder: XonaVh, position: Int) {
        val xona = xonaList[position]
        holder.binding.roomName.text = xona.room_count + " - xona"
        holder.binding.detailsButton.setOnClickListener {
            xonaOnClick(xona)
        }
    }
}