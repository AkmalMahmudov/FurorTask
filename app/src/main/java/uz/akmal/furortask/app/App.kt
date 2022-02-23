package uz.akmal.furortask.app

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class App : Application() {
    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    open fun getContext(): Context {
        return instance
        // or return instance.getApplicationContext();
    }
}