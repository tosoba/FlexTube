package com.example.there.flextube.groups.group

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.afollestad.materialdialogs.MaterialDialog
import com.example.there.flextube.R
import com.example.there.flextube.addgroup.AddGroupActivity
import com.example.there.flextube.databinding.FragmentGroupBinding
import com.example.there.flextube.di.Injectable
import com.example.there.flextube.di.vm.ViewModelFactory
import com.example.there.flextube.groups.GroupsFragment
import com.example.there.flextube.lifecycle.DisposablesComponent
import com.example.there.flextube.list.SortedVideosAdapter
import com.example.there.flextube.main.MainActivity
import com.example.there.flextube.model.UiGroup
import com.example.there.flextube.subfeed.SubFeedSubscriptionsAdapter
import com.example.there.flextube.util.ext.accountName
import com.example.there.flextube.util.view.EndlessRecyclerOnScrollListener
import javax.inject.Inject


class GroupFragment : Fragment(), Injectable {

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: GroupViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this, factory).get(GroupViewModel::class.java)
    }

    private val group: UiGroup by lazy(LazyThreadSafetyMode.NONE) { arguments!!.getParcelable<UiGroup>(ARG_GROUP) }

    private val disposablesComponent = DisposablesComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        lifecycle.addObserver(disposablesComponent)

        viewModel.loadData(group)
        disposablesComponent.add(videosAdapter.videoClicked.subscribe { (activity as MainActivity).loadVideo(it) })
    }

    private val subscriptionsAdapter: SubFeedSubscriptionsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SubFeedSubscriptionsAdapter(viewModel.viewState.subscriptions, R.layout.subscription_item)
    }

    private val videosAdapter: SortedVideosAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SortedVideosAdapter(viewModel.viewState.videos, R.layout.video_item)
    }

    private val onVideosScrollListener = object : EndlessRecyclerOnScrollListener() {
        override fun onLoadMore() = viewModel.loadMoreVideos()
    }

    private val onAddMoreSubsClickListener = View.OnClickListener {
        AddGroupActivity.start(activity!!, accountName, group.name, AddGroupActivity.Mode.ADD_SUBS_TO_EXISTING)
    }

    private val view: GroupView by lazy(LazyThreadSafetyMode.NONE) {
        GroupView(
                subscriptionsAdapter,
                videosAdapter,
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                    setDrawable(ContextCompat.getDrawable(context!!, R.drawable.video_separator)!!)
                },
                onVideosScrollListener,
                onAddMoreSubsClickListener
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentGroupBinding>(inflater, R.layout.fragment_group, container, false)
        return binding.apply {
            groupView = view

            groupSubButtonsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            groupVideosRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.group_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.action_delete_group -> {
            showDeleteGroupDialog()
            true
        }
        else -> false
    }

    private fun showDeleteGroupDialog() = MaterialDialog.Builder(activity!!)
            .title(getString(R.string.want_to_delete_group, group.name))
            .onPositive { _, _ ->
                viewModel.deleteGroup(group) {
                    (parentFragment as? GroupsFragment)?.childFragmentManager?.popBackStack()
                }
            }
            .positiveText(getString(R.string.yes))
            .negativeText(getString(R.string.no))
            .build()
            .apply { show() }

    companion object {
        private const val ARG_GROUP = "ARG_GROUP"

        fun newInstance(group: UiGroup) = GroupFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_GROUP, group)
            }
        }
    }
}
