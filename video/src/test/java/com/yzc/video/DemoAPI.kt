package com.yzc.video

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val videotest: String = "/upgcxcode/71/09/730580971/730580971_x1-1-31101.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEuENvNC8aNEVEtEvE9IMvXBvE2ENvNCImNEVEIj0Y2J_aug859r1qXg8gNEVE5XREto8z5JZC2X2gkX5L5F1eTX1jkXlsTXHeux_f2o859IB_&uipk=5&nbs=1&deadline=1655814959&gen=playurlv2&os=bcache&oi=2633568537&trid=0000313bcd33a37d45ae80ff459677f7226bO&mid=0&platform=iphone&upsig=a0fa507015f456e83fbb33d51293132b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&cdnid=9597&bvc=vod&nettype=1&orderid=0,2&bw=104616&logo=80000000"

interface DemoAPI {

    @GET(videotest)
    fun videoFile(): Call<ResponseBody>

}