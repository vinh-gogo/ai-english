package com.aienglish.feature.grammargraph.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Hub
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.grammargraph.domain.model.GrammarNode
import com.aienglish.feature.grammargraph.ui.viewmodel.GrammarGraphViewModel
import org.koin.compose.koinInject
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.min
import kotlin.math.roundToInt



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphSheet(
    targetLanguage: String,
    colors: AppColors,
    onDismiss: () -> Unit,
    onSpeak: (String) -> Unit = {},
    viewModel: GrammarGraphViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()

    val transition = rememberInfiniteTransition()
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = -100f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    LaunchedEffect(targetLanguage) {
        viewModel.loadGraph(targetLanguage)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colors.surface,
        contentColor = colors.mainText,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .padding(bottom = 16.dp)
        ) {
            // Header and Group Filter Carousel Component
            GroupFilterHeader(
                groups = state.groups,
                selectedGroup = state.selectedGroup,
                editingGroup = state.editingGroup,
                colors = colors,
                onSelectGroup = { viewModel.selectGroup(it) },
                onAddGroup = { viewModel.addGroup() },
                onStartEditingGroup = { viewModel.startEditingGroup(it) },
                onRenameGroup = { oldName, newName -> viewModel.renameGroup(oldName, newName) },
                onConfigClick = { viewModel.toggleConfigMode() },
                isConfigMode = state.isConfigMode,
                onDeleteGroup = { viewModel.deleteGroup(it) }
            )

            val density = LocalDensity.current

            // Filter nodes and edges based on selected group
            val filteredNodes = remember(state.nodes, state.selectedGroup, state.groupNodeIds) {
                state.nodes.filter { node ->
                    when (state.selectedGroup) {
                        "Tất cả" -> {
                            val allMappedIds = state.groupNodeIds.values.flatten().toSet()
                            node.isSaved || node.id in allMappedIds
                        }
                        else -> {
                            val mappedIds = state.groupNodeIds[state.selectedGroup] ?: emptyList()
                            node.id in mappedIds
                        }
                    }
                }
            }

            val filteredEdges = remember(state.edges, filteredNodes, state.selectedGroup) {
                if (state.selectedGroup != "Tất cả") {
                    emptyList()
                } else {
                    state.edges.filter { edge ->
                        filteredNodes.any { it.id == edge.fromGrammarId } &&
                        filteredNodes.any { it.id == edge.toGrammarId }
                    }
                }
            }

            // Pre-allocated physics nodes representation using DP coordinate system
            val physicsNodes = remember(filteredNodes) {
                filteredNodes.mapIndexed { index, node ->
                    val angle = index * (2 * Math.PI / filteredNodes.size)
                    val radius = 130f // Reduced cluster radius for tighter layouts
                    
                    // Estimate node dimensions dynamically in DP
                    val charCount = node.pattern.length
                    val estWidthDp = ((charCount * 5.8f) + (if (node.isSaved) 28f else 20f)).coerceIn(50f, 240f)
                    val estHeightDp = 34f // Reduced height (sleek node design)
                    
                    PhysicsNode(
                        id = node.id,
                        pattern = node.pattern,
                        // Placed relative to the center of 2000dp x 1600dp virtual workspace
                        x = 1000f + (radius * cos(angle)).toFloat(),
                        y = 800f + (radius * sin(angle)).toFloat(),
                        widthDp = estWidthDp,
                        heightDp = estHeightDp
                    )
                }
            }

            // Real-time offset positions snapshot for Compose drawing
            var nodePositions by remember(physicsNodes) {
                mutableStateOf(physicsNodes.associate { it.id to Offset(it.x, it.y) })
            }

            // Ticker to wake up physics loop on pointer movement
            var triggerSimCount by remember { mutableStateOf(0) }

            // Workspace transformations (Zoom and Pan in pixel metrics)
            var scale by remember { mutableStateOf(1f) }
            var panOffset by remember { mutableStateOf(Offset.Zero) }
            var isInitialized by remember { mutableStateOf(false) }

            // Graph Workspace Box
            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RectangleShape)
                    .background(colors.background)
                    .pointerInput(Unit) {
                        // Detect pinch-to-zoom and background panning gestures
                        detectTransformGestures(panZoomLock = true) { centroid, pan, zoom, _ ->
                            val newScale = (scale * zoom).coerceIn(0.4f, 2.5f)
                            val actualZoom = newScale / scale
                            panOffset = centroid - (centroid - panOffset) * actualZoom + pan
                            scale = newScale
                        }
                    }
            ) {
                // Initialize pan offset to center the 2000dp x 1600dp canvas on screen
                if (!isInitialized && maxWidth.value > 0f && maxHeight.value > 0f) {
                    val screenWidthPx = with(density) { maxWidth.toPx() }
                    val screenHeightPx = with(density) { maxHeight.toPx() }
                    val virtualWidthPx = with(density) { 2000.dp.toPx() }
                    val virtualHeightPx = with(density) { 1600.dp.toPx() }
                    
                    panOffset = Offset(
                        x = (screenWidthPx / 2f - virtualWidthPx / 2f),
                        y = (screenHeightPx / 2f - virtualHeightPx / 2f)
                    )
                    isInitialized = true
                }

                // Workspace Content that scales and pans
                Box(
                    modifier = Modifier
                        .size(2000.dp, 1600.dp)
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = panOffset.x,
                            translationY = panOffset.y,
                            transformOrigin = TransformOrigin(0f, 0f)
                        )
                ) {
                    // Physics Simulation Loop (decoupled from screen bounds, using virtual DP coordinates)
                    LaunchedEffect(physicsNodes, triggerSimCount) {
                        var kineticEnergy = 100f
                        while (kineticEnergy > 0.01f) {
                            PhysicsSimulator.step(physicsNodes, filteredEdges)
                            
                            // Check total kinetic energy/speed
                            var totalSpeed = 0f
                            physicsNodes.forEach { u ->
                                totalSpeed += u.vx * u.vx + u.vy * u.vy
                            }
                            kineticEnergy = totalSpeed

                            // Update snapshot coordinates for Compose
                            nodePositions = physicsNodes.associate { it.id to Offset(it.x, it.y) }

                            // Await next frame V-Sync
                            withFrameNanos { _ -> }
                        }
                    }

                    // Background Dot Grid Effect (fills the full 2000dp x 1600dp workspace)
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val dotSpacing = 30.dp.toPx()
                        val dotRadius = 1.5.dp.toPx()
                        val columns = (size.width / dotSpacing).toInt()
                        val rows = (size.height / dotSpacing).toInt()
                        
                        for (c in 0..columns) {
                            for (r in 0..rows) {
                                drawCircle(
                                    color = colors.border.copy(alpha = 0.25f),
                                    radius = dotRadius,
                                    center = Offset(c * dotSpacing, r * dotSpacing)
                                )
                            }
                        }
                    }

                    // Connections Canvas Layer (Scale drawing coordinates by density to draw in DP coordinates)
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        scale(scaleX = density.density, scaleY = density.density, pivot = Offset.Zero) {
                            filteredEdges.forEach { edge ->
                                val uNode = physicsNodes.find { it.id == edge.fromGrammarId }
                                val vNode = physicsNodes.find { it.id == edge.toGrammarId }
                                val startPos = nodePositions[edge.fromGrammarId]
                                val endPos = nodePositions[edge.toGrammarId]
                                
                                if (uNode != null && vNode != null && startPos != null && endPos != null) {
                                    val startCenter = startPos + Offset(uNode.widthDp / 2f, uNode.heightDp / 2f)
                                    val endCenter = endPos + Offset(vNode.widthDp / 2f, vNode.heightDp / 2f)
                                    
                                    val dx = endCenter.x - startCenter.x
                                    val dy = endCenter.y - startCenter.y
                                    val dist = sqrt(dx * dx + dy * dy)
                                    
                                    if (dist > uNode.heightDp) {
                                        val ux = dx / dist
                                        val uy = dy / dist
                                        
                                        // Precise Capsule boundary intersections calculated entirely in DP
                                        val tStart = getCapsuleIntersection(uNode.widthDp, uNode.heightDp, ux, uy)
                                        val tEnd = getCapsuleIntersection(vNode.widthDp, vNode.heightDp, -ux, -uy)
                                        
                                        val drawStart = startCenter + Offset(ux * tStart, uy * tStart)
                                        val drawEnd = endCenter - Offset(ux * tEnd, uy * tEnd)
                                        
                                        val isFocused = state.selectedNode == null || 
                                                state.focusedConnections.contains(edge.fromGrammarId) && 
                                                state.focusedConnections.contains(edge.toGrammarId)
                                                
                                        val edgeColor = if (isFocused) colors.accent else colors.border
                                        val alpha = if (isFocused) 0.85f else 0.15f
                                        val strokeWidth = if (isFocused) 2.5f else 1.5f
                                        
                                        val path = Path().apply {
                                            moveTo(drawStart.x, drawStart.y)
                                            lineTo(drawEnd.x, drawEnd.y)
                                        }
                                        
                                        drawPath(
                                            path = path,
                                            color = edgeColor,
                                            style = Stroke(
                                                width = strokeWidth,
                                                pathEffect = PathEffect.dashPathEffect(
                                                    intervals = if (isFocused) floatArrayOf(12f, 8f) else floatArrayOf(8f, 10f),
                                                    phase = phase
                                                )
                                            ),
                                            alpha = alpha
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Interactive Capsule Nodes positioned in DP units
                    filteredNodes.forEach { node ->
                        val offset = nodePositions[node.id] ?: Offset.Zero
                        val isSelected = state.selectedNode?.id == node.id
                        val isFocused = state.selectedNode == null || state.focusedConnections.contains(node.id)
                        
                        GraphNodeView(
                            node = node,
                            offset = offset,
                            isSelected = isSelected,
                            isFocused = isFocused,
                            colors = colors,
                            density = density.density,
                            scale = scale,
                            onSelect = { viewModel.selectNode(if (isSelected) null else node) },
                            onDragStart = {
                                val u = physicsNodes.find { it.id == node.id }
                                u?.isBeingDragged = true
                                u?.vx = 0f
                                u?.vy = 0f
                            },
                            onDragEnd = {
                                val u = physicsNodes.find { it.id == node.id }
                                u?.isBeingDragged = false
                                triggerSimCount++
                            },
                            onDrag = { dragAmount ->
                                val u = physicsNodes.find { it.id == node.id }
                                if (u != null) {
                                    u.x += dragAmount.x
                                    u.y += dragAmount.y
                                    u.x = u.x.coerceIn(20f, 2000f - u.widthDp - 20f)
                                    u.y = u.y.coerceIn(20f, 1600f - u.heightDp - 20f)
                                    triggerSimCount++
                                }
                            }
                        )
                    }
                }

                // Fixed floating action (+) button in the top-right corner of the viewport
                var showAddSavedDropdown by remember { mutableStateOf(false) }
                val availableSavedNodes = remember(state.nodes, filteredNodes) {
                    state.nodes.filter { node -> filteredNodes.none { it.id == node.id } }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        FloatingActionButton(
                            onClick = { showAddSavedDropdown = true },
                            containerColor = colors.accent,
                            contentColor = colors.surface,
                            shape = CircleShape,
                            modifier = Modifier.size(40.dp),
                            elevation = FloatingActionButtonDefaults.elevation(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = "Add saved structure",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        
                        FloatingActionButton(
                            onClick = { viewModel.generateAiGraphForCurrentGroup() },
                            containerColor = colors.accent,
                            contentColor = colors.surface,
                            shape = CircleShape,
                            modifier = Modifier.size(40.dp),
                            elevation = FloatingActionButtonDefaults.elevation(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AutoAwesome,
                                contentDescription = "AI Assistant",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showAddSavedDropdown,
                        onDismissRequest = { showAddSavedDropdown = false },
                        modifier = Modifier
                            .background(colors.surface)
                            .widthIn(max = 240.dp)
                    ) {
                        if (availableSavedNodes.isEmpty()) {
                            DropdownMenuItem(
                                text = { 
                                    Text(
                                        text = "Không có cấu trúc đã lưu mới", 
                                        fontSize = 13.sp,
                                        color = colors.hintText
                                    ) 
                                },
                                onClick = { showAddSavedDropdown = false },
                                enabled = false
                            )
                        } else {
                            availableSavedNodes.forEach { node ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(
                                                text = node.pattern,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = colors.mainText
                                            )
                                            Text(
                                                text = node.meaning,
                                                fontSize = 11.sp,
                                                color = colors.mutedText,
                                                maxLines = 1
                                            )
                                        }
                                    },
                                    onClick = {
                                        showAddSavedDropdown = false
                                        viewModel.addSavedNodeToGraph(node)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = state.selectedNode != null,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                state.selectedNode?.let { node ->
                    NodeDetailsCard(
                        node = node,
                        colors = colors,
                        onDismiss = { viewModel.selectNode(null) },
                        onSpeak = onSpeak,
                        onDeleteClick = { viewModel.removeNodeFromGraph(node.id) }
                    )
                }
            }
            }
            
            // AI Loading Overlay
            if (state.isGeneratingAi) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .pointerInput(Unit) { detectTapGestures { } }, // intercept taps
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(color = colors.accent)
                        Text(
                            text = "AI đang phân tích và tạo graph...",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // AI Error Dialog
            state.aiErrorMessage?.let { errorMsg ->
                AlertDialog(
                    onDismissRequest = { viewModel.clearAiError() },
                    title = {
                        Text(
                            text = "AI Error",
                            color = colors.mainText,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Text(
                            text = errorMsg,
                            color = colors.mutedText
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { viewModel.clearAiError() }) {
                            Text("Đóng", color = colors.accent)
                        }
                    },
                    containerColor = colors.surface,
                    titleContentColor = colors.mainText,
                    textContentColor = colors.mutedText
                )
            }
        }
    }
}
