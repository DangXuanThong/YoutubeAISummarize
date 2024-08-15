package com.dangxuanthong.youtubeaisummarize.di

import com.dangxuanthong.youtubeaisummarize.ui.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val commonModules = module {
    viewModelOf(::MainViewModel)
}
