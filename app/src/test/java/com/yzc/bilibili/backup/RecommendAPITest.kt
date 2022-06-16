package com.yzc.bilibili.backup

import com.yzc.bilibili.arch.model.RecommendNet
import com.yzc.bilibili.arch.model.bean.BiliRecommend
import org.junit.Test


class RecommendAPITest {

    @Test
    fun feed(): MutableList<BiliRecommend>{
        var net = RecommendNet()
        var biliRecommend = net.getFeed()
        println(" get biliRecommend ${biliRecommend.size} success !!!")
        return biliRecommend
    }

    @Test
    fun detailVideo() {
        var net = RecommendNet()
        var aid = net.getFeed()[1].param// 第一个banner没有
        assert(aid.isNotEmpty())

        var detailVideo = net.detailVideo(aid)
        println(" get detailVideo ${detailVideo.size} success !!!")
    }
}