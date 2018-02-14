package com.appunite.githubkotlintestday.view.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.widget.EditText
import com.appunite.githubkotlintestday.R
import com.appunite.githubkotlintestday.TokenPreferences
import com.appunite.githubkotlintestday.dagger.annotations.DaggerAnnotation
import com.appunite.githubkotlintestday.dagger.annotations.Scope
import com.appunite.githubkotlintestday.dagger.module.BaseActivityModule
import com.appunite.githubkotlintestday.utils.ErrorMessages
import com.appunite.githubkotlintestday.view.BaseActivity
import com.appunite.githubkotlintestday.view.main.RepositoriesActivity
import com.appunite.rx.ResponseOrError
import com.jakewharton.rxbinding.view.RxView
import com.jakewharton.rxbinding.widget.RxTextView
import dagger.Binds
import dagger.Provides
import kotlinx.android.synthetic.main.login_activity.*
import rx.Observable
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject
import javax.inject.Named

class LoginActivity : BaseActivity() {

    companion object {
        fun newInstance(context: Context) = Intent(context, LoginActivity::class.java)
    }

    @Inject lateinit var presenter: LoginPresenter
    @Inject lateinit var tokenPreferences: TokenPreferences

    private val subscription: CompositeSubscription = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val snackbar =
                subscription.addAll(

                        RxTextView.textChanges(username_edit_text)
                                .subscribe { username_text_input.error = null },

                        RxTextView.textChanges(password_edit_text)
                                .subscribe { password_text_input.error = null },

                presenter.requestSignInObservable
                        .compose(ResponseOrError.onlySuccess())
                        .subscribe { startActivity(RepositoriesActivity.newIntent(this)) },

                        presenter.requestSignInObservable
                                .compose(ResponseOrError.onlyError())
                                .doOnNext(ErrorMessages.show(container))
                                .subscribe { tokenPreferences.edit().clear() }
                )
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


        @dagger.Module
        companion object {

            @JvmStatic
            @Provides
            @Scope.Activity
            @Named("SignInClickObservable")
            fun provideUserCredentials(activity: LoginActivity): Observable<Pair<String, String>> = RxView
                    .clicks(activity.findViewById(R.id.login_button))
                    .map {
                        Pair(
                                activity.findViewById<EditText>(R.id.username_edit_text).text.toString(),
                                activity.findViewById<EditText>(R.id.password_edit_text).text.toString()
                        )
                    }
                    .filter { credentials ->
                        when {
                            credentials.first.isEmpty() -> {
                                activity.findViewById<TextInputLayout>(R.id.username_text_input).error = "Cannot be empty"
                                false
                            }
                            credentials.second.isEmpty() -> {
                                activity.findViewById<TextInputLayout>(R.id.password_text_input).error = "Cannot be empty"
                                false
                            }
                            else -> true
                        }
                    }.share()
        }
    }
}