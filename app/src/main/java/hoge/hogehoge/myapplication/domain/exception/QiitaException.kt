package hoge.hogehoge.myapplication.domain.exception

import java.lang.Exception

sealed class QiitaException : Exception() {

    data class FaildToConvertArticleResponse(val articleId: String) : Exception("faild to convert article response: $articleId")

    object FaildToDeleteArticle : Exception("faild to delete article. only can delete already saved article")
}
