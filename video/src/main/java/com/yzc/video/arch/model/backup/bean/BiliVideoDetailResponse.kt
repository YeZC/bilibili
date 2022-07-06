package com.yzc.video.arch.model.backup.bean

import android.os.Parcel
import android.os.Parcelable

class BiliVideoDetailResponse(
    val id: Int? = 0,
    val base_url: String? = "",
    val bandwidth: Int = 0,
    val codecid: Int = 0,
    val size: Long = 0,
//    val backup_url: List<String> = listOf(),
    val frame_rate: String? = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(base_url)
        parcel.writeInt(bandwidth)
        parcel.writeInt(codecid)
        parcel.writeLong(size)
        parcel.writeString(frame_rate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BiliVideoDetailResponse> {
        override fun createFromParcel(parcel: Parcel): BiliVideoDetailResponse {
            return BiliVideoDetailResponse(parcel)
        }

        override fun newArray(size: Int): Array<BiliVideoDetailResponse?> {
            return arrayOfNulls(size)
        }
    }
}
