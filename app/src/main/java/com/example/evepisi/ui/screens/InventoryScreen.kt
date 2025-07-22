package com.example.evepisi.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evepisi.model.DogItem

@Composable
fun InventoryScreen(inventory: List<DogItem>, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onClose) {
                Text("Close")
            }
        },
        title = { Text("Inventory") },
        text = {
            val groupedItems = inventory.groupBy { it.iconResId }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(groupedItems.entries.toList()) { (iconResId, items) ->
                    Box(modifier = Modifier.size(64.dp)) {
                        Image(
                            painter = painterResource(id = iconResId),
                            contentDescription = "Item",
                            modifier = Modifier.fillMaxSize()
                        )

                        if (items.size > 1) {
                            Text(
                                text = "${items.size}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(2.dp)
                            )
                        }
                    }
                }
            }
        }
    )
}
