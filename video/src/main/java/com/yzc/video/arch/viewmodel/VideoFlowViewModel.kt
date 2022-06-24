package com.yzc.video.arch.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yzc.base.BiliCore
import com.yzc.base.util.logd
import com.yzc.base.util.loge
import com.yzc.video.BiliFFmpeg
import com.yzc.video.backup.VideoFlowNet
import com.yzc.video.backup.bean.BiliVideoFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class VideoFlowViewModel : ViewModel() {

    val biliVideos: LiveData<String>
        get() = mCurrent

    private val TAG = VideoFlowViewModel::class.java.simpleName
    private var mBiliVideos: MutableList<BiliVideoFlow>? = null
    private var mCurrent = MutableLiveData<String>()
    private val net by lazy { VideoFlowNet() }
    private val mPlayIndex = 0

    fun loadData(aid: String){
        GlobalScope.launch(Dispatchers.Default) {
            mBiliVideos = net.detailVideo(aid)
            mBiliVideos?.apply {
                var videoDetail = this[mPlayIndex]
                val videoUrl = videoDetail.videos?.get(0)?.base_url!!
                val audioUrl = videoDetail.audios?.get(0)?.base_url!!
                fetchMp4(aid, videoUrl, audioUrl)
            }?: { loge(TAG, "fetch video flow is null")}
        }
    }

    private fun fetchMp4(aid: String, videoUrl: String, audioUrl: String) {
        if(videoUrl.isEmpty() || audioUrl.isEmpty()) return
        val aidFile = File(BiliCore.App().cacheDir, "${aid}.mp4").path
        logd(TAG, "aidFile $aidFile")
        val path1 = net.download("video", videoUrl)
        val path2 = net.download("audio", audioUrl)
        if(path1.isNotEmpty() and path2!!.isNotEmpty()){
            BiliFFmpeg.audioAndVideoSynthesis(path1, path2, aidFile)
            logd(TAG, "audio and video synthesis success!")
            mCurrent.postValue(aidFile)
        }
    }

}