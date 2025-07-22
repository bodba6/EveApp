package com.example.evepisi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.runtime.*

@Composable
fun ChatScreen(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var userInput by remember { mutableStateOf("") }
    var responseText by remember { mutableStateOf("Hi! I'm Eve 🐾") }
    var isLoading by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Chat with Eve", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))

                Text(responseText, modifier = Modifier.padding(vertical = 8.dp))

                OutlinedTextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    label = { Text("Say something...") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(onClick = {
                        onDismiss()
                    }) {
                        Text("Close")
                    }

                    Button(
                        onClick = {
                            isLoading = true
                            // Simulated reply – Replace this with actual API call later
                            responseText = "Eve: " + generateFakeReply(userInput)
                            userInput = ""
                            isLoading = false
                        },
                        enabled = userInput.isNotBlank() && !isLoading
                    ) {
                        Text(if (isLoading) "..." else "Send")
                    }
                }
            }
        }
    }
}

private fun generateFakeReply(input: String): String {
    return when {
        input.contains("hi", ignoreCase = true) -> "Hi there! Want to play?"
        input.contains("treat", ignoreCase = true) -> "Yum! I love treats 🍪"
        input.contains("walk", ignoreCase = true) -> "Wanna go out? Let’s goooo!"
        else -> "I'm not sure what that means, but I’m excited anyway! 🐶"
    }
}
