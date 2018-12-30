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
import io.indrian16.indtimes.ui.search.adapter.RvSearchItem
import io.indrian16.indtimes.util.*
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class SearchActivity : AppCompatActivity(), RvSearchItem.OnSearchClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SearchViewModel
    private val mAdapter = RvSearchItem(this)

    private val searchStateObserver = Observer<SearchState> { state ->

        when (state) {

            is DefaultSearchState -> {

                d {"Default State"}
                rvSearch.toVisible()
                progressBar.toGone()
                mAdapter.add(state.dataList)
            }


            is NoInputSearchState -> {

                d {"No Input State"}
                rvSearch.toGone()
                progressBar.toGone()
            }

            is LoadingSearchState -> {

                d {"Loading State"}
                rvSearch.toGone()
                progressBar.toVisible()
            }

            is NotFoundSearchState -> {

                d {"NotFoundSearch State"}
                rvSearch.toGone()
                progressBar.toGone()
                showToast("Not Found Item!")
            }

            is ErrorSearchState -> {

                d {"Error State"}
                rvSearch.toGone()
                progressBar.toGone()
                showToast(state.errorMessage)
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

                d { "onSubmit: $query" }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                viewModel.getSearchListOnChange(newText!!)
                return false
            }
        })

        closeButton.setOnClickListener {

            searchView.setQuery("", false)
            viewModel.getSearchListOnChange("")
            mAdapter.clear()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val menuItem = menu?.findItem(R.id.searchView) as MenuItem
        expandSearchView(menuItem)
        return true
    }

    override fun onClickArticle(article: Article) {

        val intent = Intent(baseContext, DetailArticleActivity::class.java)
        intent.putExtra(DetailArticleActivity.EXTRA_ARTICLE, article)

        startActivity(intent)
    }

    private fun obtainViewModel() = obtainViewModel(viewModelFactory, SearchViewModel::class.java)
}
