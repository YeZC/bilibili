package com.yzc.video.backup

import com.yzc.base.BiliCore
import com.yzc.base.ktexpand.getBaseUrl
import com.yzc.base.ktexpand.getQueryMap
import com.yzc.base.network.BiliNet
import com.yzc.base.network.BiliRetrofit
import com.yzc.base.util.logd
import com.yzc.base.util.loge
import com.yzc.video.backup.bean.BiliVideoFlow
import okhttp3.ResponseBody
import okio.BufferedSink
import okio.Okio
import okio.Sink
import okio.Source
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.lang.Exception
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

    fun download(startWith: String, urlStr: String): String {
        var filePath = ""
        val url = URL(urlStr)
        val baseUrl = url.getBaseUrl()
        var response: Response<ResponseBody>? = null
        try{
            var retrofit = Retrofit.Builder().baseUrl(baseUrl).build()
            var service = retrofit.create(BiliVideoAPI::class.java)
            response = service.fetchRes(url.path, url.query.getQueryMap()).execute()
        }catch (e: Exception){
            loge(TAG, "${e.message}")
        }
        if(response?.code() == SUCCESS_CODE){
            var byteStream = response.body()?.byteStream()
            byteStream?.let { inputStream ->
                val fileType = (response.headers()["Content-Type"]?.toString()?: "m4s").let {
                    it.substring(it.indexOf('/') + 1, it.length)
                }
                val cacheFile = File(
                    BiliCore.App().cacheDir,
                    "${startWith}${System.currentTimeMillis()}.${fileType}"
                )
                filePath = cacheFile.path

                var source: Source? = null
                var sink: Sink? = null
                var bufferedSink: BufferedSink? = null
                try{
                    source = Okio.source(inputStream)
                    sink = Okio.sink(cacheFile)
                    bufferedSink = Okio.buffer(sink!!)
                    bufferedSink.writeAll(source!!)
                    bufferedSink.flush()
                    logd(TAG, "download $startWith ${cacheFile.path}")
                }catch (e: Exception){
                    e.printStackTrace()
                }finally {
                    bufferedSink?.close()
                    sink?.close()
                    source?.close()
                }
            }
        }else{
            loge(TAG, "download error ${response?.code()}, ${response?.message()}")
        }
        return filePath
    }

}