package hoge.hogehoge.myapplication.presentation.article.articlesaved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import hoge.hogehoge.myapplication.R
import hoge.hogehoge.myapplication.databinding.FragmentArticleSavedBinding
import hoge.hogehoge.myapplication.di.viewmodel.ViewModelFactory
import hoge.hogehoge.myapplication.presentation.base.BaseFragment
import javax.inject.Inject

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

        return binding.root
    }
}
