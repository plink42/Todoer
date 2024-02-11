package com.example.todoer.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoer.data.AppDatabase
import com.example.todoer.data.Todo
import kotlinx.coroutines.launch

class TodoViewModel(private val database: AppDatabase, context: Context) : ViewModel() {

    private val _allTodos = MutableLiveData<List<Todo>>()
    var allTodos: LiveData<List<Todo>> = _allTodos

    private val _completedTodos = MutableLiveData<List<Todo>>()
    var completedTodos: LiveData<List<Todo>> = _completedTodos

    init {
        getAllTodos()
        getCompletedTodos()
    }

    private fun getAllTodos() {
        viewModelScope.launch {
            val todos = database.todoDao.getIncompleteTodos()
            _allTodos.postValue(todos)
        }
    }

    private fun getCompletedTodos() {
        viewModelScope.launch {
            val todos = database.todoDao.getCompletedTodos()
            _completedTodos.postValue(todos)
        }
    }

    fun addTask(text: String) {
        val todo = Todo(text = text)
        viewModelScope.launch {
            database.todoDao.insertTodo(todo)
            getAllTodos()
            getCompletedTodos()
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            database.todoDao.deleteTodo(todo)
            getAllTodos()
            getCompletedTodos()
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            database.todoDao.updateTodo(todo)
            getAllTodos()
            getCompletedTodos()
        }
    }

    fun markTodoDone(todo: Todo, isDone: Boolean) {
        viewModelScope.launch {
            todo.isDone = isDone
            database.todoDao.updateTodo(todo)
            getAllTodos()
            getCompletedTodos()
        }
    }
}