package com.example.dynalaunchersdk.controllers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import java.util.*

class AppInstallationStatusReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent!!.action.equals("android.intent.action.PACKAGE_ADDED")) {        // install
            val packageName = intent.dataString
            Toast.makeText(context, "USER INSTALLED : $packageName", Toast.LENGTH_SHORT).show()
        }

        if (intent.action.equals("android.intent.action.PACKAGE_REMOVED")) {    // uninstall
            val packageName = intent.dataString
            Toast.makeText(context, "USER UNINSTALLED : $packageName", Toast.LENGTH_SHORT).show()
        }

    }
}