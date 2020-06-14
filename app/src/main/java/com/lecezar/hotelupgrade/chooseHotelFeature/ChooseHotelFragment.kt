package com.lecezar.hotelupgrade.chooseHotelFeature

import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.lecezar.hotelupgrade.GlobalVariables
import com.lecezar.hotelupgrade.MainActivity
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.databinding.ChooseHotelBinding
import com.lecezar.hotelupgrade.models.Hotel
import com.lecezar.hotelupgrade.utils.base.BaseFragment
import com.lecezar.hotelupgrade.workmanager.UpdateRoomStatuses
import kotlinx.android.synthetic.main.fragment_choose_hotel.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class ChooseHotelFragment : BaseFragment<ChooseHotelBinding, ChooseHotelVM>(R.layout.fragment_choose_hotel) {
    override val viewModel: ChooseHotelVM by viewModel()


    override fun setupViews() {
        setUpRecyclerView()
        setUpClickListeners()
        viewModel.hotelList.observe(this, Observer {
            if (it.isNotEmpty()) {
                submitHotelListRecyclerView(it)
                showHotelsView()
            } else {
                showNoHotelsView()
            }
        })
    }

    private fun setUpClickListeners() {
        add_hotel_action_button.setOnClickListener {
            navigateToAddHotelFragment()
        }
        no_hotels_add_button.setOnClickListener {
            navigateToAddHotelFragment()
        }
    }

    private fun navigateToAddHotelFragment() {
        view?.findNavController()?.navigate(ChooseHotelFragmentDirections.actionChooseHotelFragmentToAddHotelFragment())
    }

    private fun submitHotelListRecyclerView(hotelList: List<Hotel>) {
        (hotels_recycler_view.adapter as HotelsAdapter).submitList(hotelList)
    }

    private fun setUpRecyclerView() {
        hotels_recycler_view.apply {
            adapter = HotelsAdapter {
                setUpWorkerForTheChosenHotel(it.id)
                GlobalVariables.currentHotelId.set(it.id)
                view?.findNavController()?.navigate(ChooseHotelFragmentDirections.actionChooseHotelFragmentToEventsFragment())
            }
            layoutManager = LinearLayoutManager(this@ChooseHotelFragment.context)
        }
    }

    private fun setUpWorkerForTheChosenHotel(hotelId: String) {
        val updateRoomStatusesWorker = PeriodicWorkRequestBuilder<UpdateRoomStatuses>(6, TimeUnit.HOURS)
            .setInitialDelay(1, TimeUnit.MINUTES)
            .addTag(hotelId)
            .setInputData(
                workDataOf(
                    "HOTEL_ID" to hotelId
                )
            )
            .build()
        WorkManager.getInstance(this.requireContext()).enqueueUniquePeriodicWork(hotelId, ExistingPeriodicWorkPolicy.KEEP, updateRoomStatusesWorker)
    }

    private fun showHotelsView() {
        no_hotels_view.visibility = View.GONE
        hotels_recycler_view.visibility = View.VISIBLE
        add_hotel_action_button.visibility = View.VISIBLE
    }

    private fun showNoHotelsView() {
        no_hotels_view.visibility = View.VISIBLE
        hotels_recycler_view.visibility = View.GONE
        add_hotel_action_button.visibility = View.GONE

    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).hideBottomNavigation()
        viewModel.checkForHotels()
    }
}