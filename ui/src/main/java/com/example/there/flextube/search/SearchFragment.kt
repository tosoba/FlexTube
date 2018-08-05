package com.example.there.flextube.search

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.flextube.R
import com.example.there.flextube.base.fragment.HasBackNavigation
import com.example.there.flextube.base.fragment.HasTitle
import com.example.there.flextube.base.fragment.Scrollable
import com.example.there.flextube.databinding.FragmentSearchBinding
import com.example.there.flextube.di.Injectable
import com.example.there.flextube.di.vm.ViewModelFactory
import com.example.there.flextube.list.VideosAdapter
import com.example.there.flextube.util.view.DividerItemDecorator
import com.example.there.flextube.util.view.EndlessRecyclerOnScrollListener
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject


class SearchFragment : Fragment(), Injectable, Scrollable, HasTitle, HasBackNavigation {

    override val title: String
        get() = query

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: SearchViewModel by lazy {
        ViewModelProviders.of(this, factory).get(SearchViewModel::class.java)
    }

    private val query: String by lazy { arguments!!.getString(ARG_QUERY).trim() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.searchVideos(query, true)
    }

    private val foundVideosAdapter: VideosAdapter by lazy(LazyThreadSafetyMode.NONE) {
        VideosAdapter(viewModel.viewState.foundVideos, R.layout.video_item, R.layout.loading_item)
    }

    private val onFoundVideosScrollListener: EndlessRecyclerOnScrollListener by lazy(LazyThreadSafetyMode.NONE) {
        object : EndlessRecyclerOnScrollListener() {
            override fun onLoadMore() {
                foundVideosAdapter.loadingInProgress = true
                viewModel.searchVideos(query, false) { foundVideosAdapter.loadingInProgress = false }
            }
        }
    }

    private val foundVideosItemDecoration: DividerItemDecorator by lazy(LazyThreadSafetyMode.NONE) {
        DividerItemDecorator(ContextCompat.getDrawable(context!!, R.drawable.video_separator)!!)
    }

    private val view: SearchView by lazy(LazyThreadSafetyMode.NONE) {
        SearchView(
                state = viewModel.viewState,
                foundVideosAdapter = foundVideosAdapter,
                onFoundVideosScroll = onFoundVideosScrollListener,
                itemDecoration = foundVideosItemDecoration
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentSearchBinding>(inflater, R.layout.fragment_search, container, false)

        return binding.apply {
            searchView = view
            foundVideosRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun scrollToTop() {
        found_videos_recycler_view?.smoothScrollToPosition(0)
    }

    companion object {
        private const val ARG_QUERY = "ARG_QUERY"

        fun newInstance(query: String) = SearchFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_QUERY, query)
            }
        }
    }
}
