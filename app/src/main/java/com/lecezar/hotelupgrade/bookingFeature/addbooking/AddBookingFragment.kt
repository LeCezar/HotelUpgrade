package com.lecezar.hotelupgrade.bookingFeature.addbooking

import android.app.DatePickerDialog
import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.databinding.AddBookingBinding
import com.lecezar.hotelupgrade.models.Room
import com.lecezar.hotelupgrade.utils.*
import com.lecezar.hotelupgrade.utils.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_add_booking.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class AddBookingFragment : BaseFragment<AddBookingBinding, AddBookingFragmentVM>(R.layout.fragment_add_booking) {
    override val viewModel: AddBookingFragmentVM by viewModel()
    var saveButtonPressed = false

    override fun setupViews() {
        viewModel.availableRoomsList.observe(this, androidx.lifecycle.Observer {
            if (it.isEmpty()) {
                showNoSelectableRoomsView()
            } else {
                showSelectableRoomsView()
                addItemsToRecyclerView(it)
            }
        })
        viewModel.dateErrorState.observe(this, androidx.lifecycle.Observer {
            if (it) {
                showNoSelectableRoomsView()
            } else {
                showSelectableRoomsView()
            }
        })
        startDateOnClickListener()
        endDateOnCLickListener()
        onBackPressedListener()
        setUpRecyclerView()
        onSavePressedListener()
    }

    private fun addItemsToRecyclerView(rooms: List<Room>) {
        (add_booking_choose_rooms_recycler.adapter as AddBookingRoomSelectablesAdapter).submitList(rooms.sortedBy {
            it.number
        })
    }

    private fun setUpRecyclerView() {
        add_booking_choose_rooms_recycler.apply {
            layoutManager = GridLayoutManager(this@AddBookingFragment.requireContext(), 3)
            adapter = AddBookingRoomSelectablesAdapter({ checked, room ->
                if (checked)
                    viewModel.selectRoom(room)
                else
                    viewModel.unSelectRoom(room)
            }, {
                view?.findNavController()?.navigate(AddBookingFragmentDirections.actionAddBookingFragmentToRoomDetailsFragment(it.id))
            })
        }
    }

    private fun startDateOnClickListener() {
        add_booking_start_date_text_button.setOnClickListener {
            showDatePicker(DatePickerDialog.OnDateSetListener { _, year, month, day ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, day, 14, 0)
                add_booking_start_date_text_button.text = calendar.time.format_mm_DD_YY(this@AddBookingFragment.requireContext())
                viewModel.startDate.value = calendar.time
                viewModel.fetchAvailableRoomsList()
            })
        }
    }

    private fun endDateOnCLickListener() {
        add_booking_end_date_text_button.setOnClickListener {
            showDatePicker(DatePickerDialog.OnDateSetListener { _, year, month, day ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, day, 12, 0)
                add_booking_end_date_text_button.text = calendar.time.format_mm_DD_YY(this@AddBookingFragment.requireContext())
                viewModel.endDate.value = calendar.time
                viewModel.fetchAvailableRoomsList()
            })
        }
    }

    private fun onBackPressedListener() {
        add_booking_toolbar.setNavigationOnClickListener {
            this.activity?.onBackPressed()
        }
    }

    private fun onSavePressedListener() {
        add_booking_save_button.setOnClickListener {
            if (viewModel.selectedRoomsList.isEmpty()) {
                makeToast(this.requireContext(), "No ROOMS selected!")
            } else {
                if (!saveButtonPressed) {
                    saveButtonPressed = true
                    viewModel.addBooking {
                        onSuccess = {
                            makeToast(this@AddBookingFragment.requireContext(), it)
                            saveButtonPressed = false
                            viewModel.fetchAvailableRoomsList()
                        }
                        onFailure = {
                            makeToast(this@AddBookingFragment.requireContext(), it.message ?: "Failed adding booking")
                            saveButtonPressed = false
                        }
                    }
                }
            }
        }
    }

    private fun showNoSelectableRoomsView() {
        add_booking_choose_rooms_recycler.visibility = View.GONE
        add_booking_no_rooms_available_text.visibility = View.VISIBLE
    }

    private fun showSelectableRoomsView() {
        add_booking_choose_rooms_recycler.visibility = View.VISIBLE
        add_booking_no_rooms_available_text.visibility = View.GONE
    }

    private fun showDatePicker(callBack: DatePickerDialog.OnDateSetListener) {
        val currentDate = Calendar.getInstance().time
        val month = currentDate.getMonthAsInt()
        DatePickerDialog(this.requireContext(), callBack, currentDate.getYearAsInt(), month - 1, currentDate.getDayAsInt()).show()
    }
}