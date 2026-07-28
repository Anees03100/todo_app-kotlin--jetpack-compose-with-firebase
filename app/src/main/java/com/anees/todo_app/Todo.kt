package com.anees.todo_app

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey
    val id: String = "",
    val task: String = "",

    @get:PropertyName("isDone")
    @set:PropertyName("isDone")
    var isDone: Boolean = false
)
