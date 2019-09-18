package kr.co.shwoo.hwtest.network

import kr.co.shwoo.hwtest.network.model.ResponseModel

interface IResponse<T> {

    fun onSuccess(response: ResponseModel?)

    fun onFail(error: Throwable?)

}