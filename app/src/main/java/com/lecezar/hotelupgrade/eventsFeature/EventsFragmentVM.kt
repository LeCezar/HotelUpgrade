package com.lecezar.hotelupgrade.eventsFeature

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.lecezar.hotelupgrade.models.Event
import com.lecezar.hotelupgrade.repositories.EventRepository
import com.lecezar.hotelupgrade.utils.base.BaseViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

class EventsFragmentVM : BaseViewModel(), KoinComponent {
    private val eventRepository: EventRepository by inject()

    val eventList = MutableLiveData<List<Event>>()

    fun subscribeAllEventsListener(lifecycleOwner: LifecycleOwner) {
        eventRepository.subscribeListenerForAllEvents(lifecycleOwner) {
            onSuccess = {
                eventList.value = it
            }
            onFailure = {

            }
        }
    }
}