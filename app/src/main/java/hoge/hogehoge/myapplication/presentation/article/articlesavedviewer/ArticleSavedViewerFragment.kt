package hoge.hogehoge.myapplication.presentation.article.articlesavedviewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import hoge.hogehoge.myapplication.R
import hoge.hogehoge.myapplication.databinding.FragmentArticleSavedViewerBinding
import hoge.hogehoge.myapplication.di.viewmodel.ViewModelFactory
import hoge.hogehoge.myapplication.domain.entity.Article
import hoge.hogehoge.myapplication.domain.result.Result
import hoge.hogehoge.myapplication.presentation.base.BaseFragment
import io.noties.markwon.Markwon
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject
import timber.log.Timber

class ArticleSavedViewerFragment : BaseFragment() {
    companion object {
        private const val KEY_ARTICLE_ID = "KEY_ARTICLE_ID"

        fun newInstance(articleId: String): ArticleSavedViewerFragment {
            return ArticleSavedViewerFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_ARTICLE_ID, articleId)
                }
            }
        }
    }

    private lateinit var binding: FragmentArticleSavedViewerBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ArticleSavedViewerViewModel

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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_saved_viewer, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ArticleSavedViewerViewModel::class.java)
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

    //region menu

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_article_viewer, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.trash -> {
                val article = viewModel.articleValue ?: return true
                showDialogConfirmationOfDeletingArticle(article)
            }
        }
        return false
    }

    //endregion

    private fun bindUI() {
        setHasOptionsMenu(true)
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

        viewModel.eventOfDeletingArticle
            .subscribe { result ->
                Timber.d("eventOfDeletingArticle :$result")
                when (result) {
                    is Result.Success -> {
                        showToast(getString(R.string.fragment_article_remote_viewer_toast_delete_article))
                        navigationController.popFragment()
                    }
                    is Result.Failure -> {
                        handleErrorFailedToDeleteArticle(result.error)
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

    private fun showDialogConfirmationOfDeletingArticle(article: Article.Saved) {
        val title = getString(R.string.fragment_article_saved_viewer_dialog_confirm_deleting_article_title)

        showDialog(
            title,
            article.title,
            doOnClickOk = {
                viewModel.deleteArticle(article)
            }
        )
    }

    //endregion

    //region handle error

    private fun handleErrorFailedToGetArticleId(error: Throwable) {
        Timber.e(error)
        showToast(getString(R.string.fragment_article_saved_viewer_error_get_article_id))
        navigationController.popFragment()
    }

    private fun handleErrorFailedToGetArticle(error: Throwable) {
        Timber.e(error)
        showToast(getString(R.string.fragment_article_saved_viewer_error_get_article))
        navigationController.popFragment()
    }

    private fun handleErrorFailedToDeleteArticle(error: Throwable) {
        Timber.e(error)
        showToast(getString(R.string.fragment_article_saved_viewer_error_delete_article))
    }

    //endregion
}
