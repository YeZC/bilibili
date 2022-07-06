package com.yzc.video.arch.viewmodel.bean

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class VideoPath(
    val aid: String,
    var path: String
){
    override fun toString(): String {
        return "VideoPath(aid='$aid', path='$path')"
    }

}