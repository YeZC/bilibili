package com.yzc.bilibili.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yzc.bilibili.R
import com.yzc.bilibili.backup.bean.BiliRecommend
import com.yzc.bilibili.backup.bean.BiliRecommendResponse

open class BiliRecommendAdapter(val data: MutableList<BiliRecommend>? = null): RecyclerView.Adapter<BiliRecommendAdapter.RecommendViewHolder>() {

    class RecommendViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        init {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bili_app_recyclerview_recommend, parent, false)
        return RecommendViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return data?.size?: 0
    }
}