package hoge.hogehoge.myapplication.infra.api.qiita.api

import hoge.hogehoge.myapplication.common.extension.format
import hoge.hogehoge.myapplication.infra.api.qiita.QiitaAPIConfiguration
import hoge.hogehoge.myapplication.infra.api.template.APIConfiguration
import hoge.hogehoge.myapplication.infra.api.template.APIRequest
import java.util.Date

object GetArticlesAPI {
    const val KEY_TITLE = "title"
    const val KEY_BODY = "body"
    const val KEY_CODE = "code"
    const val KEY_TAG = "tag"
    const val KEY_USER = "user"
    const val KEY_STOCKS = "stocks"
    const val KEY_CREATED = "created"

    data class Request(
        val page: Int,
        val perPage: Int,
        val query: String? = null
    ) : APIRequest {
        enum class QueryName(val queryName: String) {
            PAGE("page"),
            PER_PAGE("per_page"),
            QUERY("query");
        }

        override var configuration: APIConfiguration = QiitaAPIConfiguration.GET_ARTICLES

        override val parameters: Map<String, String>
            get() = mutableMapOf(
                QueryName.PAGE.queryName to page.toString(),
                QueryName.PER_PAGE.queryName to perPage.toString()
            ).apply {
                query?.let { this[QueryName.QUERY.queryName] = it }
            }
    }

    fun createQuery(
        title: String? = null,
        body: String? = null,
        code: String? = null,
        tag: String? = null,
        user: String? = null,
        stocks: Int? = null,
        createdFrom: Date? = null,
        createdTo: Date? = null
    ): String {
        val queryBuilder = StringBuilder()

        title?.let {
            queryBuilder.append("$KEY_TITLE:$it+")
        }

        body?.let {
            queryBuilder.append("$KEY_BODY:$body+")
        }

        code?.let {
            queryBuilder.append("$KEY_CODE:$code+")
        }

        tag?.let {
            queryBuilder.append("$KEY_TAG:$tag+")
        }

        user?.let {
            queryBuilder.append("$KEY_USER:$user+")
        }

        stocks?.let {
            queryBuilder.append("$KEY_STOCKS:>$stocks+")
        }

        createdFrom?.let {
            queryBuilder.append("$KEY_CREATED:>${convertDateForQuery(createdFrom)}+")
        }

        createdTo?.let {
            queryBuilder.append("$KEY_CREATED:<${convertDateForQuery(createdTo)}+")
        }

        return queryBuilder.removeSuffix("+").toString()
    }

    private fun convertDateForQuery(date: Date): String {
        val format = "yyyy-MM-dd"
        return date.format(format)
    }
}
