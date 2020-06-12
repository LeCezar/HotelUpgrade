package com.lecezar.hotelupgrade.nfcFeature

import com.lecezar.hotelupgrade.MainActivity
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.databinding.NfcFragmentBinding
import com.lecezar.hotelupgrade.utils.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class NfcFragment : BaseFragment<NfcFragmentBinding, NfcFragmentVM>(R.layout.fragment_nfc) {
    override val viewModel: NfcFragmentVM by viewModel()

    override fun setupViews() {
    }
    override fun onStart() {
        super.onStart()
        (activity as MainActivity).showBottomNavigation()
    }

}