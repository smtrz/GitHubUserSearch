package com.tahir.anylinetask.app

import android.app.Application
import at.nineyards.anyline.core.LicenseException
import com.tahir.anylinetask.R
import com.tahir.anylinetask.components.AppLevelComponent
import com.tahir.anylinetask.components.DaggerAppLevelComponent
import com.tahir.anylinetask.modules.ContextModule
import com.tahir.anylinetask.modules.DateModule
import com.tahir.anylinetask.modules.DbRepoModule
import com.tahir.anylinetask.modules.NetModule
import io.anyline.AnylineSDK


class App : Application() {
    lateinit var appLevelComponent: AppLevelComponent


    override fun onCreate() {
        super.onCreate()
        app = this
        // setting up modules.

        appLevelComponent = DaggerAppLevelComponent.builder()
            .contextModule(ContextModule(this))
            .dateModule(DateModule())
            .netModule(NetModule("https://api.github.com/"))
            .dbRepoModule(DbRepoModule())
            .build()
        try {
            AnylineSDK.init(resources.getString(R.string.anyline_license_key), app)
        } catch (e: LicenseException) {
            // handle exception
        }

    }

    companion object {
        lateinit var app: App
    }


}
