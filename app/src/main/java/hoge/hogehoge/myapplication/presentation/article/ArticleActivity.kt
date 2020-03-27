package hoge.hogehoge.myapplication.presentation.article

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import hoge.hogehoge.myapplication.R
import hoge.hogehoge.myapplication.databinding.ActivityArticleBinding
import hoge.hogehoge.myapplication.presentation.base.BaseActivity

class ArticleActivity : BaseActivity() {

    private lateinit var binding: ActivityArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_article)

        navigationController.toArticleListFragment()
    }
}
