package com.aienglish.feature.aiteacher.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.aiteacher.domain.model.QuestionNote
import androidx.compose.foundation.shape.CircleShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesBottomSheet(
    notes: List<QuestionNote>,
    colors: AppColors,
    sentenceIndex: Int,
    onDismiss: () -> Unit,
    onDeleteNote: (String, Int) -> Unit,
    onUpdateNote: (String, Int, String) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colors.surface,
        contentColor = colors.mainText,
        dragHandle = { BottomSheetDefaults.DragHandle(color = colors.mutedText.copy(alpha = 0.5f)) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = null,
                    tint = colors.accent,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Cấu trúc đã lưu",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.mainText
                )
            }

            if (notes.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp), contentAlignment = Alignment.Center) {
                    Text("Chưa có cấu trúc nào được lưu cho câu này.", color = colors.hintText, fontSize = 14.sp)
                }
            } else {
                var editingNoteId by remember { mutableStateOf<String?>(null) }
                var editingNoteText by remember { mutableStateOf("") }
                
                notes.forEachIndexed { i, note ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.background),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, colors.border.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            if (editingNoteId == note.id) {
                                OutlinedTextField(
                                    value = editingNoteText,
                                    onValueChange = { editingNoteText = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = colors.accent,
                                        unfocusedBorderColor = colors.border,
                                        focusedTextColor = colors.mainText,
                                        unfocusedTextColor = colors.mainText
                                    ),
                                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 15.sp)
                                )
                                Spacer(Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                                    IconButton(onClick = { editingNoteId = null }) {
                                        Icon(Icons.Default.Close, null, tint = colors.mutedText, modifier = Modifier.size(20.dp))
                                    }
                                    IconButton(onClick = { 
                                        if (editingNoteText.isNotBlank()) {
                                            onUpdateNote(note.id, sentenceIndex, editingNoteText.trim())
                                            editingNoteId = null
                                        }
                                    }) {
                                        Icon(Icons.Default.Check, null, tint = colors.accent, modifier = Modifier.size(20.dp))
                                    }
                                }
                            } else {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .background(colors.accent.copy(alpha = 0.15f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${i + 1}",
                                            color = colors.accent,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Box(modifier = Modifier.weight(1f)) {
                                        MarkdownText(markdown = note.note, colors = colors, onFormulaClick = null)
                                    }
                                    Row(modifier = Modifier.padding(start = 8.dp)) {
                                        IconButton(
                                            onClick = { 
                                                editingNoteId = note.id
                                                editingNoteText = note.note
                                            },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(Icons.Default.Edit, "Edit", tint = colors.mutedText, modifier = Modifier.size(16.dp))
                                        }
                                        IconButton(
                                            onClick = { onDeleteNote(note.id, sentenceIndex) },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(Icons.Default.Delete, "Delete", tint = colors.error, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
