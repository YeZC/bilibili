package com.yzc.bilibili.arch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yzc.bilibili.backup.RecommendNet
import com.yzc.bilibili.backup.bean.BiliRecommend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RecommendViewModel : ViewModel() {

    val biliRecommend: LiveData<MutableList<BiliRecommend>>
        get() = _biliRecommend


    private val _biliRecommend = MutableLiveData<MutableList<BiliRecommend>>()
    private val net by lazy { RecommendNet() }

    init {
    }

    fun loadData(){
        GlobalScope.launch(Dispatchers.Default) {
            _biliRecommend.postValue(net.getFeed())
        }
    }


}