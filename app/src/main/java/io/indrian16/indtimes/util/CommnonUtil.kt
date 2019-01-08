package io.indrian16.indtimes.util

import android.app.Activity
import android.content.Intent

object CommnonUtil {

    fun shareArticle(activity: Activity, url: String) {

        val intent = Intent(Intent.ACTION_SEND).apply {

            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, url)
        }
        activity.startActivity(Intent.createChooser(intent, AppConstant.SHARE_ARTICLE))
    }
}