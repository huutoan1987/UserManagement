package com.example.usermanagement.ui.activity.master

import androidx.recyclerview.widget.RecyclerView
import com.example.usermanagement.data.entity.User
import com.example.usermanagement.databinding.LayoutMasterListRowBinding

class MasterVH(private val _binding: LayoutMasterListRowBinding,
               private val _vm: MasterVM, private val _isTablet: Boolean) :
    RecyclerView.ViewHolder(_binding.root) {

    fun bind(item: User) {
        _binding.item = item
        _binding.vm = _vm
        _binding.isTablet = _isTablet
        _binding.executePendingBindings()
    }
}