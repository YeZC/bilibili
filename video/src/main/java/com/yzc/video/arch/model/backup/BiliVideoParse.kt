package com.yzc.video.arch.model.backup

import com.yzc.base.ktexpand.getUriValue
import com.yzc.base.network.BaseParse
import com.yzc.video.arch.model.backup.bean.BiliVideoDetail
import com.yzc.video.arch.model.backup.bean.BiliVideoFlow
import org.json.JSONObject
import java.net.URLDecoder

class BiliVideoParse: BaseParse(){
    companion object{
        private val TAG = BiliVideoParse::class.java.simpleName
        fun toBiliVideoFlow(responseJson: JSONObject): MutableList<BiliVideoFlow> {
            val items = mutableListOf<BiliVideoFlow>()
            val jsonArray = responseJson.optJSONObject(DATA)?.optJSONArray(ITEMS)
            val len = jsonArray?.length()?: 0
            for(i in 0 until len){
                var itemObject = jsonArray!!.optJSONObject(i)
                items.add(BiliVideoFlow(
                    card_goto = itemObject.optString(CARD_GOTO),
                    goto = itemObject.optString(GOTO),
                    param = itemObject.optString(PARAM),
                    cover = itemObject.optString(COVER),
                    ff_cover = itemObject.optString(FF_COVER),
                    title = itemObject.optString(TITLE),
                    uri = itemObject.optString(URI),
                ).apply {
                    if(uri?.isNotEmpty()!!){
                        val playJson = JSONObject(URLDecoder.decode(uri).getUriValue("player_preload"))
                        val dash = playJson?.optJSONObject("dash")

                        val videos = mutableListOf<BiliVideoDetail>()
                        dash?.optJSONArray("video")?.apply {
                            for(i in 0 until length()){
                                optJSONObject(i)?.let {
                                    videos.add(BiliVideoDetail(
                                        id = it?.optInt("id"),
                                        base_url = it?.optString("base_url"),
                                        bandwidth = it?.optInt("bandwidth"),
                                        codecid = it?.optInt("codecid"),
                                        size = it?.optLong("size"),
                                        frame_rate = it?.optString("frame_rate")
                                    ))
                                }
                            }
                        }
                        var audios = mutableListOf<BiliVideoDetail>()
                        dash?.optJSONArray("audio")?.apply {
                            for(i in 0 until length()){
                                optJSONObject(i)?.let {
                                    audios.add(BiliVideoDetail(
                                        id = it?.optInt("id"),
                                        base_url = it?.optString("base_url"),
                                        bandwidth = it?.optInt("bandwidth"),
                                        codecid = it?.optInt("codecid"),
                                        size = it?.optLong("size"),
                                        frame_rate = it?.optString("frame_rate")
                                    ))
                                }
                            }
                        }
                        this.videos = videos
                        this.audios = audios
                    }
                })
            }
            return items
        }
    }
}