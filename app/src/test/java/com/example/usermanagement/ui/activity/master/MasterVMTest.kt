package com.example.usermanagement.ui.activity.master

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.usermanagement.CoroutineTestRule
import com.example.usermanagement.data.entity.User
import com.example.usermanagement.data.model.local.UserSearchRequest
import com.example.usermanagement.getOrAwaitValue
import com.example.usermanagement.objects.Command
import com.example.usermanagement.objects.UMResult
import com.example.usermanagement.repo.UserRepo
import com.example.usermanagement.util.LogUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.Matchers.empty
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class MasterVMTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private lateinit var logUtil: LogUtil
    private lateinit var userRepo: UserRepo
    private lateinit var masterVM: MasterVM

    @Before
    fun setUp() {
        userRepo = mock(UserRepo::class.java)
        logUtil = mock(LogUtil::class.java)
        masterVM = MasterVM(userRepo, logUtil, coroutinesTestRule.dispatcherProvider)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun test_onSearchQueryChange_on_tablet() = coroutinesTestRule.dispatcher.runBlockingTest {
        masterVM.onSearchQueryChange(" anyString   ", true)
        val value1 = masterVM._searchReqFlow.first()
        val value2 = masterVM.lstUser.getOrAwaitValue()
        val value3 = masterVM.command.getOrAwaitValue()

        assertThat(value1, instanceOf(UserSearchRequest::class.java))
        assertEquals(value1.searchStr, "anyString")
        assertEquals(value1.page, 1)
        assertThat(value2, CoreMatchers.`is`(empty()))
        assertThat(value3, instanceOf(Command.UpdateDetailFragment::class.java))
        assertEquals((value3 as Command.UpdateDetailFragment).user, null)
    }

    @Test
    fun test_onSearchQueryChange_on_nontablet() = coroutinesTestRule.dispatcher.runBlockingTest {
        val observer: Observer<Command> = mock()

        masterVM.command.observeForever(observer)
        masterVM.onSearchQueryChange(" anyString   ", false)
        masterVM.command.removeObserver(observer)

        val value1 = masterVM._searchReqFlow.first()
        val value2 = masterVM.lstUser.getOrAwaitValue()
        assertThat(value1, instanceOf(UserSearchRequest::class.java))
        assertEquals(value1.searchStr, "anyString")
        assertEquals(value1.page, 1)
        assertThat(value2, CoreMatchers.`is`(empty()))
        verify(observer, never()).onChanged(any())
    }

    @Test
    fun test_onListScrollToEnd_when_userList_empty() = coroutinesTestRule.dispatcher.runBlockingTest {
        masterVM._lstUserLD.value = emptyList()

        masterVM.onListScrollToEnd()

        val value = masterVM._searchReqFlow.first()
        assertThat(value, instanceOf(UserSearchRequest::class.java))
        assertEquals(value.page, 1)
    }

    @Test
    fun test_onListScrollToEnd_when_userList_not_empty() = coroutinesTestRule.dispatcher.runBlockingTest {
        masterVM._lstUserLD.value = listOf(mock(User::class.java))

        masterVM.onListScrollToEnd()

        val value = masterVM._searchReqFlow.first()
        assertThat(value, instanceOf(UserSearchRequest::class.java))
        assertEquals(value.page, 2)
    }

    @Test
    fun test_onClickMaster_on_nontablet() = coroutinesTestRule.dispatcher.runBlockingTest {
        masterVM.onClickMaster(mock(View::class.java), mock(User::class.java), false)

        val value = masterVM.command.getOrAwaitValue()
        assertThat(value, instanceOf(Command.OpenDetailActivity::class.java))
    }

    @Test
    fun test_onClickMaster_on_tablet() = coroutinesTestRule.dispatcher.runBlockingTest {
        masterVM.onClickMaster(mock(View::class.java), mock(User::class.java), true)

        val value = masterVM.command.getOrAwaitValue()
        assertThat(value, instanceOf(Command.UpdateDetailFragment::class.java))
    }

    @Test
    fun test_handleSearchFlow_error() = coroutinesTestRule.dispatcher.runBlockingTest {
        `when`(userRepo.searchRemoteUsersAsFlow(mock(UserSearchRequest::class.java))).thenReturn(
            flow { emit(UMResult.Error(Exception("Test Exception"))) }
        )

        masterVM._searchReqFlow.value = UserSearchRequest("anystring")

        val result = masterVM._searchChainFlow.first()

        assertThat(result, instanceOf(UMResult.Error::class.java))
        assertEquals((result as UMResult.Error).exception.message, "Test Exception")
    }
}