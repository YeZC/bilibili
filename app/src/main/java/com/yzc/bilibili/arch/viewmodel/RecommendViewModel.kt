package com.yzc.bilibili.arch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yzc.bilibili.arch.model.RecommendNet
import com.yzc.bilibili.arch.model.bean.BiliRecommend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RecommendViewModel : ViewModel() {

    val biliRecommend: LiveData<MutableList<BiliRecommend>>
        get() = mBiliRecommend


    private val mBiliRecommend = MutableLiveData<MutableList<BiliRecommend>>()
    private val net by lazy { RecommendNet() }

    init {
    }

    fun loadData(){
        GlobalScope.launch(Dispatchers.Default) {
            mBiliRecommend.postValue(net.getFeed())
        }
    }


}