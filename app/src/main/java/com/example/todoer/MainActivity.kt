package com.example.todoer

import android.graphics.fonts.FontStyle
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.example.todoer.data.AppDatabase
import com.example.todoer.ui.theme.TodoerTheme
import com.example.todoer.viewmodel.TodoViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = Room.databaseBuilder(
            applicationContext, AppDatabase::class.java, "todo-database"
        ).build()
        val todoModel = TodoViewModel(database, applicationContext)
        setContent {
            TodoerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TodoList(todoModel)
                }
            }
        }
    }
}

@Composable
fun TodoList(viewModel: TodoViewModel) {
    val todoList = viewModel.allTodos.observeAsState(initial = emptyList())
    val completed = viewModel.completedTodos.observeAsState(initial = emptyList())
    val newTodoText = remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .paddingFromBaseline(top = 32.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Spacer(modifier = Modifier.width(10.dp))
            TextField(
                value = newTodoText.value,
                onValueChange = { newTodoText.value = it },
                modifier = Modifier.size(250.dp, 50.dp),
                placeholder = { Text(text = "Enter new task") }
            )
            TextButton(onClick = {
                viewModel.addTask(newTodoText.value)
                newTodoText.value = ""
            },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.size(150.dp, 50.dp).fillMaxWidth()) {
                Text(text = "Add Task")
            }
        }
        Column {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Todo List",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .height(35.dp)
                    .background(Color.Gray)
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }
        if (todoList.value.isEmpty()) {
            Text(text = "No tasks",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .height(35.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }
        todoList.value.forEach { todo ->
            Row {
                Text(text = todo.text,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(0.80f)
                        .height(50.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically))
                IconButton(
                    onClick = { viewModel.markTodoDone(todo, true) },
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color(0xFF007500))
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Mark as done",
                        tint = Color.White
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Completed",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .height(35.dp)
                .background(Color.Gray)
                .fillMaxWidth()
                .fillMaxHeight()
        )
        if (completed.value.isEmpty()) {
            Text(text = "No completed tasks",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .height(35.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }
        completed.value.forEach { todo ->
            Row {
                Text(text = todo.text,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(0.80f)
                        .height(50.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically))
                IconButton(
                    onClick = { viewModel.deleteTodo(todo) },
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color(0xFFD10000))
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
