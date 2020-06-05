package com.myoutfit.modules.events

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.myoutfit.R
import com.myoutfit.base.BaseFragment
import com.myoutfit.di.AppViewModelsFactory
import com.myoutfit.utils.extentions.toastL
import javax.inject.Inject

class EventsFragment : BaseFragment() {

    @Inject
    lateinit var vmFactory: AppViewModelsFactory

    private lateinit var viewModel: EventsViewModel

    override fun layoutId(): Int = R.layout.fragment_events

    override fun onViewReady(inflatedView: View, args: Bundle?) {
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProviders.of(requireActivity(), vmFactory).get(EventsViewModel::class.java)

        viewModel.eventsLiveData.observe(viewLifecycleOwner, Observer {
            it?.let { data ->
                toastL("Success " + data.size.toString() + " events")
            }
        })

        viewModel.getEvents()
    }

    override fun setListeners() {

    }
}