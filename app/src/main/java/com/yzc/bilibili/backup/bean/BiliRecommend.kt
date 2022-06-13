package com.yzc.bilibili.backup.bean

class BiliRecommend{

    constructor(card_type: String? = null,
                card_goto: String? = null,
                cover: String? = null,
                title: String? = null,
                uri: String? = null,
                creative_style: Int = -1,
                small_cover: SmallCover? = null,
                banner_item: MutableList<BannerItem>? = null,
                cm: CM? = null) {
        this.card_type = card_type
        this.card_goto = card_goto
        this.cover = cover
        this.title = title
        this.uri = uri
        this.small_cover = small_cover
        this.banner_item = banner_item
        this.cm = cm
    }

    // universal
    var card_type: String? = null
    var card_goto: String? = null
    var cover: String? = null
    var title: String? = null
    var uri: String? = null
    var creative_style: Int = -1

    // banner_v8={banner}
    var banner_item: MutableList<BannerItem>? = null
    // small_cover_v2={av,}
    var small_cover: SmallCover? = null
    // cm_v2={ad_web_s}
    var cm: CM? = null

}

enum class RecommendVH(val cardType: String, val type: Int){
    BANNER("banner_v8", 0),
    SMALL_COVER("small_cover_v2", 1),
    CM("cm_v2", 2),
    EMPTY("empty", 3)
}

/**
 * banner
 */
class BannerItem(
    val type: String? = null,
    val index: Int? = null,
    val static_banner: StaticBanner? = null
){
    class StaticBanner(
        val title: String,
        val image: String,
        val uri: String,
    )
}

/**
 * small_cover_v2
 */
class SmallCover(
    val talk_back: String? = null,
    val cover_left_text_1: String? = null,
    val cover_left_icon_1: Int? = null,
    val cover_left_1_content_description: String? = null,
    val cover_left_text_2: String? = null,
    val cover_left_icon_2: Int? = null,
    val cover_left_2_content_description: String? = null,
    val cover_right_text: String? = null,
    val cover_right_content_description: String? = null,
    // desc_button
    val desc_button: Boolean = false,
    val desc_button_text: String? = null,
    val desc_button_uri: String? = null,
    val desc_button_event: String? = null,
    val desc_button_type: Int? = null,
    // goto_icon
    val goto_icon: Boolean = false,
    val goto_icon_icon_url: String? = null,
    val goto_icon_icon_night_url: String? = null,
    val goto_icon_icon_width: Int? = null,
    val goto_icon_icon_height: Int? = null,
    // rcmd_reason_style
    val rcmd_reason_style: Boolean = false,
    val rcmd_reason_style_text: String? = null,
    val rcmd_reason_style_text_color: String? = null,
    val rcmd_reason_style_bg_color: String? = null
)

/**
 * cm_v2
 */
class CM(
    // universal
    val goto: String? = null,
    val uri: String? = null,
    val cover_left_text_1: String? = null,
    val cover_right_content_description: String? = null,
    val cover_left_2_content_description: String? = null,
    val cover_left_1_content_description: String? = null,
    val ad_info: AdInfo? = null,
    val desc_button: DescButton? = null,
){
    // ad_info
    class AdInfo(
        // creative_content
        val image_url: String? = null,
        val description: String? = null,
        val title: String? = null,
        val url: String? = null,
        val video_id: Long? = null,
        // extra
            // card
                // ad_tag_style
                val extra_card_ad_tag_style_text_color: String? = null,
                val extra_card_ad_tag_style_text: String? = null,
                // quality_infos(array)
                val quality_infos: MutableList<QualityInfo>? = null

    )
    // desc_button
    class DescButton(
        val text: String? = null,
        val event: String? = null,
        val type: Int? = null
    )
    // ad_info extra card
    class QualityInfo(
        val icon: String? = null,
        val text: String? = null
    )
}