package io.indrian16.indtimes.ui.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.indrian16.indtimes.R
import io.indrian16.indtimes.ui.bookmark.BookmarkFragment
import io.indrian16.indtimes.util.Category
import io.indrian16.indtimes.ui.news.NewsFragment
import io.indrian16.indtimes.util.showToast
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    private val botListener = BottomNavigationView.OnNavigationItemSelectedListener {

        when (it.itemId) {

            R.id.navNews -> {

                changeFragmentWithBot(NewsFragment.newInstance(currentCategory))
                return@OnNavigationItemSelectedListener true
            }

            R.id.navBookmark -> {

                changeFragmentWithBot(BookmarkFragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
        }

        return@OnNavigationItemSelectedListener false
    }

    private val navListener = NavigationView.OnNavigationItemSelectedListener {

        when (it.itemId) {

            R.id.catAll -> {

                currentCategory = Category.ALL
                changeFragmentWithDrawer()
                return@OnNavigationItemSelectedListener true
            }

            R.id.catBusiness -> {

                currentCategory = Category.BUSINESS
                changeFragmentWithDrawer()
                return@OnNavigationItemSelectedListener true
            }

            R.id.catEntertainment -> {

                currentCategory = Category.ENTERTAIMENT
                changeFragmentWithDrawer()
                return@OnNavigationItemSelectedListener true
            }

            R.id.catHealth -> {

                currentCategory = Category.HEALTH
                changeFragmentWithDrawer()
                return@OnNavigationItemSelectedListener true
            }

            R.id.catScience -> {

                currentCategory = Category.SCIENCE
                changeFragmentWithDrawer()
                return@OnNavigationItemSelectedListener true
            }

            R.id.catSports -> {

                currentCategory = Category.SPORTS
                changeFragmentWithDrawer()
                return@OnNavigationItemSelectedListener true
            }

            R.id.catTechnology -> {

                currentCategory = Category.TECHNOLOGY
                changeFragmentWithDrawer()
                return@OnNavigationItemSelectedListener true
            }
        }

        return@OnNavigationItemSelectedListener true
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private var currentCategory = Category.ALL

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        changeFragmentWithBot(NewsFragment.newInstance(currentCategory))
        setupToolbar()
        initView()
    }

    private fun setupToolbar() {

        setSupportActionBar(mainToolbar)

        if (supportActionBar != null) supportActionBar?.let {

            it.title = getString(R.string.app_name)
        }
    }

    private fun initView() {

        mainBotNavView.setOnNavigationItemSelectedListener(botListener)
        mainNavView.setNavigationItemSelectedListener(navListener)
        mainNavView.setCheckedItem(R.id.catAll)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {

            R.id.showSearch -> showToast("Search coming soon")

            R.id.showMenu -> mainDrawer.openDrawer(Gravity.END)

            R.id.showSetting -> showToast("Setting coming soon")
        }

        return true
    }

    private fun changeFragmentWithBot(fragment: Fragment) {

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, fragment)
            .commit()
    }

    private fun changeFragmentWithDrawer() {

        supportFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, NewsFragment.newInstance(currentCategory))
                .commit()
        mainDrawer.closeDrawers()
        mainBotNavView.selectedItemId = R.id.navNews
    }
}
