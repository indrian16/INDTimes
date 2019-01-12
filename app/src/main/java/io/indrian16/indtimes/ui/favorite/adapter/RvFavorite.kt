package io.indrian16.indtimes.ui.favorite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.indrian16.indtimes.R
import io.indrian16.indtimes.data.model.Favorite
import io.indrian16.indtimes.di.module.GlideApp
import kotlinx.android.synthetic.main.favorite_item.view.*

class RvFavorite(private val listener: OnFavoriteClickListener) : RecyclerView.Adapter<RvFavorite.FavoriteHolder>() {

    private var favoriteList: List<Favorite> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.favorite_item, parent, false)
        return FavoriteHolder(view)
    }

    override fun getItemCount(): Int = favoriteList.size

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) = holder.bind(favoriteList[position])

    fun add(dataList: List<Favorite>) {

        favoriteList = dataList
        notifyDataSetChanged()
    }

    fun clear() {

        favoriteList = emptyList()
    }

    inner class FavoriteHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(favorite: Favorite) {

            itemView.apply {

                tvTitle.text = favorite.title
                GlideApp.with(this).load(favorite.urlToImage).into(imgArticle)
                btnShare.setOnClickListener { listener.onClickShare(favorite.url) }

            }.setOnClickListener { listener.onClickItem(favorite) }
        }
    }

    interface OnFavoriteClickListener {

        fun onClickItem(favorite: Favorite)

        fun onClickShare(url: String)
    }
}