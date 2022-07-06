package com.yzc.video.arch.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.yzc.base.util.logd
import com.yzc.video.R
import com.yzc.video.arch.viewmodel.VideoFlowViewModel
import com.yzc.video.widget.media.IjkVideoView


class VideoFlowActivity : AppCompatActivity() {
    companion object{
        const val INTENT_AID = "aid"

        fun start(context: Context, param: String) {
            val intent = Intent(context, VideoFlowActivity::class.java).apply {
                putExtra(INTENT_AID, param)
            }
            context.startActivity(intent)
        }
    }

    private val TAG = VideoFlowActivity::class.java.simpleName
    private lateinit var viewModel: VideoFlowViewModel
    lateinit var viewPager: ViewPager2
    lateinit var adapter: VideoFlowPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videos_flow)
        val aid = intent?.getStringExtra(INTENT_AID)?: ""

        initView()
        adapter = VideoFlowPagerAdapter(this)
        viewPager.adapter = this.adapter
        viewModel = ViewModelProvider(this).get(VideoFlowViewModel::class.java)
        viewModel.biliVideos.observe(this, Observer {
            logd(TAG, "videos has ${it.size}")
            adapter.data = it
        })
        viewModel.loadData(aid)
    }

    private fun initView() {
        viewPager = findViewById(R.id.video_flow_viewpager)
    }

}