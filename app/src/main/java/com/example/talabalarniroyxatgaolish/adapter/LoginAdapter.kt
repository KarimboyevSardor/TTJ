package com.example.talabalarniroyxatgaolish.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.talabalarniroyxatgaolish.R
import com.example.talabalarniroyxatgaolish.data.AuthDataItem

class LoginAdapter(private val context: Context, var logins: MutableList<AuthDataItem>, val onClick: (authData: AuthDataItem) -> Unit) : ArrayAdapter<AuthDataItem>(context, R.layout.login_password_ac_item, logins) {

    fun updateData(newItems: List<AuthDataItem>) {
        clear()            // Eski elementlarni o‘chirish
        addAll(newItems)   // Yangi elementlarni qo‘shish
        notifyDataSetChanged() // Adapterni xabardor qilish
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.login_password_ac_item, parent, false)
        val loginTv: TextView = view.findViewById(R.id.tvLogin)
        val passwordTv: TextView = view.findViewById(R.id.tvPassword)
        val image: ImageView = view.findViewById(R.id.ivTogglePassword)
        loginTv.text = logins[position].login
        passwordTv.text = logins[position].password
        view.setOnClickListener {
            onClick(logins[position])
        }
        return view
    }
}