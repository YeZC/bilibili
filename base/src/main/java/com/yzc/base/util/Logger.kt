package com.yzc.base.util

import android.util.Log

class Logger {
}
val TAG = "yzc-Bili-"
fun logd(tag: String, msg: String){
    Log.d(TAG + tag, msg)
}
fun logw(tag: String, msg: String){
    Log.w(TAG + tag, msg)
}
fun logi(tag: String, msg: String){
    Log.i(TAG + tag, msg)
}
fun loge(tag: String, msg: String){
    Log.e(TAG + tag, msg)
}