package io.indrian16.indtimes.ui.news.rv

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import io.indrian16.indtimes.R
import io.indrian16.indtimes.data.model.Article
import kotlinx.android.synthetic.main.article_item.view.*

class RvNewsArticle(private val listener: OnNewsArticleOnClickListener) : RecyclerView.Adapter<RvNewsArticle.NewsHolder>() {

    private var newsArticleList: List<Article> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.article_item, parent, false)
        return NewsHolder(view)
    }

    override fun getItemCount(): Int = newsArticleList.size

    override fun onBindViewHolder(holder: NewsHolder, position: Int) = holder.bind(newsArticleList[position])

    fun add(dataList: List<Article>) {

        newsArticleList = dataList
        notifyDataSetChanged()
    }

    fun clear() {

        newsArticleList = emptyList()
        notifyDataSetChanged()
    }

    inner class NewsHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(article: Article) {

            itemView.apply {

                articleTitle.text = article.title
                articlePublishAt.text = "" // Coming

                Glide.with(this)
                    .load(article.urlToImage)
                    .into(articleImage)

                setOnClickListener { listener.onClickNews(article) }
                btnShare.setOnClickListener { listener.onClickShare(article.url) }
                btnBookmark.setOnClickListener { listener.onClickBookmark(article) }
            }

        }
    }

    interface OnNewsArticleOnClickListener {

        fun onClickNews(article: Article)

        fun onClickShare(url: String)

        fun onClickBookmark(article: Article)
    }
}