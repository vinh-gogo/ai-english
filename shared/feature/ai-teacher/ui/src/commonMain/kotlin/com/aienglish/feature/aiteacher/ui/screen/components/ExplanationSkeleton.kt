package com.aienglish.feature.aiteacher.ui.screen.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.aienglish.core.designsystem.theme.AppColors

@Composable
fun ExplanationSkeleton(colors: AppColors) {
    val transition = rememberInfiniteTransition()
    val alpha by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Card(
        modifier = Modifier.alpha(alpha).fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surfaceElevated),
        border = BorderStroke(0.5.dp, colors.border)
    ) {
        Column(modifier = Modifier.padding(12.dp).fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(colors.accent.copy(alpha = 0.3f))
            )
            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(colors.border)
            )
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(colors.border)
            )
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(colors.border)
            )
        }
    }
}
