package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.BazarItem
import com.example.data.BazarSession
import com.example.ui.MarketViewModel
import com.example.ui.toBangla
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BazarSelectScreen(
    viewModel: MarketViewModel,
    onNavigateToActiveSession: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val sessions by viewModel.sessions.collectAsStateWithLifecycle()
    val activeSessions = remember(sessions) { sessions.filter { !it.isPermanent } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("বাজার", fontWeight = FontWeight.Bold) },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "বাজারের ধরন নির্বাচন করুন",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // --- Big Bazar Button ---
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .testTag("btn_big_bazar")
                    .clickable {
                        viewModel.createBazarSession("বড় বাজার") { sessionId ->
                            viewModel.setCurrentSessionId(sessionId)
                            onNavigateToActiveSession(sessionId)
                        }
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Storefront,
                            contentDescription = "Big Bazar",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "বড় বাজার",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "ডিফল্ট ১৬টি আইটেম নিয়ে শুরু করুন",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Small Bazar Button ---
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .testTag("btn_small_bazar")
                    .clickable {
                        viewModel.createBazarSession("ছোট বাজার") { sessionId ->
                            viewModel.setCurrentSessionId(sessionId)
                            onNavigateToActiveSession(sessionId)
                        }
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.ShoppingBag,
                            contentDescription = "Small Bazar",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "ছোট বাজার",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "কোনো ডিফল্ট পণ্য থাকবে না, নিজে আইটেম যোগ করুন",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // --- Resume/Active Sessions Section ---
            if (activeSessions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(32.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "চলমান বাজার হিসাবসমূহ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    textAlign = TextAlign.Start
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(activeSessions) { session ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setCurrentSessionId(session.id)
                                    onNavigateToActiveSession(session.id)
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = session.type,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        text = session.date,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "৳ ${session.totalAmount.toBangla()}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        Icons.Default.PlayArrow,
                                        contentDescription = "Resume",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BazarActiveScreen(
    viewModel: MarketViewModel,
    sessionId: Int,
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(sessionId) {
        viewModel.setCurrentSessionId(sessionId)
    }

    val currentSession by viewModel.currentSession.collectAsStateWithLifecycle()
    val items by viewModel.currentSessionItems.collectAsStateWithLifecycle()

    var showAddDialog by remember { mutableStateOf(false) }
    var showLockDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = currentSession?.type ?: "বাজার হিসাব",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = currentSession?.date ?: "",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    val session = currentSession
                    if (session != null && !session.isPermanent) {
                        IconButton(
                            onClick = { showDeleteDialog = true },
                            modifier = Modifier.testTag("btn_delete_session")
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete Session",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                        IconButton(
                            onClick = { showLockDialog = true },
                            modifier = Modifier.testTag("btn_lock_session")
                        ) {
                            Icon(Icons.Default.LockOpen, contentDescription = "Permanent Save")
                        }
                    } else if (session != null && session.isPermanent) {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = Color(0xFF43A047),
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            val session = currentSession
            if (session != null && !session.isPermanent) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    modifier = Modifier.testTag("fab_add_bazar_item")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Item")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val session = currentSession
            if (session != null) {
                // Header summarizing the total of current items
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (session.isPermanent) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.secondaryContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (session.isPermanent) "স্থায়ীভাবে সংরক্ষিত বাজার" else "চলমান বাজার মোট হিসাব",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (session.isPermanent) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = "৳ ${session.totalAmount.toBangla()}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (session.isPermanent) Color(0xFF2E7D32) else MaterialTheme.colorScheme.primary
                            )
                        }
                        if (session.isPermanent) {
                            Text(
                                text = "লক করা হয়েছে",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }
                }

                if (items.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Inventory,
                                contentDescription = "Empty",
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "কোনো পণ্য যুক্ত করা হয়নি",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (!session.isPermanent) {
                                Text(
                                    text = "নিচের (+) বাটনে চাপ দিয়ে নতুন পণ্য যোগ করুন।",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(items, key = { it.id }) { item ->
                            BazarItemRow(
                                item = item,
                                isReadOnly = session.isPermanent,
                                onSave = { qty, price, tot ->
                                    viewModel.saveBazarItem(item, qty, price, tot)
                                },
                                onUndo = {
                                    viewModel.undoBazarItemSave(item)
                                },
                                showSnackbar = { msg ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(msg)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // --- Dialog to add custom item ---
    if (showAddDialog) {
        var newItemName by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("নতুন পণ্যের নাম লিখুন") },
            text = {
                OutlinedTextField(
                    value = newItemName,
                    onValueChange = { newItemName = it },
                    placeholder = { Text("পণ্য যেমন: আলু, চিনি ইত্যাদি") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_new_item_name"),
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newItemName.isNotBlank()) {
                            viewModel.addNewItemToSession(sessionId, newItemName.trim())
                            showAddDialog = false
                        }
                    },
                    modifier = Modifier.testTag("btn_save_new_item")
                ) {
                    Text("যোগ করুন")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("বাতিল")
                }
            }
        )
    }

    // --- Dialog to lock session permanently ---
    if (showLockDialog) {
        AlertDialog(
            onDismissRequest = { showLockDialog = false },
            title = { Text("Permanent Save করবেন?") },
            text = { Text("Permanent Save চাপলে ওই List Lock হয়ে যাবে। এরপর আর কোনো Edit করা যাবে না।") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.lockSessionPermanently(sessionId)
                        showLockDialog = false
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047)),
                    modifier = Modifier.testTag("btn_confirm_permanent_save")
                ) {
                    Text("Permanent Save", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLockDialog = false }) {
                    Text("না")
                }
            }
        )
    }

    // --- Dialog to delete session ---
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("মুছে ফেলতে চান?") },
            text = { Text("আপনি কি এই বাজারটি মুছে ফেলতে চান?") },
            confirmButton = {
                Button(
                    onClick = {
                        val session = currentSession
                        if (session != null && !session.isPermanent) {
                            viewModel.deleteSession(session)
                            showDeleteDialog = false
                            onNavigateBack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.testTag("btn_confirm_delete")
                ) {
                    Text("হ্যাঁ", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("না")
                }
            }
        )
    }
}

@Composable
fun BazarItemRow(
    item: BazarItem,
    isReadOnly: Boolean,
    onSave: (Double?, Double?, Double) -> Unit,
    onUndo: () -> Unit,
    showSnackbar: (String) -> Unit
) {
    var quantityText by remember(item.quantity) { mutableStateOf(item.quantity?.toString() ?: "") }
    var priceText by remember(item.unitPrice) { mutableStateOf(item.unitPrice?.toString() ?: "") }
    var totalText by remember(item.totalPrice) { mutableStateOf(if (item.totalPrice > 0) item.totalPrice.toString() else "") }
    var showUndoDialog by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    // Trigger calculation when quantity or price changes
    LaunchedEffect(quantityText, priceText) {
        val qty = quantityText.toDoubleOrNull()
        val prc = priceText.toDoubleOrNull()
        if (qty != null && prc != null) {
            val calcTotal = qty * prc
            totalText = calcTotal.toString()
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isSaved) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Top Row: Item Name and Status Indicator / Undo Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (!isReadOnly) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (item.isSaved) {
                            Text(
                                text = "সেভ করা",
                                color = Color(0xFF2E7D32),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Saved",
                                tint = Color(0xFF43A047),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                        IconButton(
                            onClick = { showUndoDialog = true },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                Icons.Default.Undo,
                                contentDescription = "Undo",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                } else if (item.isSaved) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Saved",
                        tint = Color(0xFF43A047),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isReadOnly) {
                // Read-only presentation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val qtyBangla = item.quantity?.toBangla() ?: "০"
                    val prcBangla = item.unitPrice?.toBangla() ?: "০"
                    val totBangla = item.totalPrice.toBangla()

                    Text(
                        text = "$qtyBangla কেজি × $prcBangla টাকা = $totBangla টাকা",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "৳ $totBangla",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                // Editable presentation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 1. কেজি Input Box
                    OutlinedTextField(
                        value = quantityText,
                        onValueChange = { text ->
                            // Clean input to only digits and decimal points (prevent negative sign)
                            val cleaned = text.filter { it.isDigit() || it == '.' }
                            if (cleaned.count { it == '.' } <= 1) {
                                quantityText = cleaned
                            }
                        },
                        placeholder = { Text("কেজি", fontSize = 12.sp) },
                        modifier = Modifier
                            .weight(1.5f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        textStyle = LocalTextStyle.current.copy(fontSize = 13.sp)
                    )

                    Text("×", fontSize = 16.sp, fontWeight = FontWeight.Bold)

                    // 2. টাকা Input Box
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { text ->
                            val cleaned = text.filter { it.isDigit() || it == '.' }
                            if (cleaned.count { it == '.' } <= 1) {
                                priceText = cleaned
                            }
                        },
                        placeholder = { Text("টাকা", fontSize = 12.sp) },
                        modifier = Modifier
                            .weight(1.8f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        textStyle = LocalTextStyle.current.copy(fontSize = 13.sp)
                    )

                    Text("=", fontSize = 16.sp, fontWeight = FontWeight.Bold)

                    // 3. মোট Input Box
                    OutlinedTextField(
                        value = totalText,
                        onValueChange = { text ->
                            val cleaned = text.filter { it.isDigit() || it == '.' }
                            if (cleaned.count { it == '.' } <= 1) {
                                totalText = cleaned
                            }
                        },
                        placeholder = { Text("মোট", fontSize = 12.sp) },
                        modifier = Modifier
                            .weight(2f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        textStyle = LocalTextStyle.current.copy(fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    )

                    // 4. ✔ Check Action Button
                    IconButton(
                        onClick = {
                            keyboardController?.hide()
                            val qty = quantityText.toDoubleOrNull()
                            val prc = priceText.toDoubleOrNull()
                            val tot = totalText.toDoubleOrNull()

                            if (qty == null && prc == null && tot == null) {
                                showSnackbar("টাকার পরিমাণ লিখুন")
                            } else {
                                // Save entry
                                val resolvedTotal = tot ?: ((qty ?: 0.0) * (prc ?: 0.0))
                                onSave(qty, prc, resolvedTotal)
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Save",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }

    // --- Undo Confirmation Dialog ---
    if (showUndoDialog) {
        AlertDialog(
            onDismissRequest = { showUndoDialog = false },
            title = { Text("আপনি কি Undo করতে চান?") },
            text = { Text("এটি চাপলে এই পণ্যের সর্বশেষ সেভ করা ডাটা মুছে যাবে।") },
            confirmButton = {
                Button(
                    onClick = {
                        onUndo()
                        showUndoDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("ঠিক আছে", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showUndoDialog = false }) {
                    Text("না")
                }
            }
        )
    }
}
