package com.example.there.flextube.main

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageButton
import android.widget.RelativeLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.example.there.flextube.R
import com.example.there.flextube.groups.GroupsFragment
import com.example.there.flextube.start.StartActivity
import com.example.there.flextube.util.ext.screenHeight
import com.example.there.flextube.util.ext.screenOrientation
import com.example.there.flextube.util.ext.toPx
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

    val accessToken: String by lazy { intent.getStringExtra(EXTRA_TOKEN) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_toolbar)

        initBottomNavigation()
        initViewPager()
        initSlidingLayout()
        initYouTubePlayerView()
        addPlayerViewControls()
    }

    override fun onBackPressed() {
        val currentFragment = viewPagerAdapter.currentFragment
        if (currentFragment != null && currentFragment is GroupsFragment && currentFragment.childFragmentManager.backStackEntryCount > 0) {
            currentFragment.childFragmentManager.popBackStack()
        } else {
            showLogoutDialog()
        }
    }

    private fun showLogoutDialog() = MaterialDialog.Builder(this)
            .title(getString(R.string.want_to_logout))
            .onPositive { _, _ -> super.onBackPressed() }
            .positiveText(getString(R.string.yes))
            .negativeText(getString(R.string.no))
            .build()
            .apply { show() }

    private val itemIds: Array<Int> = arrayOf(R.id.action_home, R.id.action_sub_feed, R.id.action_groups)

    private fun initBottomNavigation() {
        main_bottom_navigation_view.setOnNavigationItemSelectedListener { item ->
            if (item.itemId == main_bottom_navigation_view.selectedItemId) {
                return@setOnNavigationItemSelectedListener false
            }

            main_view_pager.currentItem = itemIds.indexOf(item.itemId)
            true
        }
    }

    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) = Unit

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

        override fun onPageSelected(position: Int) {
            main_bottom_navigation_view?.menu?.findItem(itemIds[position])?.isChecked = true
        }
    }

    private val viewPagerAdapter: MainViewPagerAdapter by lazy { MainViewPagerAdapter(supportFragmentManager) }

    private fun initViewPager() {
        val adapter = viewPagerAdapter
        main_view_pager.adapter = adapter
        main_view_pager.addOnPageChangeListener(onPageChangeListener)
    }

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
        sliding_layout.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
    }

    private val playerMaxVerticalHeight: Int by lazy(LazyThreadSafetyMode.NONE) { toPx(screenHeight) / 2 }
    private val playerMaxHorizontalHeight: Int by lazy(LazyThreadSafetyMode.NONE) { toPx(screenHeight) }
    private val minimumPlayerHeight: Int by lazy(LazyThreadSafetyMode.NONE) { toPx(minimumPlayerHeightDp) }

    private var currentSlideOffset: Float = 0.0f

    private fun updatePlayerDimensions(slideOffset: Float) {
        if (sliding_layout.panelState != SlidingUpPanelLayout.PanelState.HIDDEN && slideOffset >= 0) {
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
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        updatePlayerDimensions(currentSlideOffset)
        updateMainContentLayoutParams()
    }

    private fun updateMainContentLayoutParams() {
        main_content_layout.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                main_content_layout.layoutParams.apply {
                    height = ViewGroup.LayoutParams.MATCH_PARENT
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                }
                main_content_layout.requestLayout()
                main_content_layout.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private lateinit var youTubePlayer: YouTubePlayer

    private fun initYouTubePlayerView() {
        lifecycle.addObserver(player_view)
        player_view.initialize({ youTubePlayer ->
            this.youTubePlayer = youTubePlayer
            youTubePlayer.addListener(object : AbstractYouTubePlayerListener() {
                override fun onReady() {

                }
            })

        }, true)

        player_view.playerUIController.showFullscreenButton(false)
    }

    fun loadVideo(videoId: String) {
        if (sliding_layout.panelState == SlidingUpPanelLayout.PanelState.HIDDEN)
            sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        if (lifecycle.currentState == Lifecycle.State.RESUMED)
            youTubePlayer.loadVideo(videoId, 0f)
        else
            youTubePlayer.cueVideo(videoId, 0f)
    }

    private val minimizeBtn: ImageButton by lazy(LazyThreadSafetyMode.NONE) {
        ImageButton(this).apply {
            setImageResource(R.drawable.maximize)
            layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(5, 5, 5, 5)
            }
            setOnClickListener {
                if (sliding_layout.panelState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
                    minimizeBtn.setImageResource(R.drawable.minimize)
                } else if (sliding_layout.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    sliding_layout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
                    minimizeBtn.setImageResource(R.drawable.maximize)
                }
            }
            setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private val closeBtn: ImageButton by lazy(LazyThreadSafetyMode.NONE) {
        ImageButton(this).apply {
            setImageResource(R.drawable.close)
            layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(5, 5, 5, 5)
                addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            }
            setOnClickListener {
                sliding_layout.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
                youTubePlayer.pause()
            }
            setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private fun addPlayerViewControls() = findViewById<RelativeLayout>(R.id.controls_root).apply {
        addView(minimizeBtn)
        addView(closeBtn)
    }

    companion object {
        private const val minimumPlayerHeightDp = 100

        private const val EXTRA_TOKEN = "EXTRA_TOKEN"

        fun start(activity: Activity, token: String) {
            val intent = Intent(activity, MainActivity::class.java).apply {
                putExtra(EXTRA_TOKEN, token)
            }
            activity.startActivityForResult(intent, StartActivity.REQUEST_MAIN_ACTIVITY)
        }
    }
}
