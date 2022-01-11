package com.example.usermanagement.ui.activity.master

import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.usermanagement.data.entity.User
import com.example.usermanagement.data.model.local.UserSearchRequest
import com.example.usermanagement.interfaces.IDispatcher
import com.example.usermanagement.objects.Command
import com.example.usermanagement.objects.UMResult
import com.example.usermanagement.repo.UserRepo
import com.example.usermanagement.ui.BaseActivityVM
import com.example.usermanagement.util.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class MasterVM @Inject constructor(
    private val _userRepo: UserRepo,
    private val _logUtil: LogUtil,
    dispatch: IDispatcher) : BaseActivityVM(dispatch) {

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    val _searchReqFlow = MutableStateFlow(UserSearchRequest())

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    val _lstUserLD = MutableLiveData<List<User>>()
    val lstUser: LiveData<List<User>> = _lstUserLD

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    val _searchChainFlow = _searchReqFlow
        .debounce(500)
        .flatMapLatest { searchReq -> _userRepo.searchRemoteUsersAsFlow(searchReq) }
        .flowOn(dispatcher.io())
        .catch { t -> _logUtil.e(t) }  // if this line is run, whole flow will be canceled

    init {
        viewModelScope.launch(dispatcher.main()) {
            _searchChainFlow.collect { result ->
                when (result) {
                    is UMResult.Success -> {
                        if (_searchReqFlow.value.isFirstPage()) {
                            _lstUserLD.value = result.data ?: emptyList()
                        } else {
                            _lstUserLD.value = _lstUserLD.value?.plus(result.data)
                        }
                    }
                    is UMResult.Error -> handleException(result.exception)
                    is UMResult.Loading -> {}
                }
            }
        }
    }

    internal fun onSearchQueryChange(query: String, isTablet: Boolean) {
        _lstUserLD.value = emptyList()
        if (isTablet) setCommand(Command.UpdateDetailFragment(null))

        _searchReqFlow.value = UserSearchRequest(query.trim())
    }

    internal fun onListScrollToEnd() {
        _lstUserLD.value?.let {
            // TODO miss checking is last page
            if (it.isNotEmpty())
                _searchReqFlow.value = _searchReqFlow.value.copy(page = _searchReqFlow.value.page+1)
        }
    }

    fun onClickMaster(v: View, item: User, isTablet: Boolean) {
        setCommand(
            if (isTablet) Command.UpdateDetailFragment(item)
            else Command.OpenDetailActivity(item)
        )
    }
}