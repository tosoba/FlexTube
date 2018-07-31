package com.example.there.flextube.search

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
import com.example.there.flextube.databinding.FragmentSearchBinding
import com.example.there.flextube.di.Injectable
import com.example.there.flextube.di.vm.ViewModelFactory
import com.example.there.flextube.list.VideosAdapter
import com.example.there.flextube.util.view.EndlessRecyclerOnScrollListener
import javax.inject.Inject


class SearchFragment : Fragment(), Injectable {

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

    private val foundVideosAdapter: VideosAdapter by lazy(LazyThreadSafetyMode.NONE) { VideosAdapter(viewModel.viewState.foundVideos, R.layout.video_item) }

    private val onFoundVideosScrollListener: EndlessRecyclerOnScrollListener by lazy(LazyThreadSafetyMode.NONE) {
        object : EndlessRecyclerOnScrollListener() {
            override fun onLoadMore() = viewModel.searchVideos(query, false)
        }
    }

    private val foundVideosItemDecoration: DividerItemDecoration by lazy(LazyThreadSafetyMode.NONE) {
        DividerItemDecoration(activity!!, DividerItemDecoration.VERTICAL).apply {
            setDrawable(ContextCompat.getDrawable(activity!!, R.drawable.video_separator)!!)
        }
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

    companion object {
        private const val ARG_QUERY = "ARG_QUERY"

        fun newInstance(query: String) = SearchFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_QUERY, query)
            }
        }
    }
}
