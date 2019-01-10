package io.indrian16.indtimes.ui.news.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.indrian16.indtimes.R
import io.indrian16.indtimes.data.model.Article
import kotlinx.android.synthetic.main.article_item.view.*

class RvNewsArticle(private val listener: OnNewsArticleOnClickListener) : RecyclerView.Adapter<RvNewsArticle.NewsHolder>() {

    private var articleList: List<Article> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.article_item, parent, false)
        return NewsHolder(view)
    }

    override fun getItemCount(): Int = articleList.size

    override fun onBindViewHolder(holder: NewsHolder, position: Int) = holder.bind(articleList[position])

    fun add(dataList: List<Article>) {

        articleList = dataList
        notifyDataSetChanged()
    }

    fun clear() {

        articleList = emptyList()
        notifyDataSetChanged()
    }

    inner class NewsHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(article: Article) {

            itemView.apply {

                tvTitle.text = article.title
                Glide.with(this).load(article.urlToImage).into(imgArticle)
                btnBookmark.setOnClickListener { listener.onClickBookmark(article) }
                btnShare.setOnClickListener { listener.onClickShare(article.url) }

            }.setOnClickListener { listener.onClickNews(article) }
        }
    }

    interface OnNewsArticleOnClickListener {

        fun onClickNews(article: Article)

        fun onClickShare(url: String)

        fun onClickBookmark(article: Article)
    }
}