package com.appunite.githubkotlintestday.view.main

import com.appunite.githubkotlintestday.dao.GithubDao
import com.appunite.rx.ResponseOrError
import com.jacekmarchwicki.universaladapter.BaseAdapterItem
import rx.subjects.PublishSubject
import javax.inject.Inject

class RepositoriesPresenter @Inject constructor(
        githubDao: GithubDao
) {
    private val openIssuesForRepository = PublishSubject.create<Any>()

    private val repositoriesObservable = githubDao.getUserRepositoriesObservable()
            .replay(1)
            .refCount()

    val itemsObservable = repositoriesObservable
            .compose(ResponseOrError.onlySuccess())
            .map { repositories -> repositories.mapTo(mutableListOf<BaseAdapterItem>()) { RepositoryAdapterItem(it, openIssuesForRepository) } }

    val errorObservable = repositoriesObservable
            .compose(ResponseOrError.onlyError())
}
