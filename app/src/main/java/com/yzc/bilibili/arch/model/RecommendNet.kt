package com.yzc.bilibili.arch.model

import com.yzc.base.network.BiliNet
import com.yzc.base.network.BiliRetrofit
import com.yzc.base.util.logd
import com.yzc.bilibili.arch.model.bean.BiliRecommend
import com.yzc.bilibili.arch.model.parser.BiliResponseParse
import org.json.JSONObject


class RecommendNet: BiliNet {

    private val TAG = RecommendNet::class.java.simpleName
    private var isRefresh = false
    val service by lazy { BiliRetrofit.getBiliService(RecommendAPI::class.java) }

    fun getFeed(): MutableList<BiliRecommend> {
        // todo try catch
        var response = if(isRefresh) service.refreshRecommendFeed().execute() else service.recommendFeed().execute()

        isRefresh = true
        val responseJson = JSONObject(response.body()?.string()?: "")
        val resPair = resPair(responseJson)
        logd(TAG, "getFeed (code,message):$resPair")
        val res = BiliResponseParse.toBiliRecommend(responseJson)

        return res
    }

}