package io.indrian16.indtimes.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun AppCompatActivity.showToast(message: String) = Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()

fun <T: ViewModel> AppCompatActivity.obtainViewModel(factory: ViewModelProvider.Factory, viewModelClass: Class<T>): T {

    return ViewModelProviders.of(this, factory).get(viewModelClass)
}

fun <T: ViewModel> androidx.fragment.app.Fragment.obtainViewModel(factory: ViewModelProvider.Factory, viewModelClass: Class<T>): T {

    return ViewModelProviders.of(this, factory).get(viewModelClass)
}

fun androidx.fragment.app.Fragment.showToast(message: String) = Toast.makeText(context, message, Toast.LENGTH_LONG).show()

fun View.toVisible() {

    this.visibility = View.VISIBLE
}

fun View.toGone() {

    this.visibility = View.GONE
}

fun Context.isConnAvailable(): Boolean {

    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeInfo = cm.activeNetworkInfo

    return activeInfo != null && activeInfo.isConnected
}

fun String.toUri() = Uri.parse(this)

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {

    this.add(disposable)
}