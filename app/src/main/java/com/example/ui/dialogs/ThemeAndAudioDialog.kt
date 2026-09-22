package com.example.ui.dialogs

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AppSettings
import com.example.data.model.CompanionExpression
import com.example.data.model.WorldTheme
import com.example.ui.components.CompanionVisual
import com.example.ui.components.PeacefulBackground

@Composable
fun ThemePreviewAndAudioDialog(
    currentSettings: AppSettings,
    companionName: String,
    onApplyTheme: (WorldTheme) -> Unit,
    onUpdateAudio: (musicOn: Boolean, ambientOn: Boolean, musicVol: Float, ambientVol: Float) -> Unit,
    onUpdateAnimationQuality: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var previewTheme by remember { mutableStateOf(currentSettings.selectedTheme) }

    var musicEnabled by remember { mutableStateOf(currentSettings.musicEnabled) }
    var ambientEnabled by remember { mutableStateOf(currentSettings.ambientSoundEnabled) }
    var musicVolume by remember { mutableStateOf(currentSettings.musicVolume) }
    var ambientVolume by remember { mutableStateOf(currentSettings.ambientVolume) }
    var animQuality by remember { mutableStateOf(currentSettings.animationQuality) }

    var activeTab by remember { mutableStateOf(0) } // 0 = Themes, 1 = Sound & Lo-Fi

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.92f)
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Theme & Atmosphere 🎨",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Living landscapes and peaceful soundscapes",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                // Tab Switcher
                TabRow(selectedTabIndex = activeTab) {
                    Tab(
                        selected = activeTab == 0,
                        onClick = { activeTab = 0 },
                        text = { Text("Themes") },
                        icon = { Icon(Icons.Default.Palette, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                    Tab(
                        selected = activeTab == 1,
                        onClick = { activeTab = 1 },
                        text = { Text("Sound & Lo-Fi") },
                        icon = { Icon(Icons.Default.MusicNote, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                }

                if (activeTab == 0) {
                    // ================= LIVE THEME PREVIEW VIEW =================
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Live Preview Window
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                PeacefulBackground(
                                    worldTheme = previewTheme,
                                    animationQuality = animQuality
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(14.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    CompanionVisual(
                                        name = companionName,
                                        expression = CompanionExpression.PEACEFUL,
                                        size = 64.dp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
                                    ) {
                                        Text(
                                            text = "${previewTheme.emoji} ${previewTheme.title}",
                                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Text(
                            text = "Select a theme to preview:",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Themes List
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(WorldTheme.values()) { theme ->
                                val isSelected = previewTheme == theme
                                Card(
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { previewTheme = theme }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Text(text = theme.emoji, fontSize = 24.sp)
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = theme.title,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                text = theme.description,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        if (currentSettings.selectedTheme == theme) {
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = "Active",
                                                    fontSize = 10.sp,
                                                    color = MaterialTheme.colorScheme.onPrimary,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Theme Actions: "Use This Theme" vs "Cancel"
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = onDismiss,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancel")
                            }
                            Button(
                                onClick = {
                                    onApplyTheme(previewTheme)
                                    onDismiss()
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1.4f)
                            ) {
                                Text("Use This Theme")
                            }
                        }
                    }
                } else {
                    // ================= SOUND & LO-FI CONTROLS =================
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Music Control Card
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Text("🎹", fontSize = 22.sp)
                                        Column {
                                            Text("Peaceful Lo-Fi Music", fontWeight = FontWeight.Bold)
                                            Text(
                                                "Calm procedural chord progressions",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                    Switch(
                                        checked = musicEnabled,
                                        onCheckedChange = {
                                            musicEnabled = it
                                            onUpdateAudio(musicEnabled, ambientEnabled, musicVolume, ambientVolume)
                                        }
                                    )
                                }

                                if (musicEnabled) {
                                    Text("Music Volume: ${(musicVolume * 100).toInt()}%", fontSize = 12.sp)
                                    Slider(
                                        value = musicVolume,
                                        onValueChange = {
                                            musicVolume = it
                                            onUpdateAudio(musicEnabled, ambientEnabled, musicVolume, ambientVolume)
                                        }
                                    )
                                }
                            }
                        }

                        // Ambient Soundscape Card
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Text("🍃", fontSize = 22.sp)
                                        Column {
                                            Text("Nature Ambient Sound", fontWeight = FontWeight.Bold)
                                            Text(
                                                "Theme soundscape (Rain, Ocean, Forest, Hearth)",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                    Switch(
                                        checked = ambientEnabled,
                                        onCheckedChange = {
                                            ambientEnabled = it
                                            onUpdateAudio(musicEnabled, ambientEnabled, musicVolume, ambientVolume)
                                        }
                                    )
                                }

                                if (ambientEnabled) {
                                    Text("Ambient Volume: ${(ambientVolume * 100).toInt()}%", fontSize = 12.sp)
                                    Slider(
                                        value = ambientVolume,
                                        onValueChange = {
                                            ambientVolume = it
                                            onUpdateAudio(musicEnabled, ambientEnabled, musicVolume, ambientVolume)
                                        }
                                    )
                                }
                            }
                        }

                        // Animation Quality Settings (Full, Reduced, Minimal)
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("Animation Quality & Battery", fontWeight = FontWeight.Bold)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf("Full", "Reduced", "Minimal").forEach { q ->
                                        FilterChip(
                                            selected = animQuality == q,
                                            onClick = {
                                                animQuality = q
                                                onUpdateAnimationQuality(q)
                                            },
                                            label = { Text(q) },
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Done")
                        }
                    }
                }
            }
        }
    }
}
