package com.example.lyceum_saturday10_2025.features.todo.MainScreen.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.lyceum_saturday10_2025.features.todo.MainScreen.presentation.model.TodoItemUi
import com.example.lyceum_saturday10_2025.features.todo.MainScreen.presentation.model.TodoUiState

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TodoScreenContent(
    state: TodoUiState,
    addItem: (String) -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        var textFieldValue by remember { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth(),
            value = textFieldValue,
            onValueChange = { newValue ->
                if (newValue.endsWith('\n')) {
                    if (textFieldValue.isNotBlank()) {
                        addItem(textFieldValue)
                        textFieldValue = ""
                    }
                } else {
                    textFieldValue = newValue
                }
            },
            placeholder = {
                Text("Введите текст")
            }
        )

        Row {
            Button(
                modifier = Modifier.weight(1f).padding(8.dp),
                onClick = {
                    if (textFieldValue.isNotBlank()) {
                        addItem(textFieldValue)
                        textFieldValue = ""
                    }
                }
            ) {
                Text("Добавить")
            }

            Button(
                modifier = Modifier.weight(1f).padding(8.dp),
                onClick = onLogout
            ) {
                Text("Выйти")
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.items) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            text = item.text
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun TodoScreenPreview() {
    TodoScreenContent(
        TodoUiState(
            listOf(
                TodoItemUi(
                    1, "Ivan"
                )
            )
        ),
        {}, {}
    )
}