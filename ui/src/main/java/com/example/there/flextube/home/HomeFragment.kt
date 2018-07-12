package com.example.there.flextube.home

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
import com.example.there.flextube.databinding.FragmentHomeBinding
import com.example.there.flextube.di.Injectable
import com.example.there.flextube.di.vm.ViewModelFactory
import com.example.there.flextube.event.AuthEvent
import com.example.there.flextube.lifecycle.DisposablesComponent
import com.example.there.flextube.lifecycle.EventBusComponent
import com.example.there.flextube.list.VideosAdapter
import com.example.there.flextube.main.MainActivity
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject


class HomeFragment : Fragment(), Injectable {

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
    }

    private val videosAdapter: VideosAdapter by lazy { VideosAdapter(viewModel.viewState.homeItems, R.layout.video_item) }

    private val view: HomeView by lazy {
        HomeView(viewModel.viewState, videosAdapter, DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
            setDrawable(ContextCompat.getDrawable(context!!, R.drawable.video_separator)!!)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater, R.layout.fragment_home, container, false)

        return binding.apply {
            homeView = view
            homeItemsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposablesComponent.add(videosAdapter.videoClicked.subscribe {
            (activity as MainActivity).loadVideo(it)
        })
    }

    private val eventBusComponent = EventBusComponent(this)
    private val disposablesComponent = DisposablesComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(eventBusComponent)
        lifecycle.addObserver(disposablesComponent)
    }

    @Suppress("unused")
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onEvent(event: AuthEvent) {
        if (event is AuthEvent.Successful) {
            viewModel.loadHomeItems(event.accessToken)
        }
    }
}
