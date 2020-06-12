package com.lecezar.hotelupgrade.models

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Booking(
    val id: String,
    val clientIdName: Pair<String, String>,
    val startDate: Date,
    val endDate: Date,
    val rooms: Map<String, String>
) : Parcelable {

    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "clientIdName" to mapOf(clientIdName.first to clientIdName.second),
            "startDate" to Timestamp(startDate),
            "endDate" to Timestamp(endDate),
            "rooms" to rooms
        )
    }

    companion object {
        fun fromDocument(document: DocumentSnapshot): Booking {
            val clientMap = (document["clientIdName"] as Map<String, String>)
            val clientPair = clientMap.keys.first() to clientMap.values.first()
            return Booking(
                document["id"].toString(),
                clientPair,
                (document["startDate"] as Timestamp).toDate(),
                (document["endDate"] as Timestamp).toDate(),
                (document["rooms"] as Map<String, String>)
            )
        }
    }
}