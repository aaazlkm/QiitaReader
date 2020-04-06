package hoge.hogehoge.presentation.article.articlepager

import android.content.Context
import androidx.fragment.app.Fragment
import hoge.hogehoge.presentation.R
import hoge.hogehoge.presentation.article.articleremote.popular.ArticlePopularFragment
import hoge.hogehoge.presentation.article.articleremote.timeline.ArticleTimelineFragment
import hoge.hogehoge.presentation.article.articleremote.trend.ArticleTrendFragment
import hoge.hogehoge.presentation.article.articlesaved.ArticleSavedFragment

enum class TabItem {
    TREND,
    POPULAR,
    TIMELINE,
    SAVED;

    fun getTitle(context: Context): String {
        return when (this) {
            TREND -> context.getString(R.string.fragment_article_pager_tab_trend)
            POPULAR -> context.getString(R.string.fragment_article_pager_tab_popular)
            TIMELINE -> context.getString(R.string.fragment_article_pager_tab_timeline)
            SAVED -> context.getString(R.string.fragment_article_pager_tab_saved)
        }
    }

    fun createFragment(): Fragment {
        return when (this) {
            TREND -> ArticleTrendFragment.newInstance()
            POPULAR -> ArticlePopularFragment.newInstance()
            TIMELINE -> ArticleTimelineFragment.newInstance()
            SAVED -> ArticleSavedFragment.newInstance()
        }
    }
}
