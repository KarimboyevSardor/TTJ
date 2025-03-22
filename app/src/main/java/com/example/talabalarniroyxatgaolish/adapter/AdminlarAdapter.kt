package com.example.talabalarniroyxatgaolish.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.talabalarniroyxatgaolish.data.AdminDataItem
import com.example.talabalarniroyxatgaolish.databinding.AdminlarRvItemBinding

class AdminlarAdapter(var admins: MutableList<AdminDataItem>, val editOnClick: (admin: AdminDataItem) -> Unit, val deleteOnClick: (id: Long) -> Unit) : Adapter<AdminlarAdapter.AdminlarAdapterVH>() {

    fun updateList(admins: MutableList<AdminDataItem>) {
        this.admins = admins
        notifyDataSetChanged()
    }

    inner class AdminlarAdapterVH(val binding: AdminlarRvItemBinding) : ViewHolder(binding.root) {
        fun onBind(admin: AdminDataItem) {
            binding.adminName.text = admin.name
            binding.adminRole.text = "admin"
            binding.btnEdit.setOnClickListener {
                editOnClick(admin)
            }
            binding.btnRemove.setOnClickListener {
                deleteOnClick(admin.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminlarAdapterVH {
        return AdminlarAdapterVH(AdminlarRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = admins.size

    override fun onBindViewHolder(holder: AdminlarAdapterVH, position: Int) {
        holder.onBind(admins[position])
    }
}