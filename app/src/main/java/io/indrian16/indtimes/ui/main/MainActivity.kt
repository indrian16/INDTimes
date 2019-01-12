package io.indrian16.indtimes.ui.main

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.indrian16.indtimes.R
import io.indrian16.indtimes.ui.base.BaseActivity
import io.indrian16.indtimes.ui.favorite.FavoriteFragment
import io.indrian16.indtimes.util.Category
import io.indrian16.indtimes.ui.news.NewsFragment
import io.indrian16.indtimes.ui.search.SearchActivity
import io.indrian16.indtimes.util.showToast
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity(), HasSupportFragmentInjector {

    private val botListener = BottomNavigationView.OnNavigationItemSelectedListener {

        when (it.itemId) {

            R.id.navNews -> {

                changeFragmentWithBot(NewsFragment.newInstance(currentCategory))
                return@OnNavigationItemSelectedListener true
            }

            R.id.navFavorite -> {

                changeFragmentWithBot(FavoriteFragment.newInstance())
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

                currentCategory = Category.ENTERTAINMENT
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, NewsFragment.newInstance(currentCategory))
                .commit()
        }
        setupToolbar()
        initView()
    }

    private fun setupToolbar() {

        setSupportActionBar(mainToolbar).apply {

            title = getString(R.string.app_name)
        }
    }

    private fun initView() {

        val toggle = ActionBarDrawerToggle(
            this, mainDrawer, mainToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        mainDrawer.addDrawerListener(toggle)
        toggle.syncState()

        mainBotNavView.setOnNavigationItemSelectedListener(botListener)
        mainNavView.setNavigationItemSelectedListener(navListener)
        mainNavView.setCheckedItem(R.id.catAll)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when (item?.itemId) {

            android.R.id.home -> {

                mainDrawer.openDrawer(GravityCompat.START)
                true
            }

            R.id.showSearch -> {

                startActivity(Intent(this, SearchActivity::class.java))
                true
            }

            R.id.showSetting -> {

                showToast("Setting coming soon")
                true
            }

            else -> onOptionsItemSelected(item)
        }

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

    override fun onBackPressed() {

        if (mainDrawer.isDrawerOpen(GravityCompat.START)) {
            mainDrawer.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }
}
