package hoge.hogehoge.myapplication.presentation.article.articlelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hoge.hogehoge.myapplication.R
import hoge.hogehoge.myapplication.databinding.FragmentArticleListBinding
import hoge.hogehoge.myapplication.di.viewmodel.ViewModelFactory
import hoge.hogehoge.myapplication.domain.result.Result
import hoge.hogehoge.myapplication.presentation.base.BaseFragment
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject
import timber.log.Timber

class ArticleListFragment : BaseFragment() {
    companion object {
        fun newInstance(): ArticleListFragment {
            return ArticleListFragment()
        }
    }

    private lateinit var binding: FragmentArticleListBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ArticleListViewModel

    //region lifecycle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_list, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ArticleListViewModel::class.java)

        bindUI()
        bindViewModelEvent()
        bindViewModelValue()
        fetchData()

        return binding.root
    }

    //endregion

    private fun bindUI() {
        with(binding.swipeRefreshLayout) {
            setOnRefreshListener {
                (binding.articleRecyclerView.adapter as? ArticleListAdapter)?.clearArticles()
                viewModel.resetLastReadPage()
                viewModel.fetchArticlesIfNotLoading()
            }
        }

        with(binding.articleRecyclerView) {
            layoutManager = LinearLayoutManager(this@ArticleListFragment.context)
            adapter = ArticleListAdapter(context, compositeDisposable)

            // 画面下にスクロールした時の処理
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = recyclerView.adapter?.itemCount // 合計のアイテム数
                    val firstPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() // RecyclerViewの一番上に表示されているアイテムのポジション
                    val visibleChildCount = recyclerView.childCount // RecyclerViewに表示されてるアイテム数
                    if (totalItemCount == firstPosition + visibleChildCount) {
                        viewModel.fetchArticlesIfNotLoading()
                    }
                }
            })
        }
    }

    private fun bindViewModelEvent() {
        viewModel.articlesEvent
            .subscribe { result ->
                Timber.d("articlesEvent :$result")
                if (result is Result.Failure) {
                    handleArticlesEventError(result.error)
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
                    insertArticles(it)
                }
            }
            .addTo(compositeDisposable)
    }

    private fun fetchData() {
        viewModel.fetchArticlesIfNotLoading()
    }

    //region handle error

    private fun handleArticlesEventError(error: Throwable) {
        val title = getString(R.string.error_get_articles_title)
        val message = getString(R.string.error_get_articles_message)

        showDialog(
            title,
            message,
            okText = getString(R.string.common_dialog_retry),
            doOnClickOk = {
                viewModel.fetchArticlesIfNotLoading()
            }
        )
    }

    //endregion
}
