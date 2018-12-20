package io.indrian16.indtimes.util

import android.content.Context
import android.net.ConnectivityManager
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun AppCompatActivity.showToast(message: String) = Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()

fun Fragment.showToast(message: String) = Toast.makeText(context, message, Toast.LENGTH_LONG).show()

fun View.toVisisble() {

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

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {

    this.add(disposable)
}