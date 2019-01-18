package com.example.there.multifeeds.main

import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.there.multifeeds.list.VideosAdapter
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class MainActivityView(
        val state: MainActivityViewState,
        val relatedVideosAdapter: VideosAdapter,
        val onRelatedVideosScroll: RecyclerView.OnScrollListener,
        val itemDecoration: RecyclerView.ItemDecoration,
        val onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener,
        val viewPagerAdapter: FragmentStatePagerAdapter,
        val onPageChangeListener: ViewPager.OnPageChangeListener,
        val offScreenPageLimit: Int,
        val fadeOnClickListener: View.OnClickListener,
        val slideListener: SlidingUpPanelLayout.PanelSlideListener,
        val initialSlidePanelState: SlidingUpPanelLayout.PanelState,
        val onScrollToTopClicked: View.OnClickListener
)