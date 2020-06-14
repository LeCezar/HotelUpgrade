package com.lecezar.hotelupgrade.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.Timestamp
import com.google.firebase.functions.FirebaseFunctions
import java.util.*

class UpdateRoomStatuses(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val currentTimestamp = Timestamp(Calendar.getInstance().time)
        val data = mapOf(
            "hotelId" to inputData.getString("HOTEL_ID"),
            "currentSeconds" to currentTimestamp.seconds,
            "currentNano" to currentTimestamp.nanoseconds
        )
        FirebaseFunctions.getInstance("europe-west3").getHttpsCallable("updateRoomsStatuses").call(data)
        return Result.success()
    }
}