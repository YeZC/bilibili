package com.yzc.bilibili.backup.parser

import com.yzc.bilibili.backup.bean.*
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
                    var cardType = itemObject?.optString("card_type")
                    when(cardType){
                        "banner_v8" -> {
                            bannerItems(this.items!!, itemObject)
                        }
                        "small_cover_v2" -> {
                            smallCoverItems(this.items!!, itemObject)
                        }
                        "cm_v2" -> {
                            cmItems(this.items!!, itemObject)
                        }
                        else -> {
                            println("toBiliRecommend: [${cardType}] parse error")
                        }
                    }
                }
            }
        }

        private fun cmItems(items: MutableList<BiliRecommend>, itemObject: JSONObject) {
            var adInfo = itemObject.optJSONObject("ad_info")
            var creativeContent = adInfo?.optJSONObject("creative_content")

            var descButton = itemObject.optJSONObject("desc_button")

            items.add(BiliRecommend(
                card_type = itemObject.optString("card_type"),
                cover = itemObject.optString("cover"),
                title = itemObject.optString("title"),
                uri = itemObject.optString("uri"),
                cm = CM(
                    cover_left_text_1 = itemObject.optString("cover_left_text_1"),
                    goto = itemObject.optString("goto"),
                    cover_right_content_description = itemObject.optString("cover_right_content_description"),
                    cover_left_2_content_description = itemObject.optString("cover_left_2_content_description"),
                    uri = itemObject.optString("uri"),
                    cover_left_1_content_description = itemObject.optString("cover_left_1_content_description"),
                    CM.AdInfo(
                        image_url = creativeContent?.optString("image_url"),
                        description = creativeContent?.optString("description"),
                        title = creativeContent?.optString("title"),
                        url = creativeContent?.optString("url"),
                        video_id = creativeContent?.optLong("video_id")
                    ),
                    CM.DescButton(
                        text = descButton?.optString("text"),
                        event = descButton?.optString("event"),
                        type = descButton?.optInt("type")
                    )
                )
            ))
        }

        private fun smallCoverItems(items: MutableList<BiliRecommend>, itemObject: JSONObject) {
            val desc_button = itemObject.optJSONObject("desc_button")
            val goto_icon = itemObject.optJSONObject("goto_icon")
            val rcmd_reason_style = itemObject.optJSONObject("rcmd_reason_style")
            items.add(BiliRecommend(
                card_type = itemObject.optString("card_type"),
                cover = itemObject.optString("cover"),
                title = itemObject.optString("title"),
                uri = itemObject.optString("uri"),
                SmallCover(
                    talk_back = itemObject.optString("talk_back"),
                    cover_left_text_1 = itemObject.optString("cover_left_text_1"),
                    cover_left_icon_1 = itemObject.optInt("cover_left_icon_1"),
                    cover_left_1_content_description = itemObject.optString("cover_left_1_content_description"),
                    cover_left_text_2 = itemObject.optString("cover_left_text_2"),
                    cover_left_icon_2 = itemObject.optInt("cover_left_icon_2"),
                    cover_left_2_content_description = itemObject.optString("cover_left_2_content_description"),
                    cover_right_text = itemObject.optString("cover_right_text"),
                    cover_right_content_description = itemObject.optString("cover_right_content_description"),
                    // desc_button
                    desc_button = desc_button != null,
                    desc_button_text = desc_button?.optString("text"),
                    desc_button_uri = desc_button?.optString("uri"),
                    desc_button_event = desc_button?.optString("event"),
                    desc_button_type = desc_button?.optInt("type"),
                    // goto_icon
                    goto_icon = goto_icon != null,
                    goto_icon_icon_url = goto_icon?.optString("icon_url"),
                    goto_icon_icon_night_url = goto_icon?.optString("icon_night_url"),
                    goto_icon_icon_width = goto_icon?.optInt("icon_width"),
                    goto_icon_icon_height = goto_icon?.optInt("icon_height"),
                    // rcmd_reason_style
                    rcmd_reason_style = rcmd_reason_style != null,
                    rcmd_reason_style_text = rcmd_reason_style?.optString("text"),
                    rcmd_reason_style_text_color = rcmd_reason_style?.optString("text_color"),
                    rcmd_reason_style_bg_color = rcmd_reason_style?.optString("bg_color"),
                )
            ))
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