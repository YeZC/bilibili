package com.yzc.bilibili.arch.view

import android.net.Uri
import android.os.Bundle
import android.widget.TableLayout
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.yzc.base.util.logd
import com.yzc.bilibili.R
import com.yzc.bilibili.arch.viewmodel.VideoFlowViewModel
import com.yzc.video.widget.media.IjkVideoView
import tv.danmaku.ijk.media.player.IMediaPlayer

class VideoFlowActivity : AppCompatActivity() {

    companion object{
        const val INTENT_AID = "aid"
    }

    private val TAG = VideoFlowActivity::class.java.simpleName
    private val viewModel = VideoFlowViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_flow)
        var aid = intent?.getStringExtra(INTENT_AID)?: -1

        logd(TAG, "onCreate $aid")
        ijkVideoView(findViewById<IjkVideoView>(R.id.ijk_video_view))
//        videoView(findViewById<VideoView>(R.id.video_view))
    }

    private fun videoView(videoView: VideoView) {
        val uri = Uri.parse("https://www.bilibili.com/video/BV1R7411A7iK?t=2.9")
        videoView.setVideoURI(uri)
        videoView.start()
    }

    private fun ijkVideoView(ijkVideoView: IjkVideoView) {
        ijkVideoView.apply {
            ijkVideoView.setVideoPath("http://upos-sz-mirrorcoso1.bilivideo.com/upgcxcode/20/08/747340820/747340820-1-31101.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEuENvNC8aNEVEtEvE9IMvXBvE2ENvNCImNEVEIj0Y2J_aug859r1qXg8gNEVE5XREto8z5JZC2X2gkX5L5F1eTX1jkXlsTXHeux_f2o859IB_&uipk=5&nbs=1&deadline=1655805900&gen=playurlv2&os=coso1bv&oi=3070630519&trid=1b2f5a2770db40d7bfa08308d55c2432O&mid=0&platform=iphone&upsig=887f56c63a375c507dac0b8d84bedafb&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=1&orderid=0,2&bw=133486&logo=80000000")// mp3
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
                    ijkVideoView.start()
                }
                setHudView(TableLayout(baseContext))
            }
        }
    }
}