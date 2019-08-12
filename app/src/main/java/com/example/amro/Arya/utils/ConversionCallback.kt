package com.example.amro.Arya.utils

interface ConversionCallback {

    fun onSuccess(result: String)

    fun onCompletion()

    fun onErrorOccurred(errorMessage: String)

}