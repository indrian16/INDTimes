package io.indrian16.indtimes

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ViewModelFactory(private val viewModels: ViewModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(viewModels.javaClass)) {

            @Suppress("UNCHECKED_CAST")
            return viewModels as T
        }

        throw IllegalArgumentException("unknown class name")
    }
}