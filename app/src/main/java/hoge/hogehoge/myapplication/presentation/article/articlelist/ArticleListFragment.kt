package hoge.hogehoge.myapplication.presentation.article.articlelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import hoge.hogehoge.myapplication.R
import hoge.hogehoge.myapplication.databinding.FragmentArticleListBinding
import hoge.hogehoge.myapplication.di.viewmodel.ViewModelFactory
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_list, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ArticleListViewModel::class.java)

        bindViewModelValue()
        fetchData()

        return binding.root
    }

    private fun bindViewModelValue() {
        viewModel.articles
            .subscribe { result ->
                Timber.d("result $result")
            }
            .addTo(compositeDisposable)
    }

    private fun fetchData() {
        viewModel.fetchArticles()
    }
}
