package com.yzc.video.arch.view

import android.net.Uri
import android.os.Bundle
import android.widget.TableLayout
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.yzc.base.util.logd
import com.yzc.video.R
import com.yzc.video.arch.viewmodel.VideoFlowViewModel
import com.yzc.video.widget.media.IjkVideoView
import okio.BufferedSink
import okio.Okio
import okio.Source
import tv.danmaku.ijk.media.player.IMediaPlayer
import java.io.File
import java.lang.Exception


class VideoFlowActivity : AppCompatActivity() {

    companion object{
        const val INTENT_AID = "aid"
    }

    private val TAG = VideoFlowActivity::class.java.simpleName
    private val viewModel = VideoFlowViewModel()
    lateinit var ijkVideoView: IjkVideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_flow)
        val aid = intent?.getStringExtra(INTENT_AID)?: ""
        logd(TAG, "onCreate $aid")

        initView()
//        videoView(findViewById<VideoView>(R.id.video_view))
        viewModel.biliVideos.observe(this, Observer {
            play(it)
        })
        viewModel.loadData(aid)
    }

    private fun videoView(videoView: VideoView) {
        val uri = Uri.parse("https://www.bilibili.com/video/BV1R7411A7iK?t=2.9")
        videoView.setVideoURI(uri)
        videoView.start()
    }

    private fun play(path: String) {
        ijkVideoView.apply {
            setVideoPath(path)
            start()
        }
    }

    private fun initView() {
        ijkVideoView = findViewById(R.id.ijk_video_view)
        ijkVideoView.apply {
            setOnErrorListener(object : IMediaPlayer.OnErrorListener {
                override fun onError(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean {
                    logd(TAG, "onError: p1:$p1 p2:$p2")
                    return true
                }
            })
            setOnCompletionListener {
                logd(TAG, "setOnCompletionListener:")
            }
            setOnPreparedListener {
                logd(TAG, "setOnPreparedListener:")
            }
            setHudView(TableLayout(baseContext))
//            toggleAspectRatio()
        }
    }
}