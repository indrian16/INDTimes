package io.indrian16.indtimes.ui.search

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import androidx.core.view.MenuItemCompat
import androidx.appcompat.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.github.ajalt.timberkt.d
import io.indrian16.indtimes.R
import io.indrian16.indtimes.data.model.Article
import io.indrian16.indtimes.ui.base.BaseActivity
import io.indrian16.indtimes.ui.detail.DetailArticleActivity
import io.indrian16.indtimes.ui.news.adapter.RvNewsArticle
import io.indrian16.indtimes.util.*
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class SearchActivity : BaseActivity(), RvNewsArticle.OnNewsArticleOnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: SearchViewModel

    private lateinit var searchView: SearchView
    private val mAdapter = RvNewsArticle(this)

    private val onChangeStateObserver = Observer<SearchChangeState> { changeState ->

        when (changeState) {

            is NoInputState -> {

                searchLayout.toGone()
                searchLayoutEmpty.toGone()
                searchLayoutError.toGone()
            }
        }
    }

    private val onSubmitStateObserver = Observer<SearchSubmitState> { onSubmitState ->

        when (onSubmitState) {

            is GetDataState -> {

                d { "Get Data State" }
                defaultLayoutState()
                mAdapter.add(onSubmitState.dataList)
                progressBar.toGone()
            }

            is LoadingState -> {

                d { "Loading State" }
                defaultLayoutState()
                progressBar.toVisible()
                mAdapter.clear()
            }

            is EmptyState -> {

                d { "No Item State" }
                emptyLayoutState()
            }

            is ErrorState -> {

                d { "Error State" }
                errorLayoutState()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initView()

        viewModel = obtainViewModel().apply {

            changeStateLiveData.observe(this@SearchActivity, onChangeStateObserver)
            submitStateLiveData.observe(this@SearchActivity, onSubmitStateObserver)
        }
    }

    private fun initView() {

        rvSearch.apply {

            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(baseContext)
            adapter = mAdapter
        }
    }

    private fun expandSearchView(menuItem: MenuItem) {

        menuItem.expandActionView()
        MenuItemCompat.setOnActionExpandListener(menuItem, object : MenuItemCompat.OnActionExpandListener {

            override fun onMenuItemActionExpand(menuItem: MenuItem?): Boolean {
                return false
            }

            override fun onMenuItemActionCollapse(menuItem: MenuItem?): Boolean {
                finish()
                return false
            }
        })
    }

    private fun setupSearchView() {

        val closeButton = searchView.findViewById<View>(androidx.appcompat.R.id.search_close_btn)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {

                rvSearch.smoothScrollToPosition(0)
                viewModel.getSearchListOnSubmit(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                d { "onChange: $newText" }
                return false
            }
        })

        closeButton.setOnClickListener {

            searchView.setQuery("", false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val menuItem = menu?.findItem(R.id.searchView) as MenuItem
        searchView = menuItem.actionView as SearchView
        expandSearchView(menuItem)
        setupSearchView()
        return true
    }

    override fun onClickNews(article: Article) {

        val intent = Intent(baseContext, DetailArticleActivity::class.java).apply {

            putExtra(DetailArticleActivity.EXTRA_ARTICLE, article)
        }
        startActivity(intent)
    }

    override fun onClickShare(url: String) {

        CommnonUtil.shareArticle(this, url)
    }

    override fun onClickBookmark(article: Article) {

        showToast("Coming!")
    }

    private fun defaultLayoutState() {

        searchLayout.toVisible()
        searchLayoutEmpty.toGone()
        searchLayoutError.toGone()
    }
    private fun emptyLayoutState() {

        searchLayout.toGone()
        searchLayoutEmpty.toVisible()
        searchLayoutError.toGone()
    }
    private fun errorLayoutState() {

        searchLayout.toGone()
        searchLayoutEmpty.toGone()
        searchLayoutError.toVisible()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.changeStateLiveData.removeObserver(onChangeStateObserver)
        viewModel.submitStateLiveData.removeObserver(onSubmitStateObserver)
    }

    private fun obtainViewModel() = obtainViewModel(viewModelFactory, SearchViewModel::class.java)
}
