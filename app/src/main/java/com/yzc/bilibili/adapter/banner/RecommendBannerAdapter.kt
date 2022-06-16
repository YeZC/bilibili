package com.yzc.bilibili.adapter.banner

import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.youth.banner.adapter.BannerAdapter
import com.yzc.bilibili.R
import com.yzc.bilibili.arch.model.bean.BannerItem

class RecommendBannerAdapter(mData: MutableList<BannerItem>?): BannerAdapter<BannerItem, RecommendBannerHolder?>(mData) {

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): RecommendBannerHolder {
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        val itemParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val itemView = RelativeLayout(parent.context).apply { layoutParams = itemParams }

        var resources = parent.context.resources
        val imageView = AppCompatImageView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            scaleType = ImageView.ScaleType.CENTER_CROP
            tag = "imageView"
            itemView.addView(this)
        }

//        val shadow = AppCompatTextView(parent.context).apply {
//            setBackgroundResource(R.drawable.bangumi_recommend_mask)
//            var params = RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT,
//                50f.toPx(context)
//            )
//            params.addRule(RelativeLayout.ALIGN_BOTTOM)
//            itemView.addView(this, params)
//        }

        val title = AppCompatTextView(parent.context).apply {
            textSize = 14f// 14sp
            tag = "title"
            isSingleLine = true
            typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            setBackgroundResource(R.drawable.bangumi_recommend_mask)
            setTextColor(resources.getColor(R.color.white))
            var padding = resources.getDimension(R.dimen.ad_feed_card_content_margin_horizontal).toInt()
            setPadding(padding, 0, 0, padding)

            var textParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            ).apply { addRule(RelativeLayout.ALIGN_PARENT_BOTTOM) }
            itemView.addView(this, textParams)
        }

        return RecommendBannerHolder(itemView)
    }

    override fun onBindView(holder: RecommendBannerHolder?, item: BannerItem, position: Int, size: Int) {
        holder?.imageView?.let { Glide.with(it).load(item.static_banner?.image).into(holder.imageView) }
        holder?.title?.apply { text = item.static_banner?.title }
    }
}

class RecommendBannerHolder(view: View) : RecyclerView.ViewHolder(view) {
    var imageView: AppCompatImageView
    var title: AppCompatTextView

    init {
        imageView = view.findViewWithTag("imageView") as AppCompatImageView
        title = view.findViewWithTag("title") as AppCompatTextView
    }
}
