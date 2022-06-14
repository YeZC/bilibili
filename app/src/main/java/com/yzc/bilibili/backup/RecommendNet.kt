package com.yzc.bilibili.backup

import com.yzc.base.network.BiliRetrofit
import com.yzc.base.util.loge
import com.yzc.bilibili.backup.bean.BiliDetailVideo
import com.yzc.bilibili.backup.bean.BiliRecommend
import com.yzc.bilibili.backup.parser.BiliResponseParse
import org.json.JSONObject


class RecommendNet {

    private var isRefresh = false
    val service = BiliRetrofit.getBiliService(RecommendAPI::class.java)

    fun getFeed(): MutableList<BiliRecommend> {
        var response = if(isRefresh) service.refreshRecommendFeed().execute() else service.recommendFeed().execute()

        isRefresh = true
        val responseJson = JSONObject(response.body()?.string()?: "")
        var resPair = resPair(responseJson)
        val res = BiliResponseParse.toBiliRecommend(responseJson)

        return res
    }

    fun detailVideo(aid: String): MutableList<BiliDetailVideo> {
        var response = service.detailVideo(aid).execute()
        val responseJson = JSONObject(response.body()?.string()?: "")
        val biliRecommend = BiliResponseParse.toBiliDetailVideo(responseJson)

//        val res = biliRecommend.items ?: mutableListOf()

        return mutableListOf()
    }

    fun resPair(json: JSONObject): Pair<Int, String> =
        Pair(json.optInt("code"), json.optString("message"))

}