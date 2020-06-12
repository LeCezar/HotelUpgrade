package com.lecezar.hotelupgrade.chooseHotelFeature

import androidx.lifecycle.MutableLiveData
import com.lecezar.hotelupgrade.repositories.HotelRepository
import com.lecezar.hotelupgrade.utils.base.BaseViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

class AddHotelVM : BaseViewModel(), KoinComponent {

    private val hotelRepository: HotelRepository by inject()
    val hotelName = MutableLiveData<String>()
    val roomsStartNumber = MutableLiveData<String>()
    val roomsEndNumber = MutableLiveData<String>()

    fun addHotel() {
//        val roomsStart = roomsStartNumber.value ?: ""
//        val roomsEnd = roomsEndNumber.value ?: ""
//        if (roomsStart.isNotBlank() && roomsEnd.isNotBlank()) {
//            if (roomsStart.toInt() < roomsEnd.toInt()) {
//                //TODO add hotel with rooms
//            } else {
//                //TODO warn wrong room numbers
//            }
//        }

        hotelName.value?.apply {
            if (this.isNotBlank())
                hotelRepository.addHotel(this) {
                    onSuccess = {

                    }
                    onFailure = {

                    }
                }
        }
    }

}