package com.myoutfit.modules.events

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.login.LoginManager
import com.myoutfit.R
import com.myoutfit.base.BaseFragment
import com.myoutfit.data.locale.sharedpreferences.AppSharedPreferences
import com.myoutfit.decorators.EventSpaceDecorator
import com.myoutfit.di.AppViewModelsFactory
import com.myoutfit.models.network.ApiRequestStatus
import com.myoutfit.modules.eventdetail.EventDetailFragment.Companion.EXTRA_EVENT_ID
import com.myoutfit.modules.events.adapters.EventAdapter
import com.myoutfit.utils.extentions.goneWithAnimationAlpha
import com.myoutfit.utils.extentions.showWithAnimationAlpha
import com.myoutfit.utils.extentions.toastL
import kotlinx.android.synthetic.main.fragment_events.*
import javax.inject.Inject

class EventsFragment : BaseFragment() {

    @Inject
    lateinit var vmFactory: AppViewModelsFactory

    @Inject
    lateinit var sp: AppSharedPreferences

    private lateinit var viewModel: EventsViewModel

    override fun layoutId(): Int = R.layout.fragment_events

    override fun onViewReady(inflatedView: View, args: Bundle?) {
        initRecycle()
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProviders.of(this, vmFactory).get(EventsViewModel::class.java)

        viewModel.eventsLiveData.observe(viewLifecycleOwner, Observer {
            it?.let { data ->
                (rvEvents.adapter as? EventAdapter)?.setData(data)
            }
        })
        viewModel.requestStatusLiveData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ApiRequestStatus.RUNNING -> {
                    loadView.showWithAnimationAlpha()
                }
                ApiRequestStatus.SUCCESSFUL -> {
                    loadView.goneWithAnimationAlpha()
                }
                ApiRequestStatus.FAILED -> {
                    toastL(getString(R.string.error_server_default))
                    loadView.goneWithAnimationAlpha()
                }
                ApiRequestStatus.NO_INTERNET -> {
                    loadView.goneWithAnimationAlpha()
                    showNoInternetDialog {
                        viewModel.getEvents(true)
                    }
                }
            }
        })

        /*if data empty - show loader*/
        val setLoader = viewModel.eventsLiveData.value.isNullOrEmpty()
        viewModel.getEvents(setLoader)
    }

    private fun initRecycle() {
        with(rvEvents) {
            layoutManager = LinearLayoutManager(context)
            adapter = EventAdapter { event ->
                event.id?.let { eventId ->
                    Navigation.findNavController(requireActivity(), R.id.nav_host)
                        .navigate(R.id.action_open_event_detail,
                            Bundle().apply {
                                putInt(EXTRA_EVENT_ID, eventId)
                            })
                }
            }
        /*    val margin = resources.getDimensionPixelSize(R.dimen.margin_very_small)
            addItemDecoration(EventSpaceDecorator(margin))*/
        }
    }

    override fun setListeners() {
        btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        sp.clearAuthKey()
        LoginManager.getInstance().logOut()
        Navigation.findNavController(requireActivity(), R.id.nav_host).navigate(R.id.action_logout)
    }
}