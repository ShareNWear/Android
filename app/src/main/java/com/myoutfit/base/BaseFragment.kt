package com.myoutfit.base

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.myoutfit.R
import com.myoutfit.di.Injectable

abstract class BaseFragment : Fragment(), Injectable {

    /*Using for getting layout id to be inflated*/
    @LayoutRes
    abstract fun layoutId(): Int

    /*Using for triggering inflating finished event*/
    abstract fun onViewReady(inflatedView: View, args: Bundle?)

    /*Using for initializing view models with viewModelProvider*/
    abstract fun initViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context).inflate(layoutId(), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setListeners()
        initViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewReady(view, savedInstanceState)
    }

    abstract fun setListeners()

    fun showNoInternetDialog(onReload: () -> Unit) {
        AlertDialog.Builder(activity, R.style.Theme_AppCompat_Light_Dialog).apply {
            setTitle(getString(R.string.no_internet))
            setMessage(getString(R.string.error_internet_connection))
            setPositiveButton(getString(R.string.reload)) { dialog, _ ->
                dialog.cancel()
                onReload()
            }
            setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }
        }.create().show()
    }
}