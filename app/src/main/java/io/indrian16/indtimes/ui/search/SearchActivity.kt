package io.indrian16.indtimes.ui.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.github.ajalt.timberkt.d
import dagger.android.AndroidInjection
import io.indrian16.indtimes.R
import io.indrian16.indtimes.data.model.Article
import io.indrian16.indtimes.ui.detail.DetailArticleActivity
import io.indrian16.indtimes.ui.news.adapter.RvNewsArticle
import io.indrian16.indtimes.util.*
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class SearchActivity : AppCompatActivity(), RvNewsArticle.OnNewsArticleOnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SearchViewModel
    private val mAdapter = RvNewsArticle(this)

    private val searchStateObserver = Observer<SearchState> { state ->

        when (state) {

            is DefaultSearchState -> {

                d {"Default State"}
                searchLayout.toVisible()
                searchLayoutError.toGone()
                searchLayoutNoFound.toGone()
                rvSearch.toVisible()
                progressBar.toGone()
                mAdapter.add(state.dataList)
            }


            is NoInputSearchState -> {

                d {"No Input State"}
                searchLayout.toVisible()
                searchLayoutError.toGone()
                searchLayoutNoFound.toGone()
                rvSearch.toGone()
                progressBar.toGone()
            }

            is LoadingSearchState -> {

                d {"Loading State"}
                searchLayout.toVisible()
                searchLayoutError.toGone()
                searchLayoutNoFound.toGone()
                rvSearch.toGone()
                progressBar.toVisible()
            }

            is NotFoundSearchState -> {

                d {"NotFoundSearch State"}
                searchLayout.toGone()
                searchLayoutError.toGone()
                searchLayoutNoFound.toVisible()
            }

            is ErrorSearchState -> {

                d {"Error State"}
                searchLayout.toGone()
                searchLayoutError.toVisible()
                searchLayoutNoFound.toGone()
                d { state.errorMessage }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initView()

        viewModel = obtainViewModel().apply {

            stateLiveData.observe(this@SearchActivity, searchStateObserver)
        }
    }

    private fun initView() {

        rvSearch.apply {

            layoutManager = LinearLayoutManager(baseContext)
            adapter = mAdapter
        }
    }

    private fun expandSearchView(menuItem: MenuItem) {

        val searchView = menuItem.actionView as SearchView
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
        setupSearchView(searchView)
    }

    private fun setupSearchView(searchView: SearchView) {

        val closeButton = searchView.findViewById<View>(android.support.v7.appcompat.R.id.search_close_btn)
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
        expandSearchView(menuItem)
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

    private fun obtainViewModel() = obtainViewModel(viewModelFactory, SearchViewModel::class.java)
}
