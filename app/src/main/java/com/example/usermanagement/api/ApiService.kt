package com.example.usermanagement.api

import com.example.usermanagement.data.model.remote.RemoteSearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") searchStr: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): RemoteSearchResult
}