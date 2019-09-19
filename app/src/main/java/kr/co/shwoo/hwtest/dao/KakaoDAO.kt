package kr.co.shwoo.hwtest.dao

import android.util.Log
import kr.co.shwoo.commonlibrary.dao.BaseDAO
import kr.co.shwoo.hwtest.network.IResponse
import kr.co.shwoo.hwtest.network.KakaoReqData
import kr.co.shwoo.hwtest.network.KakaoService
import kr.co.shwoo.hwtest.network.model.ResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class KakaoDAO private constructor () : BaseDAO<ResponseModel>() {

    val service = genRetrofit()?.create(KakaoService::class.java)!!;

    /**
     * [Request] ::  start request Blog list
     *
     * @param reqParams kakaoRequest data instance.
     * @param callback Response callback.
     */
    fun requestBlogList(reqParams: KakaoReqData, callback: IResponse<ResponseModel>) {
        // TODO :: 2019.09.19 shwoo :: check cache logic with current requestParam. ( result data is modified directly on server ?? )
        // :: 2019.09.18 shwoo :: request retrofit.
        val apiCallback = ResponseCallback(callback).apiCallback
        invokeBlogList(reqParams, apiCallback)
    }


    /**
     * [Request] ::  start request Cafe list
     *
     * @param reqParams kakaoRequest data instance.
     * @param callback Response callback.
     */
    fun requestCafeList(reqParams: KakaoReqData, callback: IResponse<ResponseModel>) {
        // TODO :: 2019.09.19 shwoo :: check cache logic with current requestParam. ( result data is modified directly on server ?? )
        // :: 2019.09.18 shwoo :: request retrofit.
        val apiCallback = ResponseCallback(callback).apiCallback
        invokeCafeList(reqParams, apiCallback)
    }


    /**
     * [Request] ::  start request Cafe list
     *
     * @param reqParams kakaoRequest data instance.
     * @param callback Response callback.
     */
    fun requestAllList(reqParams: KakaoReqData, callback: IResponse<ResponseModel>) {
        val apiCallback = ResponseCallback(callback).apiCallback
        invokeBlogList(reqParams, apiCallback)
        invokeCafeList(reqParams, apiCallback)

    }


    private fun invokeBlogList(reqParams: KakaoReqData, apiCallback: Callback<ResponseModel>) {
        service.run {
            this.getSearchBlogList(
                reqParams.sort,
                reqParams.page.toString(),
                reqParams.size.toString(),
                reqParams.searchText
            ).enqueue(apiCallback);

        }
    }

    private fun invokeCafeList(reqParams: KakaoReqData, apiCallback: Callback<ResponseModel>) {
        service.run {
            this.getSearchCafeList(
                reqParams.sort,
                reqParams.page.toString(),
                reqParams.size.toString(),
                reqParams.searchText
            ).enqueue(apiCallback);

        }
    }


    companion object {
        @Volatile private var instance: KakaoDAO? = null

        @JvmStatic fun getInstance(): KakaoDAO =
            instance ?: synchronized(this) {
                instance ?: KakaoDAO().also {
                    instance = it
                }
            }
    }
}


class ResponseCallback(callback : IResponse<ResponseModel>? = null) {
    // generate callback.
    val apiCallback = object : Callback<ResponseModel> {

        override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
            callback?.onFail(t)
        }

        override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
            Log.d("Response :: ", response.toString())
            if(response.isSuccessful) {
                val data : ResponseModel? = response.body()
                callback?.onSuccess(data)

            } else {
                val throwable = Throwable(response.message())
                throwable.printStackTrace()
                callback?.onFail( throwable )

            }
        }
    }
}