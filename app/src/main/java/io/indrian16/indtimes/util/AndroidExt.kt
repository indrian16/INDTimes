package io.indrian16.indtimes.util

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun AppCompatActivity.showToast(message: String) = Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()

fun AppCompatActivity.shareArticle(url: String) {

    val inShare = Intent(Intent.ACTION_SEND)
    inShare.type = "text/plain"
    inShare.putExtra(Intent.EXTRA_TEXT, url)

    startActivity(Intent.createChooser(inShare, AppConstant.SHARE_ARTICLE))
}

fun Fragment.showToast(message: String) = Toast.makeText(context, message, Toast.LENGTH_LONG).show()

fun Fragment.shareArticle(url: String) {

    val inShare = Intent(Intent.ACTION_SEND)
    inShare.type = "text/plain"
    inShare.putExtra(Intent.EXTRA_TEXT, url)

    startActivity(Intent.createChooser(inShare, AppConstant.SHARE_ARTICLE))
}

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