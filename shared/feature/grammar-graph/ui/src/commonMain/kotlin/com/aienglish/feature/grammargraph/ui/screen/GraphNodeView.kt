package com.aienglish.feature.grammargraph.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.grammargraph.domain.model.GrammarNode

@Composable
internal fun GraphNodeView(
    node: GrammarNode,
    offset: Offset,
    isSelected: Boolean,
    isFocused: Boolean,
    colors: AppColors,
    density: Float,
    scale: Float,
    onSelect: () -> Unit,
    onDragStart: () -> Unit,
    onDragEnd: () -> Unit,
    onDrag: (Offset) -> Unit,
    modifier: Modifier = Modifier
) {
    val alpha = if (isFocused) 1f else 0.3f
    val charCount = node.pattern.length
    val estWidthDp = ((charCount * 5.8f) + (if (node.isSaved) 28f else 20f)).coerceIn(50f, 240f)
    val estHeightDp = 34f

    Box(
        modifier = modifier
            .offset(offset.x.dp, offset.y.dp)
            .width(estWidthDp.dp)
            .height(estHeightDp.dp)
            .shadow(
                elevation = if (isSelected) 8.dp else 2.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = if (isSelected) colors.accent else Color.Black,
                spotColor = if (isSelected) colors.accent else Color.Black
            )
            .pointerInput(node.id) {
                detectTapGestures { onSelect() }
            }
            .pointerInput(node.id) {
                detectDragGestures(
                    onDragStart = { onDragStart() },
                    onDragEnd = { onDragEnd() },
                    onDragCancel = { onDragEnd() },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val dragDpX = (dragAmount.x / density) / scale
                        val dragDpY = (dragAmount.y / density) / scale
                        onDrag(Offset(dragDpX, dragDpY))
                    }
                )
            }
            .border(
                width = if (isSelected) 2.dp else 1.25.dp,
                color = if (isSelected) colors.accent else colors.border.copy(alpha = alpha),
                shape = RoundedCornerShape(18.dp)
            )
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        if (isSelected) colors.accent.copy(alpha = 0.25f) else colors.surface,
                        colors.surface
                    )
                )
            )
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (node.isSaved) {
                Icon(
                    imageVector = Icons.Rounded.AutoAwesome,
                    contentDescription = "Saved structure",
                    tint = colors.accent,
                    modifier = Modifier.size(10.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = node.pattern,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = colors.mainText.copy(alpha = alpha),
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
    }
}
