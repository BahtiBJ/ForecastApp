package com.bbj.myapplication.view

import android.app.Application
import com.bbj.myapplication.di.AppComponent
import com.bbj.myapplication.di.DaggerAppComponent

class MainApplication : Application() {

    lateinit var appComponent : AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().setContext(this).build()
    }


}