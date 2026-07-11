package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen(
    onNavigateToBazar: () -> Unit,
    onNavigateToSavedBazar: () -> Unit,
    onNavigateToMoney: () -> Unit,
    onNavigateToMeal: () -> Unit,
    onNavigateToEgg: () -> Unit,
    onNavigateToManager: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFD))
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // --- 1. Top Header ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 8.dp)
        ) {
            Text(
                text = "বাজার হিসাব",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B),
                letterSpacing = (-0.5).sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF22C55E))
                )
                Text(
                    text = "অফলাইন মোড সক্রিয়",
                    fontSize = 14.sp,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // --- 2. Bento Grid ---
        val dashboardItems = listOf(
            BentoItemData(
                title = "বাজার",
                subtitle = "নতুন তালিকা",
                icon = Icons.Default.ShoppingCart,
                backgroundColor = Color(0xFFD3E4FF),
                iconBgColor = Color(0xFF1D4ED8),
                titleColor = Color(0xFF1E3A8A),
                subtitleColor = Color(0xFF1D4ED8).copy(alpha = 0.7f),
                testTag = "menu_bazar",
                onClick = onNavigateToBazar
            ),
            BentoItemData(
                title = "সেভ করা বাজার",
                subtitle = "ইতিহাস দেখুন",
                icon = Icons.Default.Bookmark,
                backgroundColor = Color(0xFFDAF5E1),
                iconBgColor = Color(0xFF15803D),
                titleColor = Color(0xFF064E3B),
                subtitleColor = Color(0xFF15803D).copy(alpha = 0.7f),
                testTag = "menu_saved_bazar",
                onClick = onNavigateToSavedBazar
            ),
            BentoItemData(
                title = "টাকার হিসাব",
                subtitle = "জমা ও খরচ",
                icon = Icons.Default.Payments,
                backgroundColor = Color(0xFFF3E2FF),
                iconBgColor = Color(0xFF7E22CE),
                titleColor = Color(0xFF581C87),
                subtitleColor = Color(0xFF7E22CE).copy(alpha = 0.7f),
                testTag = "menu_money",
                onClick = onNavigateToMoney
            ),
            BentoItemData(
                title = "মিল হিসাব",
                subtitle = "দৈনিক মিল",
                icon = Icons.Default.Restaurant,
                backgroundColor = Color(0xFFFFE8D1),
                iconBgColor = Color(0xFFC2410C),
                titleColor = Color(0xFF7C2D12),
                subtitleColor = Color(0xFFC2410C).copy(alpha = 0.7f),
                testTag = "menu_meal",
                onClick = onNavigateToMeal
            ),
            BentoItemData(
                title = "ডিম হিসাব",
                subtitle = "ব্যবহারের তালিকা",
                icon = Icons.Default.LunchDining,
                backgroundColor = Color(0xFFFFF4C4),
                iconBgColor = Color(0xFFCA8A04),
                titleColor = Color(0xFF713F12),
                subtitleColor = Color(0xFFCA8A04).copy(alpha = 0.7f),
                testTag = "menu_egg",
                onClick = onNavigateToEgg
            ),
            BentoItemData(
                title = "ম্যানেজার হিসাব",
                subtitle = "সম্পূর্ণ রিপোর্ট",
                icon = Icons.Default.Assessment,
                backgroundColor = Color(0xFFFFDADA),
                iconBgColor = Color(0xFFBE123C),
                titleColor = Color(0xFF881337),
                subtitleColor = Color(0xFFBE123C).copy(alpha = 0.7f),
                testTag = "menu_manager",
                onClick = onNavigateToManager
            )
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) { BentoCard(dashboardItems[0]) }
                Box(modifier = Modifier.weight(1f)) { BentoCard(dashboardItems[1]) }
            }
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) { BentoCard(dashboardItems[2]) }
                Box(modifier = Modifier.weight(1f)) { BentoCard(dashboardItems[3]) }
            }
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) { BentoCard(dashboardItems[4]) }
                Box(modifier = Modifier.weight(1f)) { BentoCard(dashboardItems[5]) }
            }
        }

        // --- 3. Footer ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp, top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .width(96.dp)
                    .padding(bottom = 12.dp),
                color = Color(0xFFE2E8F0),
                thickness = 1.dp
            )
            Text(
                text = "DEVELOPED BY SADHIN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF94A3B8),
                letterSpacing = 2.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Professional • User Friendly",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFCBD5E1)
            )
        }
    }
}

data class BentoItemData(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val backgroundColor: Color,
    val iconBgColor: Color,
    val titleColor: Color,
    val subtitleColor: Color,
    val testTag: String,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BentoCard(item: BentoItemData) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = item.backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        onClick = item.onClick,
        modifier = Modifier
            .fillMaxSize()
            .testTag(item.testTag)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon container
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(color = item.iconBgColor, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Texts
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = item.titleColor,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.subtitle,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = item.subtitleColor
                )
            }
        }
    }
}
