package com.lecezar.hotelupgrade.roomsFeature.roomdetails

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.bookingFeature.BookingsAdapter
import com.lecezar.hotelupgrade.databinding.FragmentRoomDetailsBinding
import com.lecezar.hotelupgrade.models.Booking
import com.lecezar.hotelupgrade.utils.base.BaseFragment
import com.lecezar.hotelupgrade.utils.makeToast
import kotlinx.android.synthetic.main.fragment_room_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.math.abs


class RoomDetailsFragment : BaseFragment<FragmentRoomDetailsBinding, RoomDetailsFragmentVM>(R.layout.fragment_room_details) {
    override val viewModel: RoomDetailsFragmentVM by viewModel()
    private val args: RoomDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun setupViews() {
        setUpRecyclerView()
        setEditAndSaveOnClickListeners()
        onBackPressedListener()
        viewModel.subscribeSelectedRoomListenerWithBookings(args.roomId, this)
        viewModel.selectedRoomBookings.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                addItemsToRecyclerView(it)
            }
            //todo show no bookings for this room view
        })

    }

    private fun onBackPressedListener() {
        details_room_toolbar.setNavigationOnClickListener {
            this.activity?.onBackPressed()
        }
    }

    private fun setEditAndSaveOnClickListeners() {
        details_room_edit_price_button.setOnClickListener {
            switchEditToSave()
            if (viewModel.selectedRoomPrice.value == RoomDetailsFragmentVM.NO_PRICE_TEXT) {
                viewModel.selectedRoomPrice.value = ""
            }
        }
        details_room_save_price_button.setOnClickListener {
            switchSaveToEdit()
            if (viewModel.selectedRoomPrice.value.isNullOrBlank()) {
                if (viewModel.selectedRoom.value?.price != null) {
                    viewModel.selectedRoomPrice.value = viewModel.selectedRoom.value?.price.toString()
                } else
                    viewModel.selectedRoomPrice.value = RoomDetailsFragmentVM.NO_PRICE_TEXT
            } else {
                val price = viewModel.selectedRoom.value?.price.toString()
                if (viewModel.selectedRoomPrice.value != null && viewModel.selectedRoomPrice.value != price) {
                    viewModel.updateRoomPrice {
                        onSuccess = {
                            makeToast(this@RoomDetailsFragment.requireContext(), it)
                        }
                        onFailure = {

                        }
                    }
                }
            }
        }
    }

    private fun switchEditToSave() {
        details_room_price_view_switcher.showNext()
        details_room_price_view_switcher_button.showNext()
    }

    private fun switchSaveToEdit() {
        details_room_price_view_switcher.showPrevious()
        details_room_price_view_switcher_button.showPrevious()
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