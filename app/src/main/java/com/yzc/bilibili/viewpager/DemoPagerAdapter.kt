package com.yzc.bilibili.viewpager

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yzc.bilibili.fragment.BiliRecommendFragment

class DemoPagerAdapter(fm: AppCompatActivity, var data: List<String>) : FragmentStateAdapter(fm) {

    override fun getItemCount(): Int = data.size

    override fun createFragment(i: Int): Fragment {
        var fragment: Fragment? = null
        if(i == 1){
            fragment = BiliRecommendFragment()
        }else{
            fragment = DemoFragment()
            fragment.arguments = Bundle().apply {
                putString("object", data[i])
            }
        }
        return fragment
    }


}
