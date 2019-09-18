package kr.co.shwoo.hwtest.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.shwoo.commonlibrary.activity.BaseActivity
import kr.co.shwoo.hwtest.R
import kr.co.shwoo.hwtest.dao.KakaoDAO
import kr.co.shwoo.hwtest.network.model.ResponseModel
import kr.co.shwoo.hwtest.network.IResponse
import kr.co.shwoo.hwtest.network.KakaoReqData


/**
 * <p>[BaseActivity] - Main of Activity </p>
 *
 * @author shwoo
 * @history 2019.09.17 | shwoo | create.
 *
case 0. 검색어 정상 유무 여부. ( validation :: not null, not empty ).
case 0-0. 검색어가 정상이다.
:: [case 1]
case 0-1. 검색어가 비정상이다.
:: [default]

[case 1]. api 조회. ( 키워드 > category > 검색 우선순위 )
case 1-1. 조회 성공 ( 상태코드 : 200 ~ 300 )
:: [case 2]
case 1-2. 조회 실패 ( 기타 상태코드 )
:: [default]

[case 2]. 조회된 데이터 캐싱 여부 판별.( object compareTo 를 DAO 내부에 구현 ) :: %캐싱 갯수는 100 ( 4 =pageCount * 25 =pageSize )개로 주작함.
case 2-0: 조회된 데이터가 0개 일때 ( 마지막 페이지 )
:: 기존 리스트 유지 - 갱신 없음.
case 2-1. 캐싱 x.
:: 리스트에 add 이후에 제목( colName :: title )으로 sorting 해서 25 ( pageSize:MAX = 25 ) 개 노출.
case 2-2. 캐싱 o.
:: 리스트에 add 이후에 제목( colName :: title )으로 sorting 해서 25 ( pageSize:MAX = 25 ) 개 노출.

[case 3]. 리스트 표기. ( Grid / List )
:: 해당 데이터로 리스트 갱신.


[default] ( 추가 필요 :: 재시도 가능한 UI 제공. )
::  empty 형태로 리스트 표기.


### 추가 기능 사항
- 재시도 가능한 UI 제공.
:: 수동 재시도, list empty 일때만 가능. 안내메세지("재시도 하시겠습니까?")와 "재시도" button.
- 검색 history 목록 표기.
:: api request 직전에 stack처럼 수행 ( 저장 위치 여부 ( memory / Pref / DB ) 는 기획서 참고 )
- 검색시 필터조건 적용 ( accuracy (정확도순) or recency (최신순) )
::	api param에 있는지 조사.
- Grid & List 형태 전환.
::	View하나에 LayoutManager 두개.
- memory Cache 대신 DB로 전환.  ( 동기화가 정상적으로 이루어 지는지 :: 수정 / 삭제 / 탈퇴 등,  앱 용량이 증가함 )
::	이후 step에 대한 추가시나리오 검토 후 적용 여부 결정.
- memory Cache 대신 Pref로 전환. ( 동기화가 정상적으로 이루어 지는지 :: 수정 / 삭제 / 탈퇴 등, 앱 용량이 증가함 )
::	이후 step에 대한 추가시나리오 검토 후 적용 여부 결정.


### OpenAPI 의 고려사항
- failOver ( 해당 트래픽 max 일때의 응답 및 서버 응답 없을 때 )
:: 상태코드 200 일 때만 처리.
-

 */
class MainActivity : AppCompatActivity() {

    private val TAG = this.javaClass.simpleName


    /** search input Text */
    var searchText: String? = ""
    /** sort type index */
    var mISortType:Int = SORT_TYPE.ACCURACY.index

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_main)

        // EditText extension. for clear button.
        et_activity_main_input.setupClearButtonWithAction()

        // EditText soft keyboard 'Done' button event.
        et_activity_main_input.setOnEditorActionListener() { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkSearchText()

            }
            false
        }
    }


    /**
     * [List] update view adapter.
     */
    private fun updateListAdapter() {

    }


    /**
     * [List] :: clear previous list.
     */
    private fun clearPreviousList() {

    }


    /**
     * [Result] :: result empty.
     */
    private fun setEmptyResult() {
        Log.d(TAG, "setEmptyResult()::")
        // TODO :: set empty result.
        // 1. clear list.
        clearPreviousList()
        // 2. update adapter.
        updateListAdapter()
    }


    /**
     * [Request] start Search Request.
     */
    private fun startRequestWithSearchText() {
        Log.d(TAG, "startRequestWithSearchText()::[" + searchText + "]")
        // TODO :: start request with search text. ( do not trim!! )

        val sort = getSortType()
        val params = KakaoReqData(sort.value, "1", "25", searchText)
        KakaoDAO.getInstance().requestBlogList(params, object : IResponse<ResponseModel> {

            /* case 1-1. 조회 성공 ( 상태코드 : 200 ~ 300 ) */
            override fun onSuccess(response: ResponseModel?) {
                /* case 2-1. 캐싱 x.:: 리스트에 add 이후에 제목( colName :: title )으로 sorting 해서 25 ( pageSize:MAX = 25 ) 개 노출. */
                Log.d(TAG, "ResponseModel = ${response.toString()}")
            }


            /* case 1-2. 조회 실패 ( 기타 상태코드 ) */
            override fun onFail(error: Throwable?) {
                /* default ( 추가 필요 :: 재시도 가능한 UI 제공. ) ::  empty 형태로 리스트 표기. */
                error?.printStackTrace()

            }

        })
    }


    private fun getSortType(): SORT_TYPE {
        if(mISortType == SORT_TYPE.RECENCY.index)
            return SORT_TYPE.RECENCY
        // default
        return SORT_TYPE.ACCURACY
    }


    /**
     * <pre>
     *     [OnClick] main on Click event listener.
     * </pre>
     *
     */
    fun onClickMainActivity(view: View) {
        when (view.id) {
            // search button.
            R.id.btn_activity_main_input_search -> {
                checkSearchText()

            }
        }
    }


    private fun checkSearchText() {
        // case 0. 검색어 정상 유무 여부. ( validation :: not null, not empty ).
        searchText = getInputText()
        if (searchText.isNullOrEmpty()) {
            // :: set empty list.
            setEmptyResult()
            return

        } else {
            /* case 0-0. 검색어가 정상이다. -> :: [case 1] */
            searchText.let {
                it.also {
                    hideKeyboard()
                    // :: start request.
                    startRequestWithSearchText()
                }
            }
        }
    }


    /**
     * [Keyboard] hide softKeyboard.
     */
    private fun hideKeyboard() {
        val inputManager =
            baseContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.toggleSoftInput(0, 0)
    }


    /**
     * <UI> get input Text.
     */
    private fun getInputText(): String? {
        return et_activity_main_input.text.toString()
    }


    //=========================================================================================//
    ///////////// Extension :: EditText Clear Button ////////////////
    fun EditText.setupClearButtonWithAction() {

        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                val clearIcon = if (editable?.isNotEmpty() == true) R.mipmap.ic_clear else 0
                setCompoundDrawablesWithIntrinsicBounds(0, 0, clearIcon, 0)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })

        setOnTouchListener(View.OnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (this.right - this.compoundPaddingRight)) {
                    this.setText("")
                    return@OnTouchListener true
                }
            }
            return@OnTouchListener false
        })

    }
}


enum class SORT_TYPE(val value: String, val index : Int) {
    ACCURACY("accuracy", 0)        //  정확도순 ( Default )
    , RECENCY("recency", 1)        //  최신순
}