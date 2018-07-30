package com.example.there.flextube.groups.list

import android.util.Log
import com.example.there.domain.usecase.impl.GetGroup
import com.example.there.domain.usecase.impl.GetUserGroups
import com.example.there.flextube.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GroupsListViewModel @Inject constructor(
        private val getUserGroups: GetUserGroups,
        private val getGroup: GetGroup
) : BaseViewModel() {

    val viewState = GroupsListViewState()

    fun loadGroups(accountName: String) {
        disposables.add(getUserGroups.execute(accountName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ newGroups ->
                    viewState.groups.removeAll { !newGroups.contains(it) }
                    viewState.groups.addAll(newGroups)
                }, { Log.e(this.javaClass.name, it.message) }))
    }

    fun checkIfGroupExists(accountName: String, groupName: String, ifExists: () -> Unit, ifNotExists: () -> Unit) {
        disposables.add(getGroup.execute(GetGroup.Params(accountName, groupName))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ ifExists() }, { ifNotExists() }))
    }
}