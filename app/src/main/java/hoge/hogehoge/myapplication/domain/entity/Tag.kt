package hoge.hogehoge.myapplication.domain.entity

/**
 * 現段階でローカルにタグ情報を保存するつもりはないが、今後の変更のためとArticleの実装と同じにするために下記のようにしている
 */
sealed class Tag {
    data class Remote(
        val id: String,
        val followersCount: Int,
        val iconUrl: String,
        val itemsCount: Int
    )
}
