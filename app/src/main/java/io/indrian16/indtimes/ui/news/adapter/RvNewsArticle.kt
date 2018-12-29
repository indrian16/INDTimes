package io.indrian16.indtimes.ui.news.adapter

import android.support.v7.widget.PopupMenu
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

    inner class NewsHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(article: Article) {

            val menuPopupMenuListener = PopupMenu.OnMenuItemClickListener {

                when (it?.itemId) {

                    R.id.detailBookmark -> {

                        listener.onClickBookmark(article)
                        true
                    }

                    R.id.detailShare -> {

                        listener.onClickShare(article.url)
                        true
                    }

                    else -> false
                }
            }

            itemView.apply {

                Glide.with(this).load(article.urlToImage).into(imgArticle)
                tvAuthor.text = checkAuthorNull(article.author)
                tvTitle.text = article.title
                tvOptionMenu.setOnClickListener {

                    val popupMenu = PopupMenu(it.context, it)
                    popupMenu.inflate(R.menu.detail_menu)
                    popupMenu.setOnMenuItemClickListener(menuPopupMenuListener)
                    popupMenu.show()
                }

            }.setOnClickListener { listener.onClickNews(article) }
        }

        private fun checkAuthorNull(author: String?): String {

            val label = "${view.resources.getString(R.string.author_label)} "
            val anonymous = view.resources.getString(R.string.author_anonymous)
            return author.let {

                if (!it.isNullOrEmpty()) {

                    label + author
                } else {

                    label + anonymous
                }
            }
        }
    }

    interface OnNewsArticleOnClickListener {

        fun onClickNews(article: Article)

        fun onClickShare(url: String)

        fun onClickBookmark(article: Article)
    }
}