package hoge.hogehoge.myapplication.infra.api.qiita.api

import hoge.hogehoge.myapplication.infra.api.qiita.QiitaAPIConfiguration
import hoge.hogehoge.myapplication.infra.api.template.APIConfiguration
import hoge.hogehoge.myapplication.infra.api.template.APIRequest

object GetArticlesAPI {
    data class Request(
        val page: Int,
        val perPage: Int
    ) : APIRequest {
        enum class QueryName(val queryName: String) {
            PAGE("page"),
            PER_OAGE("per_page");
        }

        override var configuration: APIConfiguration = QiitaAPIConfiguration.GET_ARTICLES

        override val parameters: Map<String, String>
            get() = mapOf(
                QueryName.PAGE.queryName to page.toString(),
                QueryName.PER_OAGE.queryName to perPage.toString()
            )
    }
}
