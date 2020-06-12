package com.lecezar.hotelupgrade.splashScreen

import android.os.Handler
import androidx.navigation.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lecezar.hotelupgrade.GlobalVariables
import com.lecezar.hotelupgrade.MainActivity
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.databinding.SplashFragmentBinding
import com.lecezar.hotelupgrade.utils.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class SplashFragment : BaseFragment<SplashFragmentBinding, SplashFragmentVM>(R.layout.fragment_splash) {
    override val viewModel: SplashFragmentVM by viewModel()

    private val currentUser = Firebase.auth.currentUser

    override fun setupViews() {
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).hideBottomNavigation()
        Handler().postDelayed({
            if (currentUser == null) {
                view?.findNavController()?.navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
            } else {
                GlobalVariables.currentUserId.set(currentUser.uid)
                view?.findNavController()?.navigate(SplashFragmentDirections.actionSplashFragmentToChooseHotelFragment())
            }
        }, 1000)
    }
}