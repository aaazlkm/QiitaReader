package hoge.hogehoge.myapplication.presentation.article.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import hoge.hogehoge.myapplication.R
import hoge.hogehoge.myapplication.databinding.FragmentArticleBinding
import hoge.hogehoge.myapplication.di.viewmodel.ViewModelFactory
import hoge.hogehoge.myapplication.presentation.base.BaseFragment
import javax.inject.Inject

class ArticleFragment : BaseFragment() {
    companion object {
        fun newInstance(): ArticleFragment {
            return ArticleFragment()
        }
    }

    private lateinit var binding: FragmentArticleBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: ArticleViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ArticleViewModel::class.java)

        return binding.root
    }
}