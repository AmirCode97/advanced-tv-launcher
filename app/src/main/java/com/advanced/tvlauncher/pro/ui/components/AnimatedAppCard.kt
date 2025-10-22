package com.advanced.tvlauncher.pro.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.advanced.tvlauncher.pro.data.model.AppInfo

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AnimatedAppCard(
    app: AppInfo,
    isFavorite: Boolean,
    onAppClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    
    AnimatedContent(
        targetState = isFocused,
        transitionSpec = {
            scaleIn() + fadeIn() with scaleOut() + fadeOut()
        }, label = "app_card_animation"
    ) { focused ->
        Box(
            modifier = modifier
                .size(180.dp, 200.dp)
                .shadow(
                    elevation = if (focused) 16.dp else 4.dp,
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (focused) listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        ) else listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // App Icon
                AppIcon(
                    icon = app.icon,
                    size = 80.dp,
                    modifier = Modifier.size(80.dp)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // App Name
                Text(
                    text = app.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2
                )
                
                // Favorite Button
                FavoriteButton(
                    isFavorite = isFavorite,
                    onToggle = onFavoriteToggle,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun AppIcon(
    icon: Drawable?,
    size: Dp,
    modifier: Modifier = Modifier
) {
    // Implementation for displaying app icon
    // Using Coil or other image loading library
}

@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Implementation for favorite toggle button
}
