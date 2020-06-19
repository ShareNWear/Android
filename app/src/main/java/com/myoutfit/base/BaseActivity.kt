package com.myoutfit.base

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> =
        dispatchingAndroidInjector


    companion object {
        private const val REQUEST_PERMISSIONS = 12
    }

    private var permissionCallback: IPermissionResultCallback? = null


    /**
     * Callback interfaces
     * Uses for getting now if the requested permissions are granted
     * */
    interface IPermissionResultCallback{
        fun onPermissionGranted()
        fun onPermissionDenied()
    }

    fun checkPermissions(permissions: List<String>, callback: IPermissionResultCallback){
        permissionCallback = callback
        val notGrantedPermissionList = mutableListOf<String>()
        permissions.forEach {
            if(ContextCompat.checkSelfPermission(this@BaseActivity,it)
                != PackageManager.PERMISSION_GRANTED){
                notGrantedPermissionList.add(it)
            }
        }
        if(notGrantedPermissionList.isEmpty()){
            permissionCallback?.onPermissionGranted()
            permissionCallback = null
            return
        }

        ActivityCompat.requestPermissions(
            this,
            notGrantedPermissionList.toTypedArray(),
            REQUEST_PERMISSIONS
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_PERMISSIONS){
            if(grantResults.isNotEmpty()){
                var isGranted = true
                grantResults.forEach {
                    if(it == PackageManager.PERMISSION_DENIED){
                        isGranted = false
                    }
                }
                if(isGranted){
                    permissionCallback?.onPermissionGranted()
                }else{
                    permissionCallback?.onPermissionDenied()
                }
            }else{
                permissionCallback?.onPermissionDenied()
            }
            permissionCallback = null
        }
    }

}