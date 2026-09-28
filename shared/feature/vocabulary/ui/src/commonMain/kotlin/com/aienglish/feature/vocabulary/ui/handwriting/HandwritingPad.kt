package com.aienglish.feature.vocabulary.ui.handwriting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Backspace
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.ElectricBolt
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.SpaceBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Interactive Handwriting Input Pad for vocabulary input.
 *
 * Employs local $P Point-Cloud geometric recognition & offline Trie dictionary autocomplete.
 * 0 Token cost • 100% Offline • Zero LLM latency.
 */
@Composable
fun HandwritingPad(
    currentWord: String,
    colors: AppColors,
    modifier: Modifier = Modifier,
    onWordCommitted: (String) -> Unit
) {
    var buffer by remember { mutableStateOf(currentWord) }
    var strokes by remember { mutableStateOf<List<List<Point2D>>>(emptyList()) }
    var currentStroke by remember { mutableStateOf<List<Point2D>>(emptyList()) }
    var candidateResults by remember { mutableStateOf<List<RecognitionResult>>(emptyList()) }
    var lastAddedChar by remember { mutableStateOf<String?>(null) }
    var debounceJob by remember { mutableStateOf<Job?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val recognizer = remember { GeometricRecognizer() }
    val trie = remember { EnglishDictionaryTrie.DEFAULT }

    // Query Trie for word completions based on current buffer prefix
    val suggestions = remember(buffer) {
        if (buffer.isNotBlank()) {
            trie.searchPrefix(buffer, limit = 10)
        } else {
            emptyList()
        }
    }

    // Process recognition on strokes
    fun performRecognition() {
        if (strokes.isEmpty() && currentStroke.isEmpty()) return
        val allStrokes = if (currentStroke.isNotEmpty()) strokes + listOf(currentStroke) else strokes
        val results = recognizer.recognize(allStrokes)
        if (results.isNotEmpty()) {
            candidateResults = results
            val topMatch = results.first()

            when (topMatch.character) {
                GeometricRecognizer.GESTURE_BACKSPACE -> {
                    if (buffer.isNotEmpty()) {
                        buffer = buffer.dropLast(1)
                    }
                    lastAddedChar = null
                }
                GeometricRecognizer.GESTURE_SPACE -> {
                    buffer += " "
                    lastAddedChar = " "
                }
                else -> {
                    // Append top candidate
                    val charToAppend = topMatch.character
                    buffer += charToAppend
                    lastAddedChar = charToAppend
                }
            }
        }
        // Clear canvas strokes after character is committed
        strokes = emptyList()
        currentStroke = emptyList()
    }

    // Schedule auto-recognition 650ms after user lifts pen/mouse
    fun scheduleAutoRecognition() {
        debounceJob?.cancel()
        debounceJob = coroutineScope.launch {
            delay(650L)
            performRecognition()
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // --- 1. Buffer & Control Row ---
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = colors.surfaceContainer,
            border = BorderStroke(1.dp, colors.border.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Word buffer display
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Văn bản:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.mutedText
                    )
                    Text(
                        text = if (buffer.isEmpty()) "(vẽ chữ cái bên dưới...)" else buffer,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = if (buffer.isEmpty()) colors.mutedText.copy(alpha = 0.6f) else colors.mainText
                    )
                }

                // Quick manipulation buttons: Backspace, Space, Clear all
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Space button
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(colors.surface)
                            .clickable {
                                buffer += " "
                                lastAddedChar = " "
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.SpaceBar,
                            contentDescription = "Khoảng trắng",
                            tint = colors.mutedText,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    // Backspace button
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(colors.surface)
                            .clickable {
                                if (buffer.isNotEmpty()) {
                                    buffer = buffer.dropLast(1)
                                    lastAddedChar = null
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.Backspace,
                            contentDescription = "Xóa 1 ký tự",
                            tint = colors.mutedText,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    // Clear all button
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(colors.surface)
                            .clickable {
                                buffer = ""
                                strokes = emptyList()
                                currentStroke = emptyList()
                                candidateResults = emptyList()
                                lastAddedChar = null
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.DeleteOutline,
                            contentDescription = "Xóa hết",
                            tint = colors.mutedText,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }

        // --- 2. Interactive Handwriting Canvas ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colors.surfaceContainer.copy(alpha = 0.85f))
                .border(1.dp, colors.border.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
        ) {
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                debounceJob?.cancel()
                                currentStroke = listOf(
                                    Point2D(offset.x, offset.y, strokes.size, System.currentTimeMillis())
                                )
                            },
                            onDrag = { change, _ ->
                                change.consume()
                                currentStroke = currentStroke + Point2D(
                                    change.position.x,
                                    change.position.y,
                                    strokes.size,
                                    System.currentTimeMillis()
                                )
                            },
                            onDragEnd = {
                                if (currentStroke.isNotEmpty()) {
                                    strokes = strokes + listOf(currentStroke)
                                    currentStroke = emptyList()
                                    scheduleAutoRecognition()
                                }
                            },
                            onDragCancel = {
                                currentStroke = emptyList()
                            }
                        )
                    }
            ) {
                val w = size.width
                val h = size.height

                // Draw ruled handwriting guide lines
                // 1. Top ascender line (25%)
                drawLine(
                    color = Color.Gray.copy(alpha = 0.2f),
                    start = Offset(0f, h * 0.25f),
                    end = Offset(w, h * 0.25f),
                    strokeWidth = 1f
                )

                // 2. Mean midline (dashed, 50%)
                drawLine(
                    color = Color.Gray.copy(alpha = 0.25f),
                    start = Offset(0f, h * 0.50f),
                    end = Offset(w, h * 0.50f),
                    strokeWidth = 1.2f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )

                // 3. Baseline (solid, 75%)
                drawLine(
                    color = Color.Gray.copy(alpha = 0.35f),
                    start = Offset(0f, h * 0.75f),
                    end = Offset(w, h * 0.75f),
                    strokeWidth = 1.5f
                )

                val strokeStyle = Stroke(
                    width = 4.5.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )

                // Draw completed strokes
                for (stroke in strokes) {
                    if (stroke.size >= 2) {
                        val path = Path().apply {
                            moveTo(stroke.first().x, stroke.first().y)
                            for (i in 1 until stroke.size) {
                                lineTo(stroke[i].x, stroke[i].y)
                            }
                        }
                        drawPath(path, color = colors.accent, style = strokeStyle)
                    } else if (stroke.size == 1) {
                        drawCircle(
                            color = colors.accent,
                            radius = 3.dp.toPx(),
                            center = Offset(stroke.first().x, stroke.first().y)
                        )
                    }
                }

                // Draw currently active stroke
                if (currentStroke.size >= 2) {
                    val path = Path().apply {
                        moveTo(currentStroke.first().x, currentStroke.first().y)
                        for (i in 1 until currentStroke.size) {
                            lineTo(currentStroke[i].x, currentStroke[i].y)
                        }
                    }
                    drawPath(path, color = colors.accent, style = strokeStyle)
                } else if (currentStroke.size == 1) {
                    drawCircle(
                        color = colors.accent,
                        radius = 3.dp.toPx(),
                        center = Offset(currentStroke.first().x, currentStroke.first().y)
                    )
                }
            }

            // Canvas Watermark / Hint when empty
            if (strokes.isEmpty() && currentStroke.isEmpty()) {
                Box(
                    modifier = Modifier.matchParentSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✍️ Vẽ từng ký tự (A-Z, a-z) hoặc số (0-9)\nVuốt ngang ➔ sang phải: Dấu cách | ⬅ sang trái: Xóa",
                        fontSize = 11.5.sp,
                        fontStyle = FontStyle.Italic,
                        color = colors.mutedText.copy(alpha = 0.55f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 17.sp
                    )
                }
            }

            // Quick Canvas Controls on bottom right: "Nhận diện ngay" & "Xóa nét"
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (strokes.isNotEmpty() || currentStroke.isNotEmpty()) {
                    // Button "Xóa nét vừa vẽ"
                    Surface(
                        shape = CircleShape,
                        color = colors.surface.copy(alpha = 0.9f),
                        border = BorderStroke(1.dp, colors.border.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                debounceJob?.cancel()
                                strokes = emptyList()
                                currentStroke = emptyList()
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.Refresh,
                                contentDescription = "Vẽ lại nét",
                                tint = colors.mutedText,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Button "Nhận diện ngay ⚡"
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = colors.accent,
                        modifier = Modifier.clickable {
                            debounceJob?.cancel()
                            performRecognition()
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ElectricBolt,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Nhận diện",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // --- 3. Recognized Candidate Alternatives Row ---
        if (candidateResults.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Ký tự nhận diện:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.mutedText
                )

                val altScrollState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(altScrollState),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    candidateResults.forEachIndexed { index, candidate ->
                        val isSelected = index == 0
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isSelected) colors.accent.copy(alpha = 0.18f) else colors.surfaceContainer,
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) colors.accent else colors.border.copy(alpha = 0.4f)
                            ),
                            modifier = Modifier.clickable {
                                // Replace last character with this alternative
                                if (lastAddedChar != null && buffer.endsWith(lastAddedChar!!)) {
                                    buffer = buffer.dropLast(lastAddedChar!!.length) + candidate.character
                                } else {
                                    buffer += candidate.character
                                }
                                lastAddedChar = candidate.character
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Text(
                                    text = candidate.character,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) colors.accent else colors.mainText
                                )
                                Text(
                                    text = "${(candidate.score * 100).toInt()}%",
                                    fontSize = 9.5.sp,
                                    color = colors.mutedText
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 4. Autocomplete Suggestions (Offline Dictionary Trie) ---
        if (suggestions.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "💡 Gợi ý từ tiếng Anh hoàn chỉnh:",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.accent
                    )
                    Text(
                        text = "Tự động hoàn thành",
                        fontSize = 10.sp,
                        color = colors.mutedText.copy(alpha = 0.7f)
                    )
                }

                val sugScrollState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(sugScrollState),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    suggestions.forEach { word ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = colors.accent.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, colors.accent.copy(alpha = 0.45f)),
                            modifier = Modifier.clickable {
                                buffer = word
                                onWordCommitted(word)
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = word,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = colors.accent
                                )
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = "Chọn từ",
                                    tint = colors.accent,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 5. Commit Word to Input Field Button ---
        if (buffer.isNotBlank()) {
            Button(
                onClick = { onWordCommitted(buffer.trim()) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = colors.accent),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Áp dụng từ: \"${buffer.trim()}\"",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // --- 6. 100% Offline Notice ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "⚡ Thuật toán hình học \$P Point-Cloud • 100% Offline • 0 AI tokens",
                fontSize = 10.sp,
                fontStyle = FontStyle.Italic,
                color = colors.mutedText.copy(alpha = 0.7f)
            )
        }
    }
}
