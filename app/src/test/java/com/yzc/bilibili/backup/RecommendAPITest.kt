package com.yzc.bilibili.backup

import com.yzc.base.network.BiliRetrofit
import com.yzc.bilibili.backup.bean.BiliRecommendResponse
import com.yzc.bilibili.backup.parser.BiliResponseParse
import org.json.JSONObject
import org.junit.Test
import retrofit2.Retrofit

class RecommendAPITest {

    @Test
    fun feed(){
        var net = RecommendNet()
        var biliRecommend = net.getFeed()
        println(" get biliRecommend success !!!")
    }
}