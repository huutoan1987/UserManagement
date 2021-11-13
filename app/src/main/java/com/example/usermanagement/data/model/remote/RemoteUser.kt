package com.example.usermanagement.data.model.remote

import com.example.usermanagement.data.entity.User
import com.google.gson.annotations.SerializedName

data class RemoteUser(
    @SerializedName("id")
    val remoteId: Long,

    @SerializedName("login")
    val name: String,

    @SerializedName("avatar_url")
    val avatar: String,

    @SerializedName("score")
    val score: Float,

    @SerializedName("url")
    val url: String,
) {
    fun toUser() = User(
        localId = -1L,
        remoteId = remoteId,
        name = name,
        avatar = avatar,
        score = score,
        url = url
    )
}
