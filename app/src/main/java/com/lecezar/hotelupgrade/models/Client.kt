package com.lecezar.hotelupgrade.models

import com.google.firebase.firestore.DocumentSnapshot

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

    companion object {
        fun fromDocument(document: DocumentSnapshot): Client {
            return Client(
                document["id"].toString(),
                document["name"].toString(),
                document["phoneNumber"].toString()
            )
        }
    }
}