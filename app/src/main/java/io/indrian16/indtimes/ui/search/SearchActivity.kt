package io.indrian16.indtimes.ui.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.github.ajalt.timberkt.d
import dagger.android.AndroidInjection
import io.indrian16.indtimes.R
import io.indrian16.indtimes.util.obtainViewModel
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SearchViewModel

    private val searchStateObserver = Observer<SearchState> { state ->

        when (state) {

            is DefaultSearchState -> {

                d {"Default State"}
                d{"Dapat: ${state.dataList[0].title}"}
            }

            is LoadingSearchState -> {

                d {"Loading State"}
            }

            is NotFoundSearchState -> {

                d {"Not Found State"}
            }

            is ErrorSearchState -> {

                d {"Error State: ${state.errorMessage}"}
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModel = obtainViewModel().apply {

            stateLiveData.observe(this@SearchActivity, searchStateObserver)
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

                newText?.let {

                    if (!TextUtils.isEmpty(it)) {

                        Handler().postDelayed({ viewModel.getNewsListOnChange(newText) }, 400)
                    }
                }
                return false
            }
        })

        closeButton.setOnClickListener {

            searchView.setQuery("", false)
            viewModel.getNewsListOnChange("")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val menuItem = menu?.findItem(R.id.searchView) as MenuItem
        expandSearchView(menuItem)
        return true
    }

    private fun obtainViewModel(): SearchViewModel = obtainViewModel(viewModelFactory, SearchViewModel::class.java)
}
