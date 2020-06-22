package com.myoutfit.modules.myoutfit

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.myoutfit.R
import com.myoutfit.base.BaseFragment
import com.myoutfit.decorators.HorizontalMarginItemDecoration
import com.myoutfit.models.image.ImageAdapterModel
import com.myoutfit.modules.myoutfit.adapters.ImagesViewPagerAdapter
import com.myoutfit.utils.extentions.gone
import com.myoutfit.utils.extentions.show
import kotlinx.android.synthetic.main.fragment_confirm_photo.*

class ConfirmPhotoFragment : BaseFragment() {

    companion object {
        const val EXT_IMAGES = "ext_images"

        fun newInstance(imageList: List<String>) = ConfirmPhotoFragment().apply {
            val bundle = Bundle()
            bundle.putStringArrayList(EXT_IMAGES, imageList as? ArrayList<String>)
            this.arguments = bundle
        }
    }

    var confirmPhotoFragmentListener: IConfirmPhotoFragmentListener? = null

    override fun layoutId(): Int = R.layout.fragment_confirm_photo

    override fun onViewReady(inflatedView: View, args: Bundle?) {
        initViewPager()
        fillView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is IConfirmPhotoFragmentListener) {
            confirmPhotoFragmentListener = (parentFragment as IConfirmPhotoFragmentListener)
        }
    }

    override fun initViewModel() {
    }

    override fun setListeners() {
        btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        btnOK.setOnClickListener {
            confirmPhotoFragmentListener?.onImagesSelected()
            activity?.onBackPressed()
        }
    }

    private fun fillView() {
        arguments?.getStringArrayList(EXT_IMAGES)?.let {
            (vpImages.adapter as? ImagesViewPagerAdapter)?.setData(it.map { path ->
                ImageAdapterModel(
                    path = path,
                    id = 0 //useless id value in current screen
                )
            })
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
                updateItemCount(position)
            }
        })
    }

    private fun updateItemCount(position: Int) {
        val itemCount = (vpImages.adapter as? ImagesViewPagerAdapter)?.itemCount
        if (itemCount != null && itemCount > 1) {
            tvImageCount.show()
            val currentItemCount = "${position + 1}/${itemCount}"
            tvImageCount.text = currentItemCount
        } else {
            tvImageCount.gone()
        }
    }
}

interface IConfirmPhotoFragmentListener {
    fun onImagesSelected()
}