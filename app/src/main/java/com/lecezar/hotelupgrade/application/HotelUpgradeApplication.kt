package com.lecezar.hotelupgrade.application

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.core.context.startKoin

class HotelUpgradeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(KoinModules.modules)
        }
        AndroidThreeTen.init(this)
    }
}