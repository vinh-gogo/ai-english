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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalUriHandler
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.core.designsystem.theme.AppTheme
import com.aienglish.core.designsystem.theme.LocalAppColors
import com.aienglish.feature.profile.ui.viewmodel.ProfileState
import com.aienglish.feature.profile.ui.viewmodel.ProfileViewModel
import com.aienglish.feature.pro.ui.viewmodel.ProViewModel
import com.aienglish.feature.pro.ui.intent.ProIntent
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSheet(
    profileViewModel: ProfileViewModel = koinInject(),
    proViewModel: ProViewModel = koinInject(),
    onGoogleSignIn: () -> Unit = {},
    onGoogleSignOut: () -> Unit = {}
) {
    val state by profileViewModel.state.collectAsState()
    val colors = LocalAppColors.current

    ModalBottomSheet(
        onDismissRequest = { profileViewModel.hideProfile() },
        containerColor = colors.background,
        contentColor = colors.mainText,
        dragHandle = {
            Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp).height(4.dp)
                        .clip(CircleShape)
                        .background(colors.border)
                )
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Tài khoản & Cài đặt",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.mainText,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { profileViewModel.hideProfile() }) {
                    Icon(Icons.Default.Close, "Đóng", tint = colors.hintText)
                }
            }

            HorizontalDivider(color = colors.border)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                UserInfoSection(state, profileViewModel, proViewModel, colors, onGoogleSignIn, onGoogleSignOut)
                HorizontalDivider(color = colors.border)
                LanguageSettingsSection(state, profileViewModel, colors)
                HorizontalDivider(color = colors.border)
                ApiProviderSection(state, profileViewModel, colors)
                HorizontalDivider(color = colors.border)
                if (state.apiProvider == "novita") {
                    NovitaApiKeySection(state, profileViewModel, colors)
                    HorizontalDivider(color = colors.border)
                    HowToGetNovitaApiKeySection(state, profileViewModel, colors)
                } else {
                    ApiKeySection(state, profileViewModel, colors)
                    HorizontalDivider(color = colors.border)
                    HowToGetApiKeySection(state, profileViewModel, colors)
                }
                HorizontalDivider(color = colors.border)
                QuotaSection(state, profileViewModel, colors)
                HorizontalDivider(color = colors.border)
                ThemeSection(state, profileViewModel, colors)
                Spacer(Modifier.height(16.dp))
            }

        }
    }
}

@Composable
private fun UserInfoSection(
    state: ProfileState,
    vm: ProfileViewModel,
    proViewModel: ProViewModel,
    colors: AppColors,
    onGoogleSignIn: () -> Unit,
    onGoogleSignOut: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.size(72.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(if (state.isSignedIn) colors.accent else colors.surface),
                contentAlignment = Alignment.Center
            ) {
                if (state.isSignedIn && state.displayName != null) {
                    Text(
                        state.displayName.first().uppercase(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                } else {
                    Icon(
                        Icons.Rounded.Person, null,
                        modifier = Modifier.size(36.dp),
                        tint = colors.mutedText
                    )
                }
            }

            if (state.isPaid) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(Color(0xFFFFB300))
                        .border(1.5.dp, colors.background, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.WorkspacePremium,
                        contentDescription = "VIP",
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        if (state.isSignedIn) {
            Text(state.displayName ?: "Người dùng", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = colors.mainText)
            if (state.email != null) Text(state.email, fontSize = 13.sp, color = colors.mutedText)
            Spacer(Modifier.height(12.dp))

            // VIP / PRO Upgrade Button (placed above logout button for signed-in users)
            if (!state.isPaid) {
                Button(
                    onClick = { proViewModel.onIntent(ProIntent.ShowVip) },
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300))
                ) {
                    Icon(Icons.Rounded.WorkspacePremium, null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(
                        if (state.languageNative == "vi") "Đăng ký VIP (20 bài/tháng)" else "Register VIP (20 lessons/mo)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(Modifier.height(8.dp))
            }

            OutlinedButton(
                onClick = { onGoogleSignOut(); vm.signOut() },
                border = BorderStroke(1.dp, colors.error.copy(0.6f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(44.dp)
            ) {
                Icon(Icons.Default.Logout, null, tint = colors.error, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text("Đăng xuất", color = colors.error, fontSize = 14.sp)
            }
        } else {
            Text("Chưa đăng nhập", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = colors.mainText)
            Text("Đăng nhập để đồng bộ tiến trình học", fontSize = 13.sp, color = colors.mutedText, textAlign = TextAlign.Center)
            Spacer(Modifier.height(12.dp))

            // VIP / PRO Upgrade Button (placed above Google Login button for non-signed-in users)
            if (!state.isPaid) {
                Button(
                    onClick = { proViewModel.onIntent(ProIntent.ShowVip) },
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300))
                ) {
                    Icon(Icons.Rounded.WorkspacePremium, null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(
                        if (state.languageNative == "vi") "Đăng ký VIP (20 bài/tháng)" else "Register VIP (20 lessons/mo)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(Modifier.height(8.dp))
            }

            if (state.isSigningIn) {
                CircularProgressIndicator(color = colors.accent, modifier = Modifier.size(28.dp), strokeWidth = 2.dp)
            } else {
                Button(
                    onClick = { vm.onSigningIn(); onGoogleSignIn() },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.surface),
                    border = BorderStroke(1.dp, colors.border)
                ) {
                    Box(
                        modifier = Modifier.size(24.dp).clip(CircleShape).background(Color(0xFF4285F4)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("G", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.width(10.dp))
                    Text("Đăng nhập với Google", color = colors.mainText, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { vm.signInAsGuest() },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.mainText),
                    border = BorderStroke(1.dp, colors.border)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = "Guest",
                        tint = colors.mutedText,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text("Dùng thử làm Khách", color = colors.mainText, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        if (state.error != null) {
            Spacer(Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = colors.error.copy(0.1f)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Warning, null, tint = colors.error, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(state.error, color = colors.error, fontSize = 12.sp, modifier = Modifier.weight(1f))
                    IconButton(onClick = { vm.dismissError() }, modifier = Modifier.size(20.dp)) {
                        Icon(Icons.Default.Close, null, tint = colors.error, modifier = Modifier.size(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ApiKeySection(state: ProfileState, vm: ProfileViewModel, colors: AppColors) {
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
            visualTransformation = if (state.apiKeyVisible) VisualTransformation.None else PasswordVisualTransformation('•'),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
private fun HowToGetApiKeySection(state: ProfileState, vm: ProfileViewModel, colors: AppColors) {
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
private fun HowToStep(step: Int, title: String, description: String, colors: AppColors) {
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

@Composable
private fun LanguageSettingsSection(
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
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                currentLabel,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.mainText
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = colors.mutedText,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(colors.surface)
                ) {
                    DropdownMenuItem(
                        text = { Text("Tiếng Việt", fontSize = 13.sp, color = colors.mainText) },
                        onClick = {
                            vm.onLanguageNativeChanged("vi")
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Tiếng Anh", fontSize = 13.sp, color = colors.mainText) },
                        onClick = {
                            vm.onLanguageNativeChanged("en")
                            expanded = false
                        }
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
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                currentLabel,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.mainText
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = colors.mutedText,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(colors.surface)
                ) {
                    DropdownMenuItem(
                        text = { Text("Tiếng Anh", fontSize = 13.sp, color = colors.mainText) },
                        onClick = {
                            vm.onLanguageTargetChanged("en")
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Tiếng Tây Ban Nha", fontSize = 13.sp, color = colors.mainText) },
                        onClick = {
                            vm.onLanguageTargetChanged("es")
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Tiếng Nhật", fontSize = 13.sp, color = colors.mainText) },
                        onClick = {
                            vm.onLanguageTargetChanged("ja")
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Tiếng Trung", fontSize = 13.sp, color = colors.mainText) },
                        onClick = {
                            vm.onLanguageTargetChanged("zh")
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Tiếng Hindi", fontSize = 13.sp, color = colors.mainText) },
                        onClick = {
                            vm.onLanguageTargetChanged("hi")
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Tiếng Pháp", fontSize = 13.sp, color = colors.mainText) },
                        onClick = {
                            vm.onLanguageTargetChanged("fr")
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Tiếng Đức", fontSize = 13.sp, color = colors.mainText) },
                        onClick = {
                            vm.onLanguageTargetChanged("de")
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Tiếng Hàn", fontSize = 13.sp, color = colors.mainText) },
                        onClick = {
                            vm.onLanguageTargetChanged("ko")
                            expanded = false
                        }
                    )
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
                    // Short Tab
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isShort) colors.accent else Color.Transparent)
                            .clickable { vm.onAiResponseLengthChanged("short") }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Ngắn",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isShort) Color.White else colors.mutedText
                        )
                    }
                    // Normal Tab
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (!isShort) colors.accent else Color.Transparent)
                            .clickable { vm.onAiResponseLengthChanged("normal") }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Bình thường",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (!isShort) Color.White else colors.mutedText
                        )
                    }
                }
            }
            if (state.aiResponseLength == "short") {
                Text(
                    "Chú thích: AI sẽ trả lời giải thích ngắn gọn nhưng bao hàm đủ các chữ trong câu.",
                    fontSize = 11.sp,
                    color = colors.mutedText,
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun QuotaSection(state: ProfileState, vm: ProfileViewModel, colors: AppColors) {
    val isVietnamese = state.languageNative == "vi"
    val remaining = (state.monthlyLimit - state.monthlyUsage).coerceAtLeast(0)
    val progress = if (state.isPaid) 1f else (if (state.monthlyLimit > 0) remaining.toFloat() / state.monthlyLimit.toFloat() else 0f)

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.Speed,
                    contentDescription = null,
                    tint = colors.accent,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    if (isVietnamese) "Hạn ngạch sử dụng" else "Usage Quota",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.mainText
                )
            }
            Text(
                if (isVietnamese) {
                    if (state.isPaid) "Còn lại: Không giới hạn" else "Còn lại: $remaining / ${state.monthlyLimit}"
                } else {
                    if (state.isPaid) "Left: Unlimited" else "Left: $remaining / ${state.monthlyLimit}"
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.mutedText
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(colors.border)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        if (state.isPaid) colors.accent else (if (progress < 0.2f) colors.error else colors.accent)
                    )
            )
        }

        Text(
            if (isVietnamese) {
                if (state.isPaid) {
                    "Tài khoản VIP Premium có quyền tạo bài học không giới hạn."
                } else {
                    "Số bài học được tạo trong tháng hiện tại. Đăng ký VIP để không giới hạn bài học, hoặc cấu hình API Key riêng để nâng hạn ngạch lên 20 bài/tháng."
                }
            } else {
                if (state.isPaid) {
                    "VIP Premium account has unlimited lesson access."
                } else {
                    "Number of lessons generated in the current month. Upgrade to VIP for unlimited lessons, or configure custom API Key to raise limit to 20/mo."
                }
            },
            fontSize = 11.sp,
            color = colors.hintText,
            lineHeight = 16.sp
        )
    }
}

@Composable
private fun ApiProviderSection(state: ProfileState, vm: ProfileViewModel, colors: AppColors) {
    val isVietnamese = state.languageNative == "vi"
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.Tune, null, tint = colors.accent, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                if (isVietnamese) "Dịch vụ AI AI" else "AI Service Provider",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = colors.mainText
            )
        }
        Text(
            if (isVietnamese) {
                "Chọn nhà cung cấp dịch vụ AI để tạo nội dung bài học và đánh giá câu trả lời của bạn."
            } else {
                "Select AI provider to generate lessons and evaluate your answers."
            },
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
            val providers = listOf(
                "gemini" to "Google Gemini",
                "novita" to "Novita AI"
            )
            
            providers.forEach { (type, label) ->
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
                        text = label,
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
private fun NovitaApiKeySection(state: ProfileState, vm: ProfileViewModel, colors: AppColors) {
    val isVietnamese = state.languageNative == "vi"
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.Key, null, tint = colors.accent, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Novita API Key", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = colors.mainText)
        }
        Text(
            if (isVietnamese) {
                "Nhập API Key cá nhân từ Novita AI để sử dụng dịch vụ."
            } else {
                "Enter custom API Key from Novita AI to use the service."
            },
            fontSize = 12.sp, color = colors.mutedText, lineHeight = 17.sp
        )
        OutlinedTextField(
            value = state.novitaApiKeyInput,
            onValueChange = { vm.onNovitaApiKeyChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            placeholder = { Text(if (isVietnamese) "Nhập Novita API Key..." else "Enter Novita API Key...", color = colors.hintText, fontSize = 13.sp) },
            visualTransformation = if (state.novitaApiKeyVisible) VisualTransformation.None else PasswordVisualTransformation('•'),
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
private fun HowToGetNovitaApiKeySection(state: ProfileState, vm: ProfileViewModel, colors: AppColors) {
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
                            color = colors.accent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// ── Theme Section ─────────────────────────────────────────────────────────
@Composable
private fun ThemeSection(
    state: ProfileState,
    viewModel: ProfileViewModel,
    colors: AppColors
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Section header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                Icons.Rounded.Palette,
                contentDescription = null,
                tint = colors.accent,
                modifier = Modifier.size(20.dp)
            )
            Text(
                "Giao diện",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.mainText
            )
        }

        Text(
            "Chọn bảng màu và phong cách giao diện cho app",
            fontSize = 13.sp,
            color = colors.mutedText,
            lineHeight = 18.sp
        )

        // Theme cards
        AppTheme.entries.forEach { theme ->
            ThemeOptionCard(
                theme = theme,
                isSelected = state.appTheme == theme,
                colors = colors,
                onSelect = { viewModel.onThemeChanged(theme) }
            )
        }
    }
}

@Composable
private fun ThemeOptionCard(
    theme: AppTheme,
    isSelected: Boolean,
    colors: AppColors,
    onSelect: () -> Unit
) {
    val borderColor = if (isSelected) colors.accent else colors.border
    val bgColor     = if (isSelected) colors.accent.copy(alpha = 0.06f) else colors.surface

    // Preview swatches per theme
    val swatches = when (theme) {
        AppTheme.SUNSET_WARMTH -> listOf(
            androidx.compose.ui.graphics.Color(0xFF944A00),
            androidx.compose.ui.graphics.Color(0xFFE67E22),
            androidx.compose.ui.graphics.Color(0xFFFFF8F6),
            androidx.compose.ui.graphics.Color(0xFFFFDCC5)
        )
        AppTheme.NATURE_MINT -> listOf(
            androidx.compose.ui.graphics.Color(0xFF006D36),
            androidx.compose.ui.graphics.Color(0xFF50C878),
            androidx.compose.ui.graphics.Color(0xFFF8FAF8),
            androidx.compose.ui.graphics.Color(0xFF83FBA5)
        )
        AppTheme.BERRY_SORBET -> listOf(
            androidx.compose.ui.graphics.Color(0xFFB60E3D),
            androidx.compose.ui.graphics.Color(0xFFDA3054),
            androidx.compose.ui.graphics.Color(0xFFFFF8F7),
            androidx.compose.ui.graphics.Color(0xFFFEB2C0)
        )
        AppTheme.NORDIC_SLATE -> listOf(
            androidx.compose.ui.graphics.Color(0xFF162839),
            androidx.compose.ui.graphics.Color(0xFF2C3E50),
            androidx.compose.ui.graphics.Color(0xFFF7FAF9),
            androidx.compose.ui.graphics.Color(0xFFB5C8DF)
        )
        AppTheme.MIDNIGHT_INDIGO -> listOf(
            androidx.compose.ui.graphics.Color(0xFF101415),
            androidx.compose.ui.graphics.Color(0xFF1D2022),
            androidx.compose.ui.graphics.Color(0xFFD0BCFF),
            androidx.compose.ui.graphics.Color(0xFFA078FF)
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onSelect)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Color swatches preview
        Row(horizontalArrangement = Arrangement.spacedBy((-8).dp)) {
            swatches.forEachIndexed { index, color ->
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(1.5.dp, colors.border.copy(alpha = 0.4f), CircleShape)
                )
            }
        }

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                "${theme.emoji} ${theme.displayName}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.mainText
            )
            Text(
                when (theme) {
                    AppTheme.SUNSET_WARMTH -> "Ấm áp • Terracotta & Kem"
                    AppTheme.NATURE_MINT   -> "Tươi mát • Xanh Mint & Trắng"
                    AppTheme.BERRY_SORBET  -> "Năng động • Berry Đỏ & Kem Hồng"
                    AppTheme.NORDIC_SLATE  -> "Tối giản • Slate Xám & Tuyết"
                
                        AppTheme.MIDNIGHT_INDIGO -> "Dark Mode u{2022} Violet & Deep Space"
},
                fontSize = 12.sp,
                color = colors.mutedText
            )
        }

        // Selected indicator
        if (isSelected) {
            Icon(
                Icons.Rounded.CheckCircle,
                contentDescription = "Đang dùng",
                tint = colors.accent,
                modifier = Modifier.size(22.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, colors.border, CircleShape)
            )
        }
    }
}

