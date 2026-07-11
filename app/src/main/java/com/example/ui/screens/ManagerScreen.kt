package com.example.ui.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MarketViewModel
import com.example.ui.toBangla

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerScreen(
    viewModel: MarketViewModel,
    onNavigateBack: () -> Unit
) {
    val summary by viewModel.managerSummaryState.collectAsStateWithLifecycle()
    val personSummaries by viewModel.personSummariesState.collectAsStateWithLifecycle()

    val tableScrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ম্যানেজার হিসাব", fontWeight = FontWeight.Bold) },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // --- TOP HEADER ICON & TITLE ---
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Assessment,
                            contentDescription = "Summary",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "মেস সামগ্রিক তথ্য",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "সকল সদস্যদের অটো-ক্যালকুলেটেড হিসাব বিবরণী",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // --- SUMMARY GRID CARDS ---
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Row 1: মোট উঠেছে & মোট বাজার খরচ
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SummaryMiniCard(
                            label = "মোট টাকা উঠেছে",
                            value = "৳ ${summary.totalMoneyRaised.toBangla()}",
                            color = Color(0xFFE8F5E9),
                            textColor = Color(0xFF2E7D32),
                            modifier = Modifier.weight(1f)
                        )
                        SummaryMiniCard(
                            label = "মোট বাজার খরচ",
                            value = "৳ ${summary.totalBazarExpense.toBangla()}",
                            color = Color(0xFFFFEBEE),
                            textColor = Color(0xFFC62828),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Row 2: মোট মিল & বর্তমান ক্যাশ
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SummaryMiniCard(
                            label = "মোট মিল",
                            value = "${summary.totalMeals.toBangla(1)} টি",
                            color = Color(0xFFF3E5F5),
                            textColor = Color(0xFF6A1B9A),
                            modifier = Modifier.weight(1f)
                        )
                        SummaryMiniCard(
                            label = "বর্তমান ক্যাশ",
                            value = "৳ ${summary.currentCash.toBangla()}",
                            color = if (summary.currentCash >= 0) Color(0xFFE3F2FD) else Color(0xFFFFF3E0),
                            textColor = if (summary.currentCash >= 0) Color(0xFF1565C0) else Color(0xFFD84315),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Row 3: মিল রেট & অবশিষ্ট ডিম
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SummaryMiniCard(
                            label = "মিল রেট",
                            value = "৳ ${summary.mealRate.toBangla(2)}",
                            color = Color(0xFFE0F7FA),
                            textColor = Color(0xFF00838F),
                            modifier = Modifier.weight(1f)
                        )
                        SummaryMiniCard(
                            label = "অবশিষ্ট ডিম",
                            value = "${summary.remainingEggs.toBangla(0)} টি",
                            color = Color(0xFFFFFDE7),
                            textColor = Color(0xFFF57F17),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Row 4: ডিম কেনা & ডিম ব্যবহার
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SummaryMiniCard(
                            label = "মোট ডিম কেনা",
                            value = "${summary.totalEggsBought.toBangla(0)} টি",
                            color = Color(0xFFECEFF1),
                            textColor = Color(0xFF37474F),
                            modifier = Modifier.weight(1f)
                        )
                        SummaryMiniCard(
                            label = "মোট ডিম ব্যবহার",
                            value = "${summary.totalEggsUsed.toBangla()} টি",
                            color = Color(0xFFEFEBE9),
                            textColor = Color(0xFF4E342E),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // --- PERSON WISE DETAILED TABLE ---
            item {
                Text(
                    text = "সদস্যভিত্তিক হিসাব বিবরণী",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            if (personSummaries.isEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = "Info",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "কোনো হিসাব প্রদর্শনের জন্য সদস্য যুক্ত করুন।",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        personSummaries.forEach { summary ->
                            val isPositive = summary.balance >= 0.0
                            val balanceColor = if (isPositive) Color(0xFF2E7D32) else Color(0xFFC62828)
                            val balanceBgColor = if (isPositive) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)

                            Card(
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth()
                                ) {
                                    // Member Name Header
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "নাম: ${summary.person.name}",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        
                                        // Balance Badge
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    color = balanceBgColor,
                                                    shape = RoundedCornerShape(10.dp)
                                                )
                                                .padding(vertical = 6.dp, horizontal = 12.dp)
                                        ) {
                                            Text(
                                                text = "ব্যালেন্স: ৳ ${summary.balance.toBangla(1)}",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = balanceColor
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))
                                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Details Grid (2x2 style)
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                text = "টাকা দিয়েছেন",
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                text = "৳ ${summary.moneyGiven.toBangla()}",
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }

                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                text = "মোট মিল",
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                text = "${summary.mealsCount.toBangla(1)} টি",
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                text = "মিল খরচ",
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                text = "৳ ${summary.mealExpense.toBangla(1)}",
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }

                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                text = "হিসাব অবস্থা",
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                text = if (isPositive) "উদ্বৃত্ত আছে" else "বকেয়া আছে",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = balanceColor
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
    }
}

@Composable
fun SummaryMiniCard(
    label: String,
    value: String,
    color: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = modifier.height(85.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                color = textColor.copy(alpha = 0.8f),
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    isHeader: Boolean = false,
    isBold: Boolean = false
) {
    Text(
        text = text,
        fontSize = if (isHeader) 12.sp else 13.sp,
        fontWeight = if (isHeader || isBold) FontWeight.Bold else FontWeight.Normal,
        color = if (isHeader) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .weight(weight)
            .padding(horizontal = 4.dp),
        textAlign = TextAlign.Start,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}
