package hoge.hogehoge.myapplication.presentation

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import hoge.hogehoge.myapplication.R
import hoge.hogehoge.myapplication.presentation.article.article.ArticleFragment
import hoge.hogehoge.myapplication.presentation.article.articlelist.ArticleListFragment
import javax.inject.Inject

class NavigationController @Inject constructor(
    activity: AppCompatActivity
) {
    private val containerId: Int = R.id.container
    private val fragmentManager = activity.supportFragmentManager

    fun toArticleFragment(articleId: String) {
        replaceFragment(ArticleFragment.newInstance(articleId))
    }

    fun toArticleListFragment() {
        replaceFragment(ArticleListFragment.newInstance())
    }

    fun popFragment() {
        fragmentManager.popBackStack()
    }

    private fun addFragment(fragment: Fragment) {
        fragmentManager
            .beginTransaction()
            .add(containerId, fragment, fragment::class.java.simpleName)
            .addToBackStack(fragment::class.java.simpleName)
            .commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        fragmentManager
            .beginTransaction()
            .replace(containerId, fragment, fragment::class.java.simpleName)
            .addToBackStack(fragment::class.java.simpleName)
            .commit()
    }
}
