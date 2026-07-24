package com.anees.todo_app

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class TodoViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val collection = db.collection("todos")

    var todos = mutableStateListOf<Todo>()
        private set

    init {
        fetchTodos()
    }

    private fun fetchTodos() {
        collection.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) {
                return@addSnapshotListener
            }
            todos.clear()
            todos.addAll(snapshot.toObjects(Todo::class.java))
        }
    }
    fun addTodo(task: String){
        if(task.isBlank())return
        val id = collection.document().id
        val todo = Todo(id = id , task = task)
        collection.document(id).set(todo)
    }

    fun toggleTodo(todo: Todo){
        collection.document(todo.id).update("isDone", !todo.isDone)
    }

    fun updateTodoText(todo: Todo, newTaskText: String){
        if(newTaskText.isBlank()) return
        collection.document(todo.id).update("task", newTaskText)
    }

    fun deleteTodo(todo:Todo){
        collection.document(todo.id).delete()
    }
}