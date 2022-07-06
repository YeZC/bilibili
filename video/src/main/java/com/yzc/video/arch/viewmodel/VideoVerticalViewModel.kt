package com.yzc.video.arch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yzc.base.util.logd
import com.yzc.video.BiliFFmpeg
import com.yzc.video.Constants
import com.yzc.video.arch.model.backup.VideoFlowNet
import com.yzc.video.arch.viewmodel.bean.VideoPath
import com.yzc.video.bean.BiliVideoEntry
import com.yzc.video.bean.VideoFetch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

class VideoVerticalViewModel : ViewModel() {

    val videoPath: LiveData<ConcurrentHashMap<String, VideoPath>>
        get() = mVideoPaths

    private val TAG = VideoVerticalViewModel::class.java.simpleName
    private val net by lazy { VideoFlowNet() }

    private var mVideoPaths = MutableLiveData<ConcurrentHashMap<String, VideoPath>>()
    private var mAidFileMap = ConcurrentHashMap<String, VideoPath>()

    // 视频中转产物缓存
    private val cachePaths = mutableSetOf<String>()
    private val fetch = AtomicBoolean(false)


    fun loadData(aid: String, videoDetail: BiliVideoEntry){
        GlobalScope.launch(Dispatchers.Default) {
            val videoUrl = videoDetail.videos?.get(0)?.base_url!!
            val audioUrl = videoDetail.audios?.get(0)?.base_url!!
//                logd(TAG, "videoUrl: $videoUrl")
//                logd(TAG, "audioUrl: $audioUrl")
            if(videoUrl.isNotEmpty() || audioUrl.isNotEmpty()){
                mAidFileMap[aid] = VideoPath(aid, File(Constants.videoCachePath(), "${aid}.mp4").path)

                val videoPair = net.getFilePair(videoUrl)// (filesize,filetype)
                var audioPair = net.getFilePair(audioUrl, "mp4")
                val videoFileName = "video_${System.currentTimeMillis()}.${videoPair.second}"
                val audioFileName = "audio_${System.currentTimeMillis()}.${audioPair.second}"

                val video = VideoFetch(videoUrl, videoPair.first, videoPair.second, videoFileName)
                val audio = VideoFetch(audioUrl, audioPair.first, audioPair.second, audioFileName)
                downloadRange(aid, video, audio, mAidFileMap[aid]?.path!!)
            }
        }
    }

    private fun downloadRange(aid: String, videoFetch: VideoFetch, audioFetch: VideoFetch, outputFile: String) {
        val aidFile = outputFile
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
        if(videoDst.isNotEmpty() and audioDst.isNotEmpty()){
            val synthesis = BiliFFmpeg.audioAndVideoSynthesis(videoDst, audioDst, aidFile)
            if(synthesis) logd(TAG, "audio and video synthesis success!")
            mAidFileMap[aid]?.path = aidFile
            mVideoPaths.postValue(mAidFileMap)
            val substring = aidFile.substring(aidFile.lastIndexOf("."), aidFile.length)
            val outputFile = aidFile.replace(substring, "0$substring")
//            mAidFileMap[aid]?.path = outputFile
            downloadRange(aid, videoFetch, audioFetch, outputFile)
        }else logd(TAG, "downloadRange stop")
    }

//    fun find(aid: String): Int? {
//        if(aid.isEmpty()) return null
//        mCurrent.forEachIndexed { index, mutableLiveData ->
//            if(aid.equals(mutableLiveData.value?.aid)) return index
//        }
//        return null
//    }

//    private fun fetchVideo(aid: String){
//        find(aid)?.let { index ->
//            var videoDetail = mBiliVideos?.get(index)!!
//            val videoUrl = videoDetail.videos?.get(0)?.base_url!!
//            val audioUrl = videoDetail.audios?.get(0)?.base_url!!
////                logd(TAG, "videoUrl: $videoUrl")
////                logd(TAG, "audioUrl: $audioUrl")
//            if(videoUrl.isNotEmpty() || audioUrl.isNotEmpty()){
//                mCurPlayPath = File(Constants.videoCachePath(), "${aid}.mp4").path
//
//                val videoPair = net.getFilePair(videoUrl)// (filesize,filetype)
//                var audioPair = net.getFilePair(audioUrl, "mp4")
//                val videoFileName = "video_${System.currentTimeMillis()}.${videoPair.second}"
//                val audioFileName = "audio_${System.currentTimeMillis()}.${audioPair.second}"
//
//                val video = VideoFetch(videoUrl, videoPair.first, videoPair.second, videoFileName)
//                val audio = VideoFetch(audioUrl, audioPair.first, audioPair.second, audioFileName)
//                downloadRange(aid, video, audio, mCurPlayPath)
//            }
//        }?: { loge(TAG, "aid video is empty")}
//    }

//    private fun fetchFlow(aid: String){
//        logd(TAG, "fetch: fetch:$fetch")
//        if(fetch.get()) {
//            find(aid)?.let {
//                mCurrent[it].apply { postValue(value) }
//            }
//            return
//        }
//        GlobalScope.launch(Dispatchers.Default) {
//            mBiliVideos = net.detailVideo(aid)
//            mBiliVideos?.forEach {
//                mCurrent.add(MutableLiveData(VideoPath(aid, "")))
//            }
//            fetchVideo(aid)
//        }
//        fetch.set(true)
//    }

    override fun onCleared() {
        fetch.set(false)
        logd(TAG, "onCleared: fetch:$fetch")
        cachePaths.forEach {
            File(it).apply {
                var delete = delete()
//                logd(TAG, "onCleared: delete: $delete $path")
            }
        }
    }


}