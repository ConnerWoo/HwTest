package kr.co.shwoo.hwtest.activity

import android.os.Bundle
import kr.co.shwoo.commonlibrary.activity.BaseActivity
import kr.co.shwoo.hwtest.R

/**
 * <p>[BaseActivity] - Main of Activity </p>
 *
 * @author shwoo
 * @history 2019.09.17 | shwoo | create.
 */
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_main)

        //Volley.newRequestQueue(applicationContext)

    }
}
