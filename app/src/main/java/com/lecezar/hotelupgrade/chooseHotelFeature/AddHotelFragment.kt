package com.lecezar.hotelupgrade.chooseHotelFeature

import androidx.navigation.findNavController
import com.lecezar.hotelupgrade.MainActivity
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.databinding.AddHotelBinding
import com.lecezar.hotelupgrade.utils.base.BaseFragment
import com.lecezar.hotelupgrade.utils.makeToast
import kotlinx.android.synthetic.main.fragment_add_hotel.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddHotelFragment : BaseFragment<AddHotelBinding, AddHotelVM>(R.layout.fragment_add_hotel) {
    override val viewModel: AddHotelVM by viewModel()

    override fun setupViews() {
        save_hotel_added_button.setOnClickListener {
            viewModel.addHotel {
                onSuccess = {
                    makeToast(this@AddHotelFragment.requireContext(), it)
                    view?.findNavController()?.navigate(AddHotelFragmentDirections.actionAddHotelFragmentToChooseHotelFragment())
                }
                onFailure = {

                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).hideBottomNavigation()
    }
}