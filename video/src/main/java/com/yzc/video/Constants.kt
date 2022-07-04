package com.yzc.video

import com.yzc.base.BiliCore
import java.io.File

class Constants {
    companion object{
        private const val VIDEOS_CACHE = "bili_video/"
        fun videoCachePath(): File{
            return File(BiliCore.App().cacheDir, VIDEOS_CACHE).apply {
                this.mkdir()
            }
        }
    }
}