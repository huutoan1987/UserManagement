package com.example.usermanagement.ui.activity.master

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import com.example.usermanagement.data.entity.User
import com.example.usermanagement.databinding.LayoutMasterListRowBinding
import java.util.concurrent.Executors

class MasterAdpt(private val _vm: MasterVM, private val _isTablet: Boolean) :
    ListAdapter<User, MasterVH>(
        AsyncDifferConfig.Builder(MasterDiffCallback())
            .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
            .build()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutMasterListRowBinding.inflate(inflater, parent, false)
        return MasterVH(binding, _vm, _isTablet)
    }

    override fun onBindViewHolder(holder: MasterVH, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}