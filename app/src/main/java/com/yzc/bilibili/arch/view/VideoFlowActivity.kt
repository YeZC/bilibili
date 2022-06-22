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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_flow)
        var aid = intent?.getStringExtra(INTENT_AID)?: -1

        logd(TAG, "onCreate $aid")
        ijkVideoView(findViewById<IjkVideoView>(R.id.ijk_video_view))
//        videoView(findViewById<VideoView>(R.id.video_view))
    }

    private fun videoView(videoView: VideoView) {
        val uri = "android.resource://" + packageName + "/" + R.raw.testvideo
        logd(TAG, "uri：${Uri.parse(uri)}")
//        val uri = Uri.parse("https://www.bilibili.com/video/BV1R7411A7iK?t=2.9")
        videoView.setVideoURI(Uri.parse(uri))
        videoView.start()
    }

    private fun ijkVideoView(ijkVideoView: IjkVideoView) {
        ijkVideoView.apply {
            var videoPath = File(cacheDir, "video.m4s")
            var source: Source? = null
            var bufferedSink: BufferedSink? = null
            try{
                source = Okio.source(assets.open("test.m4s"))
                bufferedSink = Okio.buffer(Okio.sink(videoPath))
                bufferedSink.writeAll(source)
                bufferedSink.flush()
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                bufferedSink?.close()
                source?.close()
            }

            ijkVideoView.apply {
                setVideoPath(videoPath.path)
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
                toggleAspectRatio()
                start()
            }
        }
    }
}