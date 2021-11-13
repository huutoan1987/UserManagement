package com.example.usermanagement.repo

import com.example.usermanagement.api.ApiService
import com.example.usermanagement.data.entity.User
import com.example.usermanagement.data.model.local.UserSearchRequest
import com.example.usermanagement.objects.UMConstants
import com.example.usermanagement.objects.UMResult
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepo @Inject constructor(private val _apiService: ApiService) {

    internal fun searchRemoteUsersAsFlow(searchReq: UserSearchRequest) = flow<UMResult<List<User>>>{
        if (!searchReq.isValid()) throw Exception("Invalid search key")

        emit(UMResult.Success(
            if (searchReq.searchStr.isEmpty()) emptyList()
            else _apiService.searchUsers(
                searchReq.buildRequest(), searchReq.page, UMConstants.NUMBER_ITEM_PER_PAGE)
                .list.map { remoteUser -> remoteUser.toUser() }
            )
        )
    }.catch { e -> emit(UMResult.Error(e)) }
}