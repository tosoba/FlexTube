package com.example.there.multifeeds.home

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.data.repo.store.IYoutubeCache
import com.example.there.multifeeds.R
import com.example.there.multifeeds.base.fragment.HasTitle
import com.example.there.multifeeds.base.fragment.Scrollable
import com.example.there.multifeeds.databinding.FragmentHomeBinding
import com.example.there.multifeeds.di.Injectable
import com.example.there.multifeeds.di.vm.ViewModelFactory
import com.example.there.multifeeds.lifecycle.ConnectivityComponent
import com.example.there.multifeeds.lifecycle.DisposablesComponent
import com.example.there.multifeeds.list.CategoryVideosAdapter
import com.example.there.multifeeds.list.VideoCategoriesAdapter
import com.example.there.multifeeds.main.MainActivity
import com.example.there.multifeeds.util.ext.expandMainAppBar
import com.example.there.multifeeds.util.view.DividerItemDecorator
import com.example.there.multifeeds.util.view.EndlessRecyclerOnScrollListener
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject


class HomeFragment : Fragment(), Injectable, Scrollable, HasTitle {

    override val title: String
        get() = getString(R.string.app_name)

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: HomeViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
    }

    private val videosAdapter: CategoryVideosAdapter by lazy(LazyThreadSafetyMode.NONE) {
        CategoryVideosAdapter(
                viewModel.viewState.homeItems,
                videoCategoriesAdapter,
                R.layout.video_item,
                R.layout.category_list_item,
                R.layout.loading_item
        )
    }

    private val onVideosScrollListener = object : EndlessRecyclerOnScrollListener(returnFromOnScrolledItemCount = 2) {
        override fun onLoadMore() {
            val onFinally: () -> Unit = { videosAdapter.loadingInProgress = false }
            videosAdapter.loadingInProgress = true
            if (viewModel.viewState.currentVideoCategoryId == IYoutubeCache.CATEGORY_GENERAL) {
                viewModel.loadGeneralHomeItems((activity as MainActivity).accessToken, false, onFinally)
            } else {
                viewModel.loadHomeItemsByCategory(viewModel.viewState.currentVideoCategoryId, false, onFinally)
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
                DividerItemDecorator(ContextCompat.getDrawable(context!!, R.drawable.video_separator)!!),
                onVideosScrollListener
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater, R.layout.fragment_home, container, false)

        return binding.apply {
            homeView = view
            homeItemsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }.root
    }

    override fun scrollToTop() {
        home_items_recycler_view?.scrollToPosition(0)
        expandMainAppBar()
    }

    private val disposablesComponent = DisposablesComponent()

    private val connectivityComponent: ConnectivityComponent by lazy(LazyThreadSafetyMode.NONE) {
        ConnectivityComponent(
                activity as Activity,
                viewModel.loadingGeneralHomeItemsComplete,
                {
                    viewModel.clearDisposables()
                    loadInitialHomeItems()
                    viewModel.loadVideoCategories()
                },
                activity!!.findViewById(R.id.scroll_to_top_fab)
        )
    }

    private fun loadInitialHomeItems() {
        viewModel.loadGeneralHomeItems((activity as MainActivity).accessToken, false, onAfterAdd = {
            home_items_recycler_view?.scrollToPosition(0)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(disposablesComponent)

        loadInitialHomeItems()
        viewModel.loadVideoCategories()

        disposablesComponent.add(videosAdapter.videoClicked.subscribe {
            (activity as MainActivity).loadVideo(it)
        })

        disposablesComponent.add(videoCategoriesAdapter.categoryClicked.subscribe {
            videoCategoriesAdapter.scrollToTop()
            viewModel.viewState.homeItems.clear()
            viewModel.viewState.currentVideoCategoryId = it
            if (it == IYoutubeCache.CATEGORY_GENERAL) {
                viewModel.loadGeneralHomeItems((activity as MainActivity).accessToken, shouldReturnAll = true) {
                    scrollToTop()
                }
            } else {
                viewModel.loadHomeItemsByCategory(it, onAfterAdd = { scrollToTop() })
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }
}
