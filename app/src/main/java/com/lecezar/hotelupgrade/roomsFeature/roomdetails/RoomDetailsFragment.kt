package com.lecezar.hotelupgrade.roomsFeature.roomdetails

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.bookingFeature.BookingsAdapter
import com.lecezar.hotelupgrade.databinding.RoomDetailsBinding
import com.lecezar.hotelupgrade.models.Booking
import com.lecezar.hotelupgrade.utils.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_room_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.math.abs


class RoomDetailsFragment : BaseFragment<RoomDetailsBinding, RoomDetailsFragmentVM>(R.layout.fragment_room_details) {
    override val viewModel: RoomDetailsFragmentVM by viewModel()
    private val args: RoomDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun setupViews() {
        setUpRecyclerView()
        viewModel.subscribeSelectedRoomListenerWithBookings(args.roomId, this)
        viewModel.selectedRoomBookings.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                addItemsToRecyclerView(it)
            }
            //todo show no bookings for this room view

        })
    }

    private fun setUpRecyclerView() {
        details_room_bookings_recycler_view.apply {
            this.layoutManager = LinearLayoutManager(this@RoomDetailsFragment.requireContext())
            this.adapter = BookingsAdapter(this@RoomDetailsFragment.requireContext()) {

            }
        }
    }

    private fun addItemsToRecyclerView(bookingList: List<Booking>) {
        (details_room_bookings_recycler_view.adapter as BookingsAdapter).submitList(bookingList.sortedByDescending {
            abs((it.startDate.time - Calendar.getInstance().time.time))
        })
    }
}