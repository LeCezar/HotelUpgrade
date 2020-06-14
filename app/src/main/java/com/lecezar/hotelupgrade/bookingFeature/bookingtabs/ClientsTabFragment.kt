package com.lecezar.hotelupgrade.bookingFeature.bookingtabs

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.databinding.ClientsTabBinding
import com.lecezar.hotelupgrade.models.Client
import com.lecezar.hotelupgrade.utils.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_clients_tab.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ClientsTabFragment : BaseFragment<ClientsTabBinding, ClientsTabFragmentVM>(R.layout.fragment_clients_tab) {
    override val viewModel: ClientsTabFragmentVM by viewModel()

    override fun setupViews() {
        setUpRecyclerView()
        viewModel.subscribeClientsListener(this)
        viewModel.clientList.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                submitListToRecyclerView(it)
                showClientsView()
            } else {
                showNoClientsView()
            }
        })
    }

    private fun submitListToRecyclerView(clienstList: List<Client>) {
        (clients_recycler_view.adapter as ClientsAdapter).submitList(clienstList.sortedBy {
            it.name
        })
    }

    private fun setUpRecyclerView() {
        clients_recycler_view.apply {
            adapter = ClientsAdapter()
            layoutManager = LinearLayoutManager(this@ClientsTabFragment.context)
        }
    }

    private fun showNoClientsView() {
        no_clients_view.visibility = View.VISIBLE
        clients_recycler_view.visibility = View.GONE
    }

    private fun showClientsView() {
        no_clients_view.visibility = View.GONE
        clients_recycler_view.visibility = View.VISIBLE
    }

}