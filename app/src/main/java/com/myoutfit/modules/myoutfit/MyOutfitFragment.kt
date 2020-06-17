package com.myoutfit.modules.myoutfit

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.myoutfit.R
import com.myoutfit.base.BaseFragment
import com.myoutfit.decorators.HorizontalMarginItemDecoration
import com.myoutfit.di.AppViewModelsFactory
import com.myoutfit.models.network.ApiRequestStatus
import com.myoutfit.modules.eventdetail.EventDetailFragment.Companion.EXTRA_EVENT_ID
import com.myoutfit.modules.myoutfit.adapters.ImagesViewPagerAdapter
import com.myoutfit.utils.extentions.goneWithAnimationAlpha
import com.myoutfit.utils.extentions.showWithAnimationAlpha
import com.myoutfit.utils.extentions.toastL
import kotlinx.android.synthetic.main.fragment_myoutfit.*
import javax.inject.Inject

class MyOutfitFragment : BaseFragment() {

    @Inject
    lateinit var vmFactory: AppViewModelsFactory

    private lateinit var viewModel: MyOutfitViewModel

    override fun layoutId(): Int = R.layout.fragment_myoutfit

    override fun onViewReady(inflatedView: View, args: Bundle?) {
        initViewPager()
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProviders.of(this, vmFactory).get(MyOutfitViewModel::class.java)

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
                    toastL(getString(R.string.error_internet_connection))
                }
            }
        })

        viewModel.myImagesLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                (vpImages.adapter as? ImagesViewPagerAdapter)?.setData(it)
            }
        })

        getEventId()?.let {
            viewModel.getMyImages(it)
        }
    }

    override fun setListeners() {
        btnBack.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.nav_host).popBackStack()
        }
    }

    private fun getEventId(): Int? {
        return arguments?.getInt(EXTRA_EVENT_ID)
    }

    private fun initViewPager() {
        vpImages.adapter = ImagesViewPagerAdapter()

        vpImages.offscreenPageLimit = 1

        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            page.scaleY = 1 - (0.1f * kotlin.math.abs(position))
        }
        vpImages.setPageTransformer(pageTransformer)

        val itemDecoration = HorizontalMarginItemDecoration(
            requireContext(),
            R.dimen.viewpager_current_item_horizontal_margin
        )
        vpImages.addItemDecoration(itemDecoration)
    }
}