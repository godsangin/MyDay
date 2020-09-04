package com.msproject.myhome.mydays.application

import androidx.multidex.MultiDexApplication
import android.util.Log
import com.msproject.myhome.mydays.di.AppComponent
import com.msproject.myhome.mydays.di.DaggerAppComponent
import kotlin.system.exitProcess

class MyApplication : MultiDexApplication() {

    companion object {
        private val TAG = MyApplication::class.java.simpleName
    }

    val appComponent:AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
    private var androidDefaultUncaughtExceptionHandler: Thread.UncaughtExceptionHandler? = null
    private lateinit var uncaughtExceptionHandler:UncaughtExceptionHandler

    override fun onCreate() {
        androidDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        uncaughtExceptionHandler = UncaughtExceptionHandler()
        super.onCreate()
    }

    class UncaughtExceptionHandler:Thread.UncaughtExceptionHandler{
        override fun uncaughtException(t: Thread, ex: Throwable) {
            Log.e(TAG, "error ------------>$ex")
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(0)
        }
    }
}