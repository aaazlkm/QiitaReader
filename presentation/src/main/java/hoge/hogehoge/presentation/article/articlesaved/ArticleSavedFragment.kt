package hoge.hogehoge.presentation.article.articlesaved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hoge.hogehoge.core.di.viewmodel.ViewModelFactory
import hoge.hogehoge.domain.entity.Article
import hoge.hogehoge.presentation.R
import hoge.hogehoge.presentation.base.BaseFragment
import hoge.hogehoge.presentation.databinding.FragmentArticleSavedBinding
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject
import kotlinx.android.synthetic.main.view_retry.messageText
import kotlinx.android.synthetic.main.view_retry.retryButton
import timber.log.Timber

class ArticleSavedFragment : BaseFragment() {
    companion object {

        fun newInstance() = ArticleSavedFragment()
    }

    private lateinit var binding: FragmentArticleSavedBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ArticleSavedViewModel

    //region lifecycle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_saved, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ArticleSavedViewModel::class.java)

        bindUI()
        bindViewModelEvent()
        bindViewModelValue()
        fetchData()

        return binding.root
    }

    //region override BaseFragment methods

    override fun setupActionBar(title: String) {
        super.setupActionBar(getString(R.string.fragment_article_pager_title))
    }

    //endregion

    private fun bindUI() {
        with(binding.swipeRefreshLayout) {
            setColorSchemeResources(R.color.color_accent)
            setOnRefreshListener {
                (binding.articleRecyclerView.adapter as? ArticleSavedAdapter)?.clearArticles()
                viewModel.fetchArticles()
            }
        }

        with(binding.articleRecyclerView) {
            layoutManager = LinearLayoutManager(this.context)
            adapter = ArticleSavedAdapter().apply {
                setOnItemClickListener(object : ArticleSavedAdapter.OnItemClickListener {
                    override fun onItemClicked(article: Article.Saved) {
                        navigationController.toArticleSavedViewerFragment(article.articleId)
                    }
                })
            }
        }
    }

    private fun bindViewModelEvent() {
        viewModel.eventOfGettingArticles
            .subscribe { result ->
                Timber.d("eventOfGettingArticles :$result")
                if (result is hoge.hogehoge.domain.result.Result.Failure) {
                    handleErrorGettingArticles(result.error)
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
                binding.emptyArticleView.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                binding.numberOfArticleText.text = getString(R.string.fragment_article_saved_number_of_articles, it.size)
                (binding.articleRecyclerView.adapter as? ArticleSavedAdapter)?.run {
                    insertArticles(it)
                }
            }
            .addTo(compositeDisposable)
    }

    private fun fetchData() {
        viewModel.fetchArticles()
    }

    //region handle error

    private fun handleErrorGettingArticles(error: Throwable) {
        Timber.e(error)
        val message = getString(R.string.fragment_article_saved_error_get_articles_message)

        with(binding.retryView.root) {
            visibility = View.VISIBLE
            messageText.text = message
            retryButton.setOnClickListener {
                binding.retryView.root.visibility = View.GONE
                viewModel.fetchArticles()
            }
        }
    }

    //endregion
}
