package com.aienglish.feature.aiteacher.ui.screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.aienglish.core.designsystem.theme.AppColors
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.window.PopupProperties
import androidx.compose.animation.core.FastOutSlowInEasing

@Composable
fun SaveActionTooltip(
    offset: IntOffset,
    colors: AppColors,
    onSave: () -> Unit,
    onDismissRequest: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector = Icons.Default.Book,
    text: String = "Lưu"
) {
    var isSaving by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isSaving) 0f else 1f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        finishedListener = { if (isSaving) onSave() }
    )
    val flyOffsetX by animateDpAsState(
        targetValue = if (isSaving) 200.dp else 0.dp,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
    )
    val flyOffsetY by animateDpAsState(
        targetValue = if (isSaving) 400.dp else 0.dp,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
    )

    Popup(
        alignment = Alignment.TopStart,
        offset = offset,
        onDismissRequest = { if (!isSaving) onDismissRequest() },
        properties = PopupProperties(clippingEnabled = false)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = colors.accent,
            shadowElevation = 4.dp,
            modifier = Modifier
                .padding(top = 4.dp)
                .offset(x = flyOffsetX, y = flyOffsetY)
                .graphicsLayer { 
                    scaleX = scale
                    scaleY = scale
                    alpha = scale
                }
        ) {
            Row(
                modifier = Modifier
                    .clickable(enabled = !isSaving) { isSaving = true }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
