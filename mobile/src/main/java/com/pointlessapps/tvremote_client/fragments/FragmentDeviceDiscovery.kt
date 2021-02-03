package com.pointlessapps.tvremote_client.fragments

import androidx.lifecycle.ViewModelProvider
import com.pointlessapps.tvremote_client.R
import com.pointlessapps.tvremote_client.utils.Utils
import com.pointlessapps.tvremote_client.viewModels.ViewModelDeviceDiscovery

class FragmentDeviceDiscovery : FragmentBaseImpl() {

    private lateinit var viewModel: ViewModelDeviceDiscovery

    override fun getLayoutId() = R.layout.fragment_device_discovery

    override fun created() {
        viewModel = ViewModelProvider(
            this,
            Utils.getViewModelFactory(activity(), root())
        ).get(ViewModelDeviceDiscovery::class.java)

        onPauseActivity = { viewModel.onPauseActivityListener?.invoke() }
//        onResumeActivity = { viewModel.onResumeActivityListener?.invoke() }
        viewModel.onChangeFragmentListener = onChangeFragment
        viewModel.setDeviceListener()
        viewModel.loadDeviceInfo()
        viewModel.startDiscovery()
        viewModel.setDeviceList()
        viewModel.setClickListeners()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setDeviceListener()
        viewModel.loadDeviceInfo()
        viewModel.startDiscovery()
        viewModel.setDeviceList()
    }
}