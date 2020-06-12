package com.lecezar.hotelupgrade.chooseHotelFeature

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lecezar.hotelupgrade.GlobalVariables
import com.lecezar.hotelupgrade.MainActivity
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.databinding.ChooseHotelBinding
import com.lecezar.hotelupgrade.models.Hotel
import com.lecezar.hotelupgrade.utils.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_choose_hotel.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChooseHotelFragment : BaseFragment<ChooseHotelBinding, ChooseHotelVM>(R.layout.fragment_choose_hotel) {
    override val viewModel: ChooseHotelVM by viewModel()

    //TODO 1: modify the app so that the slash screen is an activity
    //TODO 2: Navigation in the app should be managed by the main activity (add a search bar to it,bottom nav bar)


    override fun setupViews() {
//        view?.findNavController()?.popBackStack(R.id.splashFragment, false)
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
                GlobalVariables.currentHotelId.set(it.id)
                view?.findNavController()?.navigate(ChooseHotelFragmentDirections.actionChooseHotelFragmentToEventsFragment())
                Log.d("Hotel clicked: ", it.name)
            }
            layoutManager = LinearLayoutManager(this@ChooseHotelFragment.context)
        }
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