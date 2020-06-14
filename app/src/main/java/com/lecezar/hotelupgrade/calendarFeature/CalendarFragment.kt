package com.lecezar.hotelupgrade.calendarFeature

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.databinding.CalendarFragmentBinding
import com.lecezar.hotelupgrade.eventsFeature.EventsAdapter
import com.lecezar.hotelupgrade.models.Event
import com.lecezar.hotelupgrade.utils.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.item_calendar_day.view.*
import kotlinx.android.synthetic.main.item_calendar_header.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder as MonthHeaderFooterBinder1


class CalendarFragment : BaseFragment<CalendarFragmentBinding, CalendarFragmentVM>(R.layout.fragment_calendar) {
    override val viewModel: CalendarFragmentVM by viewModel()

    var isRecyclerViewSetUp = false
    var selectedDate: LocalDate? = null
        set(value) {
            field = value
            giveRecyclerViewEventsFromASelectedDate(value)
        }
    private var currentMonthSelected: LocalDate = LocalDate.now()
    private var isCalendarSetUpMade = false

    override fun setupViews() {
        viewModel.subscribeAllEventsListener(this)
        viewModel.eventMapForThisOneMonth.observe(viewLifecycleOwner, Observer { eventList ->
            if (!isCalendarSetUpMade) {
                setUpCalendar()
                isCalendarSetUpMade = true
                setUpRecyclerView()
            }
            calendar_view_root.notifyCalendarChanged()
        })
        viewModel.eventList.observe(viewLifecycleOwner, Observer {
            viewModel.currentMonthSelected.value = currentMonthSelected
        })
    }

    private fun giveRecyclerViewEventsFromASelectedDate(date: LocalDate?) {
        if (date != null) {
            viewModel.eventMapForThisOneMonth.value?.apply {
                val eventListToday = this[date]
                if (eventListToday != null) {
                    giveItemsToRecyclerView(eventListToday)
                } else {
                    giveItemsToRecyclerView(listOf())
                }

            }
        } else {
            if (isRecyclerViewSetUp)
                giveItemsToRecyclerView(listOf())
        }
    }

    private fun giveItemsToRecyclerView(eventList: List<Event>) {
        (calendar_events_recycler_view.adapter as EventsAdapter).submitList(eventList)
    }

    private fun setUpRecyclerView() {
        calendar_events_recycler_view.apply {
            adapter = EventsAdapter(this@CalendarFragment.requireContext()) {}
            layoutManager = LinearLayoutManager(this@CalendarFragment.requireContext())
        }
        isRecyclerViewSetUp = true
    }

    private fun setUpCalendar() {
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(10)
        val endMonth = currentMonth.plusMonths(10)
        calendar_view_root.dayBinder = DayBinderCalendar()


        calendar_view_root.monthHeaderBinder = object : MonthHeaderFooterBinder1<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                @SuppressLint("SetTextI18n") // Concatenation warning for `setText` call.
                container.textView.text = "${month.yearMonth.month.name.toLowerCase().capitalize()} ${month.year}"
            }
        }
        calendar_view_root.setup(startMonth, endMonth, DayOfWeek.MONDAY)
        calendar_view_root.scrollToMonth(currentMonth)
        calendar_view_root.monthScrollListener = {
            currentMonthSelected = LocalDate.of(it.year, it.month, 1)
            viewModel.currentMonthSelected.value = currentMonthSelected
        }
    }

    inner class DayBinderCalendar() : DayBinder<DayViewContainer> {

        override fun bind(container: DayViewContainer, day: CalendarDay) {
            container.setDay(day)
            if (day.owner == DayOwner.THIS_MONTH) {
                viewModel.eventMapForThisOneMonth.value?.apply {
                    val eventsForToday = this[day.date]
                    if (eventsForToday != null) {
                        if (eventsForToday.size >= 2) {
                            val eventTypesSet = getEventTypesPresentInList(eventsForToday)
                            if (eventTypesSet.size == 2) {
                                container.representBothCheckInAndCheckOut()
                            } else {
                                if (eventTypesSet.contains(Event.Companion.EventType.CHECKIN)) {
                                    container.representCheckIn()
                                } else {
                                    container.representCheckOut()
                                }
                            }
                        }
                        if (eventsForToday.size == 1) {
                            when (eventsForToday[0].eventType) {
                                Event.Companion.EventType.CHECKIN -> container.representCheckIn()
                                Event.Companion.EventType.CHECKOUT -> container.representCheckOut()
                            }
                        }
                    } else {
                        container.representNormalDay()
                    }
                }
                container.isCurrentDay = day.date == LocalDate.now()
            } else {
                container.showAsInactive()
            }
            container.setDayText(day.date.dayOfMonth.toString())
        }

        private fun getEventTypesPresentInList(listEvents: List<Event>): Set<Event.Companion.EventType> {
            val eventTypeSet = mutableSetOf<Event.Companion.EventType>()
            listEvents.forEach {
                eventTypeSet.add(it.eventType)
            }
            return eventTypeSet
        }

        override fun create(view: View): DayViewContainer = DayViewContainer(view)
    }

    inner class DayViewContainer(view: View) : ViewContainer(view) {
        private val fragmentContext: Context = this@CalendarFragment.requireContext()
        val textView = view.calendarDayText
        private lateinit var day: CalendarDay
        var isCurrentDay = false
        private var isInactive = false

        init {
            textView.setOnClickListener {
                if (day.owner == DayOwner.THIS_MONTH) {
                    if (selectedDate == day.date) {
                        this.deselect()
                    } else {
                        this.select()
                    }
                }
            }
        }

        fun setDay(day: CalendarDay) {
            this.day = day
        }

        fun setDayText(string: String) {
            this.textView.text = string
            if (selectedDate != this.day.date) {
                this.updateSelectedViewToDeseleted()
            }
        }

        fun showAsInactive() {
            this.isInactive = true
            textView.background = fragmentContext.getDrawable(R.color.white)
            textView.foreground = fragmentContext.getDrawable(R.color.transparent)
            textView.setTextColor(fragmentContext.getColor(R.color.grey))
        }

        private fun select() {
            val oldDate: LocalDate? = selectedDate
            selectedDate = day.date
            calendar_view_root.notifyDateChanged(day.date)
            if (oldDate != null) {
                calendar_view_root.notifyDateChanged(oldDate)
            }
            textView.background = fragmentContext.getDrawable(R.drawable.background_rounded_corners_blue)
            textView.setTextColor(fragmentContext.getColor(R.color.white))
        }

        private fun deselect() {
            updateSelectedViewToDeseleted()
            selectedDate = null
            calendar_view_root.notifyDayChanged(day)

        }

        private fun updateSelectedViewToDeseleted() {
            textView.background = fragmentContext.getDrawable(R.color.white)
            if (isCurrentDay) {
                textView.setTextColor(fragmentContext.getColor(R.color.colorPrimaryDark))
            } else {
                if (isInactive) {
                    textView.setTextColor(fragmentContext.getColor(R.color.light_grey))
                } else {
                    textView.setTextColor(fragmentContext.getColor(R.color.black))
                }
            }
        }

        fun representBothCheckInAndCheckOut() {
            textView.foreground = fragmentContext.getDrawable(R.drawable.calendar_day_both_checkin_checkout)
        }

        fun representCheckIn() {
            textView.foreground = fragmentContext.getDrawable(R.drawable.calendar_day_only_checkin)
        }

        fun representCheckOut() {
            textView.foreground = fragmentContext.getDrawable(R.drawable.calendar_day_only_checkout)
        }

        fun representNormalDay() {
            textView.foreground = fragmentContext.getDrawable(R.color.transparent)
        }

    }

    inner class MonthViewContainer(view: View) : ViewContainer(view) {
        val textView = view.exTwoHeaderText
    }
}