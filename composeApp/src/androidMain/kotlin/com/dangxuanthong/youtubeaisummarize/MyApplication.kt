package com.dangxuanthong.youtubeaisummarize

import android.app.Application
import com.dangxuanthong.youtubeaisummarize.di.commonModules
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
