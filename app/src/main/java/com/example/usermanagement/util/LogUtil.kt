package com.example.usermanagement.util

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogUtil @Inject constructor(){
    fun d(content: String) {
        Log.d("UserManagement", content)
    }
    fun e(t: Throwable) {
        System.out.println(t)
    }
}