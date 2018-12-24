package io.indrian16.indtimes.ui.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import io.indrian16.indtimes.R
import io.indrian16.indtimes.customtab.CustomTabActivityHelper
import io.indrian16.indtimes.data.model.Article
import io.indrian16.indtimes.util.*
import kotlinx.android.synthetic.main.activity_detail_article.*

class DetailArticleActivity : AppCompatActivity(), View.OnClickListener, CustomTabActivityHelper.CustomTabFallback {

    companion object {

        const val EXTRA_ARTICLE = "ARTICLE"
    }

    private lateinit var shareUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_article)

        setupToolbar()
        setArticle()
        setListener()
    }

    private fun setupToolbar() {

        setSupportActionBar(detailToolbar).apply {

            title = getString(R.string.read_article_title)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setArticle() {

        val article = intent?.getParcelableExtra<Article>(EXTRA_ARTICLE)

        article?.let {

            Glide.with(this).load(it.urlToImage).into(imgDetail)
            tvTitleDetail.text = it.title
            tvContentDetail.text = checkContentNull(it.content)
            shareUrl = it.url
        }
    }

    private fun setListener() {

        btnOpenChrome.setOnClickListener(this)
    }

    private fun checkContentNull(content: String?): String {

        return if (!TextUtils.isEmpty(content)) {

            content!!
        } else {

            AppConstant.NO_CONTENT
        }
    }

    @SuppressLint("PrivateResource")
    private fun openChromeTab(url: String) {

        val uri = url.toUri()
        val intent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()
        CustomTabActivityHelper.openCustomTab(this, intent, uri, this)
    }

    override fun onSupportNavigateUp(): Boolean {

        finish()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when (item?.itemId) {

            android.R.id.home -> { finish(); true }

            R.id.detailBookmark -> { showToast("Coming!"); true }

            R.id.detailShare -> {

                CommnonUtil.shareArticle(this, shareUrl)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.btnOpenChrome -> openChromeTab(shareUrl)
        }
    }

    override fun openUri(activity: Activity?, uri: Uri?) {

        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}
