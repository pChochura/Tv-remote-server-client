package com.pointlessapps.tvremote_server

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.pointlessapps.tvremote_server.services.ServerService

class MainActivity : Activity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startForegroundService(Intent(this, ServerService::class.java))
        Toast.makeText(this, "Service is started", Toast.LENGTH_SHORT).show()
        finish()
    }
}
