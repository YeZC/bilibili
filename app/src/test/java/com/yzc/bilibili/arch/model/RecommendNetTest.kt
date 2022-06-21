package com.yzc.bilibili.arch.model

import android.net.Uri
import com.yzc.base.network.BiliRetrofit
import com.yzc.base.network.Host
import com.yzc.bilibili.arch.model.bean.BiliRecommend
import okio.Buffer
import okio.Okio
import org.junit.Test
import retrofit2.Retrofit
import java.io.File
import java.net.URLDecoder


internal class RecommendNetTest {

    @Test
    fun feed() {
        var biliRecommend = getFeed()
        println(" get biliRecommend ${biliRecommend.size} success !!!")
    }

    private inline fun getFeed(): MutableList<BiliRecommend> = RecommendNet().getFeed()

    @Test
    fun detailVideo() {
        var net = RecommendNet()
        var feed = getFeed()
        var aid = feed[1].param// 第一个banner没有
        assert(aid.isNotEmpty())

        var detailVideo = net.detailVideo(aid)
        println(" get detailVideo ${detailVideo.size} success !!!")
    }

}