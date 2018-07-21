package com.example.there.flextube.groups.group

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.flextube.R
import com.example.there.flextube.databinding.FragmentGroupBinding
import com.example.there.flextube.di.Injectable
import com.example.there.flextube.di.vm.ViewModelFactory
import com.example.there.flextube.list.SortedVideosAdapter
import com.example.there.flextube.model.UiGroup
import com.example.there.flextube.subfeed.SubFeedSubscriptionsAdapter
import javax.inject.Inject


class GroupFragment : Fragment(), Injectable {

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: GroupViewModel by lazy {
        ViewModelProviders.of(this, factory).get(GroupViewModel::class.java)
    }

    private val group: UiGroup by lazy { arguments!!.getParcelable<UiGroup>(ARG_GROUP) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadData(group)
    }

    private val subscriptionsAdapter: SubFeedSubscriptionsAdapter by lazy {
        SubFeedSubscriptionsAdapter(viewModel.viewState.subscriptions, R.layout.subscription_item)
    }

    private val videosAdapter: SortedVideosAdapter by lazy {
        SortedVideosAdapter(viewModel.viewState.videos, R.layout.video_item)
    }

    private val view: GroupView by lazy {
        GroupView(
                subscriptionsAdapter,
                videosAdapter,
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                    setDrawable(ContextCompat.getDrawable(context!!, R.drawable.video_separator)!!)
                }
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

    companion object {
        private const val ARG_GROUP = "ARG_GROUP"

        fun newInstance(group: UiGroup) = GroupFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_GROUP, group)
            }
        }
    }
}
