package com.example.usermanagement.ui.activity.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.usermanagement.R
import com.example.usermanagement.data.entity.User
import com.example.usermanagement.databinding.ActivityDetailBinding
import com.example.usermanagement.ui.BaseActivity
import com.example.usermanagement.ui.fragment.detail.DetailFrg
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : BaseActivity() {
    companion object {
        fun newIntent(ctx: Context, user: User) = Intent(ctx, DetailActivity::class.java).apply {
            putExtra(DetailFrg.ARG_USER, user)
        }
    }

    private val _detailVM: DetailVM by viewModels()
    private val _user by lazy { intent.getParcelableExtra<User>(DetailFrg.ARG_USER) }
    private lateinit var _binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.toolbar.title = getString(R.string.title_item_detail)
        setSupportActionBar(_binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            _user?.let {
                supportFragmentManager.beginTransaction()
                    .add(R.id.item_detail_container, DetailFrg.newInstance(it))
                    .commit()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}