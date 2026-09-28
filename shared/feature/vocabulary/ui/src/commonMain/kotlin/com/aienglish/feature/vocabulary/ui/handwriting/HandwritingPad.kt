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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.vocabulary.ui.screen.carouselScrollAndDrag
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Supported handwriting language input modes.
 */
enum class HandwritingLanguage(val label: String, val icon: String) {
    ENGLISH("Tiếng Anh", "🔤"),
    VIETNAMESE("Tiếng Việt", "🇻🇳")
}

/**
 * Interactive Handwriting Input Pad for vocabulary input.
 *
 * Employs local multi-feature geometric recognition & offline Trie dictionary autocomplete.
 * Fully optimized for both small vertical (phone) screens and large desktop displays.
 * 0 Token cost • 100% Offline • Zero LLM latency.
 */
@Composable
fun HandwritingPad(
    currentWord: String,
    colors: AppColors,
    modifier: Modifier = Modifier,
    initialLanguage: HandwritingLanguage = HandwritingLanguage.ENGLISH,
    targetFieldLabel: String = "Từ vựng",
    onWordCommitted: (String) -> Unit
) {
    var buffer by remember { mutableStateOf(currentWord) }
    var languageMode by remember(initialLanguage) { mutableStateOf(initialLanguage) }
    var strokes by remember { mutableStateOf<List<List<Point2D>>>(emptyList()) }
    var currentStroke by remember { mutableStateOf<List<Point2D>>(emptyList()) }
    var candidateResults by remember { mutableStateOf<List<RecognitionResult>>(emptyList()) }
    var livePreviewResult by remember { mutableStateOf<RecognitionResult?>(null) }
    var lastAddedChar by remember { mutableStateOf<String?>(null) }
    var debounceJob by remember { mutableStateOf<Job?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val recognizer = remember { GeometricRecognizer() }
    val englishTrie = remember { EnglishDictionaryTrie.DEFAULT }
    val vietnameseTrie = remember { VietnameseDictionaryTrie.DEFAULT }

    // Query appropriate Trie for word completions based on current language mode
    val suggestions = remember(buffer, languageMode) {
        if (buffer.isNotBlank()) {
            if (languageMode == HandwritingLanguage.VIETNAMESE) {
                vietnameseTrie.searchPrefix(buffer, limit = 10)
            } else {
                englishTrie.searchPrefix(buffer, limit = 10)
            }
        } else {
            emptyList()
        }
    }

    // Process recognition and commit candidate to buffer
    fun performRecognition() {
        val allStrokes = if (currentStroke.isNotEmpty()) strokes + listOf(currentStroke) else strokes
        if (allStrokes.isEmpty()) return

        val results = recognizer.recognize(allStrokes, topN = 5)
        if (results.isNotEmpty()) {
            candidateResults = results
            val topMatch = results.first()

            when {
                topMatch.character == GeometricRecognizer.GESTURE_BACKSPACE -> {
                    if (buffer.isNotEmpty()) {
                        buffer = buffer.dropLast(1)
                    }
                    lastAddedChar = null
                }
                topMatch.character == GeometricRecognizer.GESTURE_SPACE -> {
                    buffer += " "
                    lastAddedChar = " "
                }
                VietnameseDiacritics.isDiacritic(topMatch.character) -> {
                    val composed = VietnameseDiacritics.composeDiacritic(buffer, topMatch.character)
                    if (composed != buffer) {
                        buffer = composed
                        lastAddedChar = topMatch.character
                    } else if (topMatch.character == "-" || topMatch.character == ".") {
                        buffer += topMatch.character
                        lastAddedChar = topMatch.character
                    }
                }
                else -> {
                    val charToAppend = topMatch.character
                    buffer += charToAppend
                    lastAddedChar = charToAppend
                }
            }
        }
        // Reset strokes and live preview
        strokes = emptyList()
        currentStroke = emptyList()
        livePreviewResult = null
    }

    // Schedule auto-recognition 1100ms after user finishes stroke
    fun scheduleAutoRecognition() {
        debounceJob?.cancel()
        debounceJob = coroutineScope.launch {
            delay(1100L)
            performRecognition()
        }
    }

    // Run quick live recognition to show real-time preview of what's being drawn
    fun updateLivePreview(newStrokes: List<List<Point2D>>) {
        if (newStrokes.isEmpty()) {
            livePreviewResult = null
            return
        }
        val live = recognizer.recognize(newStrokes, topN = 4)
        livePreviewResult = live.firstOrNull()
        if (live.isNotEmpty()) {
            candidateResults = live
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // --- 1. Buffer & Language Mode Card (Responsive 2-Row Layout) ---
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = colors.surfaceContainer,
            border = BorderStroke(1.dp, colors.border.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 7.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Line 1: Buffer text display + Target Field Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Văn bản:",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.mutedText
                        )
                        Text(
                            text = if (buffer.isEmpty()) "(vẽ chữ hoặc dấu...)" else buffer,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = if (buffer.isEmpty()) colors.mutedText.copy(alpha = 0.5f) else colors.mainText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Target indicator badge
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = colors.accent.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, colors.accent.copy(alpha = 0.35f))
                    ) {
                        Text(
                            text = targetFieldLabel,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.accent,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Line 2: Language switcher + Quick editing buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Language Switcher Toggle: EN / VI
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = colors.surface,
                        border = BorderStroke(1.dp, colors.border.copy(alpha = 0.45f))
                    ) {
                        Row(modifier = Modifier.padding(2.dp)) {
                            // English button
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (languageMode == HandwritingLanguage.ENGLISH) colors.accent else Color.Transparent,
                                modifier = Modifier.clickable {
                                    languageMode = HandwritingLanguage.ENGLISH
                                }
                            ) {
                                Text(
                                    text = "🔤 EN",
                                    fontSize = 10.5.sp,
                                    fontWeight = if (languageMode == HandwritingLanguage.ENGLISH) FontWeight.Bold else FontWeight.Medium,
                                    color = if (languageMode == HandwritingLanguage.ENGLISH) Color.White else colors.mutedText,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            // Vietnamese button
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (languageMode == HandwritingLanguage.VIETNAMESE) colors.accent else Color.Transparent,
                                modifier = Modifier.clickable {
                                    languageMode = HandwritingLanguage.VIETNAMESE
                                }
                            ) {
                                Text(
                                    text = "🇻🇳 VI",
                                    fontSize = 10.5.sp,
                                    fontWeight = if (languageMode == HandwritingLanguage.VIETNAMESE) FontWeight.Bold else FontWeight.Medium,
                                    color = if (languageMode == HandwritingLanguage.VIETNAMESE) Color.White else colors.mutedText,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }

                    // Quick editing buttons: Space, Backspace, Clear all
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Space button
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = colors.surface,
                            border = BorderStroke(1.dp, colors.border.copy(alpha = 0.4f)),
                            modifier = Modifier.clickable {
                                buffer += " "
                                lastAddedChar = " "
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.SpaceBar,
                                    contentDescription = "Khoảng trắng",
                                    tint = colors.mutedText,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "Cách",
                                    fontSize = 10.sp,
                                    color = colors.mutedText
                                )
                            }
                        }

                        // Backspace button
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = colors.surface,
                            border = BorderStroke(1.dp, colors.border.copy(alpha = 0.4f)),
                            modifier = Modifier.clickable {
                                if (buffer.isNotEmpty()) {
                                    buffer = buffer.dropLast(1)
                                    lastAddedChar = null
                                }
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.Backspace,
                                    contentDescription = "Xóa 1 ký tự",
                                    tint = colors.mutedText,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "Xóa",
                                    fontSize = 10.sp,
                                    color = colors.mutedText
                                )
                            }
                        }

                        // Clear all button
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = colors.surface,
                            border = BorderStroke(1.dp, colors.border.copy(alpha = 0.4f)),
                            modifier = Modifier.clickable {
                                buffer = ""
                                strokes = emptyList()
                                currentStroke = emptyList()
                                candidateResults = emptyList()
                                livePreviewResult = null
                                lastAddedChar = null
                                debounceJob?.cancel()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.DeleteOutline,
                                contentDescription = "Xóa hết",
                                tint = colors.mutedText,
                                modifier = Modifier.padding(4.dp).size(14.dp)
                            )
                        }
                    }
                }
            }
        }

        // --- 2. Interactive Handwriting Canvas (Optimized 140dp height for portrait screens) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
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
                                    val updatedStrokes = strokes + listOf(currentStroke)
                                    strokes = updatedStrokes
                                    currentStroke = emptyList()
                                    updateLivePreview(updatedStrokes)
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

                // Handwriting guidelines (Rule of thirds)
                // 1. Top ascender line (25%)
                drawLine(
                    color = Color.Gray.copy(alpha = 0.18f),
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
                    width = 4.2.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )

                // Smooth quadratic bezier curve rendering
                fun drawSmooth(strokePoints: List<Point2D>) {
                    if (strokePoints.size >= 3) {
                        val path = Path().apply {
                            moveTo(strokePoints[0].x, strokePoints[0].y)
                            for (i in 1 until strokePoints.size - 1) {
                                val p0 = strokePoints[i]
                                val p1 = strokePoints[i + 1]
                                val midX = (p0.x + p1.x) / 2f
                                val midY = (p0.y + p1.y) / 2f
                                quadraticTo(p0.x, p0.y, midX, midY)
                            }
                            lineTo(strokePoints.last().x, strokePoints.last().y)
                        }
                        drawPath(path, color = colors.accent, style = strokeStyle)
                    } else if (strokePoints.size == 2) {
                        drawLine(
                            color = colors.accent,
                            start = Offset(strokePoints[0].x, strokePoints[0].y),
                            end = Offset(strokePoints[1].x, strokePoints[1].y),
                            strokeWidth = 4.2.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    } else if (strokePoints.size == 1) {
                        drawCircle(
                            color = colors.accent,
                            radius = 3.5.dp.toPx(),
                            center = Offset(strokePoints[0].x, strokePoints[0].y)
                        )
                    }
                }

                // Render completed strokes
                for (stroke in strokes) {
                    drawSmooth(stroke)
                }

                // Render active stroke
                if (currentStroke.isNotEmpty()) {
                    drawSmooth(currentStroke)
                }
            }

            // Canvas Watermark / Hint when empty
            if (strokes.isEmpty() && currentStroke.isEmpty()) {
                Box(
                    modifier = Modifier.matchParentSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val hintText = if (languageMode == HandwritingLanguage.VIETNAMESE) {
                        "✍️ Viết chữ tiếng Việt hoặc nét dấu (/ \\ ? ~ . ^ ’ ˘ -)\nVuốt ngang dài ➔ phải: Dấu cách | ⬅ trái: Xóa lùi"
                    } else {
                        "✍️ Viết chữ cái (A-Z, a-z) hoặc số (0-9)\nVuốt ngang dài ➔ phải: Dấu cách | ⬅ trái: Xóa lùi"
                    }
                    Text(
                        text = hintText,
                        fontSize = 11.sp,
                        fontStyle = FontStyle.Italic,
                        color = colors.mutedText.copy(alpha = 0.55f),
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }

            // Live Preview Status Pill on Top-Left
            if (livePreviewResult != null) {
                val displayChar = VietnameseDiacritics.formatDisplaySymbol(livePreviewResult!!.character)
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = colors.accent.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, colors.accent.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = "Nhận diện:",
                            fontSize = 10.sp,
                            color = colors.mutedText
                        )
                        Text(
                            text = displayChar,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.accent
                        )
                        Text(
                            text = "${(livePreviewResult!!.score * 100).toInt()}%",
                            fontSize = 9.5.sp,
                            color = colors.accent.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // Quick Canvas Controls on bottom right: "Nhận diện ngay" & "Vẽ lại nét"
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (strokes.isNotEmpty() || currentStroke.isNotEmpty()) {
                    // Button "Vẽ lại nét"
                    Surface(
                        shape = CircleShape,
                        color = colors.surface.copy(alpha = 0.94f),
                        border = BorderStroke(1.dp, colors.border.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                debounceJob?.cancel()
                                strokes = emptyList()
                                currentStroke = emptyList()
                                livePreviewResult = null
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.Refresh,
                                contentDescription = "Vẽ lại nét",
                                tint = colors.mutedText,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }

                    // Button "Nhận diện ngay ⚡"
                    Surface(
                        shape = RoundedCornerShape(14.dp),
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
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "Nhận diện",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // --- 2.5 Quick Vietnamese Diacritics Helper Toolbar (Carousel draggable) ---
        if (languageMode == HandwritingLanguage.VIETNAMESE) {
            val quickTones = listOf(
                Pair("́", "sắc (/)"),
                Pair("̀", "huyền (\\)"),
                Pair("̉", "hỏi (?)"),
                Pair("̃", "ngã (~)"),
                Pair("̣", "nặng (.)"),
                Pair("^", "mũ (^)"),
                Pair("˘", "trăng (˘)"),
                Pair("’", "móc (’)"),
                Pair("-", "gạch đ (-)")
            )
            val toneScrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .carouselScrollAndDrag(toneScrollState)
                    .horizontalScroll(toneScrollState),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Dấu:",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.mutedText
                )
                quickTones.forEach { (mark, label) ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = colors.surfaceContainer,
                        border = BorderStroke(1.dp, colors.border.copy(alpha = 0.45f)),
                        modifier = Modifier.clickable {
                            val composed = VietnameseDiacritics.composeDiacritic(buffer, mark)
                            if (composed != buffer) {
                                buffer = composed
                                lastAddedChar = mark
                            }
                        }
                    ) {
                        Text(
                            text = label,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = colors.mainText,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.5.dp)
                        )
                    }
                }
            }
        }

        // --- 3. Recognized Candidate Alternatives Row (Carousel draggable) ---
        if (candidateResults.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "Gợi ý:",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.mutedText
                )

                val altScrollState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .carouselScrollAndDrag(altScrollState)
                        .horizontalScroll(altScrollState),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    candidateResults.forEachIndexed { index, candidate ->
                        val isSelected = index == 0
                        val displayCandidate = VietnameseDiacritics.formatDisplaySymbol(candidate.character)
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isSelected) colors.accent.copy(alpha = 0.18f) else colors.surfaceContainer,
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) colors.accent else colors.border.copy(alpha = 0.35f)
                            ),
                            modifier = Modifier.clickable {
                                if (VietnameseDiacritics.isDiacritic(candidate.character)) {
                                    val composed = VietnameseDiacritics.composeDiacritic(buffer, candidate.character)
                                    if (composed != buffer) {
                                        buffer = composed
                                        lastAddedChar = candidate.character
                                    }
                                } else {
                                    // Replace last character with this chosen alternative
                                    if (lastAddedChar != null && buffer.endsWith(lastAddedChar!!)) {
                                        buffer = buffer.dropLast(lastAddedChar!!.length) + candidate.character
                                    } else {
                                        buffer += candidate.character
                                    }
                                    lastAddedChar = candidate.character
                                }
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Text(
                                    text = displayCandidate,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) colors.accent else colors.mainText
                                )
                                Text(
                                    text = "${(candidate.score * 100).toInt()}%",
                                    fontSize = 9.sp,
                                    color = colors.mutedText
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 4. Autocomplete Suggestions (Offline Dictionary Trie, Carousel draggable) ---
        if (suggestions.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val suggestionTitle = if (languageMode == HandwritingLanguage.VIETNAMESE) {
                        "💡 Gợi ý từ tiếng Việt:"
                    } else {
                        "💡 Gợi ý từ tiếng Anh hoàn chỉnh:"
                    }
                    Text(
                        text = suggestionTitle,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.accent
                    )
                    Text(
                        text = "Chạm để điền",
                        fontSize = 9.5.sp,
                        color = colors.mutedText.copy(alpha = 0.7f)
                    )
                }

                val sugScrollState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .carouselScrollAndDrag(sugScrollState)
                        .horizontalScroll(sugScrollState),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    suggestions.forEach { word ->
                        Surface(
                            shape = RoundedCornerShape(7.dp),
                            color = colors.accent.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, colors.accent.copy(alpha = 0.4f)),
                            modifier = Modifier.clickable {
                                buffer = word
                                onWordCommitted(word)
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Text(
                                    text = word,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = colors.accent
                                )
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = "Chọn từ",
                                    tint = colors.accent,
                                    modifier = Modifier.size(12.dp)
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
                    modifier = Modifier.size(15.dp)
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    text = "Áp dụng vào $targetFieldLabel: \"${buffer.trim()}\"",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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
                text = "⚡ Đa đặc trưng (Zoning + Tangent + \$P) • Ghép dấu Tiếng Việt • 100% Offline • 0 AI tokens",
                fontSize = 9.5.sp,
                fontStyle = FontStyle.Italic,
                color = colors.mutedText.copy(alpha = 0.65f)
            )
        }
    }
}
