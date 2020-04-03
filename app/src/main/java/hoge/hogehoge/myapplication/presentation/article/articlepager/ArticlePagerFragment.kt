package hoge.hogehoge.myapplication.presentation.article.articlepager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import hoge.hogehoge.myapplication.R
import hoge.hogehoge.myapplication.databinding.FragmentArticlePagerBinding
import hoge.hogehoge.myapplication.presentation.base.BaseFragment

class ArticlePagerFragment : BaseFragment() {
    companion object {
        fun newInstance(): ArticlePagerFragment {
            return ArticlePagerFragment()
        }
    }

    private lateinit var binding: FragmentArticlePagerBinding

    //region lifecycle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_pager, container, false)

        bindUI()

        return binding.root
    }

    //endregion

    private fun bindUI() {
        with(binding.viewPager) {
            adapter = ArticleFragmentPagerAdapter(context, childFragmentManager)
            offscreenPageLimit = 2
        }

        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }
}
