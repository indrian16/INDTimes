package io.indrian16.indtimes.ui.search

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.github.ajalt.timberkt.d
import io.indrian16.indtimes.R

class SearchActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
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

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {

                d { "onSubmit: $query" }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                // debounce
                Handler().postDelayed({ viewModel.searchOnChange(newText!!) }, 400)
                return false
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val menuItem = menu?.findItem(R.id.searchView) as MenuItem
        expandSearchView(menuItem)
        return true
    }
}
