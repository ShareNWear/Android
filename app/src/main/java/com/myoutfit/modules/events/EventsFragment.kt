package com.myoutfit.modules.events

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.myoutfit.R
import com.myoutfit.base.BaseFragment
import com.myoutfit.di.AppViewModelsFactory
import com.myoutfit.models.events.adapters.EventAdapter
import com.myoutfit.utils.extentions.toastL
import kotlinx.android.synthetic.main.fragment_events.*
import javax.inject.Inject

class EventsFragment : BaseFragment() {

    @Inject
    lateinit var vmFactory: AppViewModelsFactory

    private lateinit var viewModel: EventsViewModel

    override fun layoutId(): Int = R.layout.fragment_events

    override fun onViewReady(inflatedView: View, args: Bundle?) {
        initRecycle()
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProviders.of(requireActivity(), vmFactory).get(EventsViewModel::class.java)

        viewModel.eventsLiveData.observe(viewLifecycleOwner, Observer {
            it?.let { data ->
                toastL("Success " + data.size.toString() + " events")
                (rvEvents.adapter as? EventAdapter)?.setData(data)
            }
        })

        viewModel.getEvents()
    }

    private fun initRecycle() {
        with(rvEvents) {
            layoutManager = LinearLayoutManager(context)
            adapter = EventAdapter()
        }
    }

    override fun setListeners() {

    }
}