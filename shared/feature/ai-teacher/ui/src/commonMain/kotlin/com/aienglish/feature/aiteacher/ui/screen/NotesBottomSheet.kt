package com.aienglish.feature.aiteacher.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
            NotesHeader(colors)

            if (notes.isEmpty()) {
                EmptyNotesMessage(colors)
            } else {
                var editingNoteId by remember { mutableStateOf<String?>(null) }
                var editingNoteText by remember { mutableStateOf("") }
                
                notes.forEachIndexed { i, note ->
                    NoteItemCard(
                        note = note,
                        index = i,
                        colors = colors,
                        sentenceIndex = sentenceIndex,
                        editingNoteId = editingNoteId,
                        editingNoteText = editingNoteText,
                        onEditingStateChange = { id, text -> 
                            editingNoteId = id
                            editingNoteText = text 
                        },
                        onDeleteNote = onDeleteNote,
                        onUpdateNote = onUpdateNote
                    )
                }
            }
        }
    }
}

@Composable
private fun NotesHeader(colors: AppColors) {
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
}

@Composable
private fun EmptyNotesMessage(colors: AppColors) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("Chưa có cấu trúc nào được lưu cho câu này.", color = colors.hintText, fontSize = 14.sp)
    }
}

@Composable
private fun NoteItemCard(
    note: QuestionNote,
    index: Int,
    colors: AppColors,
    sentenceIndex: Int,
    editingNoteId: String?,
    editingNoteText: String,
    onEditingStateChange: (String?, String) -> Unit,
    onDeleteNote: (String, Int) -> Unit,
    onUpdateNote: (String, Int, String) -> Unit
) {
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
                NoteEditForm(
                    editingNoteText = editingNoteText,
                    colors = colors,
                    onTextChange = { onEditingStateChange(note.id, it) },
                    onCancel = { onEditingStateChange(null, "") },
                    onSave = {
                        if (editingNoteText.isNotBlank()) {
                            onUpdateNote(note.id, sentenceIndex, editingNoteText.trim())
                            onEditingStateChange(null, "")
                        }
                    }
                )
            } else {
                NoteContentView(
                    note = note,
                    index = index,
                    colors = colors,
                    onEditClick = { onEditingStateChange(note.id, note.note) },
                    onDeleteClick = { onDeleteNote(note.id, sentenceIndex) }
                )
            }
        }
    }
}

@Composable
private fun NoteEditForm(
    editingNoteText: String,
    colors: AppColors,
    onTextChange: (String) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    OutlinedTextField(
        value = editingNoteText,
        onValueChange = onTextChange,
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
        IconButton(onClick = onCancel) {
            Icon(Icons.Default.Close, null, tint = colors.mutedText, modifier = Modifier.size(20.dp))
        }
        IconButton(onClick = onSave) {
            Icon(Icons.Default.Check, null, tint = colors.accent, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun NoteContentView(
    note: QuestionNote,
    index: Int,
    colors: AppColors,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
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
                text = "${index + 1}",
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
                onClick = onEditClick,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(Icons.Default.Edit, "Edit", tint = colors.mutedText, modifier = Modifier.size(16.dp))
            }
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(Icons.Default.Delete, "Delete", tint = colors.error, modifier = Modifier.size(16.dp))
            }
        }
    }
}
