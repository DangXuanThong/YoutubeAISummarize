package com.dangxuanthong.youtubeaisummarize.di

import com.dangxuanthong.youtubeaisummarize.ui.MainViewModel
import org.koin.dsl.module

val commonModules = module {
    factory {
        MainViewModel()
    }
}
