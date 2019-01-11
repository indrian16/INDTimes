package io.indrian16.indtimes.ui.news

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.ajalt.timberkt.d

import io.indrian16.indtimes.R
import io.indrian16.indtimes.data.model.Article
import io.indrian16.indtimes.ui.base.BaseFragment
import io.indrian16.indtimes.ui.detail.DetailArticleActivity
import io.indrian16.indtimes.ui.news.adapter.RvNewsArticle
import io.indrian16.indtimes.util.*
import kotlinx.android.synthetic.main.fragment_news.*
import javax.inject.Inject

class NewsFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener, RvNewsArticle.OnNewsArticleOnClickListener {

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

    private val mAdapter = RvNewsArticle(this)
    private var currentCategory = Category.ALL

    private val newsListStateObserver = Observer<NewsListState> { state ->

        when (state) {

            is LoadingState -> {

                d { "Loading State" }
                defaultLayoutState()
                newsLayout.isRefreshing = true
            }

            is DefaultState -> {

                d { "Default State" }
                defaultLayoutState()

                mAdapter.add(state.dataList)
                newsLayout.isRefreshing = false
            }

            is EmptyState -> {

                d {"No Data State"}
                emptyLayoutState()
            }

            is ErrorState -> {

                d { "Error State" }
                d { state.errorMessage }
                errorLayoutState()
            }
        }
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

            newsListStateLiveData.observe(this@NewsFragment, newsListStateObserver)
        }
        viewModel.updateNews(currentCategory)
    }

    private fun initView() {

        newsLayout.setOnRefreshListener(this)
        btnRefresh.setOnClickListener { onRefresh() }
        btnTryAgain.setOnClickListener { onRefresh() }
        rvNews.apply {

            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
    }

    private fun updateCategory() {

        val category = arguments?.getString(CATEGORY_KEY) ?: Category.ALL
        currentCategory = category
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.newsListStateLiveData.removeObserver(newsListStateObserver)
    }

    override fun onRefresh() {

        mAdapter.clear()
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

    private fun defaultLayoutState() {

        newsLayout.toVisible()
        newsLayoutEmpty.toGone()
        newsLayoutError.toGone()
    }

    private fun emptyLayoutState() {

        newsLayout.toGone()
        newsLayoutEmpty.toVisible()
        newsLayoutError.toGone()
    }

    private fun errorLayoutState() {

        newsLayout.toGone()
        newsLayoutEmpty.toGone()
        newsLayoutError.toVisible()
    }

    private fun obtainViewModel() = obtainViewModel(viewModelFactory, NewsViewModel::class.java)
}
