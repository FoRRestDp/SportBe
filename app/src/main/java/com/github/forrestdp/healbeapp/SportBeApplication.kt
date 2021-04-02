package com.github.forrestdp.healbeapp

import android.app.Application
import timber.log.Timber

class SportBeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}