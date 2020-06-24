package com.myoutfit.modules.myoutfit

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.myoutfit.R
import com.myoutfit.base.BaseFragment
import com.myoutfit.decorators.HorizontalMarginItemDecoration
import com.myoutfit.di.AppViewModelsFactory
import com.myoutfit.models.image.ImageAdapterModel
import com.myoutfit.models.network.ApiRequestStatus
import com.myoutfit.modules.eventdetail.EventDetailFragment.Companion.EXTRA_EVENT_ID
import com.myoutfit.modules.myoutfit.adapters.ImagesViewPagerAdapter
import com.myoutfit.utils.extentions.*
import kotlinx.android.synthetic.main.fragment_myoutfit.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lolodev.permissionswrapper.callback.OnRequestPermissionsCallBack
import lolodev.permissionswrapper.wrapper.PermissionWrapper
import javax.inject.Inject

class MyOutfitFragment : BaseFragment(), IConfirmPhotoFragmentListener {

    companion object {
        const val SELECT_MEDIA_REQUEST_CODE = 333
    }

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
                    showNoInternetDialog {
                      getMyImages()
                    }
                }
            }
        })

        viewModel.myImagesLiveData.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                clNoImage.gone()
                (vpImages.adapter as? ImagesViewPagerAdapter)?.setData(it.map { model ->
                    ImageAdapterModel(
                        path = model.path,
                        imageName = model.name,
                        id = model.id
                    )
                })
                /*update counter manually, because onPageSelected callback doesn't trigger sometimes,
                it is some bug of viewpager2*/
                val currentItemPosition = vpImages.currentItem
                updateItemCount(currentItemPosition)
            } else {
                clNoImage.show()
            }
        })

        viewModel.deletedImageIdLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                (vpImages.adapter as? ImagesViewPagerAdapter)?.removeImage(it)

                /*update counter manually, because onPageSelected callback doesn't trigger sometimes,
                 it is some bug of viewpager2*/
                val currentItemPosition = vpImages.currentItem
                updateItemCount(currentItemPosition)

                if ((vpImages.adapter as? ImagesViewPagerAdapter)?.itemCount == 0) {
                    clNoImage.show()
                }
            }
        })

        viewModel.uploadImageStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    getMyImages()
                }
            }
        })

        getMyImages()
    }

    override fun setListeners() {
        btnBack.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.nav_host).popBackStack()
        }
        btnAddPhoto.setOnClickListener {
            val itemCount = getUploadedPhotoCount()
            if (itemCount != null && itemCount >= 5) {
                toastSh(getString(R.string.warning_max_upload_size))
                return@setOnClickListener
            }
            checkStoragePermission()
            /*prevent double clicking*/
            GlobalScope.launch(Dispatchers.Main) {
                btnAddPhoto.isEnabled = false
                delay(100)
                btnAddPhoto.isEnabled = true
            }
        }
    }

    private fun getMyImages() {
        getEventId()?.let {
            viewModel.getMyImages(it)
        }
    }

    private fun getEventId(): Int? {
        return arguments?.getInt(EXTRA_EVENT_ID)
    }

    private fun initViewPager() {
        vpImages.adapter = ImagesViewPagerAdapter({ model ->
            viewModel.removePhoto(model)
        })

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

        vpImages.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateItemCount(position)
            }
        })
    }

    private fun getUploadedPhotoCount(): Int? {
        return (vpImages.adapter as? ImagesViewPagerAdapter)?.itemCount
    }

    private fun updateItemCount(position: Int) {
        val itemCount = getUploadedPhotoCount()
        if (itemCount != null && itemCount > 1) {
            tvImageCount.show()
            val currentItemCount = "${position + 1}/${itemCount}"
            tvImageCount.text = currentItemCount
        } else {
            tvImageCount.gone()
        }
    }

    private fun uploadFromGallery() {
        val pickerIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.INTERNAL_CONTENT_URI
        )
        pickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*"))
        pickerIntent.action = Intent.ACTION_GET_CONTENT
        pickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) //for multiple selection
        startActivityForResult(pickerIntent, SELECT_MEDIA_REQUEST_CODE)
    }

    private fun onPhotoSelected(imageList: List<String>) {
        showConfirmScreen(imageList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_MEDIA_REQUEST_CODE) {
                val mediaList = mutableListOf<String>()
                if (data?.clipData != null) {
                    val mClipData = data.clipData
                    for (i in 0 until mClipData!!.itemCount) {
                        val item = mClipData.getItemAt(i)
                        val selectedMedia = item.uri
                        context?.getPath(selectedMedia)?.let {
                            mediaList.add(it)
                        }
                    }
                } else {
                    if (data?.data != null) {
                        val selectedMedia = data.data
                        context?.getPath(selectedMedia)?.let {
                            mediaList.add(it)
                        }
                    }
                }
                onPhotoSelected(mediaList)
            }
        }
    }

    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            PermissionWrapper.Builder(requireContext())
                .addPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
                //enable rationale message with a custom message
                .addPermissionRationale(
                    getString(R.string.storage_perm_rationale)
                )
                //show settings dialog,in this case with default message base on requested permission/s
                .addPermissionsGoSettings(true)
                .addRequestPermissionsCallBack(object : OnRequestPermissionsCallBack {
                    override fun onDenied(p0: String?) {
                        /*none*/
                    }

                    override fun onGrant() {
                        uploadFromGallery()
                    }
                })
                .build().request()
        } else {
            uploadFromGallery()
        }
    }

    private fun showConfirmScreen(imageList: List<String>) {
        val fragment =
            ConfirmPhotoFragment.newInstance(imageList, getUploadedPhotoCount())
        childFragmentManager
            .beginTransaction()
            .add(R.id.fragmentContainer, fragment, ConfirmPhotoFragment::class.java.simpleName)
            .addToBackStack(ConfirmPhotoFragment::class.java.simpleName)
            .commit()
    }

    override fun onImagesSelected(images: List<String>) {
        getEventId()?.let { id ->
            viewModel.uploadPhoto(id, images)
        }
    }
}