package com.example.there.flextube.subfeed

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
import com.example.there.flextube.databinding.FragmentSubFeedBinding
import com.example.there.flextube.di.Injectable
import com.example.there.flextube.di.vm.ViewModelFactory
import com.example.there.flextube.lifecycle.DisposablesComponent
import com.example.there.flextube.list.SortedVideosAdapter
import com.example.there.flextube.main.MainActivity
import com.example.there.flextube.util.ext.accountName
import com.example.there.flextube.util.view.EndlessRecyclerOnScrollListener
import javax.inject.Inject


class SubFeedFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: SubFeedViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SubFeedViewModel::class.java)
    }

    private val disposablesComponent = DisposablesComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(disposablesComponent)

        disposablesComponent.add(videosAdapter.videoClicked.subscribe {
            (activity as MainActivity).loadVideo(it)
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.loadVideos((activity as MainActivity).accessToken, accountName)
    }

    private val subscriptionsAdapter: SubFeedSubscriptionsAdapter by lazy {
        SubFeedSubscriptionsAdapter(viewModel.viewState.subscriptions, R.layout.subscription_item)
    }

    private val onVideosScrollListener = object : EndlessRecyclerOnScrollListener() {
        override fun onLoadMore() = viewModel.loadMoreVideos()
    }

    private val videosAdapter: SortedVideosAdapter by lazy {
        SortedVideosAdapter(viewModel.viewState.videos, R.layout.video_item)
    }

    private val view: SubFeedView by lazy {
        SubFeedView(
                viewModel.viewState,
                subscriptionsAdapter,
                videosAdapter,
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                    setDrawable(ContextCompat.getDrawable(context!!, R.drawable.video_separator)!!)
                },
                onVideosScrollListener
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
