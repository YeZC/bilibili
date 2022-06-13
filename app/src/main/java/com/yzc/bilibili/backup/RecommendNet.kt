package com.yzc.bilibili.backup

import com.yzc.base.network.BiliRetrofit
import com.yzc.bilibili.backup.bean.BiliRecommend
import com.yzc.bilibili.backup.parser.BiliResponseParse
import org.json.JSONObject


class RecommendNet {

    private var isRefresh = false
    fun getFeed(): MutableList<BiliRecommend> {
        var service = BiliRetrofit.getBiliService(RecommendAPI::class.java)
        var response = if(isRefresh) service.refreshRecommendFeed().execute() else service.recommendFeed().execute()

        isRefresh = true
        val responseJson = JSONObject(response.body()?.string()?: "")
        val biliRecommend = BiliResponseParse.toBiliRecommend(responseJson)

        val res = biliRecommend.items ?: mutableListOf()

        return res
    }
}