package com.example.usermanagement.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.usermanagement.CoroutineTestRule
import com.example.usermanagement.api.ApiService
import com.example.usermanagement.data.model.local.UserSearchRequest
import com.example.usermanagement.data.model.remote.RemoteSearchResult
import com.example.usermanagement.data.model.remote.RemoteUser
import com.example.usermanagement.objects.UMConstants.EXCEPTION_INVALID_SEARCH_KEY
import com.example.usermanagement.objects.UMResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.Matchers.empty
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.mockito.Mockito.*

class UserRepoTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private lateinit var apiService: ApiService
    private lateinit var userRepo: UserRepo

    @Before
    fun setUp() {
        apiService = mock(ApiService::class.java)
        userRepo = UserRepo(apiService)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun test_searchRemoteUsersAsFlow_by_empty_key() = coroutinesTestRule.dispatcher.runBlockingTest {
        val result = userRepo.searchRemoteUsersAsFlow(UserSearchRequest("", 1)).first()

        assertThat(result, instanceOf(UMResult.Success::class.java))
        assertThat((result as UMResult.Success).data, `is`(empty()) )
    }

    @Test
    fun test_searchRemoteUsersAsFlow_by_invalid_key() = coroutinesTestRule.dispatcher.runBlockingTest {
        val result = userRepo.searchRemoteUsersAsFlow(UserSearchRequest("invalidkey", 1)).first()

        assertThat(result, instanceOf(UMResult.Error::class.java))
        assertEquals((result as UMResult.Error).exception.message, EXCEPTION_INVALID_SEARCH_KEY)
    }

    @Test
    fun test_searchRemoteUsersAsFlow_success() = coroutinesTestRule.dispatcher.runBlockingTest {
        `when`(apiService.searchUsers(anyString(), anyInt(), anyInt())).thenReturn(
            RemoteSearchResult(1, listOf(
                RemoteUser(123, "User Test", "", 0f, "")))
        )
        val result = userRepo.searchRemoteUsersAsFlow(UserSearchRequest("validkey", 1)).first()

        assertThat(result, instanceOf(UMResult.Success::class.java))
        assertEquals((result as UMResult.Success).data.size, 1)
        assertEquals((result as UMResult.Success).data[0].remoteId, 123)
        assertEquals((result as UMResult.Success).data[0].name, "User Test")
    }
}