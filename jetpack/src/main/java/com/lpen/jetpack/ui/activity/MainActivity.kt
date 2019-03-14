package com.lpen.jetpack.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.lpen.jetpack.R
import com.lpen.jetpack.databinding.MainActivityBinding
import com.lpen.jetpack.ui.fragment.MainFragment
import com.lpen.jetpack.utils.FastClickUtil
import com.lpen.jetpack.utils.TipsUtil

/**
 * @author LPen
 * @Date 2019/03/09
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun onBackPressed() {
        if (FastClickUtil.isFastClick(1500)) {
            finish()
            System.gc()
            System.exit(0)
        } else {
            TipsUtil.toastShortMsg(this, "再按一次退出程序")
        }
    }

}
