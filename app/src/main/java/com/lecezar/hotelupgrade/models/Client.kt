package com.lecezar.hotelupgrade.models

data class Client(
    val id: String,
    val name: String,
    val phoneNumber: String
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "name" to name,
            "phoneNumber" to phoneNumber
        )
    }
}