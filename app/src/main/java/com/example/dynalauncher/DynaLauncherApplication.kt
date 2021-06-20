package com.example.dynalauncher

import android.app.Application
import com.example.dynalaunchersdk.controllers.DynaLauncherManager

class DynaLauncherApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        DynaLauncherManager.getSharedInstance()?.registerAppInstallUninstallReceiver(this)
    }
}