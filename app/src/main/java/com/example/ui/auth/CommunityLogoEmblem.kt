package com.example.ui.auth

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Villa
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ui.theme.CashInflowGreen

enum class LogoPreset(val id: String, val titleMalayalam: String, val icon: ImageVector) {
    MOSQUE("MOSQUE", "Mosque", Icons.Default.Mosque),
    MINARET("MINARET", "Minaret", Icons.Default.Villa),
    CRESCENT("CRESCENT", "Crescent & Star", Icons.Default.NightsStay),
    BOOK("BOOK", "Quran / Book", Icons.Default.AutoStories),
    EMBLEM("EMBLEM", "Official Shield", Icons.Default.Shield)
}

@Composable
fun CommunityLogoEmblem(
    logoUri: String?,
    logoPreset: String?,
    communityName: String,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    iconTint: Color = MaterialTheme.colorScheme.primary
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(containerColor)
            .border(2.dp, iconTint.copy(alpha = 0.25f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (!logoUri.isNullOrBlank()) {
            AsyncImage(
                model = Uri.parse(logoUri),
                contentDescription = "$communityName Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
            )
        } else {
            val preset = LogoPreset.values().find { it.id.equals(logoPreset, ignoreCase = true) }
                ?: LogoPreset.MOSQUE
            Icon(
                imageVector = preset.icon,
                contentDescription = "$communityName Logo",
                tint = iconTint,
                modifier = Modifier.size(size * 0.55f)
            )
        }
    }
}
