package com.yzc.video.arch.model.backup.bean

import com.yzc.base.api.BiliBaseResponse
import com.yzc.video.bean.BiliVideoEntry
import com.yzc.video.bean.VideoFetch

class BiliVideoFlowResponse(
    val card_goto: String? = null,
    val goto: String? = null,
    val param: String? = null,
    val cover: String? = null,
    val ff_cover: String? = null,
    val title: String? = null,
    val uri: String? = null
): BiliBaseResponse() {
    // custom data
    var videos: List<BiliVideoDetailResponse>? = null
    var audios: List<BiliVideoDetailResponse>? = null

    fun toBiliVideoEntry(): BiliVideoEntry {
        return BiliVideoEntry(param, videos, audios)
    }
}