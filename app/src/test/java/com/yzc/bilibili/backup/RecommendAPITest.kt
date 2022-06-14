package com.yzc.bilibili.backup

import com.yzc.bilibili.BaseTest
import org.junit.Test

class RecommendAPITest {

    @Test
    fun feed(){
        var net = RecommendNet()
        var biliRecommend = net.getFeed()
        println(" get biliRecommend success !!!")
    }
}