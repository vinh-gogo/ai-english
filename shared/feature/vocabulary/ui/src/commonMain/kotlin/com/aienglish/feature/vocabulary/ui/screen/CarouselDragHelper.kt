package com.aienglish.feature.vocabulary.ui.screen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput

/**
 * Modifier hỗ trợ cả KÉO chuột/chạm tay và CUỘN con lăn chuột (cho cả desktop và mobile).
 */
fun Modifier.carouselScrollAndDrag(scrollState: ScrollState): Modifier {
    return this
        .pointerInput(scrollState) {
            detectHorizontalDragGestures { change, dragAmount ->
                change.consume()
                scrollState.dispatchRawDelta(-dragAmount)
            }
        }
        .pointerInput(scrollState) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    if (event.type == PointerEventType.Scroll) {
                        val change = event.changes.firstOrNull()
                        if (change != null) {
                            val delta = change.scrollDelta
                            val scrollAmount = if (delta.x != 0f) delta.x else delta.y
                            if (scrollAmount != 0f) {
                                change.consume()
                                scrollState.dispatchRawDelta(scrollAmount * 35f)
                            }
                        }
                    }
                }
            }
        }
}
