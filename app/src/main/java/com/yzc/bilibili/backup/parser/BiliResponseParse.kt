package com.yzc.bilibili.backup.parser

import com.yzc.bilibili.backup.bean.BannerItem
import com.yzc.bilibili.backup.bean.BiliRecommend
import com.yzc.bilibili.backup.bean.BiliRecommendResponse
import org.json.JSONObject

class BiliResponseParse {
    companion object {
        fun toBiliRecommend(json: JSONObject): BiliRecommendResponse{
            return BiliRecommendResponse().apply {
                this.code = json.optInt("code")
                this.message = json.optString("message")

                var itemArr = json.optJSONObject("data")?.optJSONArray("items")
                var itemObject: JSONObject? = null
                this.items = mutableListOf<BiliRecommend>()

                for(i in 0..itemArr?.length()!!){
                    itemObject = itemArr.optJSONObject(i)
                    if(i == 0){
                        bannerItems(this.items!!, itemObject)
                    }else{

                    }
                }
            }
        }

        private fun bannerItems(items: MutableList<BiliRecommend>, itemObject: JSONObject?){
            val bannerItemArr = itemObject?.optJSONArray("banner_item")

            val bannerItems = mutableListOf<BannerItem>()
            for(i in 0 until bannerItemArr?.length()!!){
                val bannerJson = bannerItemArr.getJSONObject(i)
                val staticBanner = bannerJson?.optJSONObject("static_banner")
                val bannerItem = BannerItem(
                    type = bannerJson?.optString("type"),
                    index = bannerJson?.optInt("index"),
                    static_banner = staticBanner?.let {
                        BannerItem.StaticBanner(
                            title = staticBanner?.optString("title"),
                            image = staticBanner.optString("image"),
                            uri = staticBanner.optString("uri")
                        )
                    }
                )
                bannerItems.add(bannerItem)
            }
            items.add(BiliRecommend(
                card_type = itemObject?.optString("card_type"),
                banner_item = bannerItems)
            )
        }

    }
}