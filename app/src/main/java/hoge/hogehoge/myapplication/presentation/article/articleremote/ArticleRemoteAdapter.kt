package hoge.hogehoge.myapplication.presentation.article.articleremote

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import hoge.hogehoge.myapplication.R
import hoge.hogehoge.myapplication.common.extension.format
import hoge.hogehoge.myapplication.common.utility.DownloadUtility
import hoge.hogehoge.myapplication.databinding.ItemArticleBinding
import hoge.hogehoge.myapplication.domain.entity.Article
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

    val articles = mutableListOf<Article.Remote>()

    private val urlToUserIconCache = mutableMapOf<String, Bitmap>()
    private val urlToDisposable = mutableMapOf<String, Disposable>()

    private var onItemClickListener: OnItemClickListener? = null

    class ViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root)

    //region RecyclerView.Adapter override methods

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemArticleBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_article, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        with(holder.binding) {
            titleText.text = article.title
            likeCountText.text = context.getString(R.string.fragment_article_remote_item_article_lgtm, article.likesCount)
            userNameText.text = context.getString(R.string.fragment_article_remote_item_article_by, article.user.id)
            createdAtText.text = context.getString(R.string.fragment_article_remote_item_article_at, article.createdAt.format("yyyy/MM/dd"))
            container.setOnClickListener { onItemClickListener?.onItemClicked(article) }
        }
        loadUserIcon(holder, article.user.profileImageUrl)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    //endregion

    fun insertArticles(articles: List<Article.Remote>) {
        this.articles.addAll(articles)
        notifyDataSetChanged()
    }

    fun clearArticles() {
        articles.clear()
        urlToUserIconCache.clear()
        urlToDisposable.values.forEach { it.dispose() }
        urlToDisposable.clear()
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    fun removeOnItemClickListener() {
        this.onItemClickListener = null
    }

    //region load user icon

    private fun loadUserIcon(holder: ViewHolder, userIconUrl: String) {
        holder.binding.userIconView.tag = userIconUrl

        val userIconCache = urlToUserIconCache[userIconUrl]
        if (userIconCache == null) {
            loadUserIconFromUrl(holder, userIconUrl)
        } else {
            holder.binding.userIconView.setImageBitmap(userIconCache)
        }
    }

    private fun loadUserIconFromUrl(holder: ViewHolder, userIconUrl: String) {
        holder.binding.userIconView.setImageDrawable(context.getDrawable(R.drawable.background_loading))

        // 同じurlに対するリクエストが重複して走らないようにする
        urlToDisposable[userIconUrl]?.dispose()
        urlToDisposable.remove(userIconUrl)

        val disposable = DownloadUtility.loadBitmapFromUrl(userIconUrl)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    val (url, userIcon) = it
                    urlToUserIconCache[url] = userIcon
                    if (url == holder.binding.userIconView.tag) {
                        holder.binding.userIconView.setImageBitmap(userIcon)
                    }
                },
                onError = {
                    Timber.d("failed to load image $it")
                }
            )
            .addTo(compositeDisposable)

        urlToDisposable[userIconUrl] = disposable
    }

    //endregion
}
