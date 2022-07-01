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
    private val cachePaths = mutableSetOf<String>()

    fun loadData(aid: String){
        GlobalScope.launch(Dispatchers.Default) {
            mBiliVideos = net.detailVideo(aid)
            mBiliVideos?.apply {
                var videoDetail = this[mPlayIndex]
                val videoUrl = videoDetail.videos?.get(0)?.base_url!!
                val audioUrl = videoDetail.audios?.get(0)?.base_url!!
//                logd(TAG, "videoUrl: $videoUrl")
//                logd(TAG, "audioUrl: $audioUrl")
                fetchMp4(aid, videoUrl, audioUrl)
            }?: { loge(TAG, "fetch video flow is null")}
        }
    }

    private fun fetchMp4(aid: String, videoUrl: String, audioUrl: String) {
        if(videoUrl.isEmpty() || audioUrl.isEmpty()) return
        val aidFile = File(BiliCore.App().cacheDir, "${aid}.mp4").path

        val videoPair = net.getFilePair(videoUrl)// (filesize,filetype)
        var audioPair = net.getFilePair(audioUrl, "mp4")

        val videoFileName = "video_${System.currentTimeMillis()}.${videoPair.second}"
        val audioFileName = "audio_${System.currentTimeMillis()}.${audioPair.second}"
        downloadRange(Pair(videoFileName, videoUrl), Pair(audioFileName, audioUrl), aidFile)
    }

    private fun downloadRange(videoPair: Pair<String, String>, audioPair: Pair<String, String>,
                              aidFile: String, range: Int = 0, step: Int = 500_000) {
        GlobalScope.launch(Dispatchers.Default) {
            val videoDst = net.download(videoPair.first, videoPair.second, range, step)
            val radioDst = net.download(audioPair.first, audioPair.second, range, step)
            cachePaths.add(videoDst)
            cachePaths.add(radioDst)
            if(videoDst.isNotEmpty() or radioDst.isNotEmpty()){
                val synthesis = BiliFFmpeg.audioAndVideoSynthesis(videoDst, radioDst, aidFile)
                if(synthesis) logd(TAG, "audio and video synthesis success!")
                cachePaths.add(aidFile)
                mCurrent.postValue(aidFile)
                val substring = aidFile.substring(aidFile.lastIndexOf("."), aidFile.length)
                val outputFile = aidFile.replace(substring, "0$substring")
                downloadRange(videoPair, audioPair, outputFile, step + 1, step * 2)
            }else logd(TAG, "downloadRange stop")
        }
    }

    override fun onCleared() {
        cachePaths.forEach {
            File(it).apply {
                var delete = delete()
//                logd(TAG, "onCleared: delete: $delete $path")
            }
        }
    }


}