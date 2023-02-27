package com.wislie.wanandroid.data

/**
 * to do info
 */
data class ToDoInfo(
    val completeDate: Any,
    val completeDateStr: String,
    var content: String,
    var date: Long,
    var dateStr: String,
    val id: Int,
    var priority: Int,
    var status: Int,
    var title: String,
    var type: Int,
    val userId: Int,
    var isOpen:Boolean = false
)