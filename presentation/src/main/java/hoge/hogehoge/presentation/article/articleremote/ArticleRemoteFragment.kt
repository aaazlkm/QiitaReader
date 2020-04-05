package hoge.hogehoge.presentation.article.articleremote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hoge.hogehoge.core.di.viewmodel.ViewModelFactory
import hoge.hogehoge.presentation.R
import hoge.hogehoge.presentation.base.BaseFragment
import hoge.hogehoge.presentation.databinding.FragmentArticleRemoteBinding
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject
import kotlinx.android.synthetic.main.view_retry.messageText
import kotlinx.android.synthetic.main.view_retry.retryButton
import timber.log.Timber

abstract class ArticleRemoteFragment : BaseFragment() {
    private lateinit var binding: FragmentArticleRemoteBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ArticleRemoteViewModel

    /**
     * ViewModelを生成する
     *
     * @param viewModelFactory ViewModelFactory
     * @return ArticleRemoteViewModel
     */
    abstract fun createViewModel(viewModelFactory: ViewModelFactory): ArticleRemoteViewModel

    //region lifecycle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_remote, container, false)
        viewModel = createViewModel(viewModelFactory)

        bindUI()
        bindViewModelEvent()
        bindViewModelValue()
        fetchData()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val lastScrollPosition = (binding.articleRecyclerView.layoutManager as? LinearLayoutManager)?.findFirstCompletelyVisibleItemPosition() ?: return
        val articles = (binding.articleRecyclerView.adapter as? ArticleRemoteAdapter)?.articles ?: return
        viewModel.saveState(lastScrollPosition, articles)
    }

    //endregion

    //region override BaseFragment methods

    override fun setupActionBar(title: String) {
        super.setupActionBar(getString(R.string.fragment_article_pager_title))
    }

    //endregion

    private fun bindUI() {
        with(binding.swipeRefreshLayout) {
            setColorSchemeResources(R.color.color_accent)
            setOnRefreshListener {
                (binding.articleRecyclerView.adapter as? ArticleRemoteAdapter)?.clearArticles()
                viewModel.resetState()
                viewModel.fetchArticles()
            }
        }

        with(binding.articleRecyclerView) {
            layoutManager = LinearLayoutManager(this@ArticleRemoteFragment.context)
            adapter = ArticleRemoteAdapter(context, compositeDisposable).apply {
                setOnItemClickListener(object : ArticleRemoteAdapter.OnItemClickListener {
                    override fun onItemClicked(article: hoge.hogehoge.domain.entity.Article) {
                        navigationController.toArticleRemoteViewerFragment(article.articleId)
                    }
                })
            }

            // 画面下にスクロールした時の処理
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = recyclerView.adapter?.itemCount // 合計のアイテム数
                    val firstPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() // RecyclerViewの一番上に表示されているアイテムのポジション
                    val visibleChildCount = recyclerView.childCount // RecyclerViewに表示されてるアイテム数
                    if (totalItemCount == firstPosition + visibleChildCount) {
                        viewModel.fetchArticles()
                    }
                }
            })
        }
    }

    private fun bindViewModelEvent() {
        viewModel.eventOfGettingArticles
            .subscribe { result ->
                Timber.d("eventOfGettingArticles :$result")
                if (result is hoge.hogehoge.domain.result.Result.Failure) {
                    handleErrorArticlesEvent(result.error)
                }
            }
            .addTo(compositeDisposable)
    }

    private fun bindViewModelValue() {
        viewModel.isLoading
            .subscribe {
                binding.swipeRefreshLayout.isRefreshing = it
            }
            .addTo(compositeDisposable)

        viewModel.articles
            .subscribe {
                (binding.articleRecyclerView.adapter as? ArticleRemoteAdapter)?.run {
                    insertArticles(it.articles)
                }
                when (it) {
                    is ArticleResult.Cache -> binding.articleRecyclerView.scrollToPosition(it.position)
                }
            }
            .addTo(compositeDisposable)
    }

    private fun fetchData() {
        viewModel.fetchArticlesOrCache()
    }

    //region handle error

    private fun handleErrorArticlesEvent(error: Throwable) {
        Timber.e(error)
        val message = getString(R.string.fragment_article_remote_error_get_articles_message)

        with(binding.retryView.root) {
            visibility = View.VISIBLE
            messageText.text = message
            retryButton.setOnClickListener {
                binding.retryView.root.visibility = View.GONE
                viewModel.fetchArticlesOrCache()
            }
        }
    }

    //endregion
}
