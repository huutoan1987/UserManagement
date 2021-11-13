package com.example.usermanagement.ui.activity.master

import androidx.recyclerview.widget.DiffUtil
import com.example.usermanagement.data.entity.User

class MasterDiffCallback : DiffUtil.ItemCallback<User>()  {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.remoteId == newItem.remoteId
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}