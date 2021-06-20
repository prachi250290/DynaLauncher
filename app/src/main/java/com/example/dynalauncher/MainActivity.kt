package com.example.dynalauncher

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dynalauncher.databinding.ActivityMainBinding
import com.example.dynalaunchersdk.controllers.AppInstallationStatusReceiver
import com.example.dynalaunchersdk.controllers.DynaLauncherManager
import com.example.dynalaunchersdk.models.AppDetail


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        var appDetail: List<AppDetail>? = DynaLauncherManager.getSharedInstance()?.getAllApps(this)

        populateList(appDetail);
    }

    fun populateList(appDetails: List<AppDetail>?) {


        var appListAdapter: AppListAdapter = AppListAdapter(this, appDetails?.toMutableList())
        val linearLayoutManager : LinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.appList.layoutManager = linearLayoutManager
        binding.appList.adapter = appListAdapter
        binding.appList.addItemDecoration(DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL))

        appListAdapter?.addAll(appDetails!!)
        appListAdapter.onItemClick = { appDetail ->

            val launchIntent: Intent? = this@MainActivity.getPackageManager()
                .getLaunchIntentForPackage(appDetail.packageName.toString())
            this@MainActivity.startActivity(launchIntent)
        }



        binding?.appSearchView?.imeOptions = EditorInfo.IME_ACTION_DONE
        binding?.appSearchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                appListAdapter?.filter?.filter(newText)
                return false
            }
        })
        binding?.appSearchView?.setOnCloseListener { false }
    }


}