package com.appunite.githubkotlintestday.dagger.module

import android.content.Context
import com.appunite.githubkotlintestday.App
import com.appunite.githubkotlintestday.TokenPreferences
import com.appunite.githubkotlintestday.dagger.annotations.DaggerAnnotation
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    @DaggerAnnotation.ForApplication
    fun provideContext(application: App): Context = application.applicationContext

    @Provides
    @Singleton
    fun userPreferences(@DaggerAnnotation.ForApplication context: Context): TokenPreferences = TokenPreferences(context)
}
