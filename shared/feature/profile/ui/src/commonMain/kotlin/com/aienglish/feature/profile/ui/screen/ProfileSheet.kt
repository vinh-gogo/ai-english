package com.aienglish.feature.profile.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.LocalAppColors
import com.aienglish.feature.profile.ui.viewmodel.ProfileViewModel
import com.aienglish.feature.pro.ui.viewmodel.ProViewModel
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
