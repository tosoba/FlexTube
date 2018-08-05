package com.example.there.flextube.addgroup

import android.util.Log
import com.example.there.domain.usecase.impl.AddSubscriptionsToGroup
import com.example.there.domain.usecase.impl.GetSavedSubscriptions
import com.example.there.domain.usecase.impl.GetSubscriptionsNotAddedToGroup
import com.example.there.domain.usecase.impl.InsertSubscriptionGroup
import com.example.there.flextube.base.vm.BaseViewModel
import com.example.there.flextube.model.UiSubscriptionToChoose
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddGroupViewModel @Inject constructor(
        private val getSavedSubscriptions: GetSavedSubscriptions,
        private val getSubscriptionsNotAddedToGroup: GetSubscriptionsNotAddedToGroup,
        private val insertSubscriptionGroup: InsertSubscriptionGroup,
        private val addSubscriptionsToGroup: AddSubscriptionsToGroup
) : BaseViewModel() {

    val viewState = AddGroupViewState()

    fun loadSubscriptions(accountName: String) {
        disposables.add(getSavedSubscriptions.execute(accountName)
                .subscribeOn(Schedulers.io())
                .map { it.filter { !viewState.subscriptions.map { it.subscription }.contains(it) } }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isNotEmpty()) {
                        viewState.subscriptions.addAll(it.map { UiSubscriptionToChoose(it) })
                    } else {
                        viewState.noSubscriptions.set(true)
                    }
                }, { Log.e(javaClass.name, it.message) }))
    }

    fun loadNotAddedSubscriptions(accountName: String, groupName: String) {
        disposables.add(getSubscriptionsNotAddedToGroup.execute(GetSubscriptionsNotAddedToGroup.Params(accountName, groupName))
                .subscribeOn(Schedulers.io())
                .map { it.filter { !viewState.subscriptions.map { it.subscription }.contains(it) } }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewState.subscriptions.addAll(it.map { UiSubscriptionToChoose(it) }) }, { Log.e(javaClass.name, it.message) }))
    }

    fun insertSubscriptionGroup(groupName: String, accountName: String) {
        disposables.add(insertSubscriptionGroup.execute(InsertSubscriptionGroup.Params(groupName, accountName, viewState.subscriptions
                .filter { it.isChosen.get() ?: false }
                .map { it.subscription.id }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
    }

    fun addSubscriptionsToGroup(groupName: String, accountName: String) {
        disposables.add(addSubscriptionsToGroup.execute(AddSubscriptionsToGroup.Params(groupName, accountName, viewState.subscriptions
                .filter { it.isChosen.get() ?: false }
                .map { it.subscription.id }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
    }
}