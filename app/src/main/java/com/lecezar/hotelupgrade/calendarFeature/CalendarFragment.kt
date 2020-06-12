package com.lecezar.hotelupgrade.calendarFeature

import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.databinding.CalendarFragmentBinding
import com.lecezar.hotelupgrade.utils.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class CalendarFragment : BaseFragment<CalendarFragmentBinding, CalendarFragmentVM>(R.layout.fragment_calendar) {
    override val viewModel: CalendarFragmentVM by viewModel()

    override fun setupViews() {
        
    }
}