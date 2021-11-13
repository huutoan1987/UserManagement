package com.example.usermanagement.ui

import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.example.usermanagement.objects.Command

abstract class BaseActivity : AppCompatActivity() {
    @CallSuper
    protected open fun handleCommand(cmd: Command) {
        when (cmd) {
            is Command.ShowToast -> showToast(cmd.msg)
            else -> {}
        }
    }

    protected fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}