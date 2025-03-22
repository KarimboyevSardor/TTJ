package com.example.talabalarniroyxatgaolish.db

import com.example.talabalarniroyxatgaolish.data.Auth

interface MyDbService {

    fun setAuth(auth: Auth)

    fun getAuth() : MutableList<Auth>

    fun deleteAuth()
}