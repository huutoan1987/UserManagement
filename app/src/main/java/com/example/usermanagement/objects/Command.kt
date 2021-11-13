package com.example.usermanagement.objects

import com.example.usermanagement.data.entity.User

sealed class Command {
    class ShowToast(val msg: String) : Command()
    class OpenDetailActivity(val user: User) : Command()
    class UpdateDetailFragment(val user: User?) : Command()
}