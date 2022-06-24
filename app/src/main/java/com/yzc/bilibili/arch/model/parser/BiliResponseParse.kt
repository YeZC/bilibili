package com.yzc.bilibili.arch.model.parser

import com.yzc.base.network.BaseParse
import com.yzc.base.util.loge
import com.yzc.bilibili.arch.model.bean.*
import org.json.JSONObject

class BiliResponseParse: BaseParse(){
    companion object {
        private val TAG = BiliResponseParse::class.java.simpleName
        fun toBiliRecommend(json: JSONObject): MutableList<BiliRecommend> {
            var items: MutableList<BiliRecommend> = mutableListOf<BiliRecommend>()
            var itemArr = json.optJSONObject(DATA)?.optJSONArray(ITEMS)
            var itemObject: JSONObject? = null
            var size = itemArr?.length()?:0
            for(i in 0 until size){
                itemObject = itemArr?.optJSONObject(i)
                var cardType = itemObject?.optString("card_type")
                when(cardType){
                    "banner_v8" -> {
                        bannerItems(items, itemObject)
                    }
                    "small_cover_v2" -> {
                        smallCoverItems(items, itemObject!!)
                    }
                    "cm_v2" -> {
                        cmItems(items, itemObject!!)
                    }
                    else -> {
                        loge(TAG, "toBiliRecommend: [${cardType}] parse error")
                    }
                }
            }
            return items
        }



        private fun cmItems(items: MutableList<BiliRecommend>, itemObject: JSONObject) {
            var adInfo = itemObject.optJSONObject("ad_info")
            var creativeContent = adInfo?.optJSONObject("creative_content")
            var extra = adInfo?.optJSONObject("extra")
            var card = extra?.optJSONObject("card")
            var adTagStyle = card?.optJSONObject("ad_tag_style")
            var qualityInfos = card?.optJSONArray("quality_infos")

            var descButton = itemObject.optJSONObject("desc_button")

            items.add(
                BiliRecommend(
                card_type = itemObject.optString("card_type"),
                card_goto = itemObject.optString("card_goto"),
                cover = itemObject.optString("cover"),
                title = itemObject.optString("title"),
                uri = itemObject.optString("uri"),
                creative_style = itemObject.optInt("creative_style"),
                param = itemObject.optString("param"),
                cm = CM(
                    cover_left_text_1 = itemObject.optString("cover_left_text_1"),
                    goto = itemObject.optString("goto"),
                    cover_right_content_description = itemObject.optString("cover_right_content_description"),
                    cover_left_2_content_description = itemObject.optString("cover_left_2_content_description"),
                    uri = itemObject.optString("uri"),
                    cover_left_1_content_description = itemObject.optString("cover_left_1_content_description"),
                    ad_info = CM.AdInfo(
                        image_url = creativeContent?.optString("image_url"),
                        description = creativeContent?.optString("description"),
                        title = creativeContent?.optString("title"),
                        url = creativeContent?.optString("url"),
                        video_id = creativeContent?.optLong("video_id"),
                        extra_card_ad_tag_style_text_color = adTagStyle?.optString("text_color"),
                        extra_card_ad_tag_style_text = adTagStyle?.optString("text"),
                        quality_infos = mutableListOf<CM.QualityInfo>().apply {
                            for(i in 0 until (qualityInfos?.length()?:0)){
                                val qualityInfo = qualityInfos?.optJSONObject(i)
                                add(
                                    CM.QualityInfo(
                                    icon = qualityInfo?.optString("icon"),
                                    text = qualityInfo?.optString("text")
                                ))
                            }
                        },
                    ),
                    desc_button = CM.DescButton(
                        text = descButton?.optString("text"),
                        event = descButton?.optString("event"),
                        type = descButton?.optInt("type")
                    )
                )
            )
            )
        }

        private fun smallCoverItems(items: MutableList<BiliRecommend>, itemObject: JSONObject) {
            val desc_button = itemObject.optJSONObject("desc_button")
            val goto_icon = itemObject.optJSONObject("goto_icon")
            val rcmd_reason_style = itemObject.optJSONObject("rcmd_reason_style")
            items.add(
                BiliRecommend(
                card_type = itemObject.optString("card_type"),
                card_goto = itemObject.optString("card_goto"),
                cover = itemObject.optString("cover"),
                title = itemObject.optString("title"),
                uri = itemObject.optString("uri"),
                creative_style = itemObject.optInt("creative_style"),
                param = itemObject.optString("param"),
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
            )
            )
        }

        private fun bannerItems(items: MutableList<BiliRecommend>, itemObject: JSONObject?){
            val bannerItemArr = itemObject?.optJSONArray("banner_item")

            val bannerItems = mutableListOf<BannerItem>()
            val size = bannerItemArr?.length()?: 0
            for(i in 0 until size){
                val bannerJson = bannerItemArr?.getJSONObject(i)
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
            items.add(
                BiliRecommend(
                card_type = itemObject?.optString("card_type"),
                banner_item = bannerItems)
            )
        }

    }
}