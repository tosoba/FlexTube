package com.example.there.flextube.subfeed

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.flextube.R
import com.example.there.flextube.base.HasTitle
import com.example.there.flextube.base.Scrollable
import com.example.there.flextube.databinding.FragmentSubFeedBinding
import com.example.there.flextube.di.Injectable
import com.example.there.flextube.di.vm.ViewModelFactory
import com.example.there.flextube.lifecycle.ConnectivityComponent
import com.example.there.flextube.lifecycle.DisposablesComponent
import com.example.there.flextube.list.SortedVideosAdapter
import com.example.there.flextube.main.MainActivity
import com.example.there.flextube.util.ext.accountName
import com.example.there.flextube.util.view.EndlessRecyclerOnScrollListener
import kotlinx.android.synthetic.main.fragment_sub_feed.*
import javax.inject.Inject


class SubFeedFragment : Fragment(), Injectable, Scrollable, HasTitle {

    override val title: String
        get() = getString(R.string.app_name)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: SubFeedViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this, viewModelFactory).get(SubFeedViewModel::class.java)
    }

    private val disposablesComponent = DisposablesComponent()
    private val connectivityComponent: ConnectivityComponent by lazy(LazyThreadSafetyMode.NONE) {
        ConnectivityComponent(
                activity as Activity,
                viewModel.loadingRemoteVideosComplete,
                {
                    viewModel.clearDisposables()
                    viewModel.loadData((activity as MainActivity).accessToken, accountName, true)
                },
                R.id.main_view_pager
        )
    }

    override fun scrollToTop() {
        videos_recycler_view?.smoothScrollToPosition(0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(disposablesComponent)
        lifecycle.addObserver(connectivityComponent)

        viewModel.loadData((activity as MainActivity).accessToken, accountName)

        disposablesComponent.add(videosAdapter.videoClicked.subscribe {
            (activity as MainActivity).loadVideo(it)
        })
    }

    private val subscriptionsAdapter: SubFeedSubscriptionsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SubFeedSubscriptionsAdapter(viewModel.viewState.subscriptions, R.layout.subscription_item)
    }

    private val onVideosScrollListener = object : EndlessRecyclerOnScrollListener() {
        override fun onLoadMore() = viewModel.loadMoreVideos()
    }

    private val videosAdapter: SortedVideosAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SortedVideosAdapter(viewModel.viewState.videos, R.layout.video_item)
    }

    private val onSubFeedRefreshListener: SwipeRefreshLayout.OnRefreshListener by lazy(LazyThreadSafetyMode.NONE) {
        SwipeRefreshLayout.OnRefreshListener {
            viewModel.refreshVideos {
                sub_feed_swipe_refresh_layout?.isRefreshing = false
            }
        }
    }

    private val view: SubFeedView by lazy(LazyThreadSafetyMode.NONE) {
        SubFeedView(
                viewModel.viewState,
                subscriptionsAdapter,
                videosAdapter,
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                    setDrawable(ContextCompat.getDrawable(context!!, R.drawable.video_separator)!!)
                },
                onVideosScrollListener,
                onSubFeedRefreshListener
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentSubFeedBinding>(inflater, R.layout.fragment_sub_feed, container, false)

        return binding.apply {
            subFeedView = view
            subButtonsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            videosRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }.root
    }
}
