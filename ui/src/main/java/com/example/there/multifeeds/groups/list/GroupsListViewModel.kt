package com.example.there.multifeeds.groups.list

import android.databinding.ObservableArrayList
import android.util.Log
import com.example.there.domain.model.Group
import com.example.there.domain.usecase.impl.GetGroup
import com.example.there.domain.usecase.impl.GetSubscriptionsFromGroup
import com.example.there.domain.usecase.impl.GetUserGroups
import com.example.there.multifeeds.base.vm.BaseViewModel
import com.example.there.multifeeds.mapper.UiSubscriptionMapper
import com.example.there.multifeeds.model.UiGroupWithSubscriptions
import com.example.there.multifeeds.model.UiSubscription
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GroupsListViewModel @Inject constructor(
        private val getUserGroups: GetUserGroups,
        private val getGroup: GetGroup,
        private val getSubscriptionsFromGroup: GetSubscriptionsFromGroup
) : BaseViewModel() {

    val viewState = GroupsListViewState()

    fun loadGroups(accountName: String) {
        disposables.add(getUserGroups.execute(accountName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterNext { newGroups ->
                    viewState.groups.removeAll { !newGroups.contains(Group(it.name, it.accountName)) }
                }
                .observeOn(Schedulers.io())
                .flatMapIterable { it }
                .flatMap { group ->
                    getSubscriptionsFromGroup.execute(GetSubscriptionsFromGroup.Params(group.accountName, group.name))
                            .map {
                                UiGroupWithSubscriptions(
                                        group.accountName,
                                        group.name,
                                        ObservableArrayList<UiSubscription>().apply {
                                            addAll(it.map(UiSubscriptionMapper::toUi))
                                        })
                            }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ group ->
                    if (group.subscriptions.isNotEmpty()) viewState.groups.add(group)
                }, { Log.e(this.javaClass.name, it.message) }))
    }

    fun checkIfGroupExists(accountName: String, groupName: String, ifExists: () -> Unit, ifNotExists: () -> Unit) {
        disposables.add(getGroup.execute(GetGroup.Params(accountName, groupName))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ ifExists() }, { ifNotExists() }))
    }
}