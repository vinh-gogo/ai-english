package com.aienglish.feature.aiteacher.ui.screen.components.teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors

@Composable
fun TeacherTopNavigation(
    showBackButton: Boolean,
    isSignedIn: Boolean,
    displayName: String?,
    isPaid: Boolean,
    onBackClick: () -> Unit,
    onVocabularyClick: () -> Unit = {},
    onHistoryClick: () -> Unit,
    onProfileClick: () -> Unit,
    onProClick: () -> Unit,
    colors: AppColors
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .padding(top = 18.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Group 1: Back Button (Height 48dp)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showBackButton) {
                // Standalone Circular Back Button (Perfect 48x48 circle)
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(colors.surface)
                        .border(2.dp, colors.border, CircleShape)
                        .clickable { onBackClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = colors.mainText,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(48.dp))
            }
        }

        // Group 2: Vocabulary, History, Profile, Pro badge (Grouped inside a single 48dp capsule aligned to the right)
        Row(
            modifier = Modifier
                .height(48.dp)
                .background(colors.surface, CircleShape)
                .border(2.dp, colors.border, CircleShape),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Vocabulary Button (Rounded on the left, flat on the right)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(48.dp)
                    .clip(RoundedCornerShape(topStart = 100.dp, bottomStart = 100.dp))
                    .clickable { onVocabularyClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.AutoMirrored.Rounded.MenuBook,
                    contentDescription = "Từ vựng",
                    tint = colors.mutedText,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Divider
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(20.dp)
                    .background(colors.border)
            )

            // History Button (Flat on both sides)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(48.dp)
                    .clip(RoundedCornerShape(0.dp))
                    .clickable { onHistoryClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.History,
                    contentDescription = "Lịch sử",
                    tint = colors.mutedText,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Divider
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(20.dp)
                    .background(colors.border)
            )

            // Profile Button (Perfect rectangle/square click ripple in the middle)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(48.dp)
                    .clip(RoundedCornerShape(0.dp))
                    .clickable { onProfileClick() },
                contentAlignment = Alignment.Center
            ) {
                if (isSignedIn && displayName != null) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(colors.accent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            displayName.first().uppercase(),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Icon(
                        Icons.Rounded.AccountCircle,
                        contentDescription = "Profile",
                        tint = colors.mutedText,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Divider
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(20.dp)
                    .background(colors.border)
            )

            // PRO Button (Rounded on the right, flat on the left)
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 100.dp, bottomEnd = 100.dp))
                    .clickable { onProClick() }
                    .padding(start = 12.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.WorkspacePremium,
                    contentDescription = "PRO",
                    tint = Color(0xFFFFB300),
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = if (isPaid) "PRO" else "GET",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = Color(0xFFFFB300)
                )
            }
        }
    }
}
