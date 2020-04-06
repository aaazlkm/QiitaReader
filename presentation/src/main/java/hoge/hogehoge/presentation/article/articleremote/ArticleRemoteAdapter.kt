package hoge.hogehoge.presentation.article.articleremote

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import hoge.hogehoge.core.extension.format
import hoge.hogehoge.core.utility.DownloadUtility
import hoge.hogehoge.domain.entity.Article
import hoge.hogehoge.presentation.R
import hoge.hogehoge.presentation.databinding.ItemArticleBinding
import hoge.hogehoge.presentation.databinding.ItemProgressBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class ArticleRemoteAdapter(
    private val context: Context,
    private val compositeDisposable: CompositeDisposable
) : RecyclerView.Adapter<ArticleRemoteAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClicked(article: Article)
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    sealed class Item {
        data class Article(val article: hoge.hogehoge.domain.entity.Article.Remote) : Item() {
            companion object {
                const val VIEW_TYPE = 0
            }
        }

        object Progress : Item() {
            const val VIEW_TYPE = 1
        }
    }

    sealed class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        data class Article(val binding: ItemArticleBinding) : ViewHolder(binding.root)

        data class Progress(val binding: ItemProgressBinding) : ViewHolder(binding.root)
    }

    private val items = mutableListOf<Item>()

    private val articleToDisposable = hashMapOf<Article.Remote, Disposable>()

    private var onItemClickListener: OnItemClickListener? = null
    private var onLoadMoreListener: OnLoadMoreListener? = null

    //region RecyclerView.Adapter override methods

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            Item.Article.VIEW_TYPE -> {
                val articleBinding: ItemArticleBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_article, parent, false)
                ViewHolder.Article(articleBinding)
            }
            Item.Progress.VIEW_TYPE -> {
                val progressBinding: ItemProgressBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_progress, parent, false)
                ViewHolder.Progress(progressBinding)
            }
            else -> throw IllegalArgumentException("illegal view type. please confirm view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder.Article -> {
                val item = items[position] as Item.Article
                val article = item.article
                with(holder.binding) {
                    titleText.text = article.title
                    likeCountText.text = context.getString(R.string.fragment_article_remote_item_article_lgtm, article.likesCount)
                    userNameText.text = context.getString(R.string.fragment_article_remote_item_article_by, article.user.id)
                    createdAtText.text = context.getString(R.string.fragment_article_remote_item_article_at, article.createdAt.format("yyyy/MM/dd"))
                    container.setOnClickListener { onItemClickListener?.onItemClicked(article) }
                }
                loadUserIcon(holder, article)
            }
            is ViewHolder.Progress -> {
                onLoadMoreListener?.onLoadMore()
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Item.Article -> Item.Article.VIEW_TYPE
            is Item.Progress -> Item.Progress.VIEW_TYPE
        }
    }

    //endregion

    fun insertArticlesAndResetProgress(articles: List<Article.Remote>) {
        val prevItemSize = this.items.size

        this.items.mapIndexed { index, item -> index to item }
            .filter { it.second is Item.Progress }
            .forEach {
                val (position, item) = it
                this.items.removeAt(position)
                this.notifyItemRemoved(position)
            }

        if (articles.isNotEmpty()) {
            this.items.addAll(articles.map { Item.Article(it) })
            this.items.add(Item.Progress)
            notifyItemRangeInserted(prevItemSize, this.items.size - prevItemSize)
        }
    }

    fun getArticles(): List<Article.Remote> {
        return items.mapNotNull { it as? Item.Article }.map { it.article }
    }

    fun clearArticles() {
        items.clear()
        articleToDisposable.values.forEach { it.dispose() }
        articleToDisposable.clear()
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.onItemClickListener = listener
    }

    fun setOnLoadMoreListener(listener: OnLoadMoreListener?) {
        this.onLoadMoreListener = listener
    }

    //region load user icon

    private fun loadUserIcon(holder: ViewHolder.Article, article: Article.Remote) {
        holder.binding.userIconView.tag = article

        article.userIcon?.let {
            holder.binding.userIconView.setImageBitmap(it)
        } ?: run {
            holder.binding.userIconView.setImageDrawable(context.getDrawable(R.drawable.background_loading))
            loadUserIconFromUrl(holder, article)
        }
    }

    private fun loadUserIconFromUrl(holder: ViewHolder.Article, article: Article.Remote) {
        // 同じurlに対するリクエストが重複して走らないようにする
        articleToDisposable[article]?.dispose()
        articleToDisposable.remove(article)

        DownloadUtility.loadBitmapFromUrl(article.user.profileImageUrl)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { userIcon ->
                    article.userIcon = userIcon
                    if (article == holder.binding.userIconView.tag) {
                        holder.binding.userIconView.setImageBitmap(userIcon)
                    }
                },
                onError = {
                    Timber.e("failed to load image $it")
                }
            )
            .addTo(compositeDisposable)
            .let { articleToDisposable[article] = it }
    }

    //endregion
}
