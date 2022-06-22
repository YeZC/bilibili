package com.yzc.video


import com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS
import com.arthenica.mobileffmpeg.FFmpeg
import com.yzc.base.BiliCore
import com.yzc.base.util.logd
import com.yzc.base.util.loge

/**
 * https://github.com/tanersener/mobile-ffmpeg
 */
object BiliFFmpeg {

    private val TAG = BiliFFmpeg.javaClass.simpleName

    /**
     * ffmpeg -i (video.m4s) -i (audio.m4s) -c:v copy -strict experimental (output_name.mp4)
     */
    fun audioAndVideoSynthesis(video: String, audio: String, outputPath: String){
        excutor("-i $video -i $audio -c:v copy -strict experimental $outputPath.mp4")
    }

    fun m4s2MP4(fileName: String, outputPath: String){
        excutor("-i $fileName -c copy -strict experimental $outputPath.mp4")
    }

    private fun excutor(cmd: String) {
        val rc: Int = FFmpeg.execute(cmd)
        when (rc) {
            RETURN_CODE_SUCCESS -> {
                logd(TAG, "excutor successfully.")
            }
            RETURN_CODE_CANCEL -> {
                logd(TAG, "excutor cancelled by user.")
            }
            else -> {
                loge(TAG, "Command execution failed with rc=${rc} and the output below.")
            }
        }
    }

}