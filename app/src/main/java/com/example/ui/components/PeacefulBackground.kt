package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.data.model.WorldTheme
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class ThemedParticle(
    val xPercent: Float,
    val initialYPercent: Float,
    val size: Float,
    val speed: Float,
    val rotationSpeed: Float,
    val layerDepth: Float, // 0.5 (far) to 1.5 (near)
    val alpha: Float
)

@Composable
fun PeacefulBackground(
    worldTheme: WorldTheme,
    modifier: Modifier = Modifier,
    animationQuality: String = "Full" // Full, Reduced, Minimal
) {
    if (animationQuality == "Minimal") {
        // Minimal mode: Static serene gradient canvas
        Canvas(modifier = modifier.fillMaxSize()) {
            drawRect(
                color = when (worldTheme) {
                    WorldTheme.SAKURA_GARDEN -> Color(0xFFFBF2F4)
                    WorldTheme.FOREST_MORNING -> Color(0xFFF0F5F1)
                    WorldTheme.QUIET_OCEAN -> Color(0xFFEFF5F8)
                    WorldTheme.CLOUD_DREAM -> Color(0xFFF3F5FA)
                    WorldTheme.COZY_ROOM -> Color(0xFFFAF5EE)
                    WorldTheme.MOONLIT_NIGHT -> Color(0xFF1E2430)
                    WorldTheme.STARRY_SKY -> Color(0xFF141926)
                    WorldTheme.COZY_AUTUMN -> Color(0xFFFAF2EB)
                    WorldTheme.RAINY_WINDOW -> Color(0xFFECEFF2)
                    WorldTheme.LAVENDER_FIELD -> Color(0xFFF5F0F8)
                }
            )
        }
        return
    }

    val particleCount = if (animationQuality == "Reduced") 12 else 26

    val particles = remember(worldTheme, particleCount) {
        val rnd = Random(worldTheme.ordinal * 31)
        List(particleCount) {
            ThemedParticle(
                xPercent = rnd.nextFloat(),
                initialYPercent = rnd.nextFloat(),
                size = rnd.nextFloat() * 6f + 3f,
                speed = rnd.nextFloat() * 0.0003f + 0.00015f,
                rotationSpeed = (rnd.nextFloat() - 0.5f) * 4f,
                layerDepth = rnd.nextFloat() * 0.8f + 0.6f,
                alpha = rnd.nextFloat() * 0.35f + 0.25f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "theme_ambient_anim")
    val cycleTime by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (animationQuality == "Reduced") 18000 else 12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ambient_cycle"
    )

    // Periodic breeze / whoosh wave
    val breezeTime by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(24000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "breeze_cycle"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Calculate occasional wind breeze gust
        val breezeStrength = sin(breezeTime * Math.PI.toFloat() * 2f).coerceAtLeast(0f) * 35f

        when (worldTheme) {
            WorldTheme.SAKURA_GARDEN -> {
                // Soft pink sky & gently falling sakura petals
                drawSakuraEnvironment(w, h, particles, cycleTime, breezeStrength)
            }
            WorldTheme.FOREST_MORNING -> {
                // Sunlight rays, swaying greenery & morning motes
                drawForestEnvironment(w, h, particles, cycleTime, breezeStrength)
            }
            WorldTheme.QUIET_OCEAN -> {
                // Slow rolling waves & soft water crests
                drawOceanEnvironment(w, h, cycleTime)
            }
            WorldTheme.CLOUD_DREAM -> {
                // Soft drifting pastel clouds & subtle sparkles
                drawCloudDreamEnvironment(w, h, particles, cycleTime)
            }
            WorldTheme.COZY_ROOM -> {
                // Warm ambient hearth glow & swaying plant silhouette
                drawCozyRoomEnvironment(w, h, cycleTime)
            }
            WorldTheme.RAINY_WINDOW -> {
                // Rain droplets trickling down misty glass
                drawRainyWindowEnvironment(w, h, particles, cycleTime)
            }
            WorldTheme.MOONLIT_NIGHT -> {
                // Crescent moon, soft clouds & twinkling stars
                drawMoonlitNightEnvironment(w, h, particles, cycleTime)
            }
            WorldTheme.STARRY_SKY -> {
                // Deep space canvas with twinkling stars & shooting stars
                drawStarrySkyEnvironment(w, h, particles, cycleTime)
            }
            WorldTheme.COZY_AUTUMN -> {
                // Golden amber falling maple leaves & warm autumn light
                drawAutumnEnvironment(w, h, particles, cycleTime, breezeStrength)
            }
            WorldTheme.LAVENDER_FIELD -> {
                // Swaying lavender blossoms & gentle butterfly path
                drawLavenderEnvironment(w, h, particles, cycleTime)
            }
        }
    }
}

private fun DrawScope.drawSakuraEnvironment(
    w: Float,
    h: Float,
    particles: List<ThemedParticle>,
    cycleTime: Float,
    breezeStrength: Float
) {
    val petalColor = Color(0xFFF2A3B3)

    for (p in particles) {
        val progress = (p.initialYPercent + cycleTime * p.speed * 800f * p.layerDepth) % 1f
        val currentY = progress * h
        val wobble = sin((progress * 6.28f + p.xPercent * 8f).toDouble()).toFloat() * 22f
        val currentX = (p.xPercent * w + wobble + breezeStrength * p.layerDepth).coerceIn(0f, w)

        val rotation = (progress * p.rotationSpeed * 360f) % 360f
        val petalSize = p.size * p.layerDepth

        // Draw curved sakura petal
        val petalPath = Path().apply {
            moveTo(currentX, currentY - petalSize)
            quadraticTo(currentX + petalSize * 0.8f, currentY, currentX, currentY + petalSize)
            quadraticTo(currentX - petalSize * 0.8f, currentY, currentX, currentY - petalSize)
            close()
        }
        drawPath(
            path = petalPath,
            color = petalColor.copy(alpha = (p.alpha * 0.75f).coerceIn(0f, 1f)),
            style = Fill
        )
    }
}

private fun DrawScope.drawForestEnvironment(
    w: Float,
    h: Float,
    particles: List<ThemedParticle>,
    cycleTime: Float,
    breezeStrength: Float
) {
    val leafColor = Color(0xFF7CAE8B)
    val moteColor = Color(0xFFFFF7C2)

    // Gentle sunbeam in upper corner
    drawCircle(
        color = Color(0xFFFFF9E6).copy(alpha = 0.08f),
        radius = w * 0.4f,
        center = Offset(w * 0.85f, h * 0.1f)
    )

    for (p in particles) {
        val progress = (p.initialYPercent + cycleTime * p.speed * 600f) % 1f
        val currentY = progress * h
        val wobble = sin((progress * 4f + p.xPercent * 5f).toDouble()).toFloat() * 14f
        val currentX = (p.xPercent * w + wobble + breezeStrength * 0.5f).coerceIn(0f, w)

        if (p.size > 5f) {
            // Leaf
            drawOval(
                color = leafColor.copy(alpha = p.alpha * 0.5f),
                topLeft = Offset(currentX, currentY),
                size = Size(p.size * 1.5f, p.size * 0.9f)
            )
        } else {
            // Floating sun mote
            drawCircle(
                color = moteColor.copy(alpha = p.alpha * 0.7f),
                radius = p.size * 0.6f,
                center = Offset(currentX, currentY)
            )
        }
    }
}

private fun DrawScope.drawOceanEnvironment(w: Float, h: Float, cycleTime: Float) {
    // 3 layered rolling waves at the bottom
    val waveColors = listOf(
        Color(0xFF86B9D0).copy(alpha = 0.15f),
        Color(0xFF5A9BB9).copy(alpha = 0.20f),
        Color(0xFF3B7F9E).copy(alpha = 0.25f)
    )

    waveColors.forEachIndexed { idx, col ->
        val wavePath = Path()
        val baseY = h * (0.80f + idx * 0.06f)
        val phaseOffset = cycleTime * 6.28f * (idx + 1) * 0.4f

        wavePath.moveTo(0f, h)
        wavePath.lineTo(0f, baseY)

        var x = 0f
        val waveLen = w * 0.35f
        while (x <= w) {
            val y = baseY + sin((x / waveLen * 6.28f + phaseOffset).toDouble()).toFloat() * (12f + idx * 4f)
            wavePath.lineTo(x, y)
            x += 20f
        }
        wavePath.lineTo(w, h)
        wavePath.close()

        drawPath(path = wavePath, color = col, style = Fill)
    }
}

private fun DrawScope.drawCloudDreamEnvironment(
    w: Float,
    h: Float,
    particles: List<ThemedParticle>,
    cycleTime: Float
) {
    val cloudColor = Color(0xFFFFFFFF).copy(alpha = 0.25f)
    val sparkleColor = Color(0xFFFFEAA7).copy(alpha = 0.5f)

    // Soft drifting clouds
    for (i in 0..2) {
        val cloudX = ((cycleTime * 0.1f + i * 0.35f) % 1f) * (w + 200f) - 100f
        val cloudY = h * (0.15f + i * 0.25f)
        drawCircle(color = cloudColor, radius = 60f, center = Offset(cloudX, cloudY))
        drawCircle(color = cloudColor, radius = 80f, center = Offset(cloudX + 50f, cloudY - 10f))
        drawCircle(color = cloudColor, radius = 55f, center = Offset(cloudX + 100f, cloudY))
    }

    // Sparkles
    for (p in particles) {
        val twinkle = (sin((cycleTime * 10f + p.xPercent * 20f).toDouble()).toFloat() + 1f) * 0.5f
        if (twinkle > 0.6f) {
            drawCircle(
                color = sparkleColor.copy(alpha = twinkle * 0.6f),
                radius = p.size * 0.5f,
                center = Offset(p.xPercent * w, p.initialYPercent * h)
            )
        }
    }
}

private fun DrawScope.drawCozyRoomEnvironment(w: Float, h: Float, cycleTime: Float) {
    // Warm corner lamp glow
    val lampGlow = (sin(cycleTime * 6.28f * 0.5f) * 0.04f + 0.18f).toFloat()
    drawCircle(
        color = Color(0xFFFFD59E).copy(alpha = lampGlow),
        radius = w * 0.5f,
        center = Offset(w * 0.15f, h * 0.15f)
    )

    // Subtle indoor plant leaf silhouette in bottom-right
    val plantSway = sin(cycleTime * 6.28f) * 4f
    val leafColor = Color(0xFF6B8E23).copy(alpha = 0.2f)
    drawOval(
        color = leafColor,
        topLeft = Offset(w * 0.85f + plantSway, h * 0.82f),
        size = Size(60f, 100f)
    )
}

private fun DrawScope.drawRainyWindowEnvironment(
    w: Float,
    h: Float,
    particles: List<ThemedParticle>,
    cycleTime: Float
) {
    val dropColor = Color(0xFF90A4AE).copy(alpha = 0.45f)
    val trailColor = Color(0xFFCFD8DC).copy(alpha = 0.25f)

    for (p in particles) {
        val progress = (p.initialYPercent + cycleTime * p.speed * 1800f) % 1f
        val dropY = progress * h
        val dropX = p.xPercent * w

        // Raindrop trail
        drawLine(
            color = trailColor,
            start = Offset(dropX, dropY - 24f),
            end = Offset(dropX, dropY),
            strokeWidth = 1.5f
        )
        // Droplet head
        drawCircle(
            color = dropColor,
            radius = p.size * 0.6f,
            center = Offset(dropX, dropY)
        )
    }
}

private fun DrawScope.drawMoonlitNightEnvironment(
    w: Float,
    h: Float,
    particles: List<ThemedParticle>,
    cycleTime: Float
) {
    // Crescent Moon in top right
    val moonCenter = Offset(w * 0.82f, h * 0.12f)
    drawCircle(
        color = Color(0xFFFFF9D2).copy(alpha = 0.85f),
        radius = 28f,
        center = moonCenter
    )
    // Cut-out circle for crescent shape
    drawCircle(
        color = Color(0xFF1E2430),
        radius = 24f,
        center = Offset(moonCenter.x - 10f, moonCenter.y - 4f)
    )

    // Twinkling stars
    val starColor = Color(0xFFE8EEF5)
    for (p in particles) {
        val twinkle = (sin((cycleTime * 8f + p.xPercent * 15f).toDouble()).toFloat() + 1f) * 0.5f
        drawCircle(
            color = starColor.copy(alpha = twinkle * 0.7f),
            radius = p.size * 0.4f,
            center = Offset(p.xPercent * w, p.initialYPercent * h * 0.7f)
        )
    }
}

private fun DrawScope.drawStarrySkyEnvironment(
    w: Float,
    h: Float,
    particles: List<ThemedParticle>,
    cycleTime: Float
) {
    val starColor = Color(0xFFFFFFFF)
    for (p in particles) {
        val twinkle = (sin((cycleTime * 12f + p.xPercent * 25f).toDouble()).toFloat() + 1f) * 0.5f
        drawCircle(
            color = starColor.copy(alpha = twinkle * 0.8f),
            radius = p.size * 0.45f,
            center = Offset(p.xPercent * w, p.initialYPercent * h)
        )
    }

    // Occasional shooting star streak
    val shootProgress = (cycleTime * 3f) % 1f
    if (shootProgress < 0.15f) {
        val norm = shootProgress / 0.15f
        val sx = w * 0.3f + norm * w * 0.4f
        val sy = h * 0.1f + norm * h * 0.2f
        drawLine(
            color = Color.White.copy(alpha = (1f - norm) * 0.7f),
            start = Offset(sx - 35f, sy - 20f),
            end = Offset(sx, sy),
            strokeWidth = 2f
        )
    }
}

private fun DrawScope.drawAutumnEnvironment(
    w: Float,
    h: Float,
    particles: List<ThemedParticle>,
    cycleTime: Float,
    breezeStrength: Float
) {
    val autumnColors = listOf(Color(0xFFD97C43), Color(0xFFE5A93B), Color(0xFFC0392B))

    for ((idx, p) in particles.withIndex()) {
        val progress = (p.initialYPercent + cycleTime * p.speed * 700f) % 1f
        val currentY = progress * h
        val wobble = sin((progress * 5f + p.xPercent * 7f).toDouble()).toFloat() * 20f
        val currentX = (p.xPercent * w + wobble + breezeStrength).coerceIn(0f, w)

        val col = autumnColors[idx % autumnColors.size]
        drawOval(
            color = col.copy(alpha = p.alpha * 0.65f),
            topLeft = Offset(currentX, currentY),
            size = Size(p.size * 1.6f, p.size * 1.0f)
        )
    }
}

private fun DrawScope.drawLavenderEnvironment(
    w: Float,
    h: Float,
    particles: List<ThemedParticle>,
    cycleTime: Float
) {
    val lavenderColor = Color(0xFF9B59B6).copy(alpha = 0.35f)

    // Lavender stalks swaying at the bottom
    val sway = sin(cycleTime * 6.28f * 0.8f) * 6f
    for (i in 0..12) {
        val stalkX = w * (i / 12f)
        drawLine(
            color = Color(0xFF558B2F).copy(alpha = 0.4f),
            start = Offset(stalkX, h),
            end = Offset(stalkX + sway, h - 55f),
            strokeWidth = 3f
        )
        drawCircle(
            color = lavenderColor,
            radius = 6f,
            center = Offset(stalkX + sway, h - 55f)
        )
    }

    // Floating gentle petals
    for (p in particles) {
        val progress = (p.initialYPercent + cycleTime * p.speed * 500f) % 1f
        drawCircle(
            color = lavenderColor.copy(alpha = p.alpha * 0.5f),
            radius = p.size * 0.5f,
            center = Offset(p.xPercent * w, progress * h)
        )
    }
}
