package com.example.there.flextube.groups.list

import android.databinding.ObservableArrayList
import com.example.there.domain.model.Group

data class GroupsListViewState(val groups: ObservableArrayList<Group> = ObservableArrayList())