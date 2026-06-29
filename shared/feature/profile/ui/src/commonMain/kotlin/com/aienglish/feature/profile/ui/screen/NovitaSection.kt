package com.aienglish.feature.profile.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalUriHandler
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.profile.ui.viewmodel.ProfileState
import com.aienglish.feature.profile.ui.viewmodel.ProfileViewModel

@Composable
internal fun ApiProviderSection(state: ProfileState, vm: ProfileViewModel, colors: AppColors) {
    val isVietnamese = state.languageNative == "vi"
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.Tune, null, tint = colors.accent, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                if (isVietnamese) "Dịch vụ AI" else "AI Service Provider",
                fontSize = 15.sp, fontWeight = FontWeight.Bold, color = colors.mainText
            )
        }
        Text(
            if (isVietnamese)
                "Chọn nhà cung cấp dịch vụ AI để tạo nội dung bài học và đánh giá câu trả lời của bạn."
            else
                "Select AI provider to generate lessons and evaluate your answers.",
            fontSize = 12.sp, color = colors.mutedText, lineHeight = 17.sp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.surface, RoundedCornerShape(12.dp))
                .border(1.dp, colors.border, RoundedCornerShape(12.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            listOf("gemini" to "Google Gemini", "novita" to "Novita AI").forEach { (type, label) ->
                val selected = state.apiProvider == type
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selected) colors.accent else Color.Transparent)
                        .clickable { vm.onApiProviderChanged(type) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        label,
                        color = if (selected) Color.White else colors.mutedText,
                        fontSize = 13.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
internal fun NovitaApiKeySection(state: ProfileState, vm: ProfileViewModel, colors: AppColors) {
    val isVietnamese = state.languageNative == "vi"
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.Key, null, tint = colors.accent, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Novita API Key", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = colors.mainText)
        }
        Text(
            if (isVietnamese) "Nhập API Key cá nhân từ Novita AI để sử dụng dịch vụ."
            else "Enter custom API Key from Novita AI to use the service.",
            fontSize = 12.sp, color = colors.mutedText, lineHeight = 17.sp
        )
        OutlinedTextField(
            value = state.novitaApiKeyInput,
            onValueChange = { vm.onNovitaApiKeyChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            placeholder = {
                Text(
                    if (isVietnamese) "Nhập Novita API Key..." else "Enter Novita API Key...",
                    color = colors.hintText, fontSize = 13.sp
                )
            },
            visualTransformation = if (state.novitaApiKeyVisible) VisualTransformation.None
            else PasswordVisualTransformation('•'),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = { vm.toggleNovitaApiKeyVisibility() }) {
                    Icon(
                        if (state.novitaApiKeyVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        null, tint = colors.hintText, modifier = Modifier.size(18.dp)
                    )
                }
            },
            leadingIcon = {
                Icon(Icons.Rounded.VpnKey, null, tint = colors.hintText, modifier = Modifier.size(16.dp))
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.accent,
                unfocusedBorderColor = colors.border,
                focusedContainerColor = colors.surface,
                unfocusedContainerColor = colors.surface,
                cursorColor = colors.accent
            )
        )
        Button(
            onClick = { vm.saveNovitaApiKey() },
            modifier = Modifier.fillMaxWidth().height(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (state.novitaApiKeySaved) colors.correct else colors.accent
            )
        ) {
            AnimatedContent(targetState = state.novitaApiKeySaved) { saved ->
                if (saved) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.CheckCircle, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(if (isVietnamese) "Đã lưu!" else "Saved!", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Save, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(if (isVietnamese) "Lưu Novita API Key" else "Save Novita API Key", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
internal fun HowToGetNovitaApiKeySection(state: ProfileState, vm: ProfileViewModel, colors: AppColors) {
    val isVietnamese = state.languageNative == "vi"
    var expanded by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(colors.surface)
                .clickable { expanded = !expanded }
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Rounded.HelpOutline, null, tint = colors.accent, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                if (isVietnamese) "Cách lấy Novita API Key" else "How to get Novita API Key",
                fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
                color = colors.mainText, modifier = Modifier.weight(1f)
            )
            Icon(
                if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                null, tint = colors.hintText
            )
        }
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, colors.border)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (isVietnamese) {
                        HowToStep(1, "Truy cập Novita AI Console", "Mở trình duyệt: novita.ai", colors)
                        HowToStep(2, "Đăng ký hoặc Đăng nhập", "Sử dụng tài khoản Google hoặc Email", colors)
                        HowToStep(3, "Truy cập Settings", "Vào phần Dashboard / Settings -> Key Management", colors)
                        HowToStep(4, "Tạo API Key", "Bấm Add Key để tạo API Key, copy và dán vào ô nhập liệu", colors)
                    } else {
                        HowToStep(1, "Go to Novita AI Console", "Open novita.ai in your browser", colors)
                        HowToStep(2, "Sign Up or Log In", "Use your Google account or email", colors)
                        HowToStep(3, "Access Settings", "Navigate to Dashboard / Settings -> Key Management", colors)
                        HowToStep(4, "Create API Key", "Click Add Key, copy it and paste into input above", colors)
                    }
                    val uriHandler = LocalUriHandler.current
                    Button(
                        onClick = { uriHandler.openUri("https://novita.ai/en/settings/key-management") },
                        modifier = Modifier.fillMaxWidth().height(36.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.surfaceElevated)
                    ) {
                        Text(
                            if (isVietnamese) "Đi tới quản lý API Key" else "Go to API Key Management",
                            color = colors.accent, fontSize = 12.sp, fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
