package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.Person
import com.example.ui.MarketViewModel
import com.example.ui.toBangla

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealTrackerScreen(
    viewModel: MarketViewModel,
    onNavigateBack: () -> Unit
) {
    val people by viewModel.people.collectAsStateWithLifecycle()
    val allMealEntries by viewModel.allMealEntries.collectAsStateWithLifecycle()

    var selectedPersonForMeals by remember { mutableStateOf<Person?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("মিল হিসাব", fontWeight = FontWeight.Bold) },
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
            if (people.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.RestaurantMenu,
                            contentDescription = "Empty People",
                            modifier = Modifier.size(72.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "কোনো সদস্য যুক্ত করা হয়নি",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "টাকার হিসাব স্ক্রিনে গিয়ে প্রথমে সদস্য যোগ করুন। তারপর সদস্যের পাশে থাকা পেন (Pen) চিহ্নে চাপ দিয়ে মিল হিসাব লিখুন।",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                Text(
                    text = "সদস্যদের মিল হিসাব",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(people, key = { it.id }) { person ->
                        val personMeals = remember(allMealEntries) {
                            allMealEntries.filter { it.personId == person.id }
                        }
                        val totalMeals = remember(personMeals) {
                            personMeals.sumOf { it.count }
                        }

                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = person.name,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "মোট মিল: ${totalMeals.toBangla(1)}টি",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                // Pen/Edit Button to open meals entry panel
                                IconButton(
                                    onClick = { selectedPersonForMeals = person },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                                    ),
                                    modifier = Modifier.testTag("btn_edit_person_meals_${person.id}")
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Edit Meals Entry",
                                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // --- Dialog to manage Person's Meals ---
    val person = selectedPersonForMeals
    if (person != null) {
        var mealsCountText by remember { mutableStateOf("") }
        val personEntries = remember(allMealEntries, person) {
            allMealEntries.filter { it.personId == person.id }.reversed() // sorted oldest to newest
        }
        val totalMeals = remember(personEntries) {
            personEntries.sumOf { it.count }
        }
        val keyboardController = LocalSoftwareKeyboardController.current

        AlertDialog(
            onDismissRequest = { selectedPersonForMeals = null },
            title = { Text("${person.name} - মিল হিসাব", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    // Quick stats
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "মোট মিল খেয়েছে",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = "${totalMeals.toBangla(1)} টি",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }

                    // Entry list
                    Text(
                        text = "মিলের ইতিহাস:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    if (personEntries.isEmpty()) {
                        Text(
                            text = "কোনো মিলের তথ্য পাওয়া যায়নি।",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 140.dp)
                        ) {
                            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                                items(personEntries) { entry ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = entry.date,
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = "${entry.count.toBangla(1)} মিল",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Input Box
                    OutlinedTextField(
                        value = mealsCountText,
                        onValueChange = { text ->
                            // Clean input to only digits and decimal points (prevent negative sign)
                            val cleaned = text.filter { it.isDigit() || it == '.' }
                            if (cleaned.count { it == '.' } <= 1) {
                                mealsCountText = cleaned
                            }
                        },
                        label = { Text("মিলের সংখ্যা") },
                        placeholder = { Text("যেমন: ৩ বা ১.৫") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_meals_count"),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Undo Button
                    TextButton(
                        onClick = {
                            viewModel.undoMealEntry(person.id)
                        },
                        enabled = personEntries.isNotEmpty(),
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.Default.Undo, contentDescription = "Undo")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Undo")
                    }

                    // Save Button
                    Button(
                        onClick = {
                            keyboardController?.hide()
                            val count = mealsCountText.toDoubleOrNull()
                            if (count != null && count > 0) {
                                viewModel.addMealEntry(person.id, count)
                                mealsCountText = ""
                            }
                        },
                        enabled = mealsCountText.isNotBlank()
                    ) {
                        Text("যোগ করুন")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedPersonForMeals = null }) {
                    Text("বন্ধ করুন")
                }
            }
        )
    }
}
