package io.indrian16.indtimes.ui.search

import android.arch.lifecycle.ViewModel
import android.os.Handler
import com.github.ajalt.timberkt.d
import io.reactivex.disposables.CompositeDisposable

class SearchViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun searchOnChange(query: String) {

        d { "onChange: $query" }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }
}