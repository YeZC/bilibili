package com.yzc.video


import com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS
import com.arthenica.mobileffmpeg.FFmpeg
import com.yzc.base.BiliCore
import com.yzc.base.util.logd
import com.yzc.base.util.loge
import com.yzc.base.util.logw
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

/**
 * https://github.com/tanersener/mobile-ffmpeg
 */
object BiliFFmpeg {

    private val TAG = BiliFFmpeg.javaClass.simpleName

    /**
     * ffmpeg -i (video.m4s) -i (audio.m4s) -c:v copy -strict experimental (output_name.mp4)
     */
    fun audioAndVideoSynthesis(video: String, audio: String, outputPath: String): Boolean{
//        var excutor = excutor("-i concat:\"${video}|${audio}\" -acodec copy -vcodec copy -f mp4 $outputPath")
        var excutor = excutor("-i $video -i $audio -c:v copy -strict experimental $outputPath")
//        GlobalScope.launch(Dispatchers.IO) {
//            val vDelete = File(video).delete()
//            val aDelete = File(audio).delete()
//            logd(TAG, "delete video:$vDelete audio:$aDelete")
//            logd(TAG, "delete video: audio:")
//        }
        return excutor
    }

    fun m4s2MP4(fileName: String, outputPath: String){
        excutor("-i $fileName -c copy -strict experimental $outputPath")
    }

    private fun excutor(cmd: String): Boolean {
        val rc: Int = FFmpeg.execute(cmd)
        when (rc) {
            RETURN_CODE_SUCCESS -> {
//                logd(TAG, "ffmpeg successfully.")
                return true
            }
//            RETURN_CODE_CANCEL -> {
//                logd(TAG, "excutor cancelled by user.")
//            }
            else -> {// and RETURN_CODE_CANCEL
                logw(TAG, "ffmpeg Command execution failed with rc=${rc} and the output below.")
                return false
            }
        }
    }

}