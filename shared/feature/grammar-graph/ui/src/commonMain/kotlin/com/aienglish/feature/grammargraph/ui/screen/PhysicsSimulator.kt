package com.aienglish.feature.grammargraph.ui.screen

import com.aienglish.feature.grammargraph.domain.model.GrammarEdge
import kotlin.math.abs
import kotlin.math.sqrt

internal object PhysicsSimulator {
    fun step(
        physicsNodes: List<PhysicsNode>,
        filteredEdges: List<GrammarEdge>,
        repelConstant: Float = 90000f,
        attractConstant: Float = 0.06f,
        gravityConstant: Float = 0.01f,
        damping: Float = 0.82f,
        centerX: Float = 1000f,
        centerY: Float = 800f,
        padX: Float = 24f,
        padY: Float = 10f
    ) {
        // 1. Repulsion forces
        for (i in physicsNodes.indices) {
            val u = physicsNodes[i]
            var fx = 0f
            var fy = 0f
            
            for (j in physicsNodes.indices) {
                if (i == j) continue
                val v = physicsNodes[j]
                val dx = u.x - v.x
                val dy = u.y - v.y
                val distSq = dx * dx + dy * dy + 1f
                val dist = sqrt(distSq)
                
                val force = repelConstant / distSq
                fx += (dx / dist) * force
                fy += (dy / dist) * force
            }

            // Attraction pull towards center of workspace
            val centerDx = centerX - u.x - u.widthDp / 2f
            val centerDy = centerY - u.y - u.heightDp / 2f
            fx += centerDx * gravityConstant
            fy += centerDy * gravityConstant

            // Apply forces to velocity
            if (!u.isBeingDragged) {
                u.vx = (u.vx + fx) * damping
                u.vy = (u.vy + fy) * damping
            }
        }

        // 2. Attraction spring forces (along connections/edges)
        filteredEdges.forEach { edge ->
            val u = physicsNodes.find { it.id == edge.fromGrammarId }
            val v = physicsNodes.find { it.id == edge.toGrammarId }
            if (u != null && v != null) {
                val dx = v.x - u.x
                val dy = v.y - u.y
                val dist = sqrt(dx * dx + dy * dy) + 1f
                
                // Stable randomized link length based on edge hashes to break symmetry (ranging from 100 to 140 DP)
                val stableRandomOffset = abs(edge.fromGrammarId.hashCode() + edge.toGrammarId.hashCode()) % 40
                val restLength = 100f + stableRandomOffset

                val force = attractConstant * (dist - restLength)
                val fx = (dx / dist) * force
                val fy = (dy / dist) * force
                
                if (!u.isBeingDragged) {
                    u.vx += fx
                    u.vy += fy
                }
                if (!v.isBeingDragged) {
                    v.vx -= fx
                    v.vy -= fy
                }
            }
        }

        // 3. Move nodes slightly
        physicsNodes.forEach { u ->
            if (!u.isBeingDragged) {
                u.x += u.vx
                u.y += u.vy
            }
        }

        // 4. Strict Overlap/Collision Resolution (Adaptive Capsule checks in DP)
        repeat(3) {
            for (i in physicsNodes.indices) {
                val u = physicsNodes[i]
                for (j in i + 1 until physicsNodes.size) {
                    val v = physicsNodes[j]
                    val dx = v.x - u.x
                    val dy = v.y - u.y
                    
                    val spacingX = (u.widthDp + v.widthDp) / 2f + padX
                    val spacingY = (u.heightDp + v.heightDp) / 2f + padY
                    
                    val overlapX = spacingX - abs(dx)
                    val overlapY = spacingY - abs(dy)
                    
                    if (overlapX > 0 && overlapY > 0) {
                        val pushFactor = if (u.isBeingDragged || v.isBeingDragged) 0.8f else 0.5f
                        if (overlapX < overlapY) {
                            val pushX = overlapX * pushFactor
                            if (dx > 0) {
                                if (!u.isBeingDragged) u.x -= pushX
                                if (!v.isBeingDragged) v.x += pushX
                            } else {
                                if (!u.isBeingDragged) u.x += pushX
                                if (!v.isBeingDragged) v.x -= pushX
                            }
                            u.vx *= 0.5f
                            v.vx *= 0.5f
                        } else {
                            val pushY = overlapY * pushFactor
                            if (dy > 0) {
                                if (!u.isBeingDragged) u.y -= pushY
                                if (!v.isBeingDragged) v.y += pushY
                            } else {
                                if (!u.isBeingDragged) u.y += pushY
                                if (!v.isBeingDragged) v.y -= pushY
                            }
                            u.vy *= 0.5f
                            v.vy *= 0.5f
                        }
                    }
                }
            }
        }

        // 5. Boundaries locks
        physicsNodes.forEach { u ->
            u.x = u.x.coerceIn(20f, 2000f - u.widthDp - 20f)
            u.y = u.y.coerceIn(20f, 1600f - u.heightDp - 20f)
        }
    }
}
