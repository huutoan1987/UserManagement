package com.example.usermanagement.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var localId: Long = 0,

    var remoteId: Long = -1,

    var name: String,

    val avatar: String,

    val score: Float,

    val url: String,

) : Parcelable
