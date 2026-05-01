// MainActivity.kt
package com.example.evepisi

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.evepisi.ui.theme.EvePisiTheme
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import androidx.compose.runtime.snapshots.SnapshotStateList

import com.example.evepisi.model.DogItem
import com.example.evepisi.ui.components.InventoryButton
import com.example.evepisi.ui.screens.InventoryScreen

val contentPadding = 16.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefs = getSharedPreferences("eve_pisi_prefs", Context.MODE_PRIVATE)
        val today = LocalDate.now()

        setContent {
            EvePisiTheme {
                var lastDate by remember {
                    mutableStateOf<LocalDate?>(
                        prefs.getString("last_pisi_date", null)?.let { LocalDate.parse(it) }
                    )
                }
                
                var daysSince by remember {
                    mutableStateOf(
                        lastDate?.let { ChronoUnit.DAYS.between(it, today).toInt() } ?: 0
                    )
                }

                val inventory: SnapshotStateList<DogItem> = remember { mutableStateListOf() }

                LaunchedEffect(daysSince) {
                    val newItems = mutableListOf<DogItem>()
                    
                    if (daysSince != 0 && daysSince % 3 == 0) {
                        newItems.add(DogItem(id = "treat_${daysSince}", iconResId = R.drawable.icon_treat))
                    }
                    
                    if (daysSince != 0 && daysSince % 5 == 0) {
                        val randomItems = listOf(
                            DogItem(id = "ball_${daysSince}", iconResId = R.drawable.icon_ball),
                            DogItem(id = "bone_rand_${daysSince}", iconResId = R.drawable.icon_bone),
                            DogItem(id = "toy_rand_${daysSince}", iconResId = R.drawable.icon_plushtoy),
                        )
                        newItems.add(randomItems.random())
                    }

                    newItems.forEach { item ->
                        if (inventory.none { it.id == item.id }) {
                            inventory.add(item)
                        }
                    }
                }

                var mode by remember { mutableStateOf("None") }
                var inputText by remember { mutableStateOf("") }
                
                val displayText = if (daysSince == 0) {
                    "I had a little accident today 🐾"
                } else {
                    "It's been $daysSince day(s) since I peed inside!"
                }

                var isInventoryOpen by remember { mutableStateOf(false) }

                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                            .padding(contentPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(contentPadding, Alignment.CenterVertically)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween 
                        ) {
                            var expanded by remember { mutableStateOf(false) }

                            Box(modifier = Modifier.padding(8.dp)) {
                                TextButton(onClick = { expanded = true }) {
                                    Text("Set mode: $mode")
                                }
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Set last accident date") },
                                        onClick = {
                                            mode = "date"
                                            inputText = lastDate?.toString() ?: ""
                                            expanded = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Set days since accident") },
                                        onClick = {
                                            mode = "days"
                                            inputText = daysSince.toString()
                                            expanded = false
                                        }
                                    )
                                }
                            }
                            InventoryButton(onClick = { isInventoryOpen = true })
                        }

                        if (mode != "None") {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = inputText,
                                    onValueChange = { inputText = it },
                                    label = { Text(if (mode == "date") "YYYY-MM-DD" else "Days") },
                                    modifier = Modifier.weight(1f)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(onClick = {
                                    if (mode == "date") {
                                        try {
                                            val date = LocalDate.parse(inputText)
                                            val days = ChronoUnit.DAYS.between(date, LocalDate.now()).toInt()
                                            daysSince = days.coerceAtLeast(0)
                                            prefs.edit().putString("last_pisi_date", date.toString()).apply()
                                            lastDate = date 
                                        } catch (e: Exception) {
                                            Log.e("EveApp", "Failed to parse: $inputText") 
                                        }
                                    } else if (mode == "days") {
                                        val inputDays = inputText.toIntOrNull()
                                        if (inputDays != null) {
                                            val newDate: LocalDate = today.minusDays(inputDays.toLong())
                                            prefs.edit().putString("last_pisi_date", newDate.toString()).apply()
                                            lastDate = newDate
                                            daysSince = inputDays
                                        }
                                    }
                                }) {
                                    Text("OK")
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(contentPadding))

                        Image(
                            painter = painterResource(
                                id = if (daysSince == 0) R.drawable.dog_lying else R.drawable.dog_sitting
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(200.dp)
                        )

                        Text(
                            text = displayText,
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center
                        )

                        // TODO: Implement a confirmation dialog so the user doesn't reset by accident
                        Button(onClick = {
                            val newDate = today.toString()
                            prefs.edit().putString("last_pisi_date", newDate).apply()
                            lastDate = today
                            daysSince = 0
                        }) {
                            Text("Oops, I peed 😳")
                        }

                        Button(onClick = { daysSince += 1 }) {
                            Text("Increase days (Test)")
                        }

                        if (isInventoryOpen) {
                            InventoryScreen(inventory = inventory, onClose = { isInventoryOpen = false })
                        }
                    }
                }
            }
        }
    }
}
