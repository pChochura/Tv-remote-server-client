package com.pointlessapps.tvremote_client.tv

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import androidx.core.os.HandlerCompat
import com.github.kittinunf.fuel.core.awaitUnit
import com.github.kittinunf.fuel.httpPost
import com.pointlessapps.tvremote_client.models.DeviceWrapper
import com.pointlessapps.tvremote_client.utils.SwipeTouchHandler
import com.pointlessapps.tvremote_client.utils.loadDeviceInfo
import com.pointlessapps.tvremote_client.utils.runAsyncCatch

class TvRemote(private val deviceWrapper: DeviceWrapper) {

    fun disconnect() = deviceWrapper.device?.disconnect()

    fun sendKeyEvent(keyCode: Int, action: Int) {
        deviceWrapper.device?.sendKeyEvent(keyCode, action)
    }

    fun sendClick(keyCode: Int) {
        sendKeyEvent(keyCode, KeyEvent.ACTION_DOWN)
        sendKeyEvent(keyCode, KeyEvent.ACTION_UP)
    }

    fun sendLongClick(keyCode: Int) {
        sendKeyEvent(keyCode, KeyEvent.ACTION_DOWN)
        HandlerCompat.postDelayed(Handler(Looper.getMainLooper()), {
            sendKeyEvent(keyCode, KeyEvent.ACTION_UP)
        }, keyCode, SwipeTouchHandler.LONG_CLICK_TIME)
    }

    fun sendIntent(intent: Intent) {
        deviceWrapper.device?.sendIntent(intent)
    }

    fun powerOn(context: Context) = runAsyncCatch({
        "http://${context.loadDeviceInfo()?.uri?.host}:8080/power/on".httpPost().awaitUnit()
    }) { sendClick(KeyEvent.KEYCODE_POWER) }

    fun powerOff(context: Context) = runAsyncCatch({
        "http://${context.loadDeviceInfo()?.uri?.host}:8080/power/off".httpPost().awaitUnit()
    }) { sendClick(KeyEvent.KEYCODE_POWER) }

    fun openApp(context: Context, packageName: String, activityName: String) = runAsyncCatch({
        "http://${context.loadDeviceInfo()?.uri?.host}:8080/open/${packageName}".httpPost()
            .awaitUnit()
    }) {
        sendIntent(Intent(Intent.ACTION_MAIN).apply {
            setClassName(packageName, activityName)
        })
    }
}