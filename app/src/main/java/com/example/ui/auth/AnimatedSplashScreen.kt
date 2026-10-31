package com.example.ui.auth

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MahalCommunity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AnimatedSplashScreen(
    activeMahal: MahalCommunity?,
    onSplashComplete: () -> Unit
) {
    val scale = remember { Animatable(1.5f) }
    val alpha = remember { Animatable(0f) }
    val contentAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Zoom out transition from scale 1.5f down to 1.0f
        launch {
            alpha.animateTo(1f, animationSpec = tween(400, easing = FastOutSlowInEasing))
        }
        launch {
            contentAlpha.animateTo(1f, animationSpec = tween(600, delayMillis = 200))
        }
        scale.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(durationMillis = 1100, easing = FastOutSlowInEasing)
        )
        delay(400)
        onSplashComplete()
    }

    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF064E3B), // Deep Emerald
            Color(0xFF047857),
            Color(0xFF0F766E)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onSplashComplete() }
            .testTag("animated_splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(24.dp)
                .scale(scale.value)
                .alpha(alpha.value)
        ) {
            // Community Logo / Emblem with Glow Ring
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.15f),
                modifier = Modifier
                    .size(130.dp)
                    .padding(6.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CommunityLogoEmblem(
                        logoUri = null,
                        logoPreset = "MOSQUE",
                        communityName = "Mahal Ledger",
                        size = 110.dp,
                        containerColor = Color.White,
                        iconTint = Color(0xFF047857)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App Title - Always Mahal Ledger on launch
            Text(
                text = "Mahal Ledger",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Community Subscription & Accounts Management System",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFFD1FAE5),
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(contentAlpha.value)
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Subtle tag indicator
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.2f),
                modifier = Modifier.alpha(contentAlpha.value)
            ) {
                Text(
                    text = "Welcome",
                    color = Color(0xFFFEF08A),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }
    }
}
