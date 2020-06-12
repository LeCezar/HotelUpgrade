package com.lecezar.hotelupgrade.models

import android.content.Context
import android.os.Parcelable
import androidx.annotation.ColorRes
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.utils.format_mm_DD_YY
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * The name property is also the room's document id but without spaces
 */
@Parcelize
data class Room(
    val name: String = "",
    val number: Int = -1,
    val floor: Int = -1,
    val occupationStatus: OccupationStatus = OccupationStatus.AVAILABLE,
    val cleaningStatus: CleaningStatus = CleaningStatus.CLEAN,
    val nextBookingInfo: Pair<String, Date>? = null,
    val id: String = ""
) : Parcelable {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to this.id,
            "name" to this.name,
            "number" to this.number,
            "floor" to this.floor,
            "occupationStatus" to this.occupationStatus.name,
            "cleaningStatus" to this.cleaningStatus.name
        )
    }

    @ColorRes
    fun getColorForOccupationStatus(context: Context): Int {
        return when (this.occupationStatus) {
            OccupationStatus.AVAILABLE -> {
                context.resources.getColor(R.color.ok_green, context.theme)

            }
            OccupationStatus.BOOKED_SOON -> {
                context.resources.getColor(R.color.maybe_yellow, context.theme)

            }
            OccupationStatus.OCCUPIED -> {
                context.resources.getColor(R.color.unavailable_red, context.theme)

            }
        }
    }

    fun getFormattedStartBookingDateString(context: Context): String {
        return this.nextBookingInfo?.second?.format_mm_DD_YY(context) ?: "Error formatting"
    }

    @ColorRes
    fun getColorForCleaningStatus(context: Context): Int {
        return when (this.cleaningStatus) {
            CleaningStatus.CLEAN -> {
                context.resources.getColor(R.color.ok_green, context.theme)
            }
            CleaningStatus.CLEANING -> {
                context.resources.getColor(R.color.maybe_yellow, context.theme)
            }
            CleaningStatus.NEEDS_CLEANING -> {
                context.resources.getColor(R.color.maybe_orange, context.theme)
            }
            CleaningStatus.DO_NOT_DISTURB -> {
                context.resources.getColor(R.color.unavailable_red, context.theme)
            }
        }
    }

    companion object {

        fun fromDocument(document: DocumentSnapshot): Room {
            val booking = if (document["nextBookingInfo"] != null) {
                val bookingId = (document["nextBookingInfo"] as Map<String, Any>).keys.first()
                val startDate = (document["nextBookingInfo"] as Map<String, Any>).values.first()
                if (startDate is Timestamp) {
                    bookingId to startDate.toDate()
                } else
                    null
            } else {
                null
            }

            return Room(
                document["name"].toString(),
                (document["number"] as Long).toInt(),
                (document["floor"] as Long).toInt(),
                OccupationStatus.getEnumFromString(document["occupationStatus"].toString()),
                CleaningStatus.getEnumFromString(document["cleaningStatus"].toString()),
                booking,
                document.id
            )
        }

        enum class OccupationStatus(val status: String) {
            OCCUPIED("OCCUPIED"),
            AVAILABLE("AVAILABLE"),
            BOOKED_SOON("BOOKED SOON");

            companion object {
                fun getEnumFromString(string: String): OccupationStatus {
                    return when (string) {
                        "OCCUPIED" -> OCCUPIED
                        "AVAILABLE" -> AVAILABLE
                        "BOOKED SOON" -> BOOKED_SOON
                        else -> BOOKED_SOON
                    }
                }
            }
        }

        enum class CleaningStatus(val status: String) {
            CLEAN("CLEAN"),
            CLEANING("CLEANING"),
            DO_NOT_DISTURB("DO NOT DISTURB"),
            NEEDS_CLEANING("NEEDS CLEANING");

            companion object {
                fun getEnumFromString(string: String): CleaningStatus {
                    return when (string) {
                        "CLEAN" -> CLEAN
                        "CLEANING" -> CLEANING
                        "DO NOT DISTURB" -> DO_NOT_DISTURB
                        "NEEDS CLEANING" -> NEEDS_CLEANING
                        else -> DO_NOT_DISTURB
                    }
                }
            }
        }
    }
}