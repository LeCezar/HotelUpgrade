package com.lecezar.hotelupgrade.calendarFeature

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.lecezar.hotelupgrade.models.Event
import com.lecezar.hotelupgrade.repositories.EventRepository
import com.lecezar.hotelupgrade.utils.base.BaseViewModel
import com.lecezar.hotelupgrade.utils.getMonthAsInt
import com.lecezar.hotelupgrade.utils.getYearAsInt
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.threeten.bp.LocalDate


class CalendarFragmentVM : BaseViewModel(), KoinComponent {
    private val eventRepository: EventRepository by inject()
    var eventList = MutableLiveData<List<Event>>()

    val currentMonthSelected = MutableLiveData<LocalDate>()
    val eventMapForThisOneMonth = currentMonthSelected.map {
        updateEventListWithSelectedMonth(it)
    }

    fun subscribeAllEventsListener(lifecycleOwner: LifecycleOwner) {
        eventRepository.subscribeListenerForAllEvents(lifecycleOwner) {
            onSuccess = {
                eventList.value = it
            }
            onFailure = {

            }
        }
    }

    private fun updateEventListWithSelectedMonth(localDate: LocalDate): Map<LocalDate, MutableList<Event>> {
        val eventMapForThisMonth = mutableMapOf<LocalDate, MutableList<Event>>()
        eventList.value?.forEach { event ->
            if (event.triggerDate.getMonthAsInt() == localDate.monthValue && event.triggerDate.getYearAsInt() == localDate.year) {
                eventMapForThisMonth.putIfAbsent(event.triggerDateToLocalDate(), mutableListOf(event))?.apply {
                    if (event !in this) {
                        this.add(event)
                    }
                }
            }
        }
        return eventMapForThisMonth
    }
}