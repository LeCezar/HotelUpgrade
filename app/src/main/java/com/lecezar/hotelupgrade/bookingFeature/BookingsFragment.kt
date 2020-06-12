package com.lecezar.hotelupgrade.bookingFeature

import androidx.navigation.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.lecezar.hotelupgrade.MainActivity
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.bookingFeature.bookingtabs.BookingsTabLayoutAdapter
import com.lecezar.hotelupgrade.databinding.BookingsFragmentBinding
import com.lecezar.hotelupgrade.utils.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_bookings.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class BookingsFragment : BaseFragment<BookingsFragmentBinding, BookingsFragmentVM>(R.layout.fragment_bookings) {
    override val viewModel: BookingsFragmentVM by viewModel()

    override fun setupViews() {
        setUpViewPager()
        onAddButtonClickListener()
    }

    private fun onAddButtonClickListener() {
        bookings_add_button.setOnClickListener {
            view?.findNavController()?.navigate(BookingsFragmentDirections.actionBookingsFragmentToAddBookingFragment())
        }
    }



    private fun setUpViewPager() {
        bookings_pager.apply {
            this.isUserInputEnabled = false
            adapter = BookingsTabLayoutAdapter(this@BookingsFragment)
            TabLayoutMediator(bookings_tab_layout, this) { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = "Upcoming"
                    }
                    1 -> {
                        tab.text = "Past"
                    }
                    2 -> {
                        tab.text = "Clients"
                    }
                    else -> {
                        tab.text = "OVERFLOW"
                    }
                }
            }.attach()
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).showBottomNavigation()
    }

    companion object {
        const val TAB_TYPE_KEY = "TAB_TYPE_KEY"
        const val UPCOMING_BOOKINGS = 0
        const val PAST_BOOKINGS = 1
        const val CLIENTS = 2
    }
}