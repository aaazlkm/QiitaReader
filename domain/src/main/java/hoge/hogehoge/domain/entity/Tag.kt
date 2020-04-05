package hoge.hogehoge.domain.entity

/**
 * 現段階でローカルにタグ情報を保存するつもりはないが、今後の変更のためとArticleの実装と同じにするために下記のようにしている
 */
sealed class Tag {
    data class Remote(
        val name: String?,
        val versions: List<String>?
    )
}
