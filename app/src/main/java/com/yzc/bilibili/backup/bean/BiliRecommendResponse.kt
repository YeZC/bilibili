package com.yzc.bilibili.backup.bean

import com.yzc.base.api.BiliBaseResponse

class BiliRecommendResponse : BiliBaseResponse<BiliRecommend?>() {
    var items: MutableList<BiliRecommend>? = null
    var config: String? = null
}

