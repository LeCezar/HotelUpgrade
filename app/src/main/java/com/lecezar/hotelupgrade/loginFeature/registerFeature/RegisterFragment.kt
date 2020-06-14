package com.lecezar.hotelupgrade.loginFeature.registerFeature

import androidx.navigation.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.databinding.RegisterFragmentBinding
import com.lecezar.hotelupgrade.utils.base.BaseFragment
import com.lecezar.hotelupgrade.utils.makeToast
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegisterFragment : BaseFragment<RegisterFragmentBinding, RegisterFragmentVM>(R.layout.fragment_register) {
    override val viewModel: RegisterFragmentVM by viewModel()

    override fun setupViews() {
        register_fragment_register_button.setOnClickListener {
            viewModel.registerUser(Firebase.auth) {
                onSuccess = {
                    makeToast(this@RegisterFragment.requireContext(), it)
                    view?.findNavController()?.navigate(RegisterFragmentDirections.actionRegisterFragmentToChooseHotelFragment())
                }
                onFailure = {
                    makeToast(this@RegisterFragment.requireContext(), it.message ?: it.localizedMessage ?: "Failed register!")
                }
            }
        }
    }
}