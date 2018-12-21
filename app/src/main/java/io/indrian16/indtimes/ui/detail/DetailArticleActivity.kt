package io.indrian16.indtimes.ui.detail

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import io.indrian16.indtimes.R
import io.indrian16.indtimes.data.model.Article
import io.indrian16.indtimes.util.AppConstant
import io.indrian16.indtimes.util.shareArticle
import io.indrian16.indtimes.util.showToast
import kotlinx.android.synthetic.main.activity_detail_article.*

class DetailArticleActivity : AppCompatActivity(), View.OnClickListener {

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

    override fun onSupportNavigateUp(): Boolean {

        finish()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when (item?.itemId) {

            android.R.id.home -> { finish(); true }

            R.id.detailBookmark -> { showToast("Coming!"); true }

            R.id.detailShare -> { shareArticle(shareUrl); true }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.btnOpenChrome -> showToast("Open Crome is Coming!")
        }
    }
}
