package com.lecezar.hotelupgrade.models

data class RoomsInterval(
    val startNumber: Int,
    val endNumber: Int,
    val floor: Int?
) {
    override fun toString(): String {
        return if (floor != null) {
            val startRoomString = "${floor * 100 + startNumber}"
            val endNumberString = "${floor * 100 + endNumber}"
            "Rooms $startRoomString-$endNumberString will be added."
        } else {
            "Rooms $startNumber-$endNumber will be added."
        }
    }
}