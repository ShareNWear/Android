package com.myoutfit.modules.fullscreen

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.myoutfit.R
import com.myoutfit.base.BaseFragment
import com.myoutfit.decorators.HorizontalMarginItemDecoration
import com.myoutfit.models.image.ImageAdapterModel
import com.myoutfit.models.user.UserModel
import com.myoutfit.modules.myoutfit.adapters.ImagesViewPagerAdapter
import com.myoutfit.utils.extentions.loadWithGlideCircleCrop
import kotlinx.android.synthetic.main.fragment_full_screen_image.*
import java.util.ArrayList

class FullScreenImageFragment : BaseFragment() {
    companion object {
        const val EXT_OUTFIT_IMAGE = "ext_outfit_image"
        const val EXT_NAME = "ext_name"
        const val EXT_FRIEND_IMAGE = "ext_friend_name"

        fun newInstance(user: UserModel) = FullScreenImageFragment().apply {
            val bundle = Bundle()
            bundle.putStringArrayList(EXT_OUTFIT_IMAGE, user.images?.map {
                it.path ?: ""
            } as? ArrayList<String>)
            bundle.putString(EXT_FRIEND_IMAGE, user.logoPath)
            bundle.putString(EXT_NAME, user.name)
            this.arguments = bundle
        }
    }

    override fun layoutId(): Int = R.layout.fragment_full_screen_image

    override fun onViewReady(inflatedView: View, args: Bundle?) {
        initViewPager()
        fillView()
    }

    override fun initViewModel() {

    }

    override fun setListeners() {
        btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun fillView() {
        arguments?.getStringArrayList(EXT_OUTFIT_IMAGE)?.let {
            (vpImages.adapter as? ImagesViewPagerAdapter)?.setData(it.map { path ->
                ImageAdapterModel(
                    path = path,
                    id = 0 //useless id value in current screen
                )
            })
        }
        arguments?.getString(EXT_FRIEND_IMAGE)?.let {
            ivFriend.loadWithGlideCircleCrop(it)
        }
        arguments?.getString(EXT_NAME)?.let {
            tvName.text = it
        }
    }

    private fun initViewPager() {
        vpImages.adapter = ImagesViewPagerAdapter({
            //none
        }, true)

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

        vpImages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val currentItemCount = "${position + 1}/${(vpImages.adapter as? ImagesViewPagerAdapter)?.itemCount}"
                tvImageCount.text = currentItemCount
            }
        })
    }
}