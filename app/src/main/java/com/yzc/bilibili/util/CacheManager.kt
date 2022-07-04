package com.yzc.bilibili.util

import android.content.Context
import com.yzc.base.util.FileUtils
import com.yzc.video.Constants.Companion.videoCachePath
import java.io.File
import java.io.IOException

class CacheManager {
    companion object{
        fun clearVideo(){
            FileUtils.deleteDirectory(videoCachePath())
        }
    }

}