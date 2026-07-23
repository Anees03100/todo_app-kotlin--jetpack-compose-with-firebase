package com.anees.todo_app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.anees.todo_app.ui.theme.Todo_appTheme
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Todo_appTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TodoScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TodoScreen(modifier: Modifier = Modifier, viewModel: TodoViewModel = viewModel()) {
    var text by remember { mutableStateOf("") }
    var editTaskText by remember { mutableStateOf("") }
    val context = LocalContext.current.applicationContext

    var editingTodo by remember { mutableStateOf<Todo?>(null) }

    val totalTasks = viewModel.todos.size
    val completedTasks = viewModel.todos.count { it.isDone }

    Column(modifier = modifier
        .fillMaxSize()
        .padding(12.dp)) {
        Text(
            text = "Todo App",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Spacer(modifier = Modifier.width(1.dp))
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .clip(shape = RoundedCornerShape(40.dp))
                    .background(color = MaterialTheme.colorScheme.primary)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Completed",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(text = "$completedTasks", color = Color.White, fontSize = 40.sp)
                }
            }
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .clip(shape = RoundedCornerShape(40.dp))
                    .background(color = MaterialTheme.colorScheme.primary)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(text = "$totalTasks", color = Color.White, fontSize = 40.sp)
                }
            }
            Spacer(modifier = Modifier.width(1.dp))
        }
        Spacer(modifier = Modifier.height(15.dp))
        HorizontalDivider(
            thickness = 3.dp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("New Task") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (text.isBlank()) {
                    Toast.makeText(context, "Please enter a Task", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.addTodo(text)
                    text = ""
                }
            }) {
                Text("Add")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(viewModel.todos) { todo ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = todo.isDone,
                        onCheckedChange = { viewModel.toggleTodo(todo) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            checkmarkColor = Color.White
                        )
                    )
                    if (editingTodo?.id == todo.id) {
                        Row() {
                            OutlinedTextField(
                                modifier = Modifier.weight(1f),
                                value = editTaskText,
                                onValueChange = { editTaskText = it }
                            )
                            IconButton(onClick = {
                                viewModel.updateTodoText(todo, editTaskText)
                                editingTodo = null
                            }) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Check",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            IconButton(onClick = {
                                editingTodo = null
                            }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Color.Red,
                                )
                            }
                        }
                    } else {
                        Text(
                            text = todo.task,
                            modifier = Modifier.weight(1f),
                            textDecoration = if (todo.isDone) TextDecoration.LineThrough else TextDecoration.None
                        )
                        IconButton(onClick = {
                            editTaskText = todo.task
                            editingTodo = todo
                        }) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(onClick = { viewModel.deleteTodo(todo) }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}