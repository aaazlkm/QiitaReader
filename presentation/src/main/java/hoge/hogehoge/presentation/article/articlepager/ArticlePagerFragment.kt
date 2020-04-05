package hoge.hogehoge.presentation.article.articlepager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import hoge.hogehoge.presentation.R
import hoge.hogehoge.presentation.base.BaseFragment
import hoge.hogehoge.presentation.databinding.FragmentArticlePagerBinding

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

        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }
}
