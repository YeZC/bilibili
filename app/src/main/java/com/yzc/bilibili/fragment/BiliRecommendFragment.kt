package com.yzc.bilibili.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import com.yzc.bilibili.R
import com.yzc.bilibili.adapter.BiliRecommendAdapter
import com.yzc.bilibili.util.toPx
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BiliRecommendFragment: Fragment() {

    private var banner: Banner<String, BannerImageAdapter<String>>? = null
    private var swipeRefreshView: SwipeRefreshLayout? = null
    private var recyclerView: RecyclerView? = null
    val testData= listOf<String>(
        "http://i0.hdslb.com/bfs/banner/12e85a6ed2078040d0a114bb28cb9a9c47e88e35.png",
        "http://i0.hdslb.com/bfs/banner/dc91ff69beea43ff7de4211594bc2f5cdfdf5a40.jpg",
        "http://i0.hdslb.com/bfs/banner/88e2c6e39ceb606bdb4340358a4634c3b738a52c.png",
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bili_app_fragment_recommend, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        swipeRefreshView = view.findViewById(R.id.swipe_refresh_layout)
        banner = view.findViewById(R.id.banner)
        recyclerView = view.findViewById(R.id.recycler_view)

        swipeRefreshView?.apply {
            setColorSchemeResources(R.color.theme_color_primary)
            setOnRefreshListener {
                GlobalScope.launch(context = Dispatchers.Main) {
                    delay(1_000)
                    swipeRefreshView?.isRefreshing = false
                }
            }
        }

        banner?.apply {
            isAutoLoop(true)
            setLoopTime(2_000)
            indicator = CircleIndicator(context)
            setIndicatorGravity(IndicatorConfig.Direction.RIGHT)
            setIndicatorSelectedColorRes(R.color.white)
            scrollTime = 200
            setBannerRound2(5f.toPx(context).toFloat())
                .setAdapter(object : BannerImageAdapter<String>(testData) {
                    override fun onBindView(
                        holder: BannerImageHolder,
                        data: String,
                        position: Int,
                        size: Int
                    ) {
                        Glide.with(holder.itemView)
                            .load(data)
                            .into(holder.imageView)
                    }

                })
        }

        recyclerView?.apply {
            layoutManager = GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false)
            adapter = object : BiliRecommendAdapter(null) {

            }
        }
    }
}