package com.yzc.base.network

import org.json.JSONObject

interface BiliNet {

    val SUCCESS_CODE: Int get() = 200

    fun resPair(json: JSONObject): Pair<Int, String> =
        Pair(json.optInt("code"), json.optString("message"))

}