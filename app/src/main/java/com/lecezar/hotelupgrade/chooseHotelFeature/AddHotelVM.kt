package com.lecezar.hotelupgrade.chooseHotelFeature

import androidx.lifecycle.MutableLiveData
import com.lecezar.hotelupgrade.repositories.HotelRepository
import com.lecezar.hotelupgrade.utils.base.BaseViewModel
import com.lecezar.hotelupgrade.utils.binding.CallbackKt
import org.koin.core.KoinComponent
import org.koin.core.inject

class AddHotelVM : BaseViewModel(), KoinComponent {

    private val hotelRepository: HotelRepository by inject()
    val hotelName = MutableLiveData<String>()

    fun addHotel(callbackKt: CallbackKt<String>.() -> Unit) {
        hotelName.value?.apply {
            if (this.isNotBlank())
                hotelRepository.addHotel(this,callbackKt)
        }
    }

}