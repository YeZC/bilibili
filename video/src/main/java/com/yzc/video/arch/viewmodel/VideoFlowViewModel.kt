package com.yzc.video.arch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yzc.base.util.logd
import com.yzc.base.util.loge
import com.yzc.video.BiliFFmpeg
import com.yzc.video.Constants
import com.yzc.video.arch.model.backup.VideoFlowNet
import com.yzc.video.arch.model.backup.bean.BiliVideoFlow
import com.yzc.video.bean.VideoFetch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

class VideoFlowViewModel : ViewModel() {

    val biliVideos: LiveData<String>
        get() = mCurrent
//    val progress: LiveData<Int>
//        get() = mProgress

    private val TAG = VideoFlowViewModel::class.java.simpleName
    private val net by lazy { VideoFlowNet() }

    private var mBiliVideos: MutableList<BiliVideoFlow>? = null
    private var mCurrent = MutableLiveData<String>()
//    private var mProgress = 0

    private val mPlayIndex = 0
    // 视频中转产物缓存
    private val cachePaths = mutableSetOf<String>()
    // loadding
    private val loading = AtomicBoolean(false)
    private var mCurPlayPath = ""

    fun loadData(aid: String){
        logd(TAG, "loadData: loading$loading")
        if(loading.get()) {
            mCurrent.postValue(mCurPlayPath)
            return
        }
        loading.set(true)
        GlobalScope.launch(Dispatchers.Default) {
            mBiliVideos = net.detailVideo(aid)
            mBiliVideos?.apply {
                var videoDetail = this[mPlayIndex]
                val videoUrl = videoDetail.videos?.get(0)?.base_url!!
                val audioUrl = videoDetail.audios?.get(0)?.base_url!!
//                logd(TAG, "videoUrl: $videoUrl")
//                logd(TAG, "audioUrl: $audioUrl")
                if(videoUrl.isNotEmpty() || audioUrl.isNotEmpty()){
                    mCurPlayPath = File(Constants.videoCachePath(), "${aid}.mp4").path

                    val videoPair = net.getFilePair(videoUrl)// (filesize,filetype)
                    var audioPair = net.getFilePair(audioUrl, "mp4")
                    val videoFileName = "video_${System.currentTimeMillis()}.${videoPair.second}"
                    val audioFileName = "audio_${System.currentTimeMillis()}.${audioPair.second}"

                    val video = VideoFetch(videoUrl, videoPair.first, videoPair.second, videoFileName)
                    val audio = VideoFetch(audioUrl, audioPair.first, audioPair.second, audioFileName)
                    downloadRange(video, audio, mCurPlayPath)
                }
            }?: { loge(TAG, "fetch video flow is null")}
        }
    }

    private fun downloadRange(videoFetch: VideoFetch, audioFetch: VideoFetch, aidFile: String) {
        val videoDst = videoFetch.let {
            net.download(it.fileName, it.baseUrl, it.start, it.end)
        }
        val audioDst = audioFetch.let {
            net.download(it.fileName, it.baseUrl, it.start, it.end)
        }
        videoFetch.next()
        audioFetch.next()
        cachePaths.add(videoDst)
        cachePaths.add(audioDst)
        if(videoDst.isNotEmpty() or audioDst.isNotEmpty()){
            val synthesis = BiliFFmpeg.audioAndVideoSynthesis(videoDst, audioDst, aidFile)
            if(synthesis) logd(TAG, "audio and video synthesis success!")
            mCurrent.postValue(aidFile)
            val substring = aidFile.substring(aidFile.lastIndexOf("."), aidFile.length)
            val outputFile = aidFile.replace(substring, "0$substring")
            mCurPlayPath = outputFile
            downloadRange(videoFetch, audioFetch, outputFile)
        }else logd(TAG, "downloadRange stop")
    }

    override fun onCleared() {
        loading.set(false)
        logd(TAG, "onCleared: loading$loading")
        cachePaths.forEach {
            File(it).apply {
                var delete = delete()
//                logd(TAG, "onCleared: delete: $delete $path")
            }
        }
    }


}