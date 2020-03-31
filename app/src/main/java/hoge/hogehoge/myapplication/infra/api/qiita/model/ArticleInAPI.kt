package hoge.hogehoge.myapplication.infra.api.qiita.model

data class ArticleInAPI(
    val renderedBody: String?,
    val body: String?,
    val commentsCount: Int?,
    val title: String?,
    val createdAt: String?,
    val id: String?,
    val likesCount: Int?,
    val tags: List<TagInAPI>,
    val url: String?,
    val user: UserInAPI?
)
