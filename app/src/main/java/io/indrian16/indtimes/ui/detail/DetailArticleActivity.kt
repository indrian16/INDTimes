package io.indrian16.indtimes.ui.detail

import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.d
import io.indrian16.indtimes.R
import io.indrian16.indtimes.customtab.CustomTabActivityHelper
import io.indrian16.indtimes.data.model.Article
import io.indrian16.indtimes.ui.base.BaseActivity
import io.indrian16.indtimes.util.*
import kotlinx.android.synthetic.main.activity_detail_article.*
import javax.inject.Inject

class DetailArticleActivity : BaseActivity(), View.OnClickListener, CustomTabActivityHelper.CustomTabFallback {

    companion object {

        const val EXTRA_ARTICLE = "ARTICLE"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: DetailViewModel

    private lateinit var articleUrl: String
    private lateinit var favoriteItem: MenuItem
    private var currentFavorite = false

    private val detailStateObserver = Observer<DetailState> { state ->

        when (state) {

            is DefaultState -> {

                d { "Default State" }
                setArticle(state.data)
            }

            is ChangeIconState -> {

                d { "Change Icon State" }
                currentFavorite = if (state.isFavorite) {

                    setFilledIcon()
                    true
                } else {

                    setOutlineIcon()
                    false
                }
            }

            is ErrorState -> {

                d { "Error State" }
                showToast(state.errorMessage)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_article)

        viewModel = obtainViewModel().apply {

            detailStateLiveData.observe(this@DetailArticleActivity, detailStateObserver)
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
            tvContentDetail.text = it.content ?: AppConstant.NO_CONTENT
            articleUrl = it.url
        }
    }

    private fun favoriteArticle() {

        if (currentFavorite) {

            viewModel.deleteFavorite()
            showToast(resources.getString(R.string.del_favorite))

        } else {

            viewModel.addFavorite()
            showToast(resources.getString(R.string.add_favorite))
        }
    }

    private fun setListener() {

        btnOpenChrome.setOnClickListener(this)
    }

    @SuppressLint("PrivateResource")
    private fun openChromeTab() {

        val uri = articleUrl.toUri()
        val intent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()
        CustomTabActivityHelper.openCustomTab(this, intent, uri, this)
    }

    override fun onSupportNavigateUp(): Boolean {

        finish()
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        viewModel.checkFavorite(articleUrl)
        favoriteItem = menu?.findItem(R.id.detailFavorite)!!
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when (item?.itemId) {

            android.R.id.home -> { finish(); true }

            R.id.detailFavorite -> {

                favoriteArticle()
                true
            }

            R.id.detailShare -> {

                CommnonUtil.shareArticle(this, articleUrl)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.btnOpenChrome -> openChromeTab()
        }
    }

    override fun openUri(activity: Activity?, uri: Uri?) {

        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.detailStateLiveData.removeObserver(detailStateObserver)
    }

    private fun setOutlineIcon() = favoriteItem.setIcon(ContextCompat.getDrawable(baseContext, R.drawable.icons8_heart_outline_96))

    private fun setFilledIcon() = favoriteItem.setIcon(ContextCompat.getDrawable(baseContext, R.drawable.icons8_love_96))

    private fun obtainViewModel() = obtainViewModel(viewModelFactory, DetailViewModel::class.java)
}
