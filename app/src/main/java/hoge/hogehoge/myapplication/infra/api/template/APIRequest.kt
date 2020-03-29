package hoge.hogehoge.myapplication.infra.api.template

interface APIRequest {
    var configuration: APIConfiguration

    val path: String
        get() = configuration.path
}

interface APIRequestGet : APIRequest {

    val queryParameter: Map<String, String>
}

interface APIRequestPost : APIRequest {

    val bodyParameter: Map<String, String>
}
