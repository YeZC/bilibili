package com.yzc.video.bean

import android.os.Parcel
import android.os.Parcelable
import com.yzc.video.arch.model.backup.bean.BiliVideoDetailResponse

class BiliVideoEntry(
    val aid: String?,
    val videos: List<BiliVideoDetailResponse>?,
    val audios: List<BiliVideoDetailResponse>?,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.createTypedArrayList(BiliVideoDetailResponse),
        parcel.createTypedArrayList(BiliVideoDetailResponse)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(aid)
        parcel.writeTypedList(videos)
        parcel.writeTypedList(audios)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BiliVideoEntry> {
        override fun createFromParcel(parcel: Parcel): BiliVideoEntry {
            return BiliVideoEntry(parcel)
        }

        override fun newArray(size: Int): Array<BiliVideoEntry?> {
            return arrayOfNulls(size)
        }
    }

}