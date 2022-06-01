package com.yzc.base.api

open class BiliBaseResponse<T> {
    var code = 0
    var message: String? = null
    var data: T? = null
}
