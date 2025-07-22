package com.example.evepisi.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evepisi.model.DogItem

@Composable
fun InventoryScreen(inventory: List<DogItem>, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("Inventory") },
        text = {
            LazyRow {
                items(inventory) { item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(item.iconResId),
                            contentDescription = item.id,
                            modifier = Modifier.size(64.dp)
                        )
                        Text(item.id, fontSize = 12.sp)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onClose) {
                Text("Close")
            }
        }
    )
}