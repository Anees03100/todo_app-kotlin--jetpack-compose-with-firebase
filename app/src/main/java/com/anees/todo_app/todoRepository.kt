package com.anees.todo_app

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TodoRepository(
    private val todoDao: TodoDao,
    private val firestore: FirebaseFirestore
) {
    private val collection = firestore.collection("todos")

    val allTodos: Flow<List<Todo>> = todoDao.getAllTodos()

    init {
        syncFromFirebase()
    }

    private fun syncFromFirebase() {
        collection.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) return@addSnapshotListener

            val remoteTodos = snapshot.toObjects(Todo::class.java)

            CoroutineScope(Dispatchers.IO).launch {
                todoDao.insertAll(remoteTodos)
            }
        }
    }

    suspend fun addTodo(task: String) {
        val id = collection.document().id
        val todo = Todo(id = id, task = task)

        todoDao.insertTodo(todo)
        collection.document(id).set(todo).await()
    }

    suspend fun toggleTodo(todo: Todo) {
        val updatedTodo = todo.copy(isDone = !todo.isDone)

        todoDao.updateTodo(updatedTodo)

        collection.document(todo.id).update("isDone", updatedTodo.isDone).await()
    }

    suspend fun updateTodoText(todo: Todo, newTaskText: String) {
        val updatedTodo = todo.copy(task = newTaskText)

        todoDao.updateTodo(updatedTodo)
        collection.document(todo.id).update("task", newTaskText).await()
    }

    suspend fun deleteTodo(todo: Todo) {
        todoDao.deleteTodo(todo)
        collection.document(todo.id).delete().await()
    }
}