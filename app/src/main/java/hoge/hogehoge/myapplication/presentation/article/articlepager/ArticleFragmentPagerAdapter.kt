package hoge.hogehoge.myapplication.presentation.article.articlepager

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ArticleFragmentPagerAdapter(
    private val context: Context,
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val tabItems = TabItem.values()

    override fun getPageTitle(position: Int): CharSequence? {
        return tabItems[position].getTitle(context)
    }

    override fun getItem(position: Int): Fragment {
        return tabItems[position].createFragment()
    }

    override fun getCount(): Int {
        return tabItems.size
    }
}
