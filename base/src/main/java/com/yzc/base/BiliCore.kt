package com.yzc.base

import android.content.Context
import java.lang.ref.WeakReference

object BiliCore {
    private lateinit var app: WeakReference<Context>

    fun init(context: Context){
        app = WeakReference(context)
    }

    fun App(): Context {
        return app.get()!!
    }

}