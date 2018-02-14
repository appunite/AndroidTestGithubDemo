package com.appunite.githubkotlintestday.dagger.annotations

import javax.inject.Scope

object Scope {

    @Scope
    @MustBeDocumented
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Activity

}
