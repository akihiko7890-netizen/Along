package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.data.model.WorldTheme

fun worldColorScheme(worldTheme: WorldTheme, isDark: Boolean): ColorScheme {
    return when (worldTheme) {
        WorldTheme.SAKURA_GARDEN -> if (isDark) {
            darkColorScheme(
                primary = SakuraPrimary,
                primaryContainer = Color(0xFF5A2534),
                secondary = Color(0xFFD89EA9),
                background = Color(0xFF1E1417),
                surface = Color(0xFF281C20),
                onPrimary = Color.White
            )
        } else {
            lightColorScheme(
                primary = SakuraPrimary,
                primaryContainer = SakuraContainer,
                secondary = Color(0xFF8F4C5E),
                background = SakuraBackground,
                surface = Color.White,
                onPrimary = Color.White
            )
        }

        WorldTheme.QUIET_OCEAN -> if (isDark) {
            darkColorScheme(
                primary = OceanPrimary,
                primaryContainer = Color(0xFF1D3B48),
                secondary = Color(0xFF8FBECF),
                background = Color(0xFF12191D),
                surface = Color(0xFF1A2328),
                onPrimary = Color.White
            )
        } else {
            lightColorScheme(
                primary = OceanPrimary,
                primaryContainer = OceanContainer,
                secondary = Color(0xFF254E60),
                background = OceanBackground,
                surface = Color.White,
                onPrimary = Color.White
            )
        }

        WorldTheme.MOONLIT_NIGHT -> darkColorScheme(
            primary = MoonlitPrimary,
            primaryContainer = MoonlitContainer,
            secondary = Color(0xFFA5B2D6),
            background = MoonlitBackground,
            surface = Color(0xFF1B2030),
            onPrimary = Color.White
        )

        WorldTheme.STARRY_SKY -> darkColorScheme(
            primary = StarryPrimary,
            primaryContainer = StarryContainer,
            secondary = Color(0xFFB5BFE2),
            background = StarryBackground,
            surface = Color(0xFF181B28),
            onPrimary = Color.White
        )

        WorldTheme.COZY_AUTUMN -> if (isDark) {
            darkColorScheme(
                primary = AutumnPrimary,
                primaryContainer = Color(0xFF5A2E14),
                secondary = Color(0xFFE2A17A),
                background = Color(0xFF1E1612),
                surface = Color(0xFF281F19),
                onPrimary = Color.White
            )
        } else {
            lightColorScheme(
                primary = AutumnPrimary,
                primaryContainer = AutumnContainer,
                secondary = Color(0xFF7A401D),
                background = AutumnBackground,
                surface = Color.White,
                onPrimary = Color.White
            )
        }

        WorldTheme.LAVENDER_FIELD -> if (isDark) {
            darkColorScheme(
                primary = LavenderPrimary,
                primaryContainer = Color(0xFF3F2E54),
                secondary = Color(0xFFBEA8DB),
                background = Color(0xFF17131D),
                surface = Color(0xFF221D2A),
                onPrimary = Color.White
            )
        } else {
            lightColorScheme(
                primary = LavenderPrimary,
                primaryContainer = LavenderContainer,
                secondary = Color(0xFF554173),
                background = LavenderBackground,
                surface = Color.White,
                onPrimary = Color.White
            )
        }

        WorldTheme.COZY_ROOM -> if (isDark) {
            darkColorScheme(
                primary = CozyPrimary,
                primaryContainer = Color(0xFF4C301B),
                secondary = Color(0xFFD6AB8A),
                background = Color(0xFF1C1713),
                surface = Color(0xFF27211C),
                onPrimary = Color.White
            )
        } else {
            lightColorScheme(
                primary = CozyPrimary,
                primaryContainer = CozyContainer,
                secondary = Color(0xFF6B4529),
                background = CozyBackground,
                surface = Color.White,
                onPrimary = Color.White
            )
        }

        WorldTheme.CLOUD_DREAM -> if (isDark) {
            darkColorScheme(
                primary = CloudPrimary,
                primaryContainer = Color(0xFF2F3A4F),
                secondary = Color(0xFFAAB6D3),
                background = Color(0xFF14171E),
                surface = Color(0xFF1C2029),
                onPrimary = Color.White
            )
        } else {
            lightColorScheme(
                primary = CloudPrimary,
                primaryContainer = CloudContainer,
                secondary = Color(0xFF43506B),
                background = CloudBackground,
                surface = Color.White,
                onPrimary = Color.White
            )
        }

        WorldTheme.RAINY_WINDOW -> if (isDark) {
            darkColorScheme(
                primary = RainyPrimary,
                primaryContainer = Color(0xFF283B45),
                secondary = Color(0xFF9CB8C8),
                background = Color(0xFF13181B),
                surface = Color(0xFF1B2328),
                onPrimary = Color.White
            )
        } else {
            lightColorScheme(
                primary = RainyPrimary,
                primaryContainer = RainyContainer,
                secondary = Color(0xFF374E5B),
                background = RainyBackground,
                surface = Color.White,
                onPrimary = Color.White
            )
        }

        // Forest Morning (Default)
        WorldTheme.FOREST_MORNING -> if (isDark) {
            darkColorScheme(
                primary = AlongLightSage,
                primaryContainer = Color(0xFF213B26),
                secondary = Color(0xFFA0C6AB),
                background = Color(0xFF131A14),
                surface = Color(0xFF1C241E),
                onPrimary = Color.White
            )
        } else {
            lightColorScheme(
                primary = ForestPrimary,
                primaryContainer = ForestContainer,
                secondary = AlongSage,
                background = ForestBackground,
                surface = Color.White,
                onPrimary = Color.White
            )
        }
    }
}

@Composable
fun AlongTheme(
    worldTheme: WorldTheme = WorldTheme.FOREST_MORNING,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = worldColorScheme(worldTheme, darkTheme)
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Backward compatibility alias for test suites
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    AlongTheme(worldTheme = WorldTheme.FOREST_MORNING, darkTheme = darkTheme, content = content)
}
