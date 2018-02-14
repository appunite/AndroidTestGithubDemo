package com.appunite.githubkotlintestday.dao

import com.appunite.githubkotlintestday.api.ApiService
import com.appunite.githubkotlintestday.api.model.Repository
import com.appunite.rx.ResponseOrError
import com.appunite.rx.dagger.NetworkScheduler
import com.appunite.rx.operators.MoreOperators
import rx.Observable
import rx.Scheduler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubDao @Inject constructor(private val apiService: ApiService,
                                    @NetworkScheduler private val networkScheduler: Scheduler) {

    fun getUserRepositoriesObservable(): Observable<ResponseOrError<List<Repository>>> {
        return apiService.getUserRepositories()
                .subscribeOn(networkScheduler)
                .compose(ResponseOrError.toResponseOrErrorObservable())
                .compose(MoreOperators.repeatOnError(networkScheduler))
                .compose(MoreOperators.cacheWithTimeout(networkScheduler))
    }
}
