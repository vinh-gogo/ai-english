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
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.core.designsystem.theme.LocalAppColors
import com.aienglish.feature.profile.ui.viewmodel.ProfileState
import com.aienglish.feature.profile.ui.viewmodel.ProfileViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSheet(
    profileViewModel: ProfileViewModel = koinInject(),
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
                UserInfoSection(state, profileViewModel, colors, onGoogleSignIn, onGoogleSignOut)
                HorizontalDivider(color = colors.border)
                LanguageSettingsSection(state, profileViewModel, colors)
                HorizontalDivider(color = colors.border)
                ApiKeySection(state, profileViewModel, colors)
                HorizontalDivider(color = colors.border)
                HowToGetApiKeySection(state, profileViewModel, colors)
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun UserInfoSection(
    state: ProfileState,
    vm: ProfileViewModel,
    colors: AppColors,
    onGoogleSignIn: () -> Unit,
    onGoogleSignOut: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
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

        Spacer(Modifier.height(10.dp))

        if (state.isSignedIn) {
            Text(state.displayName ?: "Người dùng", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = colors.mainText)
            if (state.email != null) Text(state.email, fontSize = 13.sp, color = colors.mutedText)
            Spacer(Modifier.height(12.dp))
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
            "API Key dùng để gọi Gemini AI. Key được lưu trên thiết bị của bạn.",
            fontSize = 12.sp, color = colors.mutedText, lineHeight = 17.sp
        )
        OutlinedTextField(
            value = state.apiKeyInput,
            onValueChange = { vm.onApiKeyChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            placeholder = { Text("AIzaSy...", color = colors.hintText, fontSize = 13.sp) },
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
                "Cách lấy Gemini API Key miễn phí",
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
                    HowToStep(4, "Copy và dán vào đây", "Copy key (bắt đầu bằng AIzaSy...) và dán vào ô trên", colors)

                    HorizontalDivider(color = colors.border)

                    Card(
                        colors = CardDefaults.cardColors(containerColor = colors.accent.copy(0.08f)),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, colors.accent.copy(0.25f))
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                            Text("💡", fontSize = 16.sp)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Gemini 2.0 Flash miễn phí: 15 req/phút, 1 triệu token/ngày — đủ học cả ngày!",
                                fontSize = 12.sp, color = colors.mutedText, lineHeight = 17.sp
                            )
                        }
                    }

                    Surface(
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
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = colors.border.copy(0.3f)
            ) {
                Text(
                    "Tiếng Việt",
                    fontSize = 12.sp,
                    color = colors.mutedText,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
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

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(colors.border.copy(0.4f))
                    .padding(2.dp)
            ) {
                val isEnglish = state.languageTarget == "en"
                // Tiếng Anh
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isEnglish) colors.accent else Color.Transparent)
                        .clickable { vm.onLanguageTargetChanged("en") }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Tiếng Anh",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isEnglish) Color.White else colors.mutedText
                    )
                }
                // Tiếng Nhật
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (!isEnglish) colors.accent else Color.Transparent)
                        .clickable { vm.onLanguageTargetChanged("ja") }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Tiếng Nhật",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (!isEnglish) Color.White else colors.mutedText
                    )
                }
            }
        }
    }
}
