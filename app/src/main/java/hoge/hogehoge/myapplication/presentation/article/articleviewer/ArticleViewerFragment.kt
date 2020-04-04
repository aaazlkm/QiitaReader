package hoge.hogehoge.myapplication.presentation.article.articleviewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import hoge.hogehoge.myapplication.R
import hoge.hogehoge.myapplication.databinding.FragmentArticleViewerBinding
import hoge.hogehoge.myapplication.di.viewmodel.ViewModelFactory
import hoge.hogehoge.myapplication.domain.entity.Article
import hoge.hogehoge.myapplication.domain.result.Result
import hoge.hogehoge.myapplication.presentation.base.BaseFragment
import io.noties.markwon.Markwon
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject
import timber.log.Timber

class ArticleViewerFragment : BaseFragment() {
    companion object {
        private const val KEY_ARTICLE_ID = "KEY_ARTICLE_ID"

        fun newInstance(articleId: String): ArticleViewerFragment {
            return ArticleViewerFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_ARTICLE_ID, articleId)
                }
            }
        }
    }

    private lateinit var binding: FragmentArticleViewerBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ArticleViewerViewModel

    private lateinit var articleId: String

    @Inject
    lateinit var markwon: Markwon

    //region lifecycle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_viewer, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ArticleViewerViewModel::class.java)
        articleId = arguments?.getString(KEY_ARTICLE_ID) ?: run {
            handleErrorWhenFailedToGetArticleId(Exception("記事IDの取得に失敗しました"))
            return binding.root
        }

        bindUI()
        bindViewModelEvent()
        bindViewModelValue()
        fetchData()

        return binding.root
    }

    //endregion

    private fun bindUI() {
        binding.floatingActionButton
            .setOnClickListener {
                val article = viewModel.articleValue ?: return@setOnClickListener
                showDialogConfirmationOfSavingArticle(article)
            }

        binding.scrollView
            .setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                if (scrollY > oldScrollY) {
                    binding.floatingActionButton.hide()
                } else if (scrollY < oldScrollY) {
                    binding.floatingActionButton.show()
                }
            }
    }

    private fun bindViewModelEvent() {
        viewModel.eventOfGettingArticle
            .subscribe { result ->
                Timber.d("eventOfGettingArticle :$result")
                if (result is Result.Failure) {
                    handleErrorWhenFailedToGetArticle(result.error)
                }
            }
            .addTo(compositeDisposable)

        viewModel.eventOfSavingArticle
            .subscribe { result ->
                Timber.d("eventOfSavingArticle :$result")
                when (result) {
                    is Result.Success -> {
                        showToast(getString(R.string.fragment_article_viewer_toast_save_article))
                    }
                    is Result.Failure -> {
                        handleErrorWhenFailedToSaveArticle(result.error)
                    }
                }
            }
            .addTo(compositeDisposable)
    }

    private fun bindViewModelValue() {
        viewModel.isLoading
            .subscribe {
                setLocadingView(it)
            }
            .addTo(compositeDisposable)

        viewModel.article
            .subscribe {
                setupActionBar(it.title)
                setMarkdownText(it.bodyMarkDown)
            }
            .addTo(compositeDisposable)
    }

    private fun fetchData() {
        viewModel.fetchArticle(articleId)
    }

    private fun setMarkdownText(markdownText: String) {
        val node = markwon.parse(markdownText)
        val markdownRendered = markwon.render(node)
        markwon.setParsedMarkdown(binding.markdownText, markdownRendered)
    }

    //region dialog

    private fun showDialogConfirmationOfSavingArticle(article: Article) {
        val title = getString(R.string.fragment_article_viewer_dialog_confirm_saving_article_title)

        showDialog(
            title,
            article.title,
            doOnClickOk = {
                viewModel.saveArticle(article)
            }
        )
    }

    //endregion

    //region handle error

    private fun handleErrorWhenFailedToGetArticleId(error: Throwable) {
        Timber.e(error)
        showToast(getString(hoge.hogehoge.myapplication.R.string.fragment_article_viewer_error_get_article_id))
        navigationController.popFragment()
    }

    private fun handleErrorWhenFailedToGetArticle(error: Throwable) {
        Timber.e(error)
        showToast(getString(hoge.hogehoge.myapplication.R.string.fragment_article_viewer_error_get_article))
        navigationController.popFragment()
    }

    private fun handleErrorWhenFailedToSaveArticle(error: Throwable) {
        Timber.e(error)
        showToast(getString(R.string.fragment_article_viewer_error_save_article))
    }

    //endregion
}
