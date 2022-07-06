package com.yzc.video.bean

class VideoFetch(
    val baseUrl: String,
    val fileSize: Int,
    val fileType: String,
    val fileName: String,// 包括后缀名
    var schedule: Float = 0.05f
) {
    // 断点下载
    var MIN_PLAY_SIZE = 1024 * 1
    var start: Int = 0
    var end: Int = 0

    init{
        var tempEnd = (fileSize * schedule).toInt()
        if(tempEnd < MIN_PLAY_SIZE){
            tempEnd = MIN_PLAY_SIZE
            MIN_PLAY_SIZE *= 2
        }
        end = tempEnd
    }

    fun next(){
        // 解决长视频用百分比
        start = end + 1
        schedule += schedule * 2
        if(schedule > 1) schedule = 1f
        end = (fileSize * schedule).toInt()

        // 解决快速播放
        if(end < MIN_PLAY_SIZE){
            end = MIN_PLAY_SIZE
            MIN_PLAY_SIZE *= 2
        }
    }
}