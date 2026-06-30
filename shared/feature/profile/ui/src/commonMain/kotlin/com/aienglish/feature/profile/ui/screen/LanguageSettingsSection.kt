package com.aienglish.feature.profile.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.profile.ui.viewmodel.ProfileState
import com.aienglish.feature.profile.ui.viewmodel.ProfileViewModel

@Composable
internal fun LanguageSettingsSection(
    state: ProfileState,
    vm: ProfileViewModel,
    colors: AppColors
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.Translate, null, tint = colors.accent, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Cài đặt ngôn ngữ", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = colors.mainText)
        }

        // Ngôn ngữ mẹ đẻ (Native Language)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(colors.surface)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Ngôn ngữ mẹ đẻ", fontSize = 13.sp, color = colors.mainText, fontWeight = FontWeight.Medium)

            var expanded by remember { mutableStateOf(false) }
            val currentLabel = when (state.languageNative) {
                "vi" -> "Tiếng Việt"
                "en" -> "Tiếng Anh"
                else -> "Tiếng Anh"
            }

            Box {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(colors.border.copy(0.15f))
                        .border(1.dp, colors.border.copy(0.2f), RoundedCornerShape(8.dp))
                        .clickable { expanded = true }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(currentLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.mainText)
                        Icon(Icons.Default.ArrowDropDown, null, tint = colors.mutedText, modifier = Modifier.size(16.dp))
                    }
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(colors.surface)
                ) {
                    DropdownMenuItem(
                        text = { Text("Tiếng Việt", fontSize = 13.sp, color = colors.mainText) },
                        onClick = { vm.onLanguageNativeChanged("vi"); expanded = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Tiếng Anh", fontSize = 13.sp, color = colors.mainText) },
                        onClick = { vm.onLanguageNativeChanged("en"); expanded = false }
                    )
                }
            }
        }

        // Ngôn ngữ học (Learning Language)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(colors.surface)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Ngôn ngữ học", fontSize = 13.sp, color = colors.mainText, fontWeight = FontWeight.Medium)

            var expanded by remember { mutableStateOf(false) }
            val currentLabel = when (state.languageTarget) {
                "en" -> "Tiếng Anh"
                "ja" -> "Tiếng Nhật"
                "zh" -> "Tiếng Trung"
                "es" -> "Tiếng Tây Ban Nha"
                "hi" -> "Tiếng Hindi"
                "fr" -> "Tiếng Pháp"
                "de" -> "Tiếng Đức"
                "ko" -> "Tiếng Hàn"
                "vi" -> "Tiếng Việt"
                else -> "Tiếng Anh"
            }

            Box {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(colors.border.copy(0.15f))
                        .border(1.dp, colors.border.copy(0.2f), RoundedCornerShape(8.dp))
                        .clickable { expanded = true }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(currentLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.mainText)
                        Icon(Icons.Default.ArrowDropDown, null, tint = colors.mutedText, modifier = Modifier.size(16.dp))
                    }
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(colors.surface)
                ) {
                    listOf(
                        "en" to "Tiếng Anh",
                        "es" to "Tiếng Tây Ban Nha",
                        "ja" to "Tiếng Nhật",
                        "zh" to "Tiếng Trung",
                        "hi" to "Tiếng Hindi",
                        "fr" to "Tiếng Pháp",
                        "de" to "Tiếng Đức",
                        "ko" to "Tiếng Hàn",
                        "vi" to "Tiếng Việt"
                    ).forEach { (code, label) ->
                        DropdownMenuItem(
                            text = { Text(label, fontSize = 13.sp, color = colors.mainText) },
                            onClick = { vm.onLanguageTargetChanged(code); expanded = false }
                        )
                    }
                }
            }
        }

        // AI Trả Lời (AI Response Mode)
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(colors.surface)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("AI trả lời", fontSize = 13.sp, color = colors.mainText, fontWeight = FontWeight.Medium)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(colors.border.copy(alpha = 0.15f))
                        .padding(2.dp)
                ) {
                    val isShort = state.aiResponseLength == "short"
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isShort) colors.accent else Color.Transparent)
                            .clickable { vm.onAiResponseLengthChanged("short") }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Ngắn", fontSize = 12.sp, fontWeight = FontWeight.Bold,
                            color = if (isShort) Color.White else colors.mutedText)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (!isShort) colors.accent else Color.Transparent)
                            .clickable { vm.onAiResponseLengthChanged("normal") }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Bình thường", fontSize = 12.sp, fontWeight = FontWeight.Bold,
                            color = if (!isShort) Color.White else colors.mutedText)
                    }
                }
            }
            if (state.aiResponseLength == "short") {
                Text(
                    "Chú thích: AI sẽ trả lời giải thích ngắn gọn nhưng bao hàm đủ các chữ trong câu.",
                    fontSize = 11.sp, color = colors.mutedText, lineHeight = 16.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                )
            }
        }

        // Tốc độ phát âm (TTS Speech Speed)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(colors.surface)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Tốc độ phát âm", fontSize = 13.sp, color = colors.mainText, fontWeight = FontWeight.Medium)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(colors.border.copy(alpha = 0.15f))
                    .padding(2.dp)
            ) {
                val currentSpeed = state.ttsSpeed
                listOf(
                    "slow" to "Chậm",
                    "normal" to "Thường",
                    "fast" to "Nhanh"
                ).forEach { (code, label) ->
                    val isSelected = currentSpeed == code
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) colors.accent else Color.Transparent)
                            .clickable { vm.onTtsSpeedChanged(code) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else colors.mutedText
                        )
                    }
                }
            }
        }
    }
}
