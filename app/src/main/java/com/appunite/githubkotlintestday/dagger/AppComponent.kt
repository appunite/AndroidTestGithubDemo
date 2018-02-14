package com.appunite.githubkotlintestday.dagger

import com.appunite.githubkotlintestday.App
import com.appunite.githubkotlintestday.dagger.module.ActivityModule
import com.appunite.githubkotlintestday.dagger.module.AndroidModule
import com.appunite.githubkotlintestday.dagger.module.AppModule
import com.appunite.githubkotlintestday.dagger.module.NetworkModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [(AndroidSupportInjectionModule::class), (ActivityModule::class), (NetworkModule::class), (AppModule::class), (AndroidModule::class)])
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}
