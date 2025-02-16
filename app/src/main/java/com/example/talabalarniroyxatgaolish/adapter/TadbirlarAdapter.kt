package com.example.talabalarniroyxatgaolish.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.talabalarniroyxatgaolish.data.Rate
import com.example.talabalarniroyxatgaolish.data.TadbirlarDataItem
import com.example.talabalarniroyxatgaolish.databinding.YigilishRvItemBinding
import com.google.android.material.chip.Chip

class TadbirlarAdapter(
    var yigilishlarList: MutableList<TadbirlarDataItem>,
    var rateStudent: MutableList<Rate>,
    val context: Context,
    val onClickItem: (TadbirlarDataItem) -> Unit
) : Adapter<TadbirlarAdapter.YigilishlarVh>() {
    inner class YigilishlarVh(var binding: YigilishRvItemBinding) : ViewHolder(binding.root)

    fun filterYigilish(yigilishlarList: MutableList<TadbirlarDataItem>) {
        this.yigilishlarList = yigilishlarList
        notifyDataSetChanged()
    }

    fun filterRate(rateStudent: MutableList<Rate>) {
        this.rateStudent = rateStudent
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YigilishlarVh {
        return YigilishlarVh(YigilishRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return yigilishlarList.size
    }

    override fun onBindViewHolder(holder: YigilishlarVh, position: Int) {
        val yigilishData = yigilishlarList[position]
        val rates = rateStudent.filter { it.meeting_id == yigilishData.id }
        if (rates.isNotEmpty()) {
            holder.binding.tadbirStudentRvItem.visibility = View.VISIBLE
        } else {
            holder.binding.tadbirStudentRvItem.visibility = View.GONE
        }
        holder.binding.yigilishJoyi.text = yigilishData.meeting_place
        holder.binding.yigilishName.text = yigilishData.name
        holder.binding.yigilishVaqti.text = yigilishData.time
        holder.binding.yigilishDescription.text = yigilishData.description
        holder.binding.yigilishChipgroupRvItem.removeAllViews()
        for (i in rates.indices) {
            val chip = Chip(context)
            chip.text = rates[i].name
            chip.setOnClickListener {
                Toast.makeText(context, chip.text.toString(), Toast.LENGTH_SHORT).show()
            }
            holder.binding.yigilishChipgroupRvItem.addView(chip)
        }
        if (yigilishData.image_base64 != null) {
            Glide.with(context)
                .load(yigilishData.image_base64)
                .into(holder.binding.yigilishImage)
        } else {
            holder.binding.yigilishImage.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            onClickItem(yigilishData)
        }
    }

    private fun base64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        }
    }
}