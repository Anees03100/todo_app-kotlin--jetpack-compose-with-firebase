package com.anees.todo_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {

    val todos: StateFlow<List<Todo>> = repository.allTodos.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addTodo(task: String) {
        if (task.isBlank()) return
        viewModelScope.launch {
            repository.addTodo(task)
        }
    }

    fun toggleTodo(todo: Todo) {
        viewModelScope.launch {
            repository.toggleTodo(todo)
        }
    }

    fun updateTodoText(todo: Todo, newTaskText: String) {
        if (newTaskText.isBlank()) return
        viewModelScope.launch {
            repository.updateTodoText(todo, newTaskText)
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            repository.deleteTodo(todo)
        }
    }
}