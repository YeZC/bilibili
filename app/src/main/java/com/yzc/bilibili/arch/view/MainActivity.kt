package com.yzc.bilibili.arch.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yzc.bilibili.R
import com.yzc.bilibili.util.CacheManager
import com.yzc.bilibili.util.toPx
import com.yzc.bilibili.view.DrawableSelector
import com.yzc.bilibili.viewpager.DemoPagerAdapter

class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val viewPager = findViewById<ViewPager2>(R.id.viewpager)
        val tablayout = findViewById<TabLayout>(R.id.tablayout)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        val tabs = listOf("直播", "推荐", "热门", "动画", "影视", "共同抗疫", "新征程")
        viewPager.adapter = DemoPagerAdapter(this, tabs)
        TabLayoutMediator(tablayout, viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()

        navigationInit(bottomNav)
        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        viewPager.setCurrentItem(1, false)

        CacheManager.clearVideo()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun navigationInit(bottomNav: BottomNavigationView) {
        bottomNav.menu.add(R.string.nav_home).icon = DrawableSelector(
            this,
            R.drawable.ic_vector_tab_bar_home_default, R.drawable.ic_vector_tab_bar_home_selected
        ).get()
        bottomNav.menu.add(R.string.main_page_attentions).icon = DrawableSelector(
            this,
            R.drawable.ic_vector_tab_bar_moments_default,
            R.drawable.ic_vector_tab_bar_moments_selected
        ).get()
        bottomNav.menu.add("")
        bottomNav.menu.add(R.string.main_page_mall).icon = DrawableSelector(
            this,
            R.drawable.ic_vector_tab_bar_shopping_default,
            R.drawable.ic_vector_tab_bar_shopping_selected
        ).get()
        bottomNav.menu.add(R.string.main_page_user_center).icon = DrawableSelector(
            this,
            R.drawable.ic_vector_tab_bar_mine_default, R.drawable.ic_vector_tab_bar_mine_selected
        ).get()

        val leftPadding = 10f.toPx(this@MainActivity)
        var size = 48f.toPx(this)
        val imageView = ImageView(this).apply {
            setImageResource(R.drawable.bili_app_view_nav_center)
            val top = 5f.toPx(this@MainActivity)
            setPadding(leftPadding, top, leftPadding, top)
        }
        bottomNav.addView(imageView, FrameLayout.LayoutParams(size + (2 * leftPadding), size).apply {
            gravity = Gravity.CENTER
        })
        imageView.setOnTouchListener { v, event ->
            if(event.actionMasked == MotionEvent.ACTION_DOWN){
                v.animate().setDuration(100)
                    .scaleX(0.9f)
                    .scaleY(0.9f).start()
            }else if(event.actionMasked == MotionEvent.ACTION_UP || event.actionMasked == MotionEvent.ACTION_CANCEL){
                v.animate().setDuration(100)
                    .scaleX(1f)
                    .scaleY(1f).start()
            }
            return@setOnTouchListener onTouchEvent(event)
        }
        imageView.setOnClickListener {
        }
    }

}