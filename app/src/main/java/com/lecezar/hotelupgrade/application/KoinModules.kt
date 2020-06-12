package com.lecezar.hotelupgrade.application

import com.lecezar.hotelupgrade.MainActivityVM
import com.lecezar.hotelupgrade.bookingFeature.BookingsFragmentVM
import com.lecezar.hotelupgrade.bookingFeature.addbooking.AddBookingFragmentVM
import com.lecezar.hotelupgrade.bookingFeature.bookingtabs.BookingsTabFragmentVM
import com.lecezar.hotelupgrade.bookingFeature.bookingtabs.ClientsTabFragmentVM
import com.lecezar.hotelupgrade.chooseHotelFeature.AddHotelVM
import com.lecezar.hotelupgrade.chooseHotelFeature.ChooseHotelVM
import com.lecezar.hotelupgrade.eventsFeature.EventsFragmentVM
import com.lecezar.hotelupgrade.loginFeature.LoginFragmentVM
import com.lecezar.hotelupgrade.nfcFeature.NfcFragmentVM
import com.lecezar.hotelupgrade.repositories.*
import com.lecezar.hotelupgrade.roomsFeature.RoomsFragmentVM
import com.lecezar.hotelupgrade.roomsFeature.addrooms.AddRoomFragmentVM
import com.lecezar.hotelupgrade.roomsFeature.roomdetails.RoomDetailsFragmentVM
import com.lecezar.hotelupgrade.splashScreen.SplashFragmentVM
import com.lecezar.hotelupgrade.utils.binding.FirebaseListenerManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object KoinModules {
    private val viewModels = module {
        viewModel { MainActivityVM() }
        viewModel { SplashFragmentVM() }
        viewModel { LoginFragmentVM() }
        viewModel { ChooseHotelVM() }
        viewModel { AddHotelVM() }
        viewModel { EventsFragmentVM() }
        viewModel { RoomsFragmentVM() }
        viewModel { NfcFragmentVM() }
        viewModel { AddRoomFragmentVM() }
        viewModel { RoomDetailsFragmentVM() }
        viewModel { BookingsFragmentVM() }
        viewModel { AddBookingFragmentVM() }
        viewModel { ClientsTabFragmentVM() }
        viewModel { BookingsTabFragmentVM() }
    }

    private val apiModule = module {
    }

    private val repoModule = module {
        single { HotelRepository() }
        single { FirebaseListenerManager() }
        single { RoomRepository() }
        single { BookingRepository() }
        single { ClientRepository() }
        single { EventRepository() }
    }


    val modules = listOf(viewModels, apiModule, repoModule)
}