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
import com.myoutfit.utils.extentions.toastSh
import kotlinx.android.synthetic.main.fragment_confirm_photo.*

class ConfirmPhotoFragment : BaseFragment() {

    companion object {
        const val EXT_IMAGES = "ext_images"
        const val EXT_UPLOADED_IMAGES_COUNT = "ext_uploaded_images_count"
        fun newInstance(imageList: List<String>, uploadedImagesCount: Int?) = ConfirmPhotoFragment().apply {
            val bundle = Bundle()
            bundle.putStringArrayList(EXT_IMAGES, imageList as? ArrayList<String>)
            bundle.putInt(EXT_UPLOADED_IMAGES_COUNT, uploadedImagesCount ?: 0)
            this.arguments = bundle
        }
    }

    private var confirmPhotoFragmentListener: IConfirmPhotoFragmentListener? = null

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
            /*calculate uploaded images + confirm image
            * sum should not be more than 5*/
            val imageCount =  arguments?.getInt(EXT_UPLOADED_IMAGES_COUNT)?.plus(
                (vpImages.adapter as? ImagesViewPagerAdapter)?.itemCount ?: 0
            ) ?: 0

            if (imageCount > 5) {
                toastSh(getString(R.string.warning_max_upload_size))
                return@setOnClickListener
            }
            (vpImages.adapter as? ImagesViewPagerAdapter)?.getData()?.map {
                it.path ?: ""
            }?.let {
                confirmPhotoFragmentListener?.onImagesSelected((it))
            }
            activity?.onBackPressed()
        }
    }

    private fun fillView() {
        arguments?.getStringArrayList(EXT_IMAGES)?.let {
            (vpImages.adapter as? ImagesViewPagerAdapter)?.setData(it.map { path ->
                ImageAdapterModel(
                    path = path,
                    imageName = "",
                    id = 0 //useless id value in current screen
                )
            })
        }
    }

    private fun initViewPager() {
        vpImages.adapter = ImagesViewPagerAdapter({
            /*if removed last image go back*/
            if ((vpImages.adapter as? ImagesViewPagerAdapter)?.itemCount == 1) {
                activity?.onBackPressed()
            } else {
                (vpImages.adapter as? ImagesViewPagerAdapter)?.removeImage(it)
            }
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
    fun onImagesSelected(images: List<String>)
}