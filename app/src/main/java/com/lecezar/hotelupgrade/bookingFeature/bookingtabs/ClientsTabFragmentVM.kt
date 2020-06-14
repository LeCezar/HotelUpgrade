package com.lecezar.hotelupgrade.bookingFeature.bookingtabs

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.lecezar.hotelupgrade.models.Client
import com.lecezar.hotelupgrade.repositories.ClientRepository
import com.lecezar.hotelupgrade.utils.base.BaseViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

class ClientsTabFragmentVM : BaseViewModel(), KoinComponent {
    private val clientRepository: ClientRepository by inject()

    val clientList = MutableLiveData<List<Client>>()

    fun subscribeClientsListener(lifecycleOwner: LifecycleOwner) {
        clientRepository.subscribeListenerForAllClients(lifecycleOwner) {
            onSuccess = {
                clientList.value = it
            }
            onFailure = {

            }
        }
    }
}