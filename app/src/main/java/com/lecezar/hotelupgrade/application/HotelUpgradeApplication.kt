package com.lecezar.hotelupgrade.application

import android.app.Application
import org.koin.core.context.startKoin

class HotelUpgradeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(KoinModules.modules)
        }
    }
}