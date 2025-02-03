package com.example.talabalarniroyxatgaolish.vm

sealed class Resource<T> {
    class Loading<T> : Resource<T>()
    class Success<T>(val data: T) : Resource<T>()
    class Error<T>(val e: Throwable) : Resource<T>()
}