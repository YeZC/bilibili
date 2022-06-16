package com.yzc.bilibili.arch.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yzc.base.util.logd
import com.yzc.bilibili.R
import com.yzc.bilibili.arch.viewmodel.VideoFlowViewModel

class VideoFlowActivity : AppCompatActivity() {

    companion object{
        const val INTENT_AID = "aid"
    }

    private val TAG = VideoFlowActivity::class.java.simpleName
    private val viewModel = VideoFlowViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_flow)
        var aid = savedInstanceState?.getString(INTENT_AID)?: -1

        logd(TAG, "onCreate $aid")
    }
}