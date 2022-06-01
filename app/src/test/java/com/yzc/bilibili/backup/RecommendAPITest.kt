package com.yzc.bilibili.backup

import com.yzc.bilibili.backup.bean.BiliRecommendResponse
import com.yzc.bilibili.backup.parser.BiliResponseParse
import org.json.JSONObject
import org.junit.Test
import retrofit2.Retrofit

class RecommendAPITest {

    @Test
    fun feed(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://app.bilibili.com/")
//            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RecommendAPI::class.java)
        var execute = service.feed().execute()
        val responseJson = JSONObject(execute.body()?.string()?: "")
        var biliRecommend = BiliResponseParse.toBiliRecommend(responseJson)
        println("dfsdfad")
    }
}