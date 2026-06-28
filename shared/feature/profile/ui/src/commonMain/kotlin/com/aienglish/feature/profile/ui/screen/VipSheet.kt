package com.aienglish.feature.profile.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.core.designsystem.theme.LocalAppColors
import com.aienglish.feature.profile.ui.viewmodel.ProfileViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VipSheet(
    profileViewModel: ProfileViewModel = koinInject()
) {
    val state by profileViewModel.state.collectAsState()
    val colors = LocalAppColors.current
    val isVietnamese = state.languageNative == "vi"

    ModalBottomSheet(
        onDismissRequest = { profileViewModel.hideVip() },
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
                    if (isVietnamese) "Đăng ký thành viên VIP" else "VIP Premium Upgrade",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.mainText,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { profileViewModel.hideVip() }) {
                    Icon(Icons.Default.Close, "Đóng", tint = colors.hintText)
                }
            }

            HorizontalDivider(color = colors.border)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 20.dp)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Crown / VIP Logo
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFB300).copy(0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.WorkspacePremium,
                        contentDescription = "VIP",
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(44.dp)
                    )
                }

                Text(
                    if (isVietnamese) "Mở khóa giới hạn AI Teacher" else "Unlock AI Teacher limits",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.mainText,
                    textAlign = TextAlign.Center
                )

                Text(
                    if (isVietnamese) {
                        "Trở thành VIP để nâng cao hạn ngạch học tập và trải nghiệm đầy đủ các tính năng."
                    } else {
                        "Upgrade to VIP to increase your learning quota and experience all premium features."
                    },
                    fontSize = 13.sp,
                    color = colors.mutedText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

                // VIP Benefits list
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.surface, RoundedCornerShape(12.dp))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val benefits = if (isVietnamese) {
                        listOf(
                            "Tăng hạn ngạch sử dụng lên 20 bài học/tháng.",
                            "Tốc độ phản hồi từ AI Teacher nhanh hơn.",
                            "Không quảng cáo, không bị gián đoạn bài học.",
                            "Ưu tiên cập nhật các tính năng và giọng nói mới."
                        )
                    } else {
                        listOf(
                            "Increase usage limit to 20 lessons/month.",
                            "Faster response speed from AI Teacher.",
                            "Ad-free, no learning interruptions.",
                            "Priority access to new features and voices."
                        )
                    }

                    benefits.forEach { benefit ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFFFFB300),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = benefit,
                                fontSize = 13.sp,
                                color = colors.mainText,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(Modifier.height(4.dp))

                // VIP Pricing Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFB300).copy(0.08f),
                    border = BorderStroke(1.5.dp, Color(0xFFFFB300))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                if (isVietnamese) "Gói VIP Premium" else "VIP Premium Package",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFB300)
                            )
                            Text(
                                if (isVietnamese) "Gia hạn hàng tháng" else "Billed monthly",
                                fontSize = 12.sp,
                                color = colors.mutedText
                            )
                        }
                        Text(
                            if (isVietnamese) "99.000đ / tháng" else "$4.99 / mo",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = colors.mainText
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Main CTA Button
                if (state.isPaid) {
                    OutlinedButton(
                        onClick = {
                            profileViewModel.setPaid(false)
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, colors.error.copy(0.6f))
                    ) {
                        Text(
                            if (isVietnamese) "Hủy gói VIP (Simulate)" else "Cancel VIP Subscription (Simulate)",
                            color = colors.error,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            profileViewModel.setPaid(true)
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300))
                    ) {
                        Text(
                            if (isVietnamese) "Kích hoạt VIP (Simulate)" else "Activate VIP Package (Simulate)",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
