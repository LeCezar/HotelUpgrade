package com.lecezar.hotelupgrade.bookingFeature.bookingtabs

import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.databinding.ClientsTabBinding
import com.lecezar.hotelupgrade.utils.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class ClientsTabFragment : BaseFragment<ClientsTabBinding, ClientsTabFragmentVM>(R.layout.fragment_clients_tab) {
    override val viewModel: ClientsTabFragmentVM by viewModel()

    override fun setupViews() {

    }

}