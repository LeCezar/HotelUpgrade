package com.lecezar.hotelupgrade.chooseHotelFeature

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.lecezar.hotelupgrade.models.Hotel
import com.lecezar.hotelupgrade.repositories.HotelRepository
import com.lecezar.hotelupgrade.utils.base.BaseViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

class ChooseHotelVM : BaseViewModel(), KoinComponent {
    private val hotelRepository: HotelRepository by inject()
    val hotelList = MutableLiveData<List<Hotel>>()

    fun checkForHotels() {
        hotelRepository.getAllHotels {
            onSuccess = {
                hotelList.value = it
                Log.d("Hotels:", hotelList.value?.size.toString())
            }
            onFailure = {}
        }
    }
}