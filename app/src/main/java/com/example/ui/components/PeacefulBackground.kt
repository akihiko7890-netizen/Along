package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.data.model.WorldTheme
import kotlin.random.Random

private data class Particle(
    val xPercent: Float,
    val initialYPercent: Float,
    val radius: Float,
    val speed: Float,
    val alpha: Float
)

@Composable
fun PeacefulBackground(
    worldTheme: WorldTheme,
    modifier: Modifier = Modifier
) {
    val particles = remember(worldTheme) {
        val count = 20
        val rnd = Random(worldTheme.ordinal)
        List(count) {
            Particle(
                xPercent = rnd.nextFloat(),
                initialYPercent = rnd.nextFloat(),
                radius = rnd.nextFloat() * 4f + 2f,
                speed = rnd.nextFloat() * 0.0003f + 0.0001f,
                alpha = rnd.nextFloat() * 0.25f + 0.15f
            )
        }
    }

    val particleColor = when (worldTheme) {
        WorldTheme.SAKURA_GARDEN -> Color(0xFFF0A8B8)
        WorldTheme.FOREST_MORNING -> Color(0xFF6B9B78)
        WorldTheme.QUIET_OCEAN -> Color(0xFF66A6C2)
        WorldTheme.CLOUD_DREAM -> Color(0xFF9FB2D4)
        WorldTheme.COZY_ROOM -> Color(0xFFD49E74)
        WorldTheme.MOONLIT_NIGHT -> Color(0xFF8B9CC4)
        WorldTheme.STARRY_SKY -> Color(0xFFE2E7FF)
        WorldTheme.COZY_AUTUMN -> Color(0xFFD97C43)
        WorldTheme.RAINY_WINDOW -> Color(0xFF759CB0)
        WorldTheme.LAVENDER_FIELD -> Color(0xFFA58DCC)
    }

    val infiniteTransition = rememberInfiniteTransition(label = "ambient_anim")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ambient_time"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        for (p in particles) {
            val progress = (p.initialYPercent + time * p.speed * 1000f) % 1f
            val currentY = progress * height
            val wobble = kotlin.math.sin((progress * 6.28f + p.xPercent * 10f).toDouble()).toFloat() * 16f
            val currentX = (p.xPercent * width + wobble).coerceIn(0f, width)

            drawCircle(
                color = particleColor.copy(alpha = p.alpha),
                radius = p.radius,
                center = Offset(currentX, currentY)
            )
        }
    }
}
