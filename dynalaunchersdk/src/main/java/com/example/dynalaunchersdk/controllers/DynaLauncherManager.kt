package com.example.dynalaunchersdk.controllers

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import com.example.dynalaunchersdk.models.AppDetail

class DynaLauncherManager {

    companion object {
        private var sharedInstance : DynaLauncherManager? = null;
        fun getSharedInstance(): DynaLauncherManager? {
            if(sharedInstance ==  null) {
                sharedInstance = DynaLauncherManager();
            }
            return sharedInstance
        }
    }

    fun getAllApps(context: Context): List<AppDetail> {
        val apps: List<AppDetail>
        val manager: PackageManager = context.packageManager
        apps = ArrayList<AppDetail>()

        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)

        val availableActivities: List<ResolveInfo> = manager.queryIntentActivities(i, 0)
        for (ri in availableActivities) {
            val app = AppDetail()
            app.label = ri.loadLabel(manager)
            app.packageName = ri.activityInfo.packageName
            app.icon = ri.activityInfo.loadIcon(manager)
            app.activityName = ri.activityInfo.parentActivityName
            apps.add(app)
        }

        return apps.sortedWith(
            compareBy { it.label.toString() })
    }

    fun registerAppInstallUninstallReceiver (context: Context) {
        val br = AppInstallationStatusReceiver();
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        intentFilter.addDataScheme("package")
        context.registerReceiver(br, intentFilter)
    }

}