package com.yzc.bilibili.backup.bean

class BiliRecommend{
    // index 0
    constructor(card_type: String? = null, banner_item: MutableList<BannerItem>? = null) {
        this.card_type = card_type
        this.banner_item = banner_item
    }

    // universal
    var card_type: String? = null

    // type banner
    var banner_item: MutableList<BannerItem>? = null

    // small_cover_v2
    var cover: String? = null
    var title: String? = null
    var uri: String? = null
}

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


class ThreePoint(

)

//{
//    "card_type": "banner_v8",
//    "card_goto": "banner",
//    "args": {},
//    "idx": 1605789194,
//    "track_id": "all_27.shjd-ai-recsys-74.1653895262509.787",
//    "hash": "15910663339887044658",
//    "banner_item": [
//    {
//        "type": "static",
//        "resource_id": 4332,
//        "id": 1000649,
//        "index": 1,
//        "static_banner": {
//        "id": 1000649,
//        "title": "我的帥氣女友！",
//        "image": "http://i0.hdslb.com/bfs/banner/fa700e0c660e4d2a3203d3cc8625b7e375360aa9.png",
//        "hash": "8a5ba1aedab7e2ea485fea22d2124aa0",
//        "uri": "https://www.bilibili.com/bangumi/play/ep475917?goto=static_banner",
//        "request_id": "1653895262693q172a27a2a37q5088",
//        "src_id": 4333,
//        "is_ad_loc": true,
//        "client_ip": "156.249.17.25",
//        "server_type": 0,
//        "resource_id": 4332,
//        "index": 1,
//        "cm_mark": 0
//    }
//    },
//    {
//        "type": "static",
//        "resource_id": 4332,
//        "id": 1000731,
//        "index": 2,
//        "static_banner": {
//        "id": 1000731,
//        "title": "跟着王心凌一起跳爱你！",
//        "image": "http://i0.hdslb.com/bfs/banner/dc91ff69beea43ff7de4211594bc2f5cdfdf5a40.jpg",
//        "hash": "e77218cd83c3f298c5b8456825e1da19",
//        "uri": "https://www.bilibili.com/blackboard/dynamic/308011",
//        "request_id": "1653895262693q172a27a2a37q5088",
//        "src_id": 4334,
//        "is_ad_loc": true,
//        "client_ip": "156.249.17.25",
//        "server_type": 0,
//        "resource_id": 4332,
//        "index": 2,
//        "cm_mark": 0
//    }
//    },
//    {
//        "type": "static",
//        "resource_id": 4332,
//        "id": 1000326,
//        "index": 3,
//        "static_banner": {
//        "id": 1000326,
//        "title": "全员性格大揭秘，深挖不为人知另一面？",
//        "image": "http://i0.hdslb.com/bfs/banner/dd0dbd3ca95ec47a1950d7651118fd3a875d5fd9.png",
//        "hash": "77c31d08d693ccdfdc8cffcbef8140f3",
//        "uri": "https://www.bilibili.com/bangumi/play/ep513940?goto=static_banner",
//        "request_id": "1653895262693q172a27a2a37q5088",
//        "src_id": 4335,
//        "is_ad_loc": true,
//        "client_ip": "156.249.17.25",
//        "server_type": 0,
//        "resource_id": 4332,
//        "index": 3,
//        "cm_mark": 0
//    }
//    }
//    ]
//}