package com.aienglish.feature.profile.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
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
    var isYearly by remember { mutableStateOf(false) }

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
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFB300).copy(0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.WorkspacePremium,
                        contentDescription = "VIP",
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(36.dp)
                    )
                }

                Text(
                    if (isVietnamese) "Nâng cấp trải nghiệm Premium" else "Upgrade to Premium Experience",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.mainText,
                    textAlign = TextAlign.Center
                )

                // Current Plan Status indicator
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (state.isPaid) Color(0xFFFFB300).copy(0.12f) else colors.surface,
                    border = BorderStroke(1.dp, if (state.isPaid) Color(0xFFFFB300) else colors.border)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = if (isVietnamese) "Gói hiện tại:" else "Current Plan:",
                            fontSize = 12.sp,
                            color = colors.mutedText
                        )
                        Text(
                            text = if (state.isPaid) "VIP Premium" else "Free-tier",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (state.isPaid) Color(0xFFFFB300) else colors.mainText
                        )
                    }
                }

                // Toggle Monthly / Yearly
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(colors.border)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (!isYearly) Color(0xFFFFB300) else Color.Transparent)
                            .clickable { isYearly = false }
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            if (isVietnamese) "Hàng tháng" else "Monthly",
                            color = if (!isYearly) Color.White else colors.mutedText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isYearly) Color(0xFFFFB300) else Color.Transparent)
                            .clickable { isYearly = true }
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                if (isVietnamese) "Hàng năm" else "Yearly",
                                color = if (isYearly) Color.White else colors.mutedText,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(colors.accent)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    "-58%",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }

                // 2 columns comparison: Free-tier vs VIP Premium
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Left Column: Free-tier
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(colors.surface, RoundedCornerShape(12.dp))
                            .border(1.dp, colors.border, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            "Free-tier",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.mutedText
                        )
                        HorizontalDivider(color = colors.border.copy(0.5f))
                        BenefitItem(if (isVietnamese) "5 bài học/tháng" else "5 lessons/mo", false, colors)
                        BenefitItem(if (isVietnamese) "Không nhập ảnh" else "No image input", false, colors)
                        BenefitItem(if (isVietnamese) "Có quảng cáo" else "Contains ads", false, colors)
                        BenefitItem(if (isVietnamese) "Anh & Việt" else "English & Vietnamese", false, colors)
                    }

                    // Right Column: VIP Premium
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFFFB300).copy(0.05f), RoundedCornerShape(12.dp))
                            .border(1.5.dp, Color(0xFFFFB300), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            "VIP Premium",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFB300)
                        )
                        HorizontalDivider(color = Color(0xFFFFB300).copy(0.3f))
                        BenefitItem(if (isVietnamese) "Không giới hạn" else "Unlimited lessons", true, colors)
                        BenefitItem(if (isVietnamese) "Nhập ảnh chủ đề" else "Topic from photo", true, colors)
                        BenefitItem(if (isVietnamese) "Không quảng cáo" else "No ads", true, colors)
                        BenefitItem(if (isVietnamese) ">10 ngôn ngữ" else ">10 languages", true, colors)
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
                                text = "VIP Premium",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFB300)
                            )
                            Text(
                                if (isVietnamese) {
                                    if (isYearly) "Gia hạn hàng năm" else "Gia hạn hàng tháng"
                                } else {
                                    if (isYearly) "Billed annually" else "Billed monthly"
                                },
                                fontSize = 12.sp,
                                color = colors.mutedText
                            )
                        }
                        Text(
                            if (isVietnamese) {
                                if (isYearly) "499.000đ / năm" else "99.000đ / tháng"
                            } else {
                                if (isYearly) "$24.99 / yr" else "$4.99 / mo"
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = colors.mainText
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))

                // Payment Methods Logos Row
                PaymentLogosRow()

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
                            if (isVietnamese) {
                                if (isYearly) "Kích hoạt VIP Năm (Simulate)" else "Kích hoạt VIP Tháng (Simulate)"
                            } else {
                                if (isYearly) "Activate Yearly VIP (Simulate)" else "Activate Monthly VIP (Simulate)"
                            },
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

@Composable
private fun BenefitItem(
    text: String,
    isVip: Boolean,
    colors: AppColors
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = if (isVip) Icons.Rounded.CheckCircle else Icons.Rounded.Cancel,
            contentDescription = null,
            tint = if (isVip) Color(0xFFFFB300) else colors.hintText,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = if (isVip) FontWeight.Bold else FontWeight.Normal,
            color = if (isVip) colors.mainText else colors.mutedText,
            maxLines = 2
        )
    }
}

@Composable
private fun PaymentLogosRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        VisaLogo()
        Spacer(Modifier.width(8.dp))
        MastercardLogo()
        Spacer(Modifier.width(8.dp))
        JcbLogo()
        Spacer(Modifier.width(8.dp))
        MomoLogo()
        Spacer(Modifier.width(8.dp))
        ChPlayLogo()
    }
}

@Composable
private fun VisaLogo() {
    Box(
        modifier = Modifier
            .size(width = 38.dp, height = 22.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFF1A1F71)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "VISA",
            color = Color(0xFFF7B600),
            fontSize = 9.sp,
            fontWeight = FontWeight.Black,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
        )
    }
}

@Composable
private fun MastercardLogo() {
    Box(
        modifier = Modifier
            .size(width = 38.dp, height = 22.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFF222222))
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy((-6).dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEB001B))
            )
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF5F00).copy(0.9f))
            )
        }
    }
}

@Composable
private fun JcbLogo() {
    Box(
        modifier = Modifier
            .size(width = 38.dp, height = 22.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White)
            .border(0.5.dp, Color.LightGray, RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(1.dp),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFF0060B6), RoundedCornerShape(topStart = 2.dp, bottomStart = 2.dp)))
            Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFFD91818)))
            Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFF008938), RoundedCornerShape(topEnd = 2.dp, bottomEnd = 2.dp)))
        }
        Text(
            text = "JCB",
            color = Color.White,
            fontSize = 7.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun MomoLogo() {
    Box(
        modifier = Modifier
            .size(width = 38.dp, height = 22.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFFA50064)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "momo",
            color = Color.White,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ChPlayLogo() {
    Box(
        modifier = Modifier
            .size(width = 46.dp, height = 22.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFFF1F3F4))
            .border(0.5.dp, Color(0xFFDADCE0), RoundedCornerShape(4.dp))
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Canvas(modifier = Modifier.size(10.dp)) {
                val w = size.width
                val h = size.height
                val ox = w * 0.35f
                val oy = h * 0.5f
                val divX = w * 0.65f
                val divYTop = h * 0.325f
                val divYBot = h * 0.675f

                // 1. Blue (Left)
                val pathBlue = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(ox, oy)
                    lineTo(0f, h)
                    close()
                }
                drawPath(pathBlue, Color(0xFF00C6FF))

                // 2. Green (Top)
                val pathGreen = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(divX, divYTop)
                    lineTo(ox, oy)
                    close()
                }
                drawPath(pathGreen, Color(0xFF00E676))

                // 3. Red (Bottom)
                val pathRed = Path().apply {
                    moveTo(0f, h)
                    lineTo(ox, oy)
                    lineTo(divX, divYBot)
                    close()
                }
                drawPath(pathRed, Color(0xFFFF1744))

                // 4. Yellow (Right)
                val pathYellow = Path().apply {
                    moveTo(divX, divYTop)
                    lineTo(w, oy)
                    lineTo(divX, divYBot)
                    lineTo(ox, oy)
                    close()
                }
                drawPath(pathYellow, Color(0xFFFFEA00))
            }
            Text(
                text = "Play",
                color = Color(0xFF5F6368),
                fontSize = 7.5.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}
