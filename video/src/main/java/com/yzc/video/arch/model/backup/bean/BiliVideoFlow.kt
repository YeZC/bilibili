package com.yzc.video.arch.model.backup.bean

import com.yzc.base.api.BiliBaseResponse

class BiliVideoFlow(
    val card_goto: String? = null,
    val goto: String? = null,
    val param: String? = null,
    val cover: String? = null,
    val ff_cover: String? = null,
    val title: String? = null,
    val uri: String? = null
): BiliBaseResponse() {
    // custom data
    var videos: List<BiliVideoDetail>? = null
    var audios: List<BiliVideoDetail>? = null
}