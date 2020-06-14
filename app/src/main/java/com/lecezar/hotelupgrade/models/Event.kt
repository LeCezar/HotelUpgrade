package com.lecezar.hotelupgrade.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.lecezar.hotelupgrade.utils.getDayAsInt
import com.lecezar.hotelupgrade.utils.getMonthAsInt
import com.lecezar.hotelupgrade.utils.getYearAsInt
import org.threeten.bp.LocalDate
import java.util.*

data class Event(
    val eventId: String,
    val bookingId: String,
    val isDismissed: Boolean,
    val roomsAffected: Map<String, String>,
    val triggerDate: Date,
    val eventType: EventType
) {

    fun triggerDateToLocalDate():LocalDate{
        return LocalDate.of(this.triggerDate.getYearAsInt(),this.triggerDate.getMonthAsInt(),this.triggerDate.getDayAsInt())
    }

    companion object {

        fun fromDocument(document: DocumentSnapshot): Event {
            return Event(
                document["eventId"].toString(),
                document["bookingId"].toString(),
                (document["isDismissed"] as Boolean),
                (document["roomsAffected"] as Map<String, String>),
                (document["triggerDate"] as Timestamp).toDate(),
                EventType.getEnumFromString(document["eventType"].toString())
            )
        }

        enum class EventType(val eventType: String) {
            CHECKIN("CHECKIN"),
            CHECKOUT("CHECKOUT");

            companion object {
                fun getEnumFromString(enumType: String): EventType {
                    return when (enumType) {
                        "CHECKIN" -> EventType.CHECKIN
                        "CHECKOUT" -> EventType.CHECKOUT
                        else -> EventType.CHECKIN
                    }
                }
            }
        }
    }
}