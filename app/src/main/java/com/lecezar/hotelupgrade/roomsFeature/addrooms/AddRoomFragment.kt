package com.lecezar.hotelupgrade.roomsFeature.addrooms

import android.view.View
import com.lecezar.hotelupgrade.MainActivity
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.databinding.AddRoomBinding
import com.lecezar.hotelupgrade.utils.base.BaseFragment
import com.lecezar.hotelupgrade.utils.makeToast
import kotlinx.android.synthetic.main.fragment_add_room.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddRoomFragment : BaseFragment<AddRoomBinding, AddRoomFragmentVM>(R.layout.fragment_add_room) {
    override val viewModel: AddRoomFragmentVM by viewModel()
    private var addingTypeFlag: Int = MULTIPLE_ADD_TYPE

    override fun setupViews() {
        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        setUpToggleAddListener()
        setUpSaveRoomsButtonListener()
        setUpSelectRoomsButtonListener()
    }

    private fun setUpToggleAddListener() {
        toggle_add_button.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.one_room_add_button -> {
                    showSingleRoomAddVIew()
                    addingTypeFlag = SINGLE_ADD_TYPE
                }
                R.id.multiple_rooms_add_button -> {
                    showMultipleRoomAddView()
                    addingTypeFlag = MULTIPLE_ADD_TYPE
                }
            }
        }
    }

    private fun setUpSaveRoomsButtonListener() {
        save_rooms_added_button.setOnClickListener {
            if (addingTypeFlag == SINGLE_ADD_TYPE && isSingleAddRoomDataValid())
                addSingleRoom()
            if (addingTypeFlag == MULTIPLE_ADD_TYPE && isMultipleRoomsAddDataValid()) {
                addMultipleRooms()
            }
        }
    }

    private fun addSingleRoom() {
        viewModel.addSingleRoom {
            onSuccess = {
                makeToast(this@AddRoomFragment.requireContext(), "Success!")
            }; onFailure = {
            makeToast(this@AddRoomFragment.requireContext(), it.message ?: "Failed")
        }
        }
    }

    private fun addMultipleRooms() {
        viewModel.addMultipleRooms {
            onSuccess = {
                makeToast(this@AddRoomFragment.requireContext(), it)
                viewModel.roomsIntervalsSelected.removeAll { true }
            };
            onFailure = {
                makeToast(this@AddRoomFragment.requireContext(), it.message ?: "Failed")
            }
        }
    }

    private fun isMultipleRoomsAddDataValid(): Boolean {
        return !(floor_text_layout.isErrorEnabled && from_room_text_layout.isErrorEnabled && to_room_text_layout.isErrorEnabled)
    }

    private fun isSingleAddRoomDataValid(): Boolean {
        return !(single_room_floor_layout.isErrorEnabled && single_room_name_layout.isErrorEnabled && single_room_number_layout.isErrorEnabled)
    }

    private fun setUpSelectRoomsButtonListener() {
        select_rooms_button.setOnClickListener {
            if (isMultipleRoomsAddDataValid()) {
                val result = viewModel.saveCurrentSelectedRoomsInterval()
                if (result.isNotBlank()) {
                    var text = selected_intervals_text.text.toString()
                    text += "\n$result"
                    selected_intervals_text.text = text
                }
            }
        }
    }

    private fun showSingleRoomAddVIew() {

        multiple_add_view_root.visibility = View.GONE
        single_add_view_root.visibility = View.VISIBLE
        save_rooms_added_button.text = "Save Room"
        showSingleRoomIcon()

    }

    private fun showMultipleRoomAddView() {
        multiple_add_view_root.visibility = View.VISIBLE
        single_add_view_root.visibility = View.GONE
        save_rooms_added_button.text = "Save Rooms"
        showMultipleRoomIcon()
    }

    private fun showMultipleRoomIcon() {
        add_room_icon_1.visibility = View.VISIBLE
        add_room_icon_3.visibility = View.VISIBLE
    }

    private fun showSingleRoomIcon() {
        add_room_icon_1.visibility = View.GONE
        add_room_icon_3.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).hideBottomNavigation()
    }

    companion object {
        private const val SINGLE_ADD_TYPE = 1
        private const val MULTIPLE_ADD_TYPE = 0
    }
}