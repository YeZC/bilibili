package com.yzc.video.arch.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yzc.base.util.logd
import com.yzc.video.R
import com.yzc.video.arch.viewmodel.VideoVerticalViewModel
import com.yzc.video.bean.BiliVideoEntry
import com.yzc.video.widget.media.IjkVideoView

class VideoVerticalFragment: Fragment() {

    companion object {
        const val INTENT_AID = "aid"
        const val INTENT_BASE_URL = "base_url"
        const val INTENT_VIDEO_ENTRY = "video_entry"
    }

    private val TAG = VideoVerticalFragment::class.java.simpleName
    private val PROGRESS = "progress"
    var biliVideoEntry: BiliVideoEntry? = null
    lateinit var aid: String
    private lateinit var viewModel: VideoVerticalViewModel
    private var curDuration = 0

    lateinit var ijkVideoView : IjkVideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        biliVideoEntry = arguments?.getParcelable<BiliVideoEntry>(INTENT_VIDEO_ENTRY)
        aid = biliVideoEntry?.aid.toString()
        curDuration = savedInstanceState?.getInt(PROGRESS)?: 0
        logd(TAG, "onCreate aid:$aid curDuration:$curDuration")

        viewModel = ViewModelProvider(this).get(VideoVerticalViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.video_fragment_video_vertical, container)
        ijkVideoView = view.findViewById(R.id.ijkvideoview)
        ijkVideoView.setCurDuration(curDuration)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.videoPath?.observe(viewLifecycleOwner, Observer {
            logd(TAG, "VideoPath:$it")
            ijkVideoView.playMyBili(it[aid]!!.path)
        })
        viewModel.loadData(aid, biliVideoEntry!!)
    }

    override fun onResume() {
        super.onResume()
        ijkVideoView.start()
    }

    override fun onPause() {
        super.onPause()
        ijkVideoView.pause()
    }

    override fun onStop() {
        super.onStop()
        ijkVideoView.stopPlayback()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(PROGRESS, ijkVideoView.getCurDuration(true))
    }

    override fun onDestroy() {
        super.onDestroy()
        ijkVideoView.release(true)
    }
}