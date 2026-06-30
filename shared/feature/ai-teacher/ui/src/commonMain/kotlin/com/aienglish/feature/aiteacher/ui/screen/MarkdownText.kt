package com.aienglish.feature.aiteacher.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors

sealed interface Block {
    data class Heading(val level: Int, val text: String) : Block
    data class Paragraph(val text: String) : Block
    data class Bullet(val text: String) : Block
    data class Ordered(val index: String, val text: String) : Block
    data object Divider : Block
}

fun parseBlocks(text: String): List<Block> {
    val groups = text.split(Regex("\\n{2,}"))
    val blocks = mutableListOf<Block>()

    for (group in groups) {
        val trimmed = group.trim()
        if (trimmed.isBlank()) continue

        // 1. Divider
        if (trimmed.all { it == '-' } && trimmed.length >= 3) {
            blocks.add(Block.Divider)
            continue
        }

        val lines = trimmed.lines().map { it.trimEnd() }

        // If it's a single line and starts with #, it's a heading
        if (lines.size == 1 && trimmed.startsWith("#")) {
            val level = trimmed.takeWhile { it == '#' }.length
            val rest = trimmed.substring(level).trim()
            blocks.add(Block.Heading(level, rest))
            continue
        }

        // Check if all lines in this group are bullet points or ordered list items
        var isAllList = true
        val parsedLines = lines.map { line ->
            val cleanLine = line.trimStart()
            if (cleanLine.startsWith("#")) {
                isAllList = false
                null
            } else {
                val bulletMatch = Regex("^[*\\-+ ]\\s+(.*)").find(cleanLine)
                val orderedMatch = Regex("^([0-9a-zA-Z]+[.)])\\s+(.*)").find(cleanLine)
                
                if (bulletMatch != null) {
                    Block.Bullet(bulletMatch.groupValues[1].trimStart())
                } else if (orderedMatch != null) {
                    Block.Ordered(orderedMatch.groupValues[1], orderedMatch.groupValues[2].trimStart())
                } else {
                    isAllList = false
                    null
                }
            }
        }

        if (isAllList) {
            parsedLines.forEach { block ->
                if (block != null) {
                    blocks.add(block)
                }
            }
        } else {
            // Mixed lines
            var currentParagraph = StringBuilder()
            for (line in lines) {
                val cleanLine = line.trimStart()
                if (cleanLine.startsWith("#")) {
                    if (currentParagraph.isNotEmpty()) {
                        blocks.add(Block.Paragraph(currentParagraph.toString().trim()))
                        currentParagraph = StringBuilder()
                    }
                    val level = cleanLine.takeWhile { it == '#' }.length
                    val rest = cleanLine.substring(level).trim()
                    blocks.add(Block.Heading(level, rest))
                } else {
                    val bulletMatch = Regex("^[*\\-+ ]\\s+(.*)").find(cleanLine)
                    val orderedMatch = Regex("^([0-9a-zA-Z]+[.)])\\s+(.*)").find(cleanLine)
                    
                    if (bulletMatch != null || orderedMatch != null) {
                        if (currentParagraph.isNotEmpty()) {
                            blocks.add(Block.Paragraph(currentParagraph.toString().trim()))
                            currentParagraph = StringBuilder()
                        }
                        if (bulletMatch != null) {
                            blocks.add(Block.Bullet(bulletMatch.groupValues[1].trimStart()))
                        } else {
                            blocks.add(Block.Ordered(orderedMatch!!.groupValues[1], orderedMatch.groupValues[2].trimStart()))
                        }
                    } else {
                        if (currentParagraph.isNotEmpty()) {
                            currentParagraph.append("\n")
                        }
                        currentParagraph.append(line)
                    }
                }
            }
            if (currentParagraph.isNotEmpty()) {
                blocks.add(Block.Paragraph(currentParagraph.toString().trim()))
            }
        }
    }

    return blocks
}

fun buildInlineAnnotatedString(
    text: String,
    colors: AppColors
): AnnotatedString {
    return buildAnnotatedString {
        appendFormattedText(text, colors)
    }
}

private fun AnnotatedString.Builder.appendFormattedText(
    text: String,
    colors: AppColors,
    outerBold: Boolean = false,
    outerItalic: Boolean = false
) {
    var i = 0
    while (i < text.length) {
        when {
            text.startsWith("***", i) -> {
                val close = text.indexOf("***", i + 3)
                if (close != -1) {
                    appendFormattedText(
                        text = text.substring(i + 3, close),
                        colors = colors,
                        outerBold = true,
                        outerItalic = true
                    )
                    i = close + 3
                } else {
                    appendCharacter(text[i], outerBold, outerItalic, colors)
                    i++
                }
            }
            text.startsWith("___", i) -> {
                val close = text.indexOf("___", i + 3)
                if (close != -1) {
                    appendFormattedText(
                        text = text.substring(i + 3, close),
                        colors = colors,
                        outerBold = true,
                        outerItalic = true
                    )
                    i = close + 3
                } else {
                    appendCharacter(text[i], outerBold, outerItalic, colors)
                    i++
                }
            }
            text.startsWith("**", i) -> {
                val close = text.indexOf("**", i + 2)
                if (close != -1) {
                    appendFormattedText(
                        text = text.substring(i + 2, close),
                        colors = colors,
                        outerBold = true,
                        outerItalic = outerItalic
                    )
                    i = close + 2
                } else {
                    appendCharacter(text[i], outerBold, outerItalic, colors)
                    i++
                }
            }
            text.startsWith("__", i) -> {
                val close = text.indexOf("__", i + 2)
                if (close != -1) {
                    appendFormattedText(
                        text = text.substring(i + 2, close),
                        colors = colors,
                        outerBold = true,
                        outerItalic = outerItalic
                    )
                    i = close + 2
                } else {
                    appendCharacter(text[i], outerBold, outerItalic, colors)
                    i++
                }
            }
            text.startsWith("*", i) -> {
                val close = text.indexOf('*', i + 1)
                if (close != -1) {
                    appendFormattedText(
                        text = text.substring(i + 1, close),
                        colors = colors,
                        outerBold = outerBold,
                        outerItalic = true
                    )
                    i = close + 1
                } else {
                    appendCharacter(text[i], outerBold, outerItalic, colors)
                    i++
                }
            }
            text.startsWith("_", i) -> {
                val close = text.indexOf('_', i + 1)
                if (close != -1) {
                    appendFormattedText(
                        text = text.substring(i + 1, close),
                        colors = colors,
                        outerBold = outerBold,
                        outerItalic = true
                    )
                    i = close + 1
                } else {
                    appendCharacter(text[i], outerBold, outerItalic, colors)
                    i++
                }
            }
            text.startsWith("`", i) -> {
                val close = text.indexOf("`", i + 1)
                if (close != -1) {
                    withStyle(SpanStyle(
                        color = colors.accent,
                        fontWeight = if (outerBold) FontWeight.Bold else FontWeight.Medium,
                        fontStyle = if (outerItalic) FontStyle.Italic else FontStyle.Normal
                    )) {
                        append(text.substring(i + 1, close))
                    }
                    i = close + 1
                } else {
                    appendCharacter(text[i], outerBold, outerItalic, colors)
                    i++
                }
            }
            else -> {
                appendCharacter(text[i], outerBold, outerItalic, colors)
                i++
            }
        }
    }
}

private fun AnnotatedString.Builder.appendCharacter(
    char: Char,
    bold: Boolean,
    italic: Boolean,
    colors: AppColors
) {
    val style = SpanStyle(
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
        color = if (italic) colors.mutedText else colors.mainText
    )
    withStyle(style) {
        append(char)
    }
}

@Composable
fun MarkdownText(
    markdown: String,
    colors: AppColors
) {
    val processedMarkdown = remember(markdown) {
        markdown
            .replace("""(?<!\*)\*["“]([^"“”\n]{1,50})["”]\*(?!\*)""".toRegex(), "***\"$1\"***")
            .replace("""(?<!_)_["“]([^"“”\n]{1,50})["”]_(?!_)""".toRegex(), "***\"$1\"***")
            .replace("""(?<=^|\W)(?<![\*_])["“]([^"“”\n]{1,50})["”](?=$|\W)(?![\*_])""".toRegex(), "**\"$1\"**")
            .replace("""(?<![\*_])\(([^)\n]{1,150})\)(?![\*_])""".toRegex(), "*($1)*")
    }
    val blocks = remember(processedMarkdown) { parseBlocks(processedMarkdown) }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        for (block in blocks) {
            when (block) {
                is Block.Divider -> {
                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = colors.border,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                is Block.Heading -> {
                    val annotated = remember<AnnotatedString>(block.text) { buildInlineAnnotatedString(block.text, colors) }
                    val hSize = when (block.level) {
                        1 -> 18.sp
                        2 -> 16.sp
                        else -> 15.sp
                    }
                    Text(
                        text = annotated,
                        fontSize = hSize,
                        color = colors.mainText,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 6.dp, bottom = 2.dp)
                    )
                }
                is Block.Paragraph -> {
                    val annotated = remember<AnnotatedString>(block.text) { buildInlineAnnotatedString(block.text, colors) }
                    Text(
                        text = annotated,
                        fontSize = 15.sp,
                        color = colors.mutedText,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                is Block.Bullet -> {
                    val annotated = remember(block.text, colors) {
                        buildAnnotatedString {
                            withStyle(ParagraphStyle(textIndent = TextIndent(firstLine = 0.sp, restLine = 16.sp))) {
                                withStyle(SpanStyle(color = colors.accent, fontWeight = FontWeight.Bold)) {
                                    append("•\u00A0\u00A0\u00A0")
                                }
                                append(buildInlineAnnotatedString(block.text, colors))
                            }
                        }
                    }
                    Text(
                        text = annotated,
                        fontSize = 15.sp,
                        color = colors.mutedText,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                is Block.Ordered -> {
                    val annotated = remember(block.index, block.text, colors) {
                        buildAnnotatedString {
                            withStyle(ParagraphStyle(textIndent = TextIndent(firstLine = 0.sp, restLine = 24.sp))) {
                                withStyle(SpanStyle(color = colors.accent, fontWeight = FontWeight.Bold)) {
                                    append("${block.index}\u00A0\u00A0")
                                }
                                append(buildInlineAnnotatedString(block.text, colors))
                            }
                        }
                    }
                    Text(
                        text = annotated,
                        fontSize = 15.sp,
                        color = colors.mutedText,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
