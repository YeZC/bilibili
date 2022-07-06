package com.yzc.video.arch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yzc.base.util.loge
import com.yzc.video.arch.model.backup.VideoFlowNet
import com.yzc.video.bean.BiliVideoEntry
import com.yzc.video.bean.VideoFetch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class VideoFlowViewModel : ViewModel() {

    val biliVideos: LiveData<MutableList<BiliVideoEntry>>
        get() = mCurrent

    private val TAG = VideoFlowViewModel::class.java.simpleName
    private val net by lazy { VideoFlowNet() }

    private var mBiliVideos: MutableList<BiliVideoEntry> = mutableListOf()
    private var mCurrent = MutableLiveData<MutableList<BiliVideoEntry>>()

    fun loadData(aid: String){
        GlobalScope.launch(Dispatchers.Default) {
            val videos = net.detailVideo(aid)
            videos?.apply {
                forEach { mBiliVideos.add(it.toBiliVideoEntry()) }
                mCurrent.postValue(mBiliVideos)
            }?: { loge(TAG, "fetch video flow is null")}
        }
    }

    override fun onCleared() {
    }


}