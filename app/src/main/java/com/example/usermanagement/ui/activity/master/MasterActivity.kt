package com.example.usermanagement.ui.activity.master

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.usermanagement.R
import com.example.usermanagement.databinding.ActivityMasterBinding
import com.example.usermanagement.objects.Command
import com.example.usermanagement.ui.BaseActivity
import com.example.usermanagement.ui.activity.detail.DetailActivity
import com.example.usermanagement.ui.fragment.detail.DetailFrg
import com.example.usermanagement.util.onCloseFlow
import com.example.usermanagement.util.onScrollToBottomFlow
import com.example.usermanagement.util.onSubmitTextFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MasterActivity : BaseActivity() {

    private var _isTablet: Boolean = false
    private val _masterVM: MasterVM by viewModels()
    private lateinit var _binding: ActivityMasterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMasterBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        setSupportActionBar(_binding.toolbar)

        findViewById<FrameLayout>(R.id.item_detail_container)?.let { _isTablet = true }

        setupView()
        setupObserver()
    }

    private fun setupView() {
        _binding.toolbar.title = getString(R.string.app_name)
        rclvUser()?.adapter = MasterAdpt(_masterVM, _isTablet)
        lifecycleScope.launchWhenResumed {
            rclvUser()?.onScrollToBottomFlow()?.collect { _masterVM.onListScrollToEnd() }
        }
        lifecycleScope.launchWhenResumed {
            _binding.searchView.onSubmitTextFlow()
                .distinctUntilChanged() // only process if data change
                .collect { _masterVM.onSearchQueryChange(it, _isTablet) }
        }
        lifecycleScope.launchWhenResumed {
            _binding.searchView.onCloseFlow().collect {
                _masterVM.onSearchQueryChange("", _isTablet)
            }
        }
    }

    private fun setupObserver() {
        _masterVM.lstUser.observe(this, { lst ->
            (rclvUser()?.adapter as? MasterAdpt)?.submitList(lst)
        })
        _masterVM.command.observe(this, { cmd -> handleCommand(cmd) })
    }

    override fun handleCommand(cmd: Command) {
        when (cmd) {
            is Command.OpenDetailActivity -> {
                startActivity(DetailActivity.newIntent(this, cmd.user))
            }
            is Command.UpdateDetailFragment -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.item_detail_container, DetailFrg.newInstance(cmd.user))
                    .commit()
            }
            else -> { super.handleCommand(cmd) }
        }
    }

    private fun rclvUser() = findViewById<RecyclerView>(R.id.rclv_user)
}