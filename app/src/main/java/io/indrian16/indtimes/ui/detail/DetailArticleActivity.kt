package io.indrian16.indtimes.ui.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
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
import dagger.android.AndroidInjection
import io.indrian16.indtimes.R
import io.indrian16.indtimes.customtab.CustomTabActivityHelper
import io.indrian16.indtimes.data.model.Article
import io.indrian16.indtimes.util.*
import kotlinx.android.synthetic.main.activity_detail_article.*
import javax.inject.Inject

class DetailArticleActivity : AppCompatActivity(), View.OnClickListener, CustomTabActivityHelper.CustomTabFallback {

    companion object {

        const val EXTRA_ARTICLE = "ARTICLE"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: DetailViewModel

    private lateinit var shareUrl: String

    private val stateObserver = Observer<DetailState> { state ->

        when (state) {

            is DefaultDetailState -> {

                setArticle(state.data)
            }

            is ErrorDetailState -> {

                showToast(state.errorMessage)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_article)

        viewModel = obtainViewModel().apply {

            stateLiveData.observe(this@DetailArticleActivity, stateObserver)
        }
        val article = intent.getParcelableExtra<Article>(EXTRA_ARTICLE)
        viewModel.receivedArticle(article)

        setupToolbar()
        setListener()
    }

    private fun setupToolbar() {

        setSupportActionBar(detailToolbar).apply {

            title = getString(R.string.read_article_title)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setArticle(article: Article?) {

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

            R.id.detailBookmark -> {

                viewModel.saveBookmark()
                showToast(resources.getString(R.string.bookmarked))
                true
            }

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

    private fun obtainViewModel() = obtainViewModel(viewModelFactory, DetailViewModel::class.java)
}
