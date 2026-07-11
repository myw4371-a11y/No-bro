package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LunchDining
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MarketViewModel
import com.example.ui.toBangla

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EggTrackerScreen(
    viewModel: MarketViewModel,
    onNavigateBack: () -> Unit
) {
    val eggEntries by viewModel.allEggEntries.collectAsStateWithLifecycle()
    val eggAddEntries by viewModel.allEggAddEntries.collectAsStateWithLifecycle()

    val totalEggsUsed = remember(eggEntries) { eggEntries.sumOf { it.count } }
    val totalEggsAdded = remember(eggAddEntries) { eggAddEntries.sumOf { it.count } }
    val remainingEggs = remember(totalEggsAdded, totalEggsUsed) { totalEggsAdded - totalEggsUsed }

    var isAddingMode by remember { mutableStateOf(false) } // false = Use mode, true = Add mode
    var isHistoryAddMode by remember { mutableStateOf(false) } // false = Use logs, true = Add logs

    var eggCountText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ডিম হিসাব", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // --- Total Summary Card ---
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "মোট ডিম যোগ",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${totalEggsAdded.toBangla()} টি",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32) // Green
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "মোট ডিম ব্যবহার",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${totalEggsUsed.toBangla()} টি",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "অবশিষ্ট ডিম",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${remainingEggs.toBangla()} টি",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // --- Egg Entry Form Card ---
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Selection chip/tabs
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = !isAddingMode,
                            onClick = { 
                                isAddingMode = false
                                eggCountText = ""
                            },
                            label = { Text("ব্যবহার এন্ট্রি", fontSize = 12.sp) }
                        )
                        FilterChip(
                            selected = isAddingMode,
                            onClick = { 
                                isAddingMode = true
                                eggCountText = ""
                            },
                            label = { Text("মোট ডিম যোগ করুন", fontSize = 12.sp) }
                        )
                    }

                    Text(
                        text = if (isAddingMode) "মোট ডিম যোগ করার এন্ট্রি" else "ডিম ব্যবহারের এন্ট্রি যোগ করুন",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = eggCountText,
                            onValueChange = { text ->
                                val filtered = text.filter { it.isDigit() }
                                eggCountText = filtered
                            },
                            label = { Text("ডিমের সংখ্যা") },
                            placeholder = { Text("যেমন: ৪") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_egg_count"),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        Button(
                            onClick = {
                                keyboardController?.hide()
                                val count = eggCountText.toIntOrNull()
                                if (count != null && count > 0) {
                                    if (isAddingMode) {
                                        viewModel.addEggAddEntry(count)
                                    } else {
                                        viewModel.addEggEntry(count)
                                    }
                                    eggCountText = ""
                                }
                            },
                            enabled = eggCountText.isNotBlank(),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .height(56.dp)
                                .testTag("btn_save_egg_entry")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("যোগ")
                        }
                    }

                    val hasEntries = if (isAddingMode) eggAddEntries.isNotEmpty() else eggEntries.isNotEmpty()
                    if (hasEntries) {
                        Spacer(modifier = Modifier.height(12.dp))
                        TextButton(
                            onClick = {
                                if (isAddingMode) {
                                    viewModel.undoEggAddEntry()
                                } else {
                                    viewModel.undoEggEntry()
                                }
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Icon(Icons.Default.Undo, contentDescription = "Undo")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("সর্বশেষ এন্ট্রি মুছুন (Undo)")
                        }
                    }
                }
            }

            // --- Entries List Section with Selector ---
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isHistoryAddMode) "যোগ করার ইতিহাস" else "ব্যবহারের ইতিহাস",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(
                        onClick = { isHistoryAddMode = false },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = if (!isHistoryAddMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    ) {
                        Text("ব্যবহার", fontWeight = if (!isHistoryAddMode) FontWeight.Bold else FontWeight.Normal)
                    }
                    TextButton(
                        onClick = { isHistoryAddMode = true },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = if (isHistoryAddMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    ) {
                        Text("যোগ", fontWeight = if (isHistoryAddMode) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }

            val currentList = if (isHistoryAddMode) eggAddEntries else eggEntries

            if (currentList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = "Empty",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isHistoryAddMode) "কোনো যোগ করার তথ্য পাওয়া যায়নি।" else "কোনো ব্যবহারের তথ্য পাওয়া যায়নি।",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(currentList.reversed()) { entry ->
                        val countVal = if (entry is com.example.data.EggAddEntry) entry.count else (entry as com.example.data.EggEntry).count
                        val dateVal = if (entry is com.example.data.EggAddEntry) entry.date else (entry as com.example.data.EggEntry).date
                        val isAddEntry = entry is com.example.data.EggAddEntry

                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isAddEntry) Color(0xFFE8F5E9).copy(alpha = 0.6f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.LunchDining,
                                        contentDescription = "Eggs Icon",
                                        tint = if (isAddEntry) Color(0xFF2E7D32) else MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = dateVal,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Text(
                                    text = "${if (isAddEntry) "+" else ""}${countVal.toBangla()} টি ডিম",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isAddEntry) Color(0xFF2E7D32) else MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
