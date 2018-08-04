package com.example.there.flextube.addgroup

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.View
import android.widget.Toast
import com.example.there.flextube.R
import com.example.there.flextube.addgroup.AddGroupActivity.Mode.ADD_SUBS_TO_EXISTING
import com.example.there.flextube.addgroup.AddGroupActivity.Mode.NEW_GROUP
import com.example.there.flextube.databinding.ActivityAddGroupBinding
import com.example.there.flextube.di.vm.ViewModelFactory
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_add_group.*
import javax.inject.Inject


class AddGroupActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: AddGroupViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this, viewModelFactory).get(AddGroupViewModel::class.java)
    }

    private val adapter: AddGroupSubscriptionsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        AddGroupSubscriptionsAdapter(viewModel.viewState.subscriptions, R.layout.subscription_to_choose_item)
    }

    private val view: AddGroupView by lazy(LazyThreadSafetyMode.NONE) {
        AddGroupView(
                viewModel.viewState,
                adapter,
                DividerItemDecoration(this@AddGroupActivity, DividerItemDecoration.VERTICAL).apply {
                    setDrawable(ContextCompat.getDrawable(this@AddGroupActivity, R.drawable.video_separator)!!)
                },
                View.OnClickListener { insertGroup() },
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean = false

                    override fun onQueryTextChange(newText: String?): Boolean {
                        adapter.filter.filter(newText)
                        return false
                    }
                })
    }

    private fun insertGroup() {
        if (viewModel.viewState.subscriptions.any { it.isChosen.get() == true }) {
            when (mode) {
                NEW_GROUP -> viewModel.insertSubscriptionGroup(groupName, accountName)
                ADD_SUBS_TO_EXISTING -> viewModel.addSubscriptionsToGroup(groupName, accountName)
            }
            finish()
        } else {
            Toast.makeText(this@AddGroupActivity, "No subscriptions chosen.", Toast.LENGTH_SHORT).show()
        }
    }

    private val accountName: String by lazy(LazyThreadSafetyMode.NONE) { intent.getStringExtra(EXTRA_ACCOUNT_NAME) }
    private val groupName: String by lazy(LazyThreadSafetyMode.NONE) { intent.getStringExtra(EXTRA_GROUP_NAME) }
    private val mode: AddGroupActivity.Mode by lazy(LazyThreadSafetyMode.NONE) { intent.getSerializableExtra(EXTRA_MODE) as Mode }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityAddGroupBinding>(this, R.layout.activity_add_group)
        binding.addGroupView = view
        binding.subscriptionsToChooseRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        when (mode) {
            NEW_GROUP -> viewModel.loadSubscriptions(accountName)
            ADD_SUBS_TO_EXISTING -> {
                viewModel.loadNotAddedSubscriptions(accountName, groupName)
                binding.addGroupButton.text = getString(R.string.add_subscriptions)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        subscriptions_search_view?.isIconified = false
        subscriptions_search_view?.clearFocus()
    }

    enum class Mode {
        NEW_GROUP, ADD_SUBS_TO_EXISTING
    }

    companion object {
        private const val EXTRA_ACCOUNT_NAME = "EXTRA_ACCOUNT_NAME"
        private const val EXTRA_GROUP_NAME = "EXTRA_GROUP_NAME"
        private const val EXTRA_MODE = "EXTRA_MODE"

        fun start(activity: Activity, accountName: String, groupName: String, mode: Mode = NEW_GROUP) {
            val intent = Intent(activity, AddGroupActivity::class.java).apply {
                putExtra(EXTRA_ACCOUNT_NAME, accountName)
                putExtra(EXTRA_GROUP_NAME, groupName)
                putExtra(EXTRA_MODE, mode)
            }
            activity.startActivity(intent)
        }
    }
}
