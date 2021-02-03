package com.pointlessapps.tvremote_client.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.pointlessapps.tvremote_client.R
import com.pointlessapps.tvremote_client.models.DeviceWrapper
import com.pointlessapps.tvremote_client.utils.Utils
import com.pointlessapps.tvremote_client.utils.loadShowDpad
import com.pointlessapps.tvremote_client.utils.loadTurnTvOn
import com.pointlessapps.tvremote_client.viewModels.ViewModelDevice
import com.pointlessapps.tvremote_client.viewModels.ViewModelRemote

class FragmentRemote : FragmentBaseImpl() {

    private lateinit var viewModel: ViewModelRemote
    private lateinit var deviceWrapper: DeviceWrapper

    override fun onAttach(context: Context) {
        super.onAttach(context)
        deviceWrapper = ViewModelProvider(
            activity(),
            Utils.getViewModelFactory(activity())
        ).get(ViewModelDevice::class.java).deviceWrapper
    }

    override fun getLayoutId() = R.layout.fragment_remote

    override fun created() {
        viewModel = ViewModelProvider(
            this,
            Utils.getViewModelFactory(activity(), root(), deviceWrapper)
        ).get(ViewModelRemote::class.java)

        if (requireContext().loadTurnTvOn()) {
            viewModel.powerOn()
        }
        if (requireContext().loadShowDpad()) {
            viewModel.setDpad()
        } else {
            viewModel.setTouchHandler()
        }
        viewModel.setClickListeners()
        viewModel.setApplicationsList()
        viewModel.setStateListeners()
        onPauseActivity = { viewModel.onPauseActivityListener?.invoke() }
        onResumeActivity = { viewModel.onResumeActivityListener?.invoke() }
        onDispatchKeyEvent = { viewModel.onDispatchKeyEventListener?.invoke(it) ?: false }
        viewModel.onPopBackStackListener = { onPopBackStack?.invoke() }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (!viewModel.onPermissionResult(requestCode, grantResults)) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}