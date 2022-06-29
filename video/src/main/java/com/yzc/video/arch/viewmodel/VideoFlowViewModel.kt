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
        var audioPair = net.getFilePair(audioUrl)
        audioPair = Pair(audioPair.first, "mp4")

        val videoFileName = "video${System.currentTimeMillis()}.${videoPair.second}"
        val audioFileName = "audio${System.currentTimeMillis()}.${audioPair.second}"
        val path1 = net.download(videoFileName, videoUrl)
        val path2 = net.download(audioFileName, audioUrl)
        cachePaths.add(path1)
        cachePaths.add(path2)
        if(path1.isNotEmpty() or path2!!.isNotEmpty()){
            var synthesis = BiliFFmpeg.audioAndVideoSynthesis(path1, path2, aidFile)
            if(synthesis) logd(TAG, "audio and video synthesis success!")
            mCurrent.postValue(aidFile)
            cachePaths.add(aidFile)
            val substring = aidFile.substring(aidFile.lastIndexOf("."), aidFile.length)
            val outputFile = aidFile.replace(substring, "0$substring")
            downloadRange(Pair(videoFileName, videoUrl), Pair(audioFileName, audioUrl), path1, path2, outputFile)
        }
    }

    private fun downloadRange(videoPair: Pair<String, String>, audioPair: Pair<String, String>,
                              path1: String, path2: String,
                              aidFile: String, range: Int = 500_001, step: Int = 1000_000) {
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
                downloadRange(videoPair, audioPair, path1, path2, outputFile, step + 1, step * 2)
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