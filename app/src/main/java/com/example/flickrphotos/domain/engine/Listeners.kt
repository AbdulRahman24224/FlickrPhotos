package com.example.flickrphotos.domain.engine

class SendSingleItemListener<T>(val item: (item: T) -> Unit) {
    fun sendItem(item: T) = item(item)
}