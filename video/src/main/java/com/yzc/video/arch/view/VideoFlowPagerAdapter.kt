package com.yzc.video.arch.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yzc.video.arch.model.backup.bean.BiliVideoFlowResponse
import com.yzc.video.arch.view.VideoVerticalFragment.Companion.INTENT_AID
import com.yzc.video.arch.view.VideoVerticalFragment.Companion.INTENT_BASE_URL
import com.yzc.video.arch.view.VideoVerticalFragment.Companion.INTENT_VIDEO_ENTRY
import com.yzc.video.bean.BiliVideoEntry

class VideoFlowPagerAdapter(fm: AppCompatActivity) : FragmentStateAdapter(fm) {

    var data: List<BiliVideoEntry>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = data?.size?: 0

    override fun createFragment(i: Int): Fragment {
        var fragment = VideoVerticalFragment()
        fragment.arguments = Bundle().apply {
            putParcelable(INTENT_VIDEO_ENTRY, data?.get(i))
        }
        return fragment
    }


}
