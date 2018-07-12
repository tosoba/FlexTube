package com.example.there.flextube.subfeed

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
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
import com.example.there.flextube.event.AuthEvent
import com.example.there.flextube.lifecycle.DisposablesComponent
import com.example.there.flextube.lifecycle.EventBusComponent
import com.example.there.flextube.list.VideosAdapter
import com.example.there.flextube.main.MainActivity
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject


class SubFeedFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: SubFeedViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SubFeedViewModel::class.java)
    }

    private val eventBusComponent = EventBusComponent(this)
    private val disposablesComponent = DisposablesComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(eventBusComponent)
        lifecycle.addObserver(disposablesComponent)
    }

    private val subscriptionsAdapter: SubFeedSubscriptionsAdapter by lazy {
        SubFeedSubscriptionsAdapter(viewModel.viewState.subscriptions, R.layout.subscription_item)
    }

    private val videosAdapter: VideosAdapter by lazy {
        VideosAdapter(viewModel.viewState.videos, R.layout.video_item)
    }

    private val view: SubFeedView by lazy {
        SubFeedView(viewModel.viewState, subscriptionsAdapter, videosAdapter, DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
            setDrawable(ContextCompat.getDrawable(context!!, R.drawable.video_separator)!!)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentSubFeedBinding>(inflater, R.layout.fragment_sub_feed, container,false)

        return binding.apply {
            subFeedView = view

            subButtonsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            videosRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposablesComponent.add(videosAdapter.videoClicked.subscribe {
            (activity as MainActivity).loadVideo(it)
        })
    }

    private val accountName: String by lazy {
        activity!!.getPreferences(Context.MODE_PRIVATE).getString(MainActivity.PREF_ACCOUNT_NAME, null)
    }

    @Suppress("unused")
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onEvent(event: AuthEvent) {
        if (event is AuthEvent.Successful) {
            viewModel.loadVideos(event.accessToken, accountName)
        }
    }
}
