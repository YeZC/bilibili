package com.yzc.bilibili.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.youth.banner.itemdecoration.MarginDecoration
import com.yzc.bilibili.R
import com.yzc.bilibili.adapter.BiliRecommendAdapter
import com.yzc.bilibili.arch.viewmodel.RecommendViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BiliRecommendFragment: Fragment() {

    private var swipeRefreshView: SwipeRefreshLayout? = null
    private var recyclerView: RecyclerView? = null

    private val viewModel = RecommendViewModel()
    private val recommendAdapter = BiliRecommendAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bili_app_fragment_recommend, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        swipeRefreshView = view.findViewById(R.id.swipe_refresh_layout)
        recyclerView = view.findViewById(R.id.recycler_view)

        swipeRefreshView?.apply {
            setColorSchemeResources(R.color.theme_color_primary)
            setOnRefreshListener {
                GlobalScope.launch(context = Dispatchers.Main) {
                    viewModel.loadData()
                    delay(1_000)
                    swipeRefreshView?.isRefreshing = false
                    recommendAdapter.notifyDataSetChanged()
                }
            }
        }

        recyclerView?.apply {
            adapter = recommendAdapter
            var topPadding = context.resources.getDimension(R.dimen.ad_feed_card_content_margin_horizontal)
                    .toInt()
            addItemDecoration(object : MarginDecoration(topPadding) {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.bottom -= topPadding
                }
            })
        }
        viewModel.biliRecommend.observe(viewLifecycleOwner){
            recommendAdapter.setDatas(it)
        }

        viewModel.loadData()
    }

//    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
//        when(event){
//            Lifecycle.Event.ON_CREATE -> {
//                lifecycle.addObserver(this)
//            }
//            Lifecycle.Event.ON_DESTROY -> {
//                lifecycle.removeObserver(this)
//            }
//        }
//    }
}