package com.example.usermanagement.ui

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.usermanagement.interfaces.IDispatcher
import com.example.usermanagement.objects.Command
import com.example.usermanagement.objects.SingleLiveEvent
import kotlinx.coroutines.launch
import retrofit2.HttpException

abstract class BaseActivityVM constructor(protected val dispatcher: IDispatcher): ViewModel() {
    private val _command: SingleLiveEvent<Command> = SingleLiveEvent()
    val command = _command
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    fun setCommand(cmd: Command) = viewModelScope.launch(dispatcher.main()) {
        command.value = cmd
    }

    protected fun handleException(t: Throwable) {
        try {
            when (t) {
                is HttpException -> {
                    setCommand(Command.ShowToast(
                        t.response()?.errorBody()?.string() ?: "Error HTTP ${t.code()}"))
                }
                else -> setCommand(Command.ShowToast(t.localizedMessage))
            }
        } catch (e: Exception) {
            setCommand(Command.ShowToast("Unknown Error"))
        }
    }
}