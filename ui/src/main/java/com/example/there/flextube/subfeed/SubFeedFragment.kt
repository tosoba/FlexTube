package com.example.there.flextube.subfeed

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.flextube.R
import com.example.there.flextube.di.Injectable
import com.example.there.flextube.di.vm.ViewModelFactory
import com.example.there.flextube.event.AuthEvent
import com.example.there.presentation.subfeed.SubFeedViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_sub_feed.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

class SubFeedFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: SubFeedViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SubFeedViewModel::class.java)
    }

    private val subscriptionsAdapter: SubFeedSubscriptionsAdapter by lazy { SubFeedSubscriptionsAdapter() }

    private val videosAdapter: SubFeedVideosAdapter by lazy { SubFeedVideosAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sub_feed, container, false)

        view.sub_buttons_recycler_view.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        view.sub_buttons_recycler_view.adapter = subscriptionsAdapter

        view.videos_recycler_view.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        view.videos_recycler_view.adapter = videosAdapter

        return view
    }

    private val disposables = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposables.add(viewModel.subscriptions.subscribe {
            subscriptionsAdapter.addSubscriptions(it)
        })

        disposables.add(viewModel.videos.subscribe {
            videosAdapter.addVideos(it)
        })
    }

    @Suppress("unused")
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onEvent(event: AuthEvent) {
        if (event is AuthEvent.Successful) {
            viewModel.loadVideos(event.accessToken, event.accountName)
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}
