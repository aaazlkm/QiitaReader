package hoge.hogehoge.presentation.article.articlesaved

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import hoge.hogehoge.domain.entity.Article
import hoge.hogehoge.presentation.R
import hoge.hogehoge.presentation.databinding.ItemArticleSavedBinding

class ArticleSavedAdapter : RecyclerView.Adapter<ArticleSavedAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClicked(article: Article.Saved)
    }

    private val articles = mutableListOf<Article.Saved>()

    private var onItemClickListener: OnItemClickListener? = null

    class ViewHolder(val binding: ItemArticleSavedBinding) : RecyclerView.ViewHolder(binding.root)

    //region RecyclerView.Adapter override methods

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemArticleSavedBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_article_saved, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        with(holder.binding) {
            titleText.text = article.title
            container.setOnClickListener { onItemClickListener?.onItemClicked(article) }
            alreadyReadMark.visibility = if (article.alreadyRead) View.GONE else View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    //endregion

    fun insertArticles(articles: List<Article.Saved>) {
        this.articles.addAll(articles)
        notifyDataSetChanged()
    }

    fun clearArticles() {
        articles.clear()
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    fun removeOnItemClickListener() {
        this.onItemClickListener = null
    }
}
