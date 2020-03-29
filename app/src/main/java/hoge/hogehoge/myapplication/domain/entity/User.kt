package hoge.hogehoge.myapplication.domain.entity

/**
 * 現段階でローカルにユーザー情報を保存するつもりはないが、今後の変更のためとArticleの実装と同じにするために下記のようにしている
 */
sealed class User {
    data class Remote(
        val id: String,
        val name: String,
        val description: String,
        val profileImageUrl: String
    )
}
