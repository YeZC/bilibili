package com.yzc.video.arch.view

import android.net.Uri
import android.os.Bundle
import android.widget.TableLayout
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yzc.base.util.logd
import com.yzc.video.R
import com.yzc.video.arch.viewmodel.VideoFlowViewModel
import com.yzc.video.widget.media.IjkVideoView
import tv.danmaku.ijk.media.player.IMediaPlayer


class VideoFlowActivity : AppCompatActivity() {

    companion object{
        const val INTENT_AID = "aid"
    }

    private val TAG = VideoFlowActivity::class.java.simpleName
    private lateinit var viewModel: VideoFlowViewModel
    lateinit var ijkVideoView: IjkVideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_flow)
        val aid = intent?.getStringExtra(INTENT_AID)?: ""
        logd(TAG, "onCreate $aid")

        initView()
        viewModel = ViewModelProvider(this).get(VideoFlowViewModel::class.java)
        viewModel.biliVideos.observe(this, Observer {
            logd(TAG, "videoFile $it")
            play(it)
        })
        viewModel.loadData(aid)
    }

    private fun play(path: String) {
        if(pathStr.isEmpty()){
            ijkVideoView.apply {
                setVideoPath(path)
                start()
            }
        }
        pathStr = path
        cache = true
    }

    private var cache = false
    private var pathStr = ""
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
                if(cache){
                    ijkVideoView.apply {
                        logd(TAG, "replay: $pathStr")
                        val mces = currentPosition
                        setVideoPath(pathStr)
                        seekTo(mces)
                        start()
                    }
                    cache = false
                }
            }
            setOnPreparedListener {
                logd(TAG, "setOnPreparedListener:")
            }
            setHudView(TableLayout(baseContext))
//            toggleAspectRatio()
        }
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

    override fun onDestroy() {
        super.onDestroy()
        ijkVideoView.release(true)
    }

}