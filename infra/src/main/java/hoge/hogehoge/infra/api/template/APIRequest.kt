package hoge.hogehoge.infra.api.template

interface APIRequest {
    var configuration: APIConfiguration

    val path: String
        get() = configuration.path

    val parameters: Map<String, String>
}
