package com.yzc.video.backup

import com.yzc.base.BiliCore
import com.yzc.base.ktexpand.getBaseUrl
import com.yzc.base.ktexpand.getQueryMap
import com.yzc.base.network.BiliNet
import com.yzc.base.network.BiliRetrofit
import com.yzc.base.util.logd
import com.yzc.base.util.loge
import com.yzc.base.util.logw
import com.yzc.video.BiliFFmpeg
import com.yzc.video.backup.bean.BiliVideoFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

    fun getFilePair(urlStr: String): Pair<Int, String> {
        val url = URL(urlStr)
        val baseUrl = url.getBaseUrl()
        var retrofit = Retrofit.Builder().baseUrl(baseUrl).build()
        var service = retrofit.create(BiliVideoAPI::class.java)
        val response = service.getFileSize(url.path, url.query.getQueryMap()).execute()
        val first = Integer.parseInt(response.headers()["Content-Length"]?: "0")
//        val second = (response!!.headers()["Content-Type"]?.toString()?: "m4s").let {
//            it.substring(it.indexOf('/') + 1, it.length)
//        }
//        logd(TAG, "getFilePair: (${first},${second})")
        return Pair(first, "ts")
//        return Pair(first, second)
    }

    fun download(fileName: String, urlStr: String, range: Int = 0, step: Int = 500_000): String {
        var filePath = ""
        val url = URL(urlStr)
        val baseUrl = url.getBaseUrl()
        var response: Response<ResponseBody>? = null
        try{
            var retrofit = Retrofit.Builder().baseUrl(baseUrl).build()
            var service = retrofit.create(BiliVideoAPI::class.java)
            response = service.getVideoFlow(url.path, "bytes=${range}-${step}", url.query.getQueryMap()).execute()
        }catch (e: Exception){
            loge(TAG, "${e.message}")
        }
        if(response?.code().toString().startsWith("2")){
//            println("download success bytes=${range}-${step}")
            val byteStream = response?.body()?.byteStream()
            byteStream?.let { inputStream ->
                val cacheFile = File(BiliCore.App().cacheDir, fileName)
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