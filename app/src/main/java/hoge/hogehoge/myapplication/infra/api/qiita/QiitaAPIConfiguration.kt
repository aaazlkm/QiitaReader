package hoge.hogehoge.myapplication.infra.api.qiita

import hoge.hogehoge.myapplication.infra.api.template.APIConfiguration

enum class QiitaAPIConfiguration : APIConfiguration {
    GET_ARTICLE,
    GET_ARTICLES;

    override val path: String
        get() = when (this) {
            GET_ARTICLE -> "items"
            GET_ARTICLES -> "items"
        }
}
