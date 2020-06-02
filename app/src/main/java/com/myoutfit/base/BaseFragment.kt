package com.myoutfit.base

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.myoutfit.android.Injectable
import com.myoutfit.dialogs.DialogLoading

abstract class BaseFragment : Fragment(), Injectable {

    private var dialogLoading: DialogLoading? = null
    /*Using for getting layout id to be inflated*/
    @LayoutRes
    abstract fun layoutId(): Int

    /*Using for triggering inflating finished event*/
    abstract fun onViewReady(inflatedView: View, args: Bundle?)

    /*Using for initializing view models with viewModelProvider*/
    abstract fun initViewModel()

    interface IOnDialogButtonClickListener {
        fun onButtonClicked()
    }

    interface IOnDialogDoubleButtonClickListener : IOnDialogButtonClickListener {
        fun onLeftButtonClicked()
    }

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
        context?.let { dialogLoading = DialogLoading(it) }
    }

/*    protected fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
    }*/

    protected fun showLoading() {
        dialogLoading?.show()
    }

    protected fun hideLoading() {
        dialogLoading?.dismiss()
    }

    abstract fun setListeners()
}