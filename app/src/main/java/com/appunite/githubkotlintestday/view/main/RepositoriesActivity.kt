package com.appunite.githubkotlintestday.view.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.appunite.githubkotlintestday.R
import com.appunite.githubkotlintestday.TokenPreferences
import com.appunite.githubkotlintestday.base.BaseViewHolderManager
import com.appunite.githubkotlintestday.dagger.annotations.DaggerAnnotation
import com.appunite.githubkotlintestday.dagger.module.BaseActivityModule
import com.appunite.githubkotlintestday.layoutmanager.MyLinearLayoutManager
import com.appunite.githubkotlintestday.utils.ErrorMessages
import com.appunite.githubkotlintestday.view.BaseActivity
import com.appunite.githubkotlintestday.view.login.LoginActivity
import com.jacekmarchwicki.universaladapter.ViewHolderManager
import com.jacekmarchwicki.universaladapter.rx.RxUniversalAdapter
import dagger.Binds
import kotlinx.android.synthetic.main.activity_main.*
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class RepositoriesActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context) = Intent(context, RepositoriesActivity::class.java)
    }

    @Inject lateinit var tokenPreferences: TokenPreferences
    @Inject lateinit var presenter: RepositoriesPresenter

    private val subscription = CompositeSubscription()

    private val adapter = RxUniversalAdapter(listOf<ViewHolderManager>(
            BaseViewHolderManager(R.layout.item_repository, ::RepositoryViewHolder, RepositoryAdapterItem::class.java)
    ))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (tokenPreferences.getToken() == null) {
            finishAffinity()
            startActivity(LoginActivity.newInstance(this))
        }

        setUpRecyclerView()

        subscription.addAll(

                presenter.itemsObservable
                        .subscribe(adapter),

                presenter.errorObservable
                        .subscribe(ErrorMessages.show(repository_main_view))
        )
    }

    private fun setUpRecyclerView() {
        recyclerView.layoutManager = MyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        subscription.unsubscribe()
    }

    @dagger.Module(includes = [(BaseActivityModule::class)])
    abstract class Module {

        @Binds
        @DaggerAnnotation.ForActivity
        abstract fun provideActivity(activity: LoginActivity): Activity
    }
}
