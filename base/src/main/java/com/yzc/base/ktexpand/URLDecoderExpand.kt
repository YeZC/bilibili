package com.yzc.base.ktexpand

import java.net.URL

fun String.getUriValue(key: String): String{
    if(this.isEmpty()) return ""
    var head = this.indexOf("?")
    if(head == -1) head = 0
//    val host = this.substring(0, head)
    var start = this.indexOf("$key=")
    if(start == -1) return ""


    while (start < length - 1 && this[start] != '=') start++
    start++
    var end = start
    while (end < length - 1 && this[end] != '&') end++
    return this.substring(start, end)
}

fun String.getQueryMap(): Map<String, String>{
    val res = mutableMapOf<String, String>()
    val split = this.split('&')
    split.forEach {
        val splitPos = it.indexOf('=')
        res[it.substring(0, splitPos)] = it.substring(splitPos + 1, it.length)
    }
    return res
}

fun URL.getBaseUrl(): String{
    val port = if(port != -1) ":$port" else ""
    return "${protocol}://${host}${port}"
}