package kr.co.shwoo.hwtest.network

import kr.co.shwoo.hwtest.network.model.ResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


data class KakaoReqData(val sort: String
                        , val page: Long
                        , val size: Long
                        , val searchText: String?)


interface KakaoService {

    @GET("v2/search/blog?")
    fun getSearchBlogList(@Query("sort") sort: String
                          , @Query("page") page: String
                          , @Query("size") size: String
                          , @Query("query") searchText: String?): Call<ResponseModel>


    //@Headers("Authorization: KakaoAK b7a6ca9739e4dba9fc8fca6e3c0eacb5")
    @GET("/v2/search/cafe?")
    fun getSearchCafeList(@Query("sort") sort: String
                          , @Query("page") page: String
                          , @Query("size") size: String
                          , @Query("query") searchText: String?): Call<ResponseModel>
}