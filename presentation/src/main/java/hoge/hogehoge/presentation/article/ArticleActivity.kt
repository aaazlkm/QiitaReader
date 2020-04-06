package hoge.hogehoge.presentation.article

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import hoge.hogehoge.presentation.R
import hoge.hogehoge.presentation.base.BaseActivity
import hoge.hogehoge.presentation.databinding.ActivityArticleBinding

class ArticleActivity : BaseActivity() {

    private lateinit var binding: ActivityArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_article)

        setSupportActionBar(binding.toolbar)
        navigationController.toArticlePagerFragment()
    }
}
