package hoge.hogehoge.myapplication.presentation.article.articlelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hoge.hogehoge.myapplication.R
import hoge.hogehoge.myapplication.databinding.FragmentArticleListBinding
import hoge.hogehoge.myapplication.di.viewmodel.ViewModelFactory
import hoge.hogehoge.myapplication.domain.entity.Article
import hoge.hogehoge.myapplication.domain.result.Result
import hoge.hogehoge.myapplication.presentation.base.BaseFragment
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject
import timber.log.Timber

abstract class ArticleListFragment : BaseFragment() {
    private lateinit var binding: FragmentArticleListBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ArticleListViewModel

    /**
     * ViewModelを生成する
     *
     * @param viewModelFactory ViewModelFactory
     * @return ArticleListViewModel
     */
    abstract fun createViewModel(viewModelFactory: ViewModelFactory): ArticleListViewModel

    //region lifecycle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_list, container, false)
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
        val articles = (binding.articleRecyclerView.adapter as? ArticleListAdapter)?.articles ?: return
        viewModel.saveState(lastScrollPosition, articles)
    }

    //endregion

    //region override BaseFragment methods

    override fun setupActionBar(title: String) {
        super.setupActionBar(getString(R.string.fragment_article_list_title))
    }

    //endregion

    private fun bindUI() {
        with(binding.swipeRefreshLayout) {
            setColorSchemeResources(R.color.color_accent)
            setOnRefreshListener {
                (binding.articleRecyclerView.adapter as? ArticleListAdapter)?.clearArticles()
                viewModel.resetState()
                viewModel.fetchArticles()
            }
        }

        with(binding.articleRecyclerView) {
            layoutManager = LinearLayoutManager(this@ArticleListFragment.context)
            adapter = ArticleListAdapter(context, compositeDisposable).apply {
                setOnItemClickListener(object : ArticleListAdapter.OnItemClickListener {
                    override fun onItemClicked(article: Article) {
                        navigationController.toArticleViewerFragment(article.articleId)
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
                if (result is Result.Failure) {
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
                (binding.articleRecyclerView.adapter as? ArticleListAdapter)?.apply {
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
        val title = getString(R.string.error_get_articles_title)
        val message = getString(R.string.error_get_articles_message)

        showDialog(
            title,
            message,
            okText = getString(R.string.common_dialog_retry),
            cancelText = getString(R.string.common_dialog_cancel_jp),
            doOnClickOk = {
                viewModel.fetchArticlesOrCache()
            }
        )
    }

    //endregion
}
