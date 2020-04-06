package hoge.hogehoge.presentation.article.articlepager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import hoge.hogehoge.core.di.viewmodel.ViewModelFactory
import hoge.hogehoge.domain.usecase.QiitaUseCase
import hoge.hogehoge.presentation.R
import hoge.hogehoge.presentation.base.BaseFragment
import hoge.hogehoge.presentation.databinding.FragmentArticlePagerBinding
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class ArticlePagerFragment : BaseFragment() {
    companion object {
        fun newInstance(): ArticlePagerFragment {
            return ArticlePagerFragment()
        }
    }

    @Inject
    lateinit var qiitaUseCase: QiitaUseCase

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ArticlePagerViewModel

    private lateinit var binding: FragmentArticlePagerBinding

    //region lifecycle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_pager, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ArticlePagerViewModel::class.java)

        bindUI()
        bindViewModelValue()
        fetchData()

        return binding.root
    }

    //endregion

    //region override BaseFragment methods

    override fun setupActionBar(title: String) {
        super.setupActionBar(getString(R.string.fragment_article_pager_title))
    }

    //endregion

    private fun bindUI() {
        with(binding.viewPager) {
            adapter = ArticleFragmentPagerAdapter(context, childFragmentManager)
            offscreenPageLimit = TabItem.values().size
        }

        with(binding.tabLayout) {
            setupWithViewPager(binding.viewPager)
            val savedTabIndex = TabItem.values().indexOf(TabItem.SAVED)
            getTabAt(savedTabIndex)?.orCreateBadge?.run {
                context?.getColor(R.color.color_accent)?.let { backgroundColor = it }
                isVisible = false
            }
        }
    }

    private fun bindViewModelValue() {
        viewModel.numberOfUnreadArticle
            .subscribe { numberOfUnreadArticle ->
                val savedTabIndex = TabItem.values().indexOf(TabItem.SAVED)
                binding.tabLayout.getTabAt(savedTabIndex)?.orCreateBadge?.run {
                    if (numberOfUnreadArticle == 0) {
                        isVisible = false
                    } else {
                        isVisible = true
                        number = numberOfUnreadArticle
                    }
                }
            }
            .addTo(compositeDisposable)
    }

    private fun fetchData() {
        viewModel.fetchNumberOfUnreadArticle()
    }
}
