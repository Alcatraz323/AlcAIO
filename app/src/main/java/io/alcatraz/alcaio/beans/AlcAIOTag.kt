package io.alcatraz.alcaio.beans

import android.os.Parcel
import android.os.Parcelable
import android.util.Base64
import io.alcatraz.alcaio.LogBuff
import java.lang.Exception

/*
*   Eg. data structure
*       AlcAIO1/4f:5d:8e:7c:9a:fe/12/$192.168.155.216:65535$/wireless_charge;charge_info;usb_transponder
 */

data class AlcAIOTag(
    val rawLink: String?,
    val name: String?,
    val mac: String?,
    val api: String?,
    val remoteAddress: String?,
    val createFromSupported: Boolean?,
    val functions: List<String>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt() == 1,
        parcel.createStringArrayList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(rawLink)
        parcel.writeString(name)
        parcel.writeString(mac)
        parcel.writeString(api)
        parcel.writeString(remoteAddress)
        if (createFromSupported!!)
            parcel.writeInt(1)
        else
            parcel.writeInt(0)
        parcel.writeStringList(functions)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun convertToLink(): String {
        var link = "alcaio://"
        var functionsInline = ""
        functions?.forEachIndexed { index, s ->
            functionsInline += s
            if (index != functions.size - 1)
                functionsInline += ";"
        }
        val dataSector = "$name/$mac/$api/$$remoteAddress$/$functionsInline"
        LogBuff.D("converter: dataSector: $dataSector")
        val encodedData = Base64.encodeToString(dataSector.toByteArray(), Base64.DEFAULT)
        link += encodedData
        return link
    }

    companion object CREATOR : Parcelable.Creator<AlcAIOTag> {
        override fun createFromParcel(parcel: Parcel): AlcAIOTag {
            return AlcAIOTag(parcel)
        }

        override fun newArray(size: Int): Array<AlcAIOTag?> {
            return arrayOfNulls(size)
        }

        fun createFromLink(link: String): AlcAIOTag {
            if (link.startsWith("alcaio://")) {
                val linkSplit = link.split("://")
                try {
                    val decode = String(Base64.decode(linkSplit[1], Base64.DEFAULT))
                    LogBuff.D("decoder: $decode")
                    val dataSectors = decode.split("/")
                    return AlcAIOTag(
                        link,
                        dataSectors[0],
                        dataSectors[1],
                        dataSectors[2],
                        dataSectors[3].replace("$", ""),
                        true,
                        dataSectors[4].split(";")
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    LogBuff.E("Incomplete link: $link")
                }
            }

            return AlcAIOTag("", "", "", "", "", false, arrayListOf())
        }
    }
}