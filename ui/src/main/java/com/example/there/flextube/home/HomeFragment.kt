package com.example.there.flextube.home

import android.app.Activity
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
import com.example.there.data.repo.store.IYoutubeCache
import com.example.there.flextube.R
import com.example.there.flextube.base.Scrollable
import com.example.there.flextube.databinding.FragmentHomeBinding
import com.example.there.flextube.di.Injectable
import com.example.there.flextube.di.vm.ViewModelFactory
import com.example.there.flextube.lifecycle.ConnectivityComponent
import com.example.there.flextube.lifecycle.DisposablesComponent
import com.example.there.flextube.list.VideoCategoriesAdapter
import com.example.there.flextube.list.VideosAdapter
import com.example.there.flextube.main.MainActivity
import com.example.there.flextube.util.view.EndlessRecyclerOnScrollListener
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject


class HomeFragment : Fragment(), Injectable, Scrollable {

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: HomeViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
    }

    private val videosAdapter: VideosAdapter by lazy(LazyThreadSafetyMode.NONE) {
        VideosAdapter(viewModel.viewState.homeItems, R.layout.video_item)
    }

    private val onVideosScrollListener = object : EndlessRecyclerOnScrollListener() {
        override fun onLoadMore() {
            if (viewModel.viewState.currentVideoCategoryId == IYoutubeCache.CATEGORY_GENERAL) {
                viewModel.loadGeneralHomeItems((activity as MainActivity).accessToken)
            } else {
                viewModel.loadHomeItemsByCategory(viewModel.viewState.currentVideoCategoryId, shouldReturnAll = false)
            }
        }
    }

    private val videoCategoriesAdapter: VideoCategoriesAdapter by lazy(LazyThreadSafetyMode.NONE) {
        VideoCategoriesAdapter(viewModel.viewState.videoCategories, R.layout.video_category_item)
    }

    private val view: HomeView by lazy(LazyThreadSafetyMode.NONE) {
        HomeView(
                viewModel.viewState,
                videosAdapter,
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                    setDrawable(ContextCompat.getDrawable(context!!, R.drawable.video_separator)!!)
                },
                onVideosScrollListener,
                videoCategoriesAdapter
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater, R.layout.fragment_home, container, false)

        return binding.apply {
            homeView = view
            homeItemsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            videoCategoriesRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }.root
    }

    override fun scrollToTop() {
        home_items_recycler_view?.smoothScrollToPosition(0)
    }

    private val disposablesComponent = DisposablesComponent()

    private val connectivityComponent: ConnectivityComponent by lazy(LazyThreadSafetyMode.NONE) {
        ConnectivityComponent(
                activity as Activity,
                viewModel.loadingGeneralHomeItemsComplete,
                {
                    viewModel.clearDisposables()
                    viewModel.loadGeneralHomeItems((activity as MainActivity).accessToken)
                    viewModel.loadVideoCategories()
                },
                R.id.main_view_pager
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(disposablesComponent)
        lifecycle.addObserver(connectivityComponent)

        viewModel.loadGeneralHomeItems((activity as MainActivity).accessToken)
        viewModel.loadVideoCategories()

        disposablesComponent.add(videosAdapter.videoClicked.subscribe {
            (activity as MainActivity).loadVideo(it)
        })

        disposablesComponent.add(videoCategoriesAdapter.categoryClicked.subscribe {
            video_categories_recycler_view?.smoothScrollToPosition(0)
            viewModel.viewState.homeItems.clear()
            viewModel.viewState.currentVideoCategoryId = it
            onVideosScrollListener.mPreviousTotal = 0
            if (it == IYoutubeCache.CATEGORY_GENERAL) {
                viewModel.loadGeneralHomeItems((activity as MainActivity).accessToken, shouldReturnAll = true)
            } else {
                viewModel.loadHomeItemsByCategory(it)
            }
        })
    }
}
