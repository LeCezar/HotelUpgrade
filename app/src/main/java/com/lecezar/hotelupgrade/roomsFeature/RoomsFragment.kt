package com.lecezar.hotelupgrade.roomsFeature

import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.lecezar.hotelupgrade.MainActivity
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.databinding.RoomsFragmentBinding
import com.lecezar.hotelupgrade.models.Room
import com.lecezar.hotelupgrade.utils.base.BaseFragment
import com.lecezar.hotelupgrade.utils.base.RecycleItemTouchHelper
import com.lecezar.hotelupgrade.utils.makeToast
import com.lecezar.hotelupgrade.utils.views.ConfirmActionDialog
import kotlinx.android.synthetic.main.fragment_rooms.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class RoomsFragment : BaseFragment<RoomsFragmentBinding, RoomsFragmentVM>(R.layout.fragment_rooms) {
    override val viewModel: RoomsFragmentVM by viewModel()


    override fun setupViews() {
        setOnClickListeners()
        viewModel.subscribeListenerForAllRooms(this)
        setUpRecyclerView()
        decideWhatViewToShow()
    }

    private fun decideWhatViewToShow() {
        viewModel.roomList.observe(this, Observer {
            if (it.isNotEmpty()) {
                submitRoomListRecyclerView(it)
                showRoomsView()
            } else {
                showNoRoomsView()
            }
        })
    }

    private fun submitRoomListRecyclerView(roomList: List<Room>) {
        (rooms_recycler_view.adapter as RoomsAdapter).submitList(roomList.sortedBy {
            it.number
        })
    }

    private fun setUpRecyclerView() {
        rooms_recycler_view.apply {
            adapter = RoomsAdapter(this@RoomsFragment.requireContext()) {
                view?.findNavController()?.navigate(RoomsFragmentDirections.actionRoomsFragmentToRoomDetailsFragment(it.id))
            }
            layoutManager = LinearLayoutManager(this@RoomsFragment.context)
        }

        ItemTouchHelper(RecycleItemTouchHelper {
            activity?.let { activity ->
                (rooms_recycler_view?.adapter as RoomsAdapter).apply {
                    ConfirmActionDialog("Delete ${this.getItemAt(it).name} ?",
                        confirmCallback = {
                            viewModel.deleteRoom(this.getItemAt(it).id) {
                                onSuccess = {
                                    makeToast(this@RoomsFragment.requireContext(), it)
                                }
                                onFailure = {}
                            }
                        },
                        cancelCallback = {
                            this.notifyItemChanged(it)
                        }
                    ).show(activity.supportFragmentManager, "Rooms delete dialog")
                }
            }
        }).attachToRecyclerView(rooms_recycler_view)
    }

    private fun setOnClickListeners() {
        no_rooms_add_button.setOnClickListener {
            view?.findNavController()?.navigate(RoomsFragmentDirections.actionRoomsFragmentToAddRoomFragment())
        }
        rooms_add_button.setOnClickListener {
            view?.findNavController()?.navigate(RoomsFragmentDirections.actionRoomsFragmentToAddRoomFragment())
        }

        rooms_update_room_statuses_button.setOnClickListener {
            viewModel.updateRoomStatuses()
        }
    }

    private fun showNoRoomsView() {
        rooms_recycler_view.visibility = View.GONE
        no_rooms_view.visibility = View.VISIBLE
    }

    private fun showRoomsView() {
        rooms_recycler_view.visibility = View.VISIBLE
        no_rooms_view.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).showBottomNavigation()
    }
}