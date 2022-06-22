package com.yzc.bilibili

import android.app.Application
import android.content.Context
import com.yzc.base.BiliCore
import java.lang.ref.WeakReference

class App: Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        BiliCore.init(this)
    }

}