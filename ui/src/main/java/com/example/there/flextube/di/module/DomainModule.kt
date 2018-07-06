package com.example.there.flextube.di.module

import com.example.there.domain.repo.IMainRepository
import com.example.there.domain.usecase.impl.GetUserSubscriptions
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class DomainModule {

    @Provides
    fun getUserSubscriptions(repository: IMainRepository): GetUserSubscriptions =
            GetUserSubscriptions(repository, Schedulers.io(), AndroidSchedulers.mainThread())

}