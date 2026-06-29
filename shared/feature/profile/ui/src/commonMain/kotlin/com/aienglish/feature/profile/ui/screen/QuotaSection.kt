package com.aienglish.feature.profile.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.profile.ui.viewmodel.ProfileState
import com.aienglish.feature.profile.ui.viewmodel.ProfileViewModel

@Composable
internal fun QuotaSection(state: ProfileState, vm: ProfileViewModel, colors: AppColors) {
    val isVietnamese = state.languageNative == "vi"
    val remaining = (state.monthlyLimit - state.monthlyUsage).coerceAtLeast(0)
    val progress = if (state.isPaid) 1f
    else if (state.monthlyLimit > 0) remaining.toFloat() / state.monthlyLimit.toFloat()
    else 0f

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Speed, null, tint = colors.accent, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    if (isVietnamese) "Hạn ngạch sử dụng" else "Usage Quota",
                    fontSize = 14.sp, fontWeight = FontWeight.Bold, color = colors.mainText
                )
            }
            Text(
                if (isVietnamese) {
                    if (state.isPaid) "Còn lại: Không giới hạn" else "Còn lại: $remaining / ${state.monthlyLimit}"
                } else {
                    if (state.isPaid) "Left: Unlimited" else "Left: $remaining / ${state.monthlyLimit}"
                },
                fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = colors.mutedText
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
                        if (state.isPaid) colors.accent
                        else if (progress < 0.2f) colors.error
                        else colors.accent
                    )
            )
        }

        Text(
            if (isVietnamese) {
                if (state.isPaid)
                    "Tài khoản VIP Premium có quyền tạo bài học không giới hạn."
                else
                    "Số bài học được tạo trong tháng hiện tại. Đăng ký VIP để không giới hạn bài học, hoặc cấu hình API Key riêng để nâng hạn ngạch lên 20 bài/tháng."
            } else {
                if (state.isPaid)
                    "VIP Premium account has unlimited lesson access."
                else
                    "Number of lessons generated in the current month. Upgrade to VIP for unlimited lessons, or configure custom API Key to raise limit to 20/mo."
            },
            fontSize = 11.sp, color = colors.hintText, lineHeight = 16.sp
        )
    }
}
