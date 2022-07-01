package com.yzc.video.bean

class VideoFetch(
    val baseUrl: String,
    val fileSize: Int,
    val fileType: String,
    val fileName: String,// 包括后缀名
    var schedule: Float = 0.05f
) {
    var start: Int = 0
    var end: Int = (fileSize * schedule).toInt()

    fun next(){
        start = end + 1
        schedule += schedule * 2
        if(schedule > 1) schedule = 1f
        end = (fileSize * schedule).toInt()
    }
}