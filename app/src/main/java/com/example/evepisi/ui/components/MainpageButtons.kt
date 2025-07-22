package com.example.evepisi.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import com.example.evepisi.R

@Composable
fun InventoryButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_toybasket),
            contentDescription = "Inventory",
            modifier = Modifier.size(64.dp)
        )
    }
}

@Composable
fun ChatButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_chat),
            contentDescription = "Chat with Eve",
            modifier = Modifier.size(64.dp)
        )
    }
}