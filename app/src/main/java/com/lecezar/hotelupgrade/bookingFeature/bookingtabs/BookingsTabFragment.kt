package com.lecezar.hotelupgrade.bookingFeature.bookingtabs

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.lecezar.hotelupgrade.MainActivity
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.bookingFeature.BookingsAdapter
import com.lecezar.hotelupgrade.bookingFeature.BookingsFragment
import com.lecezar.hotelupgrade.databinding.BookingsTabBinding
import com.lecezar.hotelupgrade.models.Booking
import com.lecezar.hotelupgrade.utils.base.BaseFragment
import com.lecezar.hotelupgrade.utils.base.RecycleItemTouchHelper
import com.lecezar.hotelupgrade.utils.makeToast
import kotlinx.android.synthetic.main.fragment_bookings_tab.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

class BookingsTabFragment : BaseFragment<BookingsTabBinding, BookingsTabFragmentVM>(R.layout.fragment_bookings_tab) {
    override val viewModel: BookingsTabFragmentVM by viewModel()

    private var tabType by Delegates.notNull<Int>()


    override fun setupViews() {

        arguments?.takeIf { it.containsKey(BookingsFragment.TAB_TYPE_KEY) }?.apply {
            tabType = getInt(BookingsFragment.TAB_TYPE_KEY)
        }
        setUpRecyclerView()
        subscribeListenerForBookings()
        listenForBookingListChanges()

    }

    private fun listenForBookingListChanges() {
        viewModel.bookingsList.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                showBookingsView()
                addItemsToRecyclerView(it)
            } else {
                showNoBookingsView()
            }
        })
    }

    private fun subscribeListenerForBookings() {
        when (tabType) {
            BookingsFragment.UPCOMING_BOOKINGS -> {
                viewModel.subscribeListenerForUpcomingBookings(this)
            }
            BookingsFragment.PAST_BOOKINGS -> {
                viewModel.subscribeListenerForPastBookings(this)
            }
        }
    }

    private fun setUpRecyclerView() {
        bookings_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@BookingsTabFragment.requireContext())
            adapter = BookingsAdapter(this@BookingsTabFragment.requireContext()) {
                (activity as MainActivity).navigateFromBookingsFragmentToRoomDetails(it)
            }
        }
        ItemTouchHelper(RecycleItemTouchHelper {
            (bookings_recycler_view?.adapter as BookingsAdapter).apply {
                viewModel.deleteBooking(this.getItemAt(it).id) {
                    onSuccess = {
                        makeToast(this@BookingsTabFragment.requireContext(), "Deleted Booking!")
                    }
                    onFailure = {}
                }
            }
        }).attachToRecyclerView(bookings_recycler_view)
    }

    private fun addItemsToRecyclerView(bookings: List<Booking>) {
        (bookings_recycler_view.adapter as BookingsAdapter).submitList(bookings)
    }

    private fun showBookingsView() {
        bookings_recycler_view.visibility = View.VISIBLE
        no_bookings_view.visibility = View.GONE

    }

    private fun showNoBookingsView() {
        bookings_recycler_view.visibility = View.GONE
        no_bookings_view.visibility = View.VISIBLE
    }

}