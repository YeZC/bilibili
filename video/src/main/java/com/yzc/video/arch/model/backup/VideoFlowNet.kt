package com.yzc.video.arch.model.backup

import com.yzc.base.ktexpand.getBaseUrl
import com.yzc.base.ktexpand.getQueryMap
import com.yzc.base.network.BiliNet
import com.yzc.base.network.BiliRetrofit
import com.yzc.base.util.logd
import com.yzc.base.util.loge
import com.yzc.base.util.logw
import com.yzc.video.Constants
import com.yzc.video.arch.model.backup.bean.BiliVideoFlow
import okhttp3.ResponseBody
import okio.BufferedSink
import okio.Okio
import okio.Sink
import okio.Source
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.net.URL

class VideoFlowNet: BiliNet {

    private val TAG = VideoFlowNet::class.java.simpleName
    val service by lazy { BiliRetrofit.getBiliService(BiliVideoAPI::class.java) }

    fun detailVideo(aid: String): MutableList<BiliVideoFlow> {
        var response = service.detailVideo(aid).execute()
        val responseJson = JSONObject(response.body()?.string()?: "")
        val resPair = resPair(responseJson)
        logd(TAG, "getFeed (code,message):$resPair")

        val videos = BiliVideoParse.toBiliVideoFlow(responseJson)
        return videos
    }

    fun getFilePair(urlStr: String, filetype: String = "ts"): Pair<Int, String> {
        URL(urlStr).let {
            val dynamicService = BiliRetrofit.getDynamicService(it.getBaseUrl(), BiliVideoAPI::class.java)
            val response = dynamicService.getFileSize(it.path, it.query.getQueryMap()).execute()
            val first = Integer.parseInt(response.headers()["Content-Length"]?: "0")
    //        val second = (response!!.headers()["Content-Type"]?.toString()?: "m4s").let {
    //            it.substring(it.indexOf('/') + 1, it.length)
    //        }
    //        logd(TAG, "getFilePair: (${first},${second})")
            return Pair(first, filetype)// default video ts
        }
    }

    fun download(fileName: String, urlStr: String, range: Int, step: Int): String {
        var filePath = ""
        var response: Response<ResponseBody>? = null
        try{
            URL(urlStr).let {
                val service = BiliRetrofit.getDynamicService(it.getBaseUrl(), BiliVideoAPI::class.java)
                response = service.getVideoFlow(it.path, "bytes=${range}-${step}", it.query.getQueryMap()).execute()
            }
        }catch (e: Exception){
            loge(TAG, "${e.message}")
        }
        if(response?.code().toString().startsWith("2")){
//            println("download success bytes=${range}-${step}")
            val byteStream = response?.body()?.byteStream()
            byteStream?.let { inputStream ->
                val cacheFile = File(Constants.videoCachePath(), fileName)
                filePath = cacheFile.path

                var source: Source? = null
                var sink: Sink? = null
                var bufferedSink: BufferedSink? = null
                try{
                    source = Okio.source(inputStream)
                    sink = Okio.appendingSink(cacheFile)
                    bufferedSink = Okio.buffer(sink!!)
                    bufferedSink.writeAll(source!!)
                    bufferedSink.flush()
//                    logd(TAG, "download $fileName ${cacheFile.path} filesize: ${cacheFile.length()}")
                }catch (e: Exception){
                    e.printStackTrace()
                }finally {
                    bufferedSink?.close()
                    sink?.close()
                    source?.close()
                }
            }
        }else{
            logw(TAG, "download error ${response?.code()}, ${response?.message()}")
        }
        return filePath
    }

}