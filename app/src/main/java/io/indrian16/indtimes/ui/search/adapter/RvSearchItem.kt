package io.indrian16.indtimes.ui.search.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.indrian16.indtimes.R
import io.indrian16.indtimes.data.model.Article
import kotlinx.android.synthetic.main.search_item.view.*

class RvSearchItem(private val listener: OnSearchClickListener) : androidx.recyclerview.widget.RecyclerView.Adapter<RvSearchItem.SearchHolder>(){

    private var articleList: List<Article> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.search_item, parent, false)
        return SearchHolder(view)
    }

    override fun getItemCount(): Int = articleList.size

    override fun onBindViewHolder(holder: SearchHolder, position: Int) = holder.bind(articleList[position])

    fun add(dataList: List<Article>) {

        articleList = dataList
        notifyDataSetChanged()
    }

    fun clear() {

        articleList = emptyList()
        notifyDataSetChanged()
    }

    inner class SearchHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        fun bind(article: Article) {

            itemView.apply {

                tvTitleSearch.text = article.title
                tvDescriptionSearch.text = article.description
            }.setOnClickListener { listener.onClickArticle(article) }
        }
    }

    interface OnSearchClickListener {

        fun onClickArticle(article: Article)
    }
}