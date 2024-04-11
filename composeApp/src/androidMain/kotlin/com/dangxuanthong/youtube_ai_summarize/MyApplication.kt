package com.dangxuanthong.youtube_ai_summarize

import android.app.Application
import com.dangxuanthong.youtube_ai_summarize.di.commonModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(commonModules)
        }
    }
}