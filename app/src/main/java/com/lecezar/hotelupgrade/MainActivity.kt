package com.lecezar.hotelupgrade

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lecezar.hotelupgrade.bookingFeature.BookingsFragmentDirections
import com.lecezar.hotelupgrade.databinding.ActivityMainBiding
import com.lecezar.hotelupgrade.utils.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<ActivityMainBiding, MainActivityVM>(R.layout.activity_main) {
    override val viewModel: MainActivityVM by viewModel()

    override fun setupViews() {
        setUpBottomNavigation()
    }

    fun showBottomNavigation() {
        bottom_navigation_bar.visibility = View.VISIBLE
    }

    fun hideBottomNavigation() {
        bottom_navigation_bar.visibility = View.GONE
    }

    fun navigateFromBookingsFragmentToRoomDetails(roomId: String) {
        findNavController(R.id.nav_host_fragment_container).navigate(BookingsFragmentDirections.actionBookingsFragmentToRoomDetailsFragment(roomId))
    }

    private fun setUpBottomNavigation() {
        bottom_navigation_bar.setupWithNavController(findNavController(R.id.nav_host_fragment_container))
    }

    companion object {
        fun getStartIntentExtra(context: Context, extra: Bundle) = Intent(context, MainActivity::class.java).putExtras(extra)
    }
}
