package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CompanionExpression
import com.example.data.model.RelationshipStyle
import com.example.data.model.WorldTheme
import com.example.ui.components.CompanionVisual
import com.example.ui.components.PeacefulBackground

@Composable
fun OnboardingScreen(
    onComplete: (userName: String, companionName: String, relationship: RelationshipStyle, preset: String, theme: WorldTheme) -> Unit,
    onRestoreBackup: (String) -> Unit
) {
    var step by remember { mutableIntStateOf(0) } // 0 = Welcome, 1 = User Info, 2 = Companion Setup, 3 = Restore dialog
    var userName by remember { mutableStateOf("") }
    var userInterests by remember { mutableStateOf("Reading, nature walks, warm tea") }
    var companionName by remember { mutableStateOf("Sprout") }
    var selectedRelationship by remember { mutableStateOf(RelationshipStyle.CLOSE_COMPANION) }
    var selectedPreset by remember { mutableStateOf("Cozy Friend") }
    var selectedTheme by remember { mutableStateOf(WorldTheme.FOREST_MORNING) }
    var restoreJsonText by remember { mutableStateOf("") }
    var restoreError by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        PeacefulBackground(worldTheme = selectedTheme)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 560.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (step) {
                0 -> {
                    // Welcome Splash Experience
                    Spacer(modifier = Modifier.height(32.dp))
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(90.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "🌿", fontSize = 44.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Along",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 38.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = "Grow. Remember. Keep going.",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 17.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Your life. Your memories.\nSomeone to walk along with you.",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textAlign = TextAlign.Center,
                            lineHeight = 24.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Spacer(modifier = Modifier.height(48.dp))

                    Button(
                        onClick = { step = 1 },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("start_journey_button"),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Start My Journey 🌱", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { step = 3 },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("restore_journey_button"),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(imageVector = Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Restore Existing Journey", fontSize = 15.sp)
                    }

                    Spacer(modifier = Modifier.height(36.dp))
                    Text(
                        text = "Created by Mr. Aye Chan Maung from Myanmar",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            letterSpacing = 0.5.sp
                        )
                    )
                }

                1 -> {
                    // User Profile Setup
                    Text(
                        text = "A Peaceful Welcome 🌸",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "What would you like Along to call you?",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.secondary)
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = { Text("Your Preferred Name") },
                        placeholder = { Text("e.g. Maya, Chris, Traveler") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("onboarding_user_name_input"),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = userInterests,
                        onValueChange = { userInterests = it },
                        label = { Text("What brings you peace or joy? (Optional)") },
                        placeholder = { Text("Reading, tea, evening walks, acoustic music") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        minLines = 2
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { step = 2 },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("onboarding_step_1_next"),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Next: Meet Your Companion", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                    }
                }

                2 -> {
                    // Create Your Companion: Someone to walk beside you 🌱
                    Text(
                        text = "Someone to walk beside you 🌱",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "A gentle, private companion that grows with your life story.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.secondary),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    CompanionVisual(
                        name = companionName.ifBlank { "Sprout" },
                        expression = CompanionExpression.HAPPY,
                        size = 90.dp,
                        speechBubbleText = "Hello! I am ready to walk Along with you."
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = companionName,
                        onValueChange = { companionName = it },
                        label = { Text("Companion Name") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("onboarding_companion_name_input"),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Relationship Style:",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            RelationshipStyle.CLOSE_COMPANION,
                            RelationshipStyle.FRIEND,
                            RelationshipStyle.MENTOR,
                            RelationshipStyle.CALM_COMPANION
                        ).forEach { style ->
                            val isSelected = selectedRelationship == style
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedRelationship = style },
                                label = { Text(style.title, fontSize = 12.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Peaceful World Theme:",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            WorldTheme.FOREST_MORNING,
                            WorldTheme.SAKURA_GARDEN,
                            WorldTheme.QUIET_OCEAN,
                            WorldTheme.COZY_ROOM
                        ).forEach { theme ->
                            val isSelected = selectedTheme == theme
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedTheme = theme },
                                label = { Text("${theme.emoji} ${theme.title}", fontSize = 12.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Button(
                        onClick = {
                            onComplete(userName, companionName, selectedRelationship, selectedPreset, selectedTheme)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("onboarding_finish_button"),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Begin Our Journey", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                3 -> {
                    // Restore Existing Journey
                    Text(
                        text = "Restore Your Journey 🌿",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Paste the contents of your .along backup file to restore your memories, achievements, and companion state.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.secondary),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = restoreJsonText,
                        onValueChange = {
                            restoreJsonText = it
                            restoreError = null
                        },
                        label = { Text(".along Backup JSON") },
                        placeholder = { Text("{\"alongFormat\":\"v1.0\", ...}") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .testTag("restore_json_input"),
                        shape = RoundedCornerShape(14.dp)
                    )

                    if (restoreError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = restoreError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (restoreJsonText.isBlank()) {
                                restoreError = "Please paste your .along backup text."
                            } else {
                                onRestoreBackup(restoreJsonText)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_restore_button"),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Restore & Open Along", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(onClick = { step = 0 }) {
                        Text("Back to Welcome")
                    }
                }
            }
        }
    }
}
}
