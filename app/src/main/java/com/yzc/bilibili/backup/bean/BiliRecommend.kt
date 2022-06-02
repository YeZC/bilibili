package com.yzc.bilibili.backup.bean

class BiliRecommend{
    // index 0
    constructor(card_type: String? = null, banner_item: MutableList<BannerItem>? = null) {
        this.card_type = card_type
        this.banner_item = banner_item
    }

    constructor(card_type: String? = null, cover: String? = null, title: String? = null, uri: String? = null, small_cover: SmallCover? = null) {
        this.card_type = card_type
        this.cover = cover
        this.title = title
        this.uri = uri
        this.small_cover = small_cover
    }

    constructor(card_type: String? = null, cm: CM? = null) {
        this.card_type = card_type
        this.cm = cm
    }

    // universal
    var card_type: String? = null

    // type banner
    var banner_item: MutableList<BannerItem>? = null

    // small_cover_v2
    var cover: String? = null
    var title: String? = null
    var uri: String? = null
    var small_cover: SmallCover? = null

    // cm_v2
    var cm: CM? = null

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
    val text: String? = null,
    val uri: String? = null,
    val event: String? = null,
    val type: Int? = null,
    // goto_icon
    val icon_url: String? = null,
    val icon_night_url: String? = null,
    val icon_width: Int? = null,
    val icon_height: Int? = null,
)

/**
 * cm_v2
 */
class CM(
    val cover_left_text_1: String? = null,
    val title: String? = null,
    val cover: String? = null,
    val goto: String? = null,
    val cover_right_content_description: String? = null,
    val cover_left_2_content_description: String? = null,
    val uri: String? = null,
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
        val video_id: Long? = null
    )
    // desc_button
    class DescButton(
        val text: String? = null,
        val event: String? = null,
        val type: Int? = null
    )
}