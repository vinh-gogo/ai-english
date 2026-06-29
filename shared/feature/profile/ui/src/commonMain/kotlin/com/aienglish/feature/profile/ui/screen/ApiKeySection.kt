package com.aienglish.feature.profile.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalUriHandler
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.profile.ui.viewmodel.ProfileState
import com.aienglish.feature.profile.ui.viewmodel.ProfileViewModel

@Composable
internal fun ApiKeySection(state: ProfileState, vm: ProfileViewModel, colors: AppColors) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.Key, null, tint = colors.accent, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Gemini API Key", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = colors.mainText)
        }
        Text(
            "API Key cá nhân",
            fontSize = 12.sp, color = colors.mutedText, lineHeight = 17.sp
        )
        OutlinedTextField(
            value = state.apiKeyInput,
            onValueChange = { vm.onApiKeyChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            placeholder = { Text("AQ.Ab8...", color = colors.hintText, fontSize = 13.sp) },
            visualTransformation = if (state.apiKeyVisible)
                androidx.compose.ui.text.input.VisualTransformation.None
            else
                androidx.compose.ui.text.input.PasswordVisualTransformation('•'),
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Password
            ),
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = { vm.toggleApiKeyVisibility() }) {
                    Icon(
                        if (state.apiKeyVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
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
            onClick = { vm.saveApiKey() },
            modifier = Modifier.fillMaxWidth().height(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (state.apiKeySaved) colors.correct else colors.accent
            )
        ) {
            AnimatedContent(targetState = state.apiKeySaved) { saved ->
                if (saved) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.CheckCircle, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Đã lưu!", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Save, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Lưu API Key", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
internal fun HowToGetApiKeySection(state: ProfileState, vm: ProfileViewModel, colors: AppColors) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(colors.surface)
                .clickable { vm.toggleHowTo() }
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Rounded.HelpOutline, null, tint = colors.accent, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                "Cách lấy API Key",
                fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
                color = colors.mainText, modifier = Modifier.weight(1f)
            )
            Icon(
                if (state.howToExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                null, tint = colors.hintText
            )
        }

        AnimatedVisibility(
            visible = state.howToExpanded,
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
                    HowToStep(1, "Truy cập Google AI Studio", "Mở trình duyệt: aistudio.google.com", colors)
                    HowToStep(2, "Đăng nhập Google", "Dùng tài khoản Google của bạn", colors)
                    HowToStep(3, "Tạo API Key", "Click \"Get API key\" → \"Create API key\"", colors)
                    HowToStep(4, "Copy và dán vào đây", "Copy key (bắt đầu bằng AQ.Ab8...) và dán vào ô trên", colors)
                    val uriHandler = LocalUriHandler.current
                    Surface(
                        onClick = { uriHandler.openUri("https://aistudio.google.com/apikey") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF4285F4).copy(0.1f),
                        border = BorderStroke(1.dp, Color(0xFF4285F4).copy(0.3f))
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.OpenInNew, null, tint = Color(0xFF4285F4), modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("aistudio.google.com/apikey", fontSize = 13.sp, color = Color(0xFF4285F4), fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun HowToStep(step: Int, title: String, description: String, colors: AppColors) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(
            modifier = Modifier.size(26.dp).clip(CircleShape).background(colors.accent),
            contentAlignment = Alignment.Center
        ) {
            Text(step.toString(), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = colors.mainText)
            Text(description, fontSize = 12.sp, color = colors.mutedText, lineHeight = 17.sp)
        }
    }
}
