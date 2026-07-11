package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToDashboard: () -> Unit) {
    val alphaAnim = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        alphaAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(1200)
        )
        delay(1500) // Keep visible
        onNavigateToDashboard()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F2027),
                        Color(0xFF203A43),
                        Color(0xFF2C5364)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(24.dp)
                .alpha(alphaAnim.value)
        ) {
            Text(
                text = "বাজার হিসাব",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 48.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Developed by Sadhin",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF00FFCC),
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .alpha(alphaAnim.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Professional  •  User Friendly",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f),
                fontWeight = FontWeight.Light,
                letterSpacing = 1.sp
            )
        }
    }
}
