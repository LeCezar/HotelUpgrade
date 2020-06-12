package com.lecezar.hotelupgrade.bookingFeature.bookingtabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lecezar.hotelupgrade.bookingFeature.BookingsFragment

class BookingsTabLayoutAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        if (position == 2) {
            val fragment = ClientsTabFragment()
            fragment.arguments = Bundle().apply {
                putInt(
                    BookingsFragment.TAB_TYPE_KEY,
                    BookingsFragment.CLIENTS
                )
            }
            return fragment
        } else {
            val fragment = BookingsTabFragment()
            fragment.arguments = Bundle().apply {
                when (position) {
                    0 -> {
                        putInt(
                            BookingsFragment.TAB_TYPE_KEY,
                            BookingsFragment.UPCOMING_BOOKINGS
                        )
                    }
                    1 -> {
                        putInt(
                            BookingsFragment.TAB_TYPE_KEY,
                            BookingsFragment.PAST_BOOKINGS
                        )
                    }
                    else -> {

                    }
                }
            }
            return fragment
        }
    }
}