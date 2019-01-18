package com.example.there.multifeeds.main

import android.app.Activity
import android.app.SearchManager
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageButton
import android.widget.RelativeLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.example.there.multifeeds.R
import com.example.there.multifeeds.base.fragment.BaseHostFragment
import com.example.there.multifeeds.base.fragment.HasBackNavigation
import com.example.there.multifeeds.base.fragment.HasTitle
import com.example.there.multifeeds.base.fragment.Scrollable
import com.example.there.multifeeds.databinding.ActivityMainBinding
import com.example.there.multifeeds.di.vm.ViewModelFactory
import com.example.there.multifeeds.lifecycle.DisposablesComponent
import com.example.there.multifeeds.list.VideosAdapter
import com.example.there.multifeeds.start.StartActivity
import com.example.there.multifeeds.util.ext.resetTitle
import com.example.there.multifeeds.util.ext.screenHeight
import com.example.there.multifeeds.util.ext.screenOrientation
import com.example.there.multifeeds.util.ext.toPx
import com.example.there.multifeeds.util.view.DividerItemDecorator
import com.example.there.multifeeds.util.view.EndlessRecyclerOnScrollListener
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer
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

    val accessToken: String by lazy(LazyThreadSafetyMode.NONE) { intent.getStringExtra(EXTRA_TOKEN) }

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: MainViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this, factory).get(MainViewModel::class.java)
    }

    //region relatedVideosRecyclerView
    private val relatedVideosAdapter: VideosAdapter by lazy(LazyThreadSafetyMode.NONE) {
        VideosAdapter(viewModel.viewState.relatedVideos, R.layout.video_item, R.layout.loading_item)
    }

    private val onRelatedVideosScrollListener: EndlessRecyclerOnScrollListener by lazy(LazyThreadSafetyMode.NONE) {
        object : EndlessRecyclerOnScrollListener(returnFromOnScrolledItemCount = 1) {
            override fun onLoadMore() {
                lastPlayedVideoId?.let {
                    relatedVideosAdapter.loadingInProgress.set(true)
                    viewModel.loadRelatedVideos(it, false, onFinally = {
                        relatedVideosAdapter.loadingInProgress.set(false)
                    })
                }
            }
        }
    }

    private val relatedVideosItemDecoration: DividerItemDecorator by lazy(LazyThreadSafetyMode.NONE) {
        DividerItemDecorator(ContextCompat.getDrawable(this, R.drawable.video_separator)!!)
    }
    //endregion

    //region bottomNavigationView
    private val itemIds: Array<Int> = arrayOf(R.id.action_home, R.id.action_sub_feed, R.id.action_groups)

    private val onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (item.itemId == main_bottom_navigation_view.selectedItemId) {
            return@OnNavigationItemSelectedListener false
        }

        main_view_pager.currentItem = itemIds.indexOf(item.itemId)
        main_app_bar_layout?.setExpanded(true, true)

        return@OnNavigationItemSelectedListener true
    }
    //endregion

    //region viewPager
    private val viewPagerAdapter: MainViewPagerAdapter by lazy(LazyThreadSafetyMode.NONE) { MainViewPagerAdapter(supportFragmentManager) }

    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) = Unit

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

        override fun onPageSelected(position: Int) {
            main_bottom_navigation_view?.menu?.findItem(itemIds[position])?.isChecked = true
            main_app_bar_layout?.setExpanded(true, true)
        }
    }
    //endregion

    //region slidingUpPanelLayout listeners
    private val fadeOnClickListener = View.OnClickListener {
        sliding_layout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
    }

    private val slideListener = object : SlidingUpPanelLayout.PanelSlideListener {
        override fun onPanelSlide(panel: View?, slideOffset: Float) = updatePlayerDimensions(slideOffset)

        override fun onPanelStateChanged(
                panel: View?,
                previousState: SlidingUpPanelLayout.PanelState?,
                newState: SlidingUpPanelLayout.PanelState?
        ) {
            if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                minimizeBtn.setImageResource(R.drawable.minimize)
            } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                minimizeBtn.setImageResource(R.drawable.maximize)
            }
        }
    }
    //endregion

    private val onScrollToTopClicked by lazy(LazyThreadSafetyMode.NONE) {
        View.OnClickListener {
            val currentFragment = viewPagerAdapter.currentFragment as? BaseHostFragment
            currentFragment?.let {
                val scrollableFragment = it.childFragmentManager.findFragmentById(it.backStackLayoutId) as? Scrollable
                scrollableFragment?.scrollToTop()
            }
        }
    }

    private val view: MainActivityView by lazy(LazyThreadSafetyMode.NONE) {
        MainActivityView(
                state = viewModel.viewState,
                relatedVideosAdapter = relatedVideosAdapter,
                onRelatedVideosScroll = onRelatedVideosScrollListener,
                itemDecoration = relatedVideosItemDecoration,
                onNavigationItemSelectedListener = onNavigationItemSelectedListener,
                viewPagerAdapter = viewPagerAdapter,
                onPageChangeListener = onPageChangeListener,
                offScreenPageLimit = 2,
                fadeOnClickListener = fadeOnClickListener,
                slideListener = slideListener,
                initialSlidePanelState = SlidingUpPanelLayout.PanelState.HIDDEN,
                onScrollToTopClicked = onScrollToTopClicked
        )
    }

    private val disposablesComponent = DisposablesComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.mainView = view
        binding.relatedVideosRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        setSupportActionBar(main_toolbar)

        lifecycle.addObserver(disposablesComponent)
        disposablesComponent.add(relatedVideosAdapter.videoClicked.subscribe { loadVideo(it) })

        initYouTubePlayerView()
        addPlayerViewControls()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleSearchIntent(intent)
    }

    private fun handleSearchIntent(intent: Intent?) {
        fun saveQuery(query: String) {
            val suggestions = SearchRecentSuggestions(this, SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE)
            suggestions.saveRecentQuery(query, null)
        }

        if (Intent.ACTION_SEARCH == intent?.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            saveQuery(query)

            val currentFragment = viewPagerAdapter.currentFragment as? BaseHostFragment
            currentFragment?.showSearchFragment(query)

            searchViewMenuItem?.collapseActionView()
        }
    }

    fun updateToolbarTitle(currentFragment: Fragment) {
        val baseHostFragment = currentFragment as? BaseHostFragment
        baseHostFragment?.let {
            val title = (it.childFragmentManager.findFragmentById(it.backStackLayoutId) as? HasTitle)?.title
            title?.let { main_toolbar?.resetTitle(it) }
        }
    }

    fun addBackNavigationToToolbar() {
        main_toolbar?.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.navigate_back, null)
        main_toolbar?.setNavigationOnClickListener { onBackPressed() }
    }

    fun updateToolbarBackNavigation(currentFragment: Fragment) {
        val baseHostFragment = currentFragment as? BaseHostFragment
        baseHostFragment?.let {
            if (it.childFragmentManager.findFragmentById(it.backStackLayoutId) is HasBackNavigation) {
                addBackNavigationToToolbar()
            } else {
                main_toolbar?.navigationIcon = null
            }
        }
    }

    override fun onBackPressed() {
        val currentFragment = viewPagerAdapter.currentFragment
        if (currentFragment != null && currentFragment.childFragmentManager.backStackEntryCount > 0) {
            currentFragment.childFragmentManager.popBackStackImmediate()
            updateToolbarTitle(currentFragment)
            updateToolbarBackNavigation(currentFragment)
        } else {
            showLogoutDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_options_menu, menu)
        initVideoSearch(menu)
        return true
    }

    private var searchViewMenuItem: MenuItem? = null

    private fun initVideoSearch(menu: Menu?) {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchViewMenuItem = menu?.findItem(R.id.action_search)
        val searchView = searchViewMenuItem?.actionView as? SearchView
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.action_logout -> {
            showLogoutDialog()
            true
        }
        R.id.action_search -> true
        else -> super.onOptionsItemSelected(item)
    }

    private fun showLogoutDialog() = MaterialDialog.Builder(this)
            .title(getString(R.string.want_to_logout))
            .onPositive { _, _ -> super.onBackPressed() }
            .positiveText(getString(R.string.yes))
            .negativeText(getString(R.string.no))
            .build()
            .apply { show() }

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

    private var youTubePlayer: YouTubePlayer? = null

    private fun initYouTubePlayerView() {
        lifecycle.addObserver(player_view)
        player_view.initialize({ this.youTubePlayer = it }, true)
        player_view.playerUIController.showFullscreenButton(false)
    }

    private var lastPlayedVideoId: String? = null

    fun loadVideo(videoId: String) {
        if (videoId == lastPlayedVideoId) return
        lastPlayedVideoId = videoId

        if (sliding_layout.panelState == SlidingUpPanelLayout.PanelState.HIDDEN)
            sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        if (lifecycle.currentState == Lifecycle.State.RESUMED)
            youTubePlayer?.loadVideo(videoId, 0f)
        else
            youTubePlayer?.cueVideo(videoId, 0f)

        viewModel.loadRelatedVideos(videoId, true, onAfterAdd = {
            related_videos_recycler_view?.scrollToPosition(0)
        })
    }

    //region player controls
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
                youTubePlayer?.pause()
            }
            setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private fun addPlayerViewControls() = findViewById<RelativeLayout>(R.id.controls_root).apply {
        addView(minimizeBtn)
        addView(closeBtn)
    }
    //endregion

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
