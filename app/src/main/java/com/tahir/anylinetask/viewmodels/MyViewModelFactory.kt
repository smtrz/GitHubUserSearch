package com.tahir.anylinetask.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class MyViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {

            MainActivityViewModel() as T

        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}