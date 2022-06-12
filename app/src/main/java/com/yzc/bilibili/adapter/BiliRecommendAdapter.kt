package com.yzc.bilibili.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.youth.banner.Banner
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.indicator.CircleIndicator
import com.yzc.base.util.logd
import com.yzc.bilibili.R
import com.yzc.bilibili.adapter.banner.RecommendBannerAdapter
import com.yzc.bilibili.backup.bean.BannerItem
import com.yzc.bilibili.backup.bean.BiliRecommend
import com.yzc.bilibili.backup.bean.RecommendVH
import com.yzc.bilibili.util.toPx
import java.lang.RuntimeException

class BiliRecommendAdapter:
    RecyclerView.Adapter<BiliRecommendAdapter.RecommendViewHolder>() {

    private val TAG = BiliRecommendAdapter::class.java.simpleName
    private var mDatas: MutableList<BiliRecommend> = mutableListOf<BiliRecommend>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            RecommendVH.BANNER.type -> {
                val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 210f.toPx(parent.context))
                val banner = Banner<BannerItem, RecommendBannerAdapter>(parent.context).apply {
                    isAutoLoop(true)
                    setLoopTime(2_000)
                    indicator = CircleIndicator(context)
                    setIndicatorGravity(IndicatorConfig.Direction.RIGHT)
                    setIndicatorSelectedColorRes(R.color.white)
                    scrollTime = 200
                    var pointPadding = 5f.toPx(context)
                    var defMargins = indicatorConfig.margins
                    setIndicatorMargins(IndicatorConfig.Margins(
                        defMargins.leftMargin,
                        defMargins.topMargin,
                        pointPadding * 2,
                        pointPadding))
                    setIndicatorNormalWidth(pointPadding)
                    setIndicatorSelectedWidth(pointPadding)
                    setBannerRound2(5f.toPx(context).toFloat())
                    setAdapter(RecommendBannerAdapter(mutableListOf()))
                    layoutParams = params
                }
                BannerVH(banner)
            }
            RecommendVH.CM.type, RecommendVH.SMALL_COVER.type -> {
                CombinationVH(inflater.inflate(R.layout.bili_app_recyclerview_recommend, parent, false))
            }
            else  -> {
                EmptyVH(inflater.inflate(R.layout.bili_app_recyclerview_recommend, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecommendViewHolder, position: Int) {
        logd(TAG, "position: $position")
        val index = if(position == 0) 0 else (position * 2) - beforeSingleLine[position]
        if(mDatas[index].card_type == RecommendVH.BANNER.cardType){
            holder.bind(mDatas[index])
        }else{
            holder.bind(mDatas[index], mDatas[index + 1])
        }
    }

    private var beforeSingleLine = mutableListOf<Int>()// 单行
    fun setDatas(list: MutableList<BiliRecommend>?){
        beforeSingleLine = mutableListOf()
        mDatas = list ?: mutableListOf()
        beforeSingleLine.add(0)
        var singleFlag = true

        if(mDatas.size > 0 && mDatas[0].card_type == RecommendVH.BANNER.cardType){
            beforeSingleLine[0] = 1
        }
        for (i in 1 until mDatas.size){
            val it = mDatas[i]
            if(it.card_type == RecommendVH.BANNER.cardType){
                beforeSingleLine.add(beforeSingleLine[beforeSingleLine.size - 1] + 1)
            }else if(singleFlag){
                beforeSingleLine.add(beforeSingleLine[beforeSingleLine.size - 1])
                singleFlag = false
            }else{
                singleFlag = true
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return beforeSingleLine.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (mDatas[position].card_type){
            RecommendVH.BANNER.cardType -> RecommendVH.BANNER.type
            RecommendVH.SMALL_COVER.cardType -> RecommendVH.SMALL_COVER.type
            RecommendVH.CM.cardType -> RecommendVH.CM.type
            else -> { RecommendVH.EMPTY.type }
        }
    }

    abstract class RecommendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // single line
        open fun bind(biliRecommend: BiliRecommend){}
        // double item of single line
        open fun bind(leftRecommend: BiliRecommend, rightRecommend: BiliRecommend){}
    }

    class BannerVH(itemView: View): RecommendViewHolder(itemView) {
        override fun bind(biliRecommend: BiliRecommend) {
            val banner = itemView as Banner<BannerItem, RecommendBannerAdapter>
            banner.adapter.setDatas(biliRecommend.banner_item)
        }
    }

    class CombinationVH(itemView: View): RecommendViewHolder(itemView) {
        private val leftHolder = CombinationHolder(itemView.findViewById(R.id.left_card))
        private val rightHolder = CombinationHolder(itemView.findViewById(R.id.right_card))

        override fun bind(leftRecommend: BiliRecommend, rightRecommend: BiliRecommend) {
            innerBind(leftHolder, leftRecommend)
            innerBind(rightHolder, rightRecommend)
        }

        fun innerBind(holder: CombinationHolder?, recommend: BiliRecommend){
            holder?.apply {
                Glide.with(cover.context).load(recommend.cover).into(cover)
                title.text = recommend.title
                recommend.small_cover?.let { smallCover ->
                    coverLeftText.text = smallCover.cover_left_text_1
                    coverLeftText2.text = smallCover.cover_left_text_2
                    coverRightText.text = smallCover.cover_right_text
                    if(smallCover.rcmd_reason_style){// rcmd_reason_style
                        descButtonGroup.visibility = View.GONE
                        rcmdReasonStyleGroup.visibility = View.VISIBLE

                        rcmdReasonStyleText.text = smallCover.rcmd_reason_style_text
                        rcmdReasonStyleText.setTextColor(Color.parseColor(smallCover.rcmd_reason_style_text_color))
                        rcmdReasonStyleText.setBackgroundColor(Color.parseColor(smallCover.rcmd_reason_style_bg_color))
                    }else if(smallCover.desc_button){// desc_button && goto_icon
                        descButtonGroup.visibility = View.VISIBLE
                        rcmdReasonStyleGroup.visibility = View.GONE

                        upName.text = smallCover.desc_button_text
                    }
                }
            }?: throw RuntimeException("CombinationVH innerBind holder is null")
        }

        class CombinationHolder(itemView: View) {
            // universal
            val cover: AppCompatImageView = itemView.findViewById(R.id.iv_cover)
            val coverLeftText: AppCompatTextView = itemView.findViewById(R.id.cover_left_text)
            val coverLeftText2: AppCompatTextView = itemView.findViewById(R.id.cover_left_text2)
            val coverRightText: AppCompatTextView = itemView.findViewById(R.id.cover_right_text)
            val title: AppCompatTextView = itemView.findViewById(R.id.tv_title)
            val threePointMenu: AppCompatImageView = itemView.findViewById(R.id.iv_three_point_menu)
            // small_cover
                // desc_button_group
                val descButtonGroup: Group = itemView.findViewById(R.id.desc_button_group)
                val upName: AppCompatTextView = itemView.findViewById(R.id.tv_up_name)
                 // rcmd_reason_style
                val rcmdReasonStyleGroup: Group = itemView.findViewById(R.id.rcmd_reason_style_group)
                val rcmdReasonStyleText: AppCompatTextView = itemView.findViewById(R.id.tv_rcmd_reason_style)
            // cm
        }
    }

    class EmptyVH(itemView: View): RecommendViewHolder(itemView) {
        override fun bind(biliRecommend: BiliRecommend) {
        }
    }
}

