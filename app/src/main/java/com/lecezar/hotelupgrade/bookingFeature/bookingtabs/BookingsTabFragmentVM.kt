package com.lecezar.hotelupgrade.bookingFeature.bookingtabs

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.lecezar.hotelupgrade.models.Booking
import com.lecezar.hotelupgrade.repositories.BookingRepository
import com.lecezar.hotelupgrade.utils.base.BaseViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

class BookingsTabFragmentVM : BaseViewModel(), KoinComponent {
    private val bookingRepository: BookingRepository by inject()

    val bookingsList = MutableLiveData<List<Booking>>()

    fun subscribeListenerForPastBookings(lifecycleOwner: LifecycleOwner) {
        bookingRepository.subscribeListenerForPastBookings(lifecycleOwner) {
            onSuccess = {
                bookingsList.value = it
            }
            onFailure = {

            }
        }
    }

    fun subscribeListenerForUpcomingBookings(lifecycleOwner: LifecycleOwner) {
        bookingRepository.subscribeListenerForUpcomingBookings(lifecycleOwner) {
            onSuccess = {
                bookingsList.value = it
            }
            onFailure = {

            }
        }
    }
}