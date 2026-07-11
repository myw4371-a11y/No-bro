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
fun MoneyTrackerScreen(
    viewModel: MarketViewModel,
    onNavigateBack: () -> Unit
) {
    val people by viewModel.people.collectAsStateWithLifecycle()
    val allMoneyEntries by viewModel.allMoneyEntries.collectAsStateWithLifecycle()

    var showAddPersonDialog by remember { mutableStateOf(false) }
    var selectedPersonForDetails by remember { mutableStateOf<Person?>(null) }
    var showDeleteConfirmForPerson by remember { mutableStateOf<Person?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("টাকার হিসাব", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddPersonDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.testTag("fab_add_person")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Person")
            }
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
                            Icons.Default.People,
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
                            text = "নিচের (+) বাটনে চাপ দিয়ে সদস্য যোগ করুন। সদস্য যোগ করার পর পেন (Pen) চিহ্নে চাপ দিয়ে টাকা যোগ করতে পারবেন।",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                Text(
                    text = "সদস্য তালিকা",
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
                        val personEntries = remember(allMoneyEntries) {
                            allMoneyEntries.filter { it.personId == person.id }
                        }
                        val totalMoney = remember(personEntries) {
                            personEntries.sumOf { it.amount }
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
                                        text = "মোট দিয়েছে: ৳ ${totalMoney.toBangla()}",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Pen/Edit Button to open deposit panel
                                    IconButton(
                                        onClick = { selectedPersonForDetails = person },
                                        colors = IconButtonDefaults.iconButtonColors(
                                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                                        ),
                                        modifier = Modifier.testTag("btn_edit_person_money_${person.id}")
                                    ) {
                                        Icon(
                                            Icons.Default.Edit,
                                            contentDescription = "Edit Money Entry",
                                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    IconButton(
                                        onClick = { showDeleteConfirmForPerson = person },
                                        colors = IconButtonDefaults.iconButtonColors(
                                            containerColor = MaterialTheme.colorScheme.errorContainer
                                        )
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Delete Person",
                                            tint = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // --- Dialog to Add Person ---
    if (showAddPersonDialog) {
        var personName by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddPersonDialog = false },
            title = { Text("নতুন সদস্যের নাম লিখুন") },
            text = {
                OutlinedTextField(
                    value = personName,
                    onValueChange = { personName = it },
                    placeholder = { Text("নাম যেমন: স্বাধীন, হাসান ইত্যাদি") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_person_name"),
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (personName.isNotBlank()) {
                            viewModel.addPerson(personName.trim())
                            showAddPersonDialog = false
                        }
                    },
                    modifier = Modifier.testTag("btn_save_person")
                ) {
                    Text("সংরক্ষণ করুন")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddPersonDialog = false }) {
                    Text("বাতিল")
                }
            }
        )
    }

    // --- Dialog to confirm Person Delete ---
    if (showDeleteConfirmForPerson != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmForPerson = null },
            title = { Text("সদস্য ডিলিট নিশ্চিতকরণ") },
            text = { Text("আপনি কি নিশ্চিতভাবে ${showDeleteConfirmForPerson?.name}-কে ডিলিট করতে চান? সদস্য ডিলিট করলে তার মেস সংক্রান্ত সকল জমা ও মিলের হিসাব চিরতরে মুছে যাবে।") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmForPerson?.let { viewModel.deletePerson(it) }
                        showDeleteConfirmForPerson = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("ডিলিট করুন", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmForPerson = null }) {
                    Text("বাতিল")
                }
            }
        )
    }

    // --- Bottom Sheet / Dialog to manage Person's Money ---
    val person = selectedPersonForDetails
    if (person != null) {
        var depositAmountText by remember { mutableStateOf("") }
        val personEntries = remember(allMoneyEntries, person) {
            allMoneyEntries.filter { it.personId == person.id }.reversed() // sorted oldest to newest
        }
        val totalMoney = remember(personEntries) {
            personEntries.sumOf { it.amount }
        }
        val keyboardController = LocalSoftwareKeyboardController.current

        AlertDialog(
            onDismissRequest = { selectedPersonForDetails = null },
            title = { Text("${person.name} - টাকার হিসাব", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    // Quick stats
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "মোট দিয়েছে",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "৳ ${totalMoney.toBangla()}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // Entry list
                    Text(
                        text = "জমার ইতিহাস:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    if (personEntries.isEmpty()) {
                        Text(
                            text = "কোনো জমা তথ্য পাওয়া যায়নি।",
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
                                            text = "৳ ${entry.amount.toBangla()}",
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
                        value = depositAmountText,
                        onValueChange = { text ->
                            // Clean input to only digits and decimal points (prevent negative sign)
                            val cleaned = text.filter { it.isDigit() || it == '.' }
                            if (cleaned.count { it == '.' } <= 1) {
                                depositAmountText = cleaned
                            }
                        },
                        label = { Text("টাকার পরিমাণ") },
                        placeholder = { Text("যেমন: ৫০০") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_money_amount"),
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
                            viewModel.undoMoneyEntry(person.id)
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
                            val amount = depositAmountText.toDoubleOrNull()
                            if (amount != null && amount > 0) {
                                viewModel.addMoneyEntry(person.id, amount)
                                depositAmountText = ""
                            }
                        },
                        enabled = depositAmountText.isNotBlank()
                    ) {
                        Text("যোগ করুন")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedPersonForDetails = null }) {
                    Text("বন্ধ করুন")
                }
            }
        )
    }
}
