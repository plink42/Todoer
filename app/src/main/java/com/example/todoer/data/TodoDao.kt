package com.example.todoer.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoDao {
    @Insert
    suspend fun insertTodo(todo: Todo)

    @Update
    suspend fun updateTodo(todo: Todo)

    @Delete
    suspend fun deleteTodo(todo: Todo)

    @Query("SELECT * FROM todo WHERE id = :id")
    suspend fun get(id: Int): Todo

    @Query("SELECT * FROM todo")
    suspend fun getAll(): List<Todo>

    @Query("SELECT * FROM todo WHERE isDone = 0")
    suspend fun getIncompleteTodos(): List<Todo>

    @Query("SELECT * FROM todo WHERE isDone = 1")
    suspend fun getCompletedTodos(): List<Todo>

    @Query("SELECT * FROM todo WHERE isDone = :isDone")
    suspend fun getTodosByDone(isDone: Boolean): List<Todo>
}