package com.example.there.multifeeds.groups.group

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.example.there.multifeeds.R
import com.example.there.multifeeds.addgroup.AddGroupActivity
import com.example.there.multifeeds.base.fragment.HasBackNavigation
import com.example.there.multifeeds.base.fragment.HasTitle
import com.example.there.multifeeds.base.fragment.Scrollable
import com.example.there.multifeeds.databinding.FragmentGroupBinding
import com.example.there.multifeeds.di.Injectable
import com.example.there.multifeeds.di.vm.ViewModelFactory
import com.example.there.multifeeds.groups.GroupsHostFragment
import com.example.there.multifeeds.lifecycle.DisposablesComponent
import com.example.there.multifeeds.list.SubscriptionsVideosAdapter
import com.example.there.multifeeds.main.MainActivity
import com.example.there.multifeeds.model.UiGroupWithSubscriptions
import com.example.there.multifeeds.util.ext.*
import com.example.there.multifeeds.util.view.DividerItemDecorator
import com.example.there.multifeeds.util.view.EndlessRecyclerOnScrollListener
import kotlinx.android.synthetic.main.fragment_group.*
import javax.inject.Inject


class GroupFragment : Fragment(), Injectable, Scrollable, HasTitle, HasBackNavigation {

    override val title: String
        get() = group.name

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: GroupViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this, factory).get(GroupViewModel::class.java)
    }

    private val group: UiGroupWithSubscriptions by lazy(LazyThreadSafetyMode.NONE) { arguments!!.getParcelable<UiGroupWithSubscriptions>(ARG_GROUP) }

    private val disposablesComponent = DisposablesComponent()

    override fun scrollToTop() {
        group_videos_recycler_view?.scrollToPosition(0)
        expandMainAppBar()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(disposablesComponent)

        viewModel.loadData(group) { if (!videosAdapter.userHasScrolled) scrollToTop() }
        disposablesComponent.add(videosAdapter.videoClicked.subscribe {
            closeGroupMenu(false)
            (activity as MainActivity).loadVideo(it)
        })
    }

    private fun closeGroupMenu(animate: Boolean) {
        if (group_menu?.isOpened == true) {
            group_menu?.close(animate)
        }
    }

    private val videosAdapter: SubscriptionsVideosAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SubscriptionsVideosAdapter(
                viewModel.viewState.videos,
                viewModel.viewState.subscriptions,
                R.layout.video_item,
                R.layout.subscription_list_item,
                R.layout.subscription_item,
                R.layout.loading_item
        )
    }

    private val onVideosScrollListener = object : EndlessRecyclerOnScrollListener(returnFromOnScrolledItemCount = 2) {
        override fun onLoadMore() {
            videosAdapter.loadingInProgress = true
            viewModel.loadMoreVideos { videosAdapter.loadingInProgress = false }
        }
    }

    private val onAddMoreSubsClickListener = View.OnClickListener {
        AddGroupActivity.start(activity!!, accountName, group.name, AddGroupActivity.Mode.ADD_SUBS_TO_EXISTING)
    }

    private val onDeleteGroupClickListener = View.OnClickListener {
        showDeleteGroupDialog()
    }

    private val view: GroupView by lazy(LazyThreadSafetyMode.NONE) {
        GroupView(
                videosAdapter,
                DividerItemDecorator(ContextCompat.getDrawable(context!!, R.drawable.video_separator)!!),
                onVideosScrollListener,
                onAddMoreSubsClickListener,
                onDeleteGroupClickListener
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentGroupBinding>(inflater, R.layout.fragment_group, container, false)
        return binding.apply {
            groupView = view

            groupRootLayout.setOnTouchListener(View.OnTouchListener { _, _ ->
                if (group_menu?.isOpened == true) {
                    closeGroupMenu(true)
                    return@OnTouchListener true
                }
                false
            })
            groupVideosRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            groupVideosRecyclerView.addOnInitialUserScrollListener()
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTranslationZ(group_menu, 1000f)
    }

    private fun showDeleteGroupDialog() = MaterialDialog.Builder(activity!!)
            .title(getString(R.string.want_to_delete_group, group.name))
            .onPositive { _, _ ->
                viewModel.deleteGroup(group) {
                    mainToolbar?.resetTitle(getString(R.string.your_groups))
                    (parentFragment as? GroupsHostFragment)?.childFragmentManager?.popBackStack()
                }
            }
            .positiveText(getString(R.string.yes))
            .negativeText(getString(R.string.no))
            .build()
            .apply { show() }

    companion object {
        private const val ARG_GROUP = "ARG_GROUP"

        fun newInstance(group: UiGroupWithSubscriptions) = GroupFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_GROUP, group)
            }
        }
    }
}
