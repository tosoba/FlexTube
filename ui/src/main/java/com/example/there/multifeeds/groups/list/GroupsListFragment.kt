package com.example.there.multifeeds.groups.list

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.example.there.cache.preferences.AppPreferences
import com.example.there.multifeeds.R
import com.example.there.multifeeds.addgroup.AddGroupActivity
import com.example.there.multifeeds.base.fragment.HasTitle
import com.example.there.multifeeds.base.fragment.Scrollable
import com.example.there.multifeeds.databinding.FragmentGroupsListBinding
import com.example.there.multifeeds.di.Injectable
import com.example.there.multifeeds.di.vm.ViewModelFactory
import com.example.there.multifeeds.groups.GroupsHostFragment
import com.example.there.multifeeds.lifecycle.DisposablesComponent
import com.example.there.multifeeds.list.SortedGroupsAdapter
import com.example.there.multifeeds.model.UiGroupWithSubscriptions
import kotlinx.android.synthetic.main.fragment_groups_list.*
import javax.inject.Inject

class GroupsListFragment : Fragment(), Injectable, Scrollable, HasTitle {

    override val title: String
        get() = "Your groups"

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: GroupsListViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this, viewModelFactory).get(GroupsListViewModel::class.java)
    }

    private val disposablesComponent = DisposablesComponent()

    private val groupsAdapter: SortedGroupsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SortedGroupsAdapter(viewModel.viewState.groups, R.layout.group_item)
    }

    private val view: GroupsListView by lazy(LazyThreadSafetyMode.NONE) {
        GroupsListView(
                viewModel.viewState,
                groupsAdapter,
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                    setDrawable(ContextCompat.getDrawable(context!!, R.drawable.video_separator)!!)
                },
                View.OnClickListener {
                    activity?.let {
                        showNewGroupDialog()
                    }
                }
        )
    }

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(disposablesComponent)

        viewModel.loadGroups(appPreferences.accountName!!)

        disposablesComponent.add(groupsAdapter.groupClicked.subscribe {
            showGroupFragment(it)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentGroupsListBinding>(inflater, R.layout.fragment_groups_list, container, false)

        return binding.apply {
            groupsListView = view
            groupsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }.root
    }

    override fun scrollToTop() {
        groups_recycler_view?.scrollToPosition(0)
    }

    private fun showGroupFragment(group: UiGroupWithSubscriptions) {
        (parentFragment as? GroupsHostFragment)?.showGroupFragment(group)
    }

    private fun showNewGroupDialog() {
        MaterialDialog.Builder(activity!!)
                .title(getString(R.string.new_group))
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(getString(R.string.group_name), "") { _, input ->
                    viewModel.checkIfGroupExists(appPreferences.accountName!!, input.trim().toString(), ifExists = {
                        Toast.makeText(activity!!, "Group named ${input.trim()} already exists.", Toast.LENGTH_SHORT).show()
                    }, ifNotExists = {
                        AddGroupActivity.start(activity!!, appPreferences.accountName!!, input.trim().toString())
                    })
                }
                .positiveText(getString(R.string.confirm))
                .build()
                .apply { show() }
    }
}
