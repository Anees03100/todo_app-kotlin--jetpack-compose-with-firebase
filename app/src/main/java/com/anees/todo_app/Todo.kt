package com.anees.todo_app

import com.google.firebase.firestore.PropertyName

data class Todo(
    val id: String = "",
    val task: String = "",

    @get:PropertyName("isDone")
    @set:PropertyName("isDone")
    var isDone: Boolean = false
)
