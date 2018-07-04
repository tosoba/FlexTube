package com.example.there.flextube.main

import android.arch.lifecycle.Lifecycle
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.there.flextube.R
import com.example.there.flextube.util.screenHeight
import com.example.there.flextube.util.screenOrientation
import com.example.there.flextube.util.toPx
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBottomNavigation()
        initViewPager()
        initSlidingLayout()
        initYouTubePlayerView()
    }

    private val itemIds: Array<Int> = arrayOf(R.id.action_home, R.id.action_sub_feed, R.id.action_groups)

    private fun initBottomNavigation() {
        val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            if (item.itemId == main_bottom_navigation_view.selectedItemId) {
                return@OnNavigationItemSelectedListener false
            }

            main_view_pager.currentItem = itemIds.indexOf(item.itemId)

            return@OnNavigationItemSelectedListener true
        }

        main_bottom_navigation_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) = Unit

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

        override fun onPageSelected(position: Int) {
            main_bottom_navigation_view?.menu?.findItem(itemIds[position])?.isChecked = true
        }
    }

    private fun initViewPager() {
        val adapter = MainViewPagerAdapter(supportFragmentManager)
        main_view_pager.adapter = adapter
        main_view_pager.addOnPageChangeListener(onPageChangeListener)
    }

    private val playerMaxVerticalHeight: Int by lazy(LazyThreadSafetyMode.NONE) { toPx(screenHeight) / 2 - supportActionBar!!.height }
    private val playerMaxHorizontalHeight: Int by lazy(LazyThreadSafetyMode.NONE) { toPx(screenHeight) - supportActionBar!!.height }
    private val minimumPlayerHeight: Int by lazy(LazyThreadSafetyMode.NONE) { toPx(minimumPlayerHeightDp) }

    private var currentSlideOffset: Float = 0.0f

    private fun initSlidingLayout() {
        sliding_layout.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) = updatePlayerDimensions(slideOffset)

            override fun onPanelStateChanged(panel: View?,
                                             previousState: SlidingUpPanelLayout.PanelState?,
                                             newState: SlidingUpPanelLayout.PanelState?) = Unit
        })

        sliding_layout.setFadeOnClickListener {
            sliding_layout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }
    }

    private fun updatePlayerDimensions(slideOffset: Float) {
        currentSlideOffset = slideOffset
        val layoutParams = player_view.layoutParams
        val height = if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
            (playerMaxVerticalHeight - minimumPlayerHeight) * slideOffset + minimumPlayerHeight
        } else {
            (playerMaxHorizontalHeight - minimumPlayerHeight) * slideOffset + minimumPlayerHeight
        }
        layoutParams.height = height.toInt()
        player_view.requestLayout()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        updatePlayerDimensions(currentSlideOffset)
    }

    private fun initYouTubePlayerView() {
        lifecycle.addObserver(player_view)

        player_view.initialize({ youTubePlayer ->
            youTubePlayer.addListener(object : AbstractYouTubePlayerListener() {
                override fun onReady() {
                    loadVideo(youTubePlayer, "6JYIGclVQdw")
                }

                private fun loadVideo(youTubePlayer: YouTubePlayer, videoId: String) {
                    if (lifecycle.currentState == Lifecycle.State.RESUMED)
                        youTubePlayer.loadVideo(videoId, 0f)
                    else
                        youTubePlayer.cueVideo(videoId, 0f)
                }
            })

        }, true)
    }

    companion object {
        private const val minimumPlayerHeightDp = 68
    }
}
