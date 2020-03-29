package hoge.hogehoge.myapplication.infra.api.qiita

import hoge.hogehoge.myapplication.infra.api.template.APIConfiguration

enum class QiitaAPIConfiguration : APIConfiguration {
    GET_ARTICLES;

    override val path: String
        get() = "items"
}
