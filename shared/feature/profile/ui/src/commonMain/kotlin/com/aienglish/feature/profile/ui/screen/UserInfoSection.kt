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
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.profile.ui.viewmodel.ProfileState
import com.aienglish.feature.profile.ui.viewmodel.ProfileViewModel
import com.aienglish.feature.pro.ui.viewmodel.ProViewModel
import com.aienglish.feature.pro.ui.intent.ProIntent
import androidx.compose.ui.text.style.TextAlign

@Composable
internal fun UserInfoSection(
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
        Box(modifier = Modifier.size(72.dp)) {
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
