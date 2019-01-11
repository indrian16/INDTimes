package io.indrian16.indtimes.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.ajalt.timberkt.d

import io.indrian16.indtimes.R
import io.indrian16.indtimes.data.model.Article
import io.indrian16.indtimes.data.model.Favorite
import io.indrian16.indtimes.ui.base.BaseFragment
import io.indrian16.indtimes.ui.detail.DetailArticleActivity
import io.indrian16.indtimes.ui.favorite.adapter.RvFavorite
import io.indrian16.indtimes.util.obtainViewModel
import io.indrian16.indtimes.util.toGone
import io.indrian16.indtimes.util.toVisible
import kotlinx.android.synthetic.main.fragment_favorite.*
import javax.inject.Inject

class FavoriteFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener, RvFavorite.OnFavoriteClickListener {

    companion object {

        const val RV_FAVORITE_LAYOUT = "rvState"

        fun newInstance() = FavoriteFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: FavoriteViewModel

    private val mAdapter = RvFavorite(this)
    private lateinit var rvState: Parcelable

    private val favoriteListStateObserver = Observer<FavoriteListState> { state ->

        when (state) {

            is DefaultState -> {

                d { "Default State" }
                defaultLayoutState()
                favoriteLayout.isRefreshing = false
                mAdapter.clear()
                mAdapter.add(state.dataList)
            }

            is LoadingState -> {

                defaultLayoutState()
                favoriteLayout.isRefreshing = true
            }

            is EmptyState -> {

                emptyLayoutState()
                mAdapter.clear()
            }

            is ErrorState -> {

                errorLayoutState()
                mAdapter.clear()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel().apply {

            favoriteListStateLiveData.observe(this@FavoriteFragment, favoriteListStateObserver)
        }
        viewModel.updateFavorite()
    }

    private fun initView() {

        favoriteLayout.setOnRefreshListener(this)
        btnRefresh.setOnClickListener { onRefresh() }
        btnTryAgain.setOnClickListener { onRefresh() }

        rvFavorite.apply {

            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.favoriteListStateLiveData.removeObserver(favoriteListStateObserver)
    }

    override fun onRefresh() {

        mAdapter.clear()
        viewModel.refreshFavorite()
    }

    override fun onClickFavorite(favorite: Favorite) {

        val currentArticle = Article(
            saveTime = favorite.saveTime,
            author = favorite.author,
            content = favorite.content,
            description = favorite.description,
            publishedAt = favorite.publishedAt,
            title = favorite.title,
            url = favorite.url,
            urlToImage = favorite.urlToImage
        )

        val intent = Intent(context, DetailArticleActivity::class.java).apply {

            putExtra(DetailArticleActivity.EXTRA_ARTICLE, currentArticle)
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

        viewModel.refreshFavorite()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        //outState.putParcelable(RV_FAVORITE_LAYOUT, rvFavorite.layoutManager?.onSaveInstanceState())
    }

    private fun defaultLayoutState() {

        favoriteLayout.toVisible()
        favoriteLayoutEmpty.toGone()
        favoriteLayoutError.toGone()
    }

    private fun emptyLayoutState() {

        favoriteLayout.toGone()
        favoriteLayoutEmpty.toVisible()
        favoriteLayoutError.toGone()
    }

    private fun errorLayoutState() {

        favoriteLayout.toGone()
        favoriteLayoutEmpty.toGone()
        favoriteLayoutError.toVisible()
    }

    private fun obtainViewModel() = obtainViewModel(viewModelFactory, FavoriteViewModel::class.java)
}
