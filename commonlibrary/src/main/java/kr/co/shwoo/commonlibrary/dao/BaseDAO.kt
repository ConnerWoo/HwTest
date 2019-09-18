package kr.co.shwoo.commonlibrary.dao

import kr.co.shwoo.commonlibrary.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * <p>[DAO] - Base Class of Data Access Object. </p>
 */
abstract class BaseDAO<T> {

    private val APP_KEY: String = "b7a6ca9739e4dba9fc8fca6e3c0eacb5"

    var retrofit : Retrofit? = null

    fun genRetrofit() : Retrofit? {
        if(retrofit == null) {
            val builder = genHttpBuilder()

            retrofit = Retrofit.Builder().baseUrl("https://dapi.kakao.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder!!.build())
                .build();
        }
        return retrofit
    }


    /**
     * [Builder] http builder. for common header & Logging.
     *
     */
    private fun genHttpBuilder(): OkHttpClient.Builder? {
        val builder = OkHttpClient().newBuilder()
        builder.readTimeout(10, TimeUnit.SECONDS)
        builder.connectTimeout(5, TimeUnit.SECONDS)

        // set log
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
            builder.addInterceptor(interceptor)
        }

        // add interceptor. for common header.
        builder.addInterceptor { chain ->
            val request =
                chain.request().newBuilder().addHeader("Authorization", "KakaoAK ${APP_KEY}")
                    .build()
            chain.proceed(request)
        }
        return builder
    }


    init {
        // TODO :: 2019.09.17 shwoo :: add common initialized for DAO.

    }

}