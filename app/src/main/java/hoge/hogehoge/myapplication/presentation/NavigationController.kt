package hoge.hogehoge.myapplication.presentation

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import hoge.hogehoge.myapplication.R
import hoge.hogehoge.myapplication.presentation.article.articlelist.trend.ArticleTrendFragment
import hoge.hogehoge.myapplication.presentation.article.articlepager.ArticlePagerFragment
import hoge.hogehoge.myapplication.presentation.article.articleviewer.ArticleViewerFragment
import javax.inject.Inject

class NavigationController @Inject constructor(
    activity: AppCompatActivity
) {
    private val containerId: Int = R.id.container
    private val fragmentManager = activity.supportFragmentManager

    fun toArticleViewerFragment(articleId: String) {
        replaceFragment(ArticleViewerFragment.newInstance(articleId))
    }

    fun toArticlePagerFragment() {
        replaceFragment(ArticlePagerFragment.newInstance())
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
