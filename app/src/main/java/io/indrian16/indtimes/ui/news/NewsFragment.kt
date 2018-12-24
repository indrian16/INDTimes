package io.indrian16.indtimes.ui.news

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection

import io.indrian16.indtimes.R
import io.indrian16.indtimes.data.model.Article
import io.indrian16.indtimes.ui.detail.DetailArticleActivity
import io.indrian16.indtimes.ui.news.rv.RvNewsArticle
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
    lateinit var newsViewModelFactory: ViewModelProvider.Factory

    private lateinit var newsViewModel: NewsViewModel
    private val newsAdapter = RvNewsArticle(this)
    private var currentCategory = Category.ALL

    private val newsStateObserver = Observer<NewsState> { state ->

        when (state) {

            is NewsLoadingState -> {

                swipeRv.isRefreshing = true
                rvNews.toGone()
            }

            is NewsDefaultState -> {

                swipeRv.isRefreshing = false
                rvNews.toVisible()
                newsAdapter.add(state.dataList)
            }

            is NewsErrorState -> {

                showToast(" Error ${state.errorMessage}")
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
        newsViewModel = ViewModelProviders.of(this, newsViewModelFactory).get(NewsViewModel::class.java)

        initView()
        updateCategory()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observerViewModel()
        savedInstanceState?.let {

            newsViewModel.restoreNews()
        } ?: newsViewModel.updateNews(currentCategory)
    }

    private fun initView() {

        swipeRv.setOnRefreshListener(this)
        rvNews.apply {

            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
    }

    private fun observerViewModel() {

        newsViewModel.newsState.observe(this, newsStateObserver)
    }

    private fun updateCategory() {

        val category = arguments?.getString(CATEGORY_KEY) ?: Category.ALL
        currentCategory = category
    }

    override fun onDestroy() {
        super.onDestroy()
        newsViewModel.newsState.removeObserver(newsStateObserver)
    }

    override fun onRefresh() {

        newsAdapter.clear()
        newsViewModel.refreshNews(currentCategory)
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
}
