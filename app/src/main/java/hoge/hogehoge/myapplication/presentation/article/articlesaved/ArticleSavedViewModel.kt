package hoge.hogehoge.myapplication.presentation.article.articlesaved

import androidx.lifecycle.ViewModel
import hoge.hogehoge.myapplication.domain.usecase.QiitaUseCase
import javax.inject.Inject

class ArticleSavedViewModel @Inject constructor(
    private val qiitaUseCase: QiitaUseCase
) : ViewModel()
