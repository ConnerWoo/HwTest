package kr.co.shwoo.hwtest.network.model


data class Document(var blogname: String
                    , var contents: String
                    , var datetime: String
                    , var thumbnail: String
                    , var title: String
                    , var url: String
)

data class Meta(var is_end: Boolean
                    , var pageable_count: Int
                    , var total_count: Int
)

class ResponseModel {

    var documents: List<Document>? = null
    var meta: Meta? = null


    override fun toString(): String {
        var log = documents?.joinToString("\n","\n\n","\n",-1,"")
        return "ResponseModel(meta=$meta, \n====[documents](${documents?.size})====$log)"
    }


}
