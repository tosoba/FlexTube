package com.example.there.flextube.groups.list

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
import com.example.there.flextube.R
import com.example.there.flextube.addgroup.AddGroupActivity
import com.example.there.flextube.databinding.FragmentGroupsListBinding
import com.example.there.flextube.di.Injectable
import com.example.there.flextube.di.vm.ViewModelFactory
import com.example.there.flextube.lifecycle.DisposablesComponent
import com.example.there.flextube.list.SortedGroupsAdapter
import com.example.there.flextube.util.ext.accountName
import javax.inject.Inject

class GroupsListFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: GroupsListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(GroupsListViewModel::class.java)
    }

    private val disposablesComponent = DisposablesComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(disposablesComponent)
        viewModel.loadGroups(accountName)
    }

    private val groupsAdapter: SortedGroupsAdapter by lazy {
        SortedGroupsAdapter(viewModel.viewState.groups, R.layout.group_item)
    }

    private val view: GroupsListView by lazy {
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

    private fun showNewGroupDialog() {
        MaterialDialog.Builder(activity!!)
                .title(getString(R.string.new_group))
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(getString(R.string.group_name), "") { _, input ->
                    viewModel.checkIfGroupExists(accountName, input.trim().toString(), ifExists =  {
                        Toast.makeText(activity!!, "Group named ${input.trim()} already exists.", Toast.LENGTH_SHORT).show()
                    }, ifNotExists = {
                        AddGroupActivity.start(activity!!, accountName, input.trim().toString())
                    })
                }
                .positiveText(getString(R.string.confirm))
                .build()
                .apply { show() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentGroupsListBinding>(inflater, R.layout.fragment_groups_list, container, false)

        return binding.apply {
            groupsListView = view
            groupsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }.root
    }
}
