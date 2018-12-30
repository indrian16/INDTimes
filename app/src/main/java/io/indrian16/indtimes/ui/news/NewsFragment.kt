package io.indrian16.indtimes.ui.news

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.ajalt.timberkt.d
import dagger.android.support.AndroidSupportInjection

import io.indrian16.indtimes.R
import io.indrian16.indtimes.data.model.Article
import io.indrian16.indtimes.ui.detail.DetailArticleActivity
import io.indrian16.indtimes.ui.news.adapter.RvNewsArticle
import io.indrian16.indtimes.util.*
import kotlinx.android.synthetic.main.fragment_news.*
import javax.inject.Inject

class NewsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, RvNewsArticle.OnNewsArticleOnClickListener {

    companion object {

        const val CATEGORY_KEY = "NEWS_CATEGORY"

        fun newInstance(category: String) = NewsFragment().apply {

            arguments = Bundle().apply {

                putString(CATEGORY_KEY, category)
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: NewsViewModel
    private val newsAdapter = RvNewsArticle(this)
    private var currentCategory = Category.ALL

    private val newsStateObserver = Observer<NewsState> { state ->

        when (state) {

            is NewsLoadingState -> {

                d { "Loading State" }
                swipeRv.toVisible()
                errorLayout.toGone()
                swipeRv.isRefreshing = true
                rvNews.toGone()
            }

            is NewsDefaultState -> {

                d { "Default State" }
                swipeRv.toVisible()
                errorLayout.toGone()
                swipeRv.isRefreshing = false
                rvNews.toVisible()
                newsAdapter.add(state.dataList)
            }

            is NewsErrorState -> {

                d { "Error State" }
                d { state.errorMessage }
                swipeRv.toGone()
                errorLayout.toVisible()
            }
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        updateCategory()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel().apply {

            newsStateLiveData.observe(this@NewsFragment, newsStateObserver)
        }
        savedInstanceState?.let {
            viewModel.restoreNews()
        } ?: viewModel.updateNews(currentCategory)
    }

    private fun initView() {

        swipeRv.setOnRefreshListener(this)
        btnRefresh.setOnClickListener { onRefresh() }
        rvNews.apply {

            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
    }

    private fun updateCategory() {

        val category = arguments?.getString(CATEGORY_KEY) ?: Category.ALL
        currentCategory = category
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.newsStateLiveData.removeObserver(newsStateObserver)
    }

    override fun onRefresh() {

        newsAdapter.clear()
        viewModel.refreshNews(currentCategory)
    }

    override fun onClickNews(article: Article) {

        val intent = Intent(context, DetailArticleActivity::class.java)
        intent.putExtra(DetailArticleActivity.EXTRA_ARTICLE, article)

        startActivity(intent)
    }

    override fun onClickShare(url: String) {

        CommnonUtil.shareArticle(activity!!, url)
    }

    override fun onClickBookmark(article: Article) {

        showToast("Bookmark is Coming")
    }

    private fun obtainViewModel() = obtainViewModel(viewModelFactory, NewsViewModel::class.java)
}
