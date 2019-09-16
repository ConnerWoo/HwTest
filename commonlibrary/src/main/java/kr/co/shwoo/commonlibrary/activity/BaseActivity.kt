package kr.co.shwoo.commonlibrary.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * <p>[AppCompatActivity] - Base Activity For Common </p>
 *
 * @author shwoo
 * @history 2019.09.17 | shwoo | create.
 */
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO :: 2019.09.17 shwoo :: add common init logic.

    }


    override fun onResume() {
        super.onResume()
        // TODO :: 2019.09.17 shwoo :: add common logic :: onResume().

    }


    override fun onPause() {
        super.onPause()
        if(isFinishing) {
            // TODO :: 2019.09.17 shwoo :: add common logic :: onPause - finishing.

        }
    }



}