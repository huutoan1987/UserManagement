package com.example.usermanagement.data.model.remote

import com.google.gson.annotations.SerializedName

data class RemoteSearchResult(
    @SerializedName("total_count")
    val total: Int,

    @SerializedName("items")
    val list: List<RemoteUser>
) {
}