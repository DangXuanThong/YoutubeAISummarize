package com.dangxuanthong.youtube_video_summarize.di

import com.dangxuanthong.youtube_video_summarize.ui.MainViewModel
import org.koin.dsl.module

val commonModules = module {
    factory {
        MainViewModel()
    }
}
