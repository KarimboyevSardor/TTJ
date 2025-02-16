package com.example.talabalarniroyxatgaolish.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.talabalarniroyxatgaolish.data.Rate
import com.example.talabalarniroyxatgaolish.data.RateData
import com.example.talabalarniroyxatgaolish.databinding.BaholashRvItemBinding

class BaholashAdapter(var rates: MutableList<Rate>) : Adapter<BaholashAdapter.BaholashAdapterVh>() {

    fun filter(rates: MutableList<Rate>) {
        this.rates = rates
        notifyDataSetChanged()
    }

    inner class BaholashAdapterVh(val binding: BaholashRvItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaholashAdapterVh {
        return BaholashAdapterVh(BaholashRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return rates.size
    }

    override fun onBindViewHolder(holder: BaholashAdapterVh, position: Int) {

    }


}