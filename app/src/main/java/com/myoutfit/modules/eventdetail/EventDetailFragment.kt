package com.myoutfit.modules.eventdetail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.myoutfit.R
import com.myoutfit.base.BaseFragment
import com.myoutfit.di.AppViewModelsFactory
import com.myoutfit.models.events.EventDetailResponse
import com.myoutfit.models.network.ApiRequestStatus
import com.myoutfit.utils.extentions.*
import kotlinx.android.synthetic.main.fragment_event_detail.*
import javax.inject.Inject

class EventDetailFragment : BaseFragment() {

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }

    @Inject
    lateinit var vmFactory: AppViewModelsFactory

    private lateinit var viewModel: EventDetailViewModel

    override fun layoutId(): Int = R.layout.fragment_event_detail

    override fun onViewReady(inflatedView: View, args: Bundle?) {
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProviders.of(this, vmFactory).get(EventDetailViewModel::class.java)

        viewModel.requestStatusLiveData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ApiRequestStatus.RUNNING -> {
                    loadView.show()
                }
                ApiRequestStatus.SUCCESSFUL -> {
                    loadView.gone()
                }
                ApiRequestStatus.FAILED -> {
                    toastL(getString(R.string.error_server_default))
                    loadView.gone()
                }
                ApiRequestStatus.NO_INTERNET -> {
                    loadView.gone()
                    toastL(getString(R.string.error_internet_connection))
                }
            }
        })

        viewModel.eventLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                fillView(it)
            }
        })

        getEventId()?.let {
            viewModel.getEventData(it)
        }
    }

    override fun setListeners() {
    }

    private fun getEventId(): Int? {
        return arguments?.getInt(EXTRA_EVENT_ID)
    }

    private fun fillView(data: EventDetailResponse) {
        ivEvent.loadWithGlide(data.coverSource)
        tvLocation.text = data.place?.name
        tvDate.text = data.startTime?.toStringDate()
    }
}