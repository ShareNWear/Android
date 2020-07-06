package com.myoutfit.modules.eventdetail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.myoutfit.R
import com.myoutfit.base.BaseFragment
import com.myoutfit.di.AppViewModelsFactory
import com.myoutfit.models.events.EventDetailResponse
import com.myoutfit.models.network.ApiRequestStatus
import com.myoutfit.models.user.UserModel
import com.myoutfit.modules.eventdetail.adapters.OutfitImagesAdapter
import com.myoutfit.modules.fullscreen.FullScreenImageFragment
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
        initRecycle()
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProviders.of(this, vmFactory).get(EventDetailViewModel::class.java)

        viewModel.requestStatusLiveData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ApiRequestStatus.RUNNING -> {
                    loadView.showWithAnimationAlpha()
                }
                ApiRequestStatus.SUCCESSFUL -> {
                    loadView.goneWithAnimationAlpha()
                }
                ApiRequestStatus.FAILED -> {
                    toastL(it.error?.error ?: getString(R.string.error_server_default))
                    loadView.goneWithAnimationAlpha()
                }
                ApiRequestStatus.NO_INTERNET -> {
                    loadView.goneWithAnimationAlpha()
                    showNoInternetDialog {
                        getEventId()?.let { id ->
                            viewModel.getEventData(id)
                        }
                    }
                }
            }
        })

        viewModel.eventLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                fillView(it)
            }
        })

        getEventId()?.let { id ->
            viewModel.getEventData(id)
        }
    }

    override fun setListeners() {
        btnBack.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.nav_host).popBackStack()
        }
        btnMyOutfit.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.nav_host).navigate(R.id.action_open_myoutfit,
                Bundle().apply {
                    getEventId()?.let {
                        putInt(EXTRA_EVENT_ID, it)
                    }
                })
        }
    }

    private fun getEventId(): Int? {
        return arguments?.getInt(EXTRA_EVENT_ID)
    }

    private fun fillView(data: EventDetailResponse) {
        btnMyOutfit.show()
        tint.show()
        ivEvent.loadWithGlide(data.coverSource)
        tvEventName.text = data.name
        tvLocation.text = data.place?.name
        tvDate.text = data.startTime?.toStringDate()
        data.users?.let {
            (rvOutfit.adapter as? OutfitImagesAdapter)?.setData(data.users.asReversed())
        }
    }

    private fun initRecycle() {
        with(rvOutfit) {
            layoutManager = GridLayoutManager(context, 2)
            adapter = OutfitImagesAdapter { user ->
                showFullScreen(user)
            }
        }
    }

    private fun showFullScreen(image: UserModel) {
        val fragment = FullScreenImageFragment.newInstance(image)
        childFragmentManager
            .beginTransaction()
            .add(R.id.fragmentContainer, fragment, FullScreenImageFragment::class.java.simpleName)
            .addToBackStack(FullScreenImageFragment::class.java.simpleName)
            .commit()
    }
}