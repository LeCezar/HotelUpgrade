package com.lecezar.hotelupgrade.eventsFeature

import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lecezar.hotelupgrade.MainActivity
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.databinding.EventsFragmentBinding
import com.lecezar.hotelupgrade.models.Event
import com.lecezar.hotelupgrade.utils.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_events.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class EventsFragment : BaseFragment<EventsFragmentBinding, EventsFragmentVM>(R.layout.fragment_events) {
    override val viewModel: EventsFragmentVM by viewModel()

    private val HOURS_23_VALUE_MILLIS = 82800000

    override fun setupViews() {
        setUpRecyclerView()
        viewModel.subscribeAllEventsListener(this)
        viewModel.eventList.observe(this, Observer { eventList ->
            if (!eventList.isNullOrEmpty()) {
                val currentTime = Calendar.getInstance().time.time
                val filteredEvents = eventList.filter {
                    val timeDiff = currentTime - it.triggerDate.time
                    timeDiff in 1 until HOURS_23_VALUE_MILLIS
                }
                if (filteredEvents.isEmpty()) {
                    showNoEventsView()
                } else {
                    addItemsToRecyclerView(filteredEvents)
                    showEventsView()
                }
            } else {
                showNoEventsView()
            }
        })
        events_choose_hotel_button.setOnClickListener {
            view?.findNavController()?.navigate(EventsFragmentDirections.actionEventsFragmentToChooseHotelFragment())
        }
        events_sign_out_button.setOnClickListener {
            Firebase.auth.signOut()
            view?.findNavController()?.navigate(EventsFragmentDirections.actionEventsFragmentToLoginFragment())
        }
    }

    private fun addItemsToRecyclerView(eventsList: List<Event>) {
        (events_recycler_view.adapter as EventsAdapter).submitList(eventsList)
    }

    private fun setUpRecyclerView() {
        events_recycler_view.apply {
            this.layoutManager = LinearLayoutManager(this@EventsFragment.requireContext())
            this.adapter = EventsAdapter(this@EventsFragment.requireContext()) {
                //on item room small click navigate
            }
        }
    }

    private fun showNoEventsView() {
        events_recycler_view.visibility = View.GONE
        no_events_view.visibility = View.VISIBLE
    }

    private fun showEventsView() {
        events_recycler_view.visibility = View.VISIBLE
        no_events_view.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).showBottomNavigation()
    }
}