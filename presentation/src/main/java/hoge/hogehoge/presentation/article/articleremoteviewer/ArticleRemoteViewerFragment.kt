package hoge.hogehoge.presentation.article.articleremoteviewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import hoge.hogehoge.core.di.viewmodel.ViewModelFactory
import hoge.hogehoge.domain.entity.Article
import hoge.hogehoge.domain.result.Result
import hoge.hogehoge.presentation.R
import hoge.hogehoge.presentation.base.BaseFragment
import hoge.hogehoge.presentation.databinding.FragmentArticleRemoteViewerBinding
import io.noties.markwon.Markwon
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject
import timber.log.Timber

class ArticleRemoteViewerFragment : BaseFragment() {
    companion object {
        private const val KEY_ARTICLE_ID = "KEY_ARTICLE_ID"

        fun newInstance(articleId: String): ArticleRemoteViewerFragment {
            return ArticleRemoteViewerFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_ARTICLE_ID, articleId)
                }
            }
        }
    }

    private lateinit var binding: FragmentArticleRemoteViewerBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ArticleRemoteViewerViewModel

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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_remote_viewer, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ArticleRemoteViewerViewModel::class.java)
        articleId = arguments?.getString(KEY_ARTICLE_ID) ?: run {
            handleErrorFailedToGetArticleId(Exception("記事IDの取得に失敗しました"))
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
                    handleErrorFailedToGetArticle(result.error)
                }
            }
            .addTo(compositeDisposable)

        viewModel.eventOfSavingArticle
            .subscribe { result ->
                Timber.d("eventOfSavingArticle :$result")
                when (result) {
                    is Result.Success -> {
                        showToast(getString(R.string.fragment_article_remote_viewer_toast_save_article))
                    }
                    is Result.Failure -> {
                        handleErrorFailedToSaveArticle(result.error)
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
                binding.titleText.text = it.title
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
        val title = getString(R.string.fragment_article_remote_viewer_dialog_confirm_saving_article_title)

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

    private fun handleErrorFailedToGetArticleId(error: Throwable) {
        Timber.e(error)
        showToast(getString(R.string.fragment_article_remote_viewer_error_get_article_id))
        navigationController.popFragment()
    }

    private fun handleErrorFailedToGetArticle(error: Throwable) {
        Timber.e(error)
        showToast(getString(R.string.fragment_article_remote_viewer_error_get_article))
        navigationController.popFragment()
    }

    private fun handleErrorFailedToSaveArticle(error: Throwable) {
        Timber.e(error)
        showToast(getString(R.string.fragment_article_remote_viewer_error_save_article))
    }

    //endregion
}
