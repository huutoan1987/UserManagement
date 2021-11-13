package com.example.usermanagement.util

import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
fun RecyclerView.onScrollToBottomFlow() = callbackFlow<RecyclerView> {
    val listener = object: RecyclerView.OnScrollListener() {
        override fun onScrolled(rclv: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(rclv, dx, dy)
            if(!rclv.canScrollVertically(1)) {
                offer(rclv)
            }
        }
    }
    addOnScrollListener(listener)
    awaitClose { removeOnScrollListener(listener) }
}

@ExperimentalCoroutinesApi
fun SearchView.onSubmitTextFlow() = callbackFlow<String> {
    val listener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            offer(query ?: "")
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    }
    setOnQueryTextListener(listener)
    awaitClose { setOnQueryTextListener(null) }
}

@ExperimentalCoroutinesApi
fun SearchView.onCloseFlow() = callbackFlow<SearchView> {
    val listener = SearchView.OnCloseListener {
        offer(this@onCloseFlow)
        false
    }
    setOnCloseListener(listener)
    awaitClose { setOnCloseListener(null) }
}