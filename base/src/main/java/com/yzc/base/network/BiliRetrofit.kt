package com.yzc.base.network

import com.yzc.base.ktexpand.getBaseUrl
import retrofit2.Retrofit
import retrofit2.create

object BiliRetrofit {

    private val biliRetrofit by lazy<Retrofit>() {
        Retrofit.Builder()
            .baseUrl(Host.BILIBILI.host)
//            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

//    private val biliVideoRetrofit by lazy<Retrofit>() {
//        Retrofit.Builder()
//            .baseUrl(Host.VIDEO.host)
//            .build()
//    }

    fun <T> getBiliService(clazz: Class<T>): T {
        return biliRetrofit.create(clazz)
    }

    fun <T> getDynamicService(domain: String, clazz: Class<T>): T = Retrofit.Builder()
        .baseUrl(domain).build().let {
            it.create(clazz)
        }

//    fun <T> getVideoService(clazz: Class<T>): T {
//        return biliVideoRetrofit.create(clazz)
//    }
}

enum class Host(val host: String) {
    BILIBILI("https://app.bilibili.com/"),
    OTHER("other")
}