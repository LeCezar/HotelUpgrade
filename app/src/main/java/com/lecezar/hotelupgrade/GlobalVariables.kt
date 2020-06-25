package com.lecezar.hotelupgrade

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import com.google.firebase.crashlytics.FirebaseCrashlytics

object GlobalVariables {
    var currentHotelId: ObservableField<String?> = ObservableField("")
    var currentUserId: ObservableField<String?> = ObservableField("")

    private val crashalitycs = FirebaseCrashlytics.getInstance()

    init {
        currentUserId.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                crashalitycs.setUserId((sender as ObservableField<String>).get() ?: "No User?")
            }
        })
    }
}