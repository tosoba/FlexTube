package com.example.there.multifeeds.subfeed

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.cache.preferences.AppPreferences
import com.example.there.multifeeds.R
import com.example.there.multifeeds.base.fragment.HasTitle
import com.example.there.multifeeds.base.fragment.Scrollable
import com.example.there.multifeeds.databinding.FragmentSubFeedBinding
import com.example.there.multifeeds.di.Injectable
import com.example.there.multifeeds.di.vm.ViewModelFactory
import com.example.there.multifeeds.lifecycle.ConnectivityComponent
import com.example.there.multifeeds.lifecycle.DisposablesComponent
import com.example.there.multifeeds.list.SubscriptionsVideosAdapter
import com.example.there.multifeeds.main.MainActivity
import com.example.there.multifeeds.util.ext.addOnInitialUserScrollListener
import com.example.there.multifeeds.util.ext.defaultConnectivityComponentSnackbarParams
import com.example.there.multifeeds.util.ext.expandMainAppBar
import com.example.there.multifeeds.util.view.DividerItemDecorator
import com.example.there.multifeeds.util.view.EndlessRecyclerOnScrollListener
import kotlinx.android.synthetic.main.fragment_sub_feed.*
import javax.inject.Inject


class SubFeedFragment : Fragment(), Injectable, Scrollable, HasTitle {

    override val title: String
        get() = "MultiFeeds"

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: SubFeedViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this, viewModelFactory).get(SubFeedViewModel::class.java)
    }

    private val disposablesComponent = DisposablesComponent()
    private val connectivityComponent: ConnectivityComponent by lazy(LazyThreadSafetyMode.NONE) {
        ConnectivityComponent(
                isDataLoaded = viewModel.loadingRemoteVideosComplete,
                reloadDataOnConnected = {
                    viewModel.clearDisposables()
                    loadData(true)
                },
                snackbarParameters = activity!!.defaultConnectivityComponentSnackbarParams(
                        parentView = activity!!.findViewById(R.id.scroll_to_top_fab)
                )
        )
    }

    @Inject
    lateinit var appPreferences: AppPreferences

    private fun loadData(reloadAfterConnectionLoss: Boolean) {
        viewModel.loadData((activity as MainActivity).accessToken, appPreferences.accountName!!, reloadAfterConnectionLoss) {
            if (!videosAdapter.userHasScrolled) scrollToTop()
        }
    }

    override fun scrollToTop() {
        videos_recycler_view?.scrollToPosition(0)
        expandMainAppBar()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(disposablesComponent)

        loadData(false)

        disposablesComponent.add(videosAdapter.videoClicked.subscribe {
            (activity as MainActivity).loadVideo(it)
        })
    }

    private val onVideosScrollListener = object : EndlessRecyclerOnScrollListener(returnFromOnScrolledItemCount = 2) {
        override fun onLoadMore() {
            videosAdapter.loadingInProgress = true
            viewModel.loadMoreVideos { videosAdapter.loadingInProgress = false }
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
                videosAdapter,
                DividerItemDecorator(ContextCompat.getDrawable(context!!, R.drawable.video_separator)!!),
                onVideosScrollListener,
                onSubFeedRefreshListener
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentSubFeedBinding>(inflater, R.layout.fragment_sub_feed, container, false)

        return binding.apply {
            subFeedView = view
            videosRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            videosRecyclerView.addOnInitialUserScrollListener()
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }
}
