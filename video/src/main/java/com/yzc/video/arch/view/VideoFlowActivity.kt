package com.yzc.video.arch.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TableLayout
import android.widget.Toast
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
        private const val PROGRESS = "progress"

        fun start(context: Context, param: String) {
            val intent = Intent(context, VideoFlowActivity::class.java).apply {
                putExtra(INTENT_AID, param)
            }
            context.startActivity(intent)
        }
    }

    private val TAG = VideoFlowActivity::class.java.simpleName
    private lateinit var viewModel: VideoFlowViewModel
    lateinit var ijkVideoView: IjkVideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_flow)
        val curDuration = savedInstanceState?.getInt(PROGRESS)?: 0
        val aid = intent?.getStringExtra(INTENT_AID)?: ""
        logd(TAG, "onCreate aid:$aid curDuration:$curDuration")

        initView(curDuration)
        viewModel = ViewModelProvider(this).get(VideoFlowViewModel::class.java)
        viewModel.biliVideos.observe(this, Observer {
            logd(TAG, "videoFile $it")
            ijkVideoView.playMyBili(it)
        })
        viewModel.loadData(aid)
    }

    private fun initView(curDuration: Int) {
        ijkVideoView = findViewById(R.id.ijk_video_view)
        ijkVideoView.setCurDuration(curDuration)
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