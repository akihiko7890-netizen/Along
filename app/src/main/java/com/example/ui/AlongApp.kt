package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.LifeMomentCategory
import com.example.ui.components.AlongTopBar
import com.example.ui.components.PeacefulBackground
import com.example.ui.dialogs.*
import com.example.ui.screens.*
import com.example.ui.theme.AlongTheme

@Composable
fun AlongApp(
    viewModel: AlongViewModel = viewModel()
) {
    val settings by viewModel.appSettings.collectAsState()
    val companion by viewModel.companionProfile.collectAsState()
    val currentDest by viewModel.currentDestination.collectAsState()

    // Dialog state
    var showAddMomentDialog by remember { mutableStateOf(false) }
    var addMomentPresetCategory by remember { mutableStateOf(LifeMomentCategory.MEMORY) }
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var showAddExpenseDialog by remember { mutableStateOf(false) }
    var showAddGoalDialog by remember { mutableStateOf(false) }
    var showAddHabitDialog by remember { mutableStateOf(false) }
    var showAddPersonDialog by remember { mutableStateOf(false) }
    var showAddTimeCapsuleDialog by remember { mutableStateOf(false) }

    AlongTheme(worldTheme = settings.selectedTheme) {
        if (!settings.hasOnboarded) {
            OnboardingScreen(
                onComplete = { userName, compName, rel, preset, theme ->
                    viewModel.completeOnboarding(userName, compName, rel, preset, theme)
                },
                onRestoreBackup = { backupJson ->
                    viewModel.restoreBackup(backupJson) { success ->
                        if (success) {
                            viewModel.completeOnboarding("Traveler", "Sprout", companion.relationshipStyle, "Cozy Friend", settings.selectedTheme)
                        }
                    }
                }
            )
        } else {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val isTablet = maxWidth >= 600.dp

                if (isTablet) {
                    // --- Tablet / Expanded Layout with Navigation Rail ---
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.systemBars)
                    ) {
                        NavigationRail(
                            header = {
                                Column(
                                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(vertical = 12.dp)
                                ) {
                                    Surface(
                                        shape = androidx.compose.foundation.shape.CircleShape,
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        modifier = Modifier.size(44.dp)
                                    ) {
                                        Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
                                            Text(text = "🌿", fontSize = 20.sp)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Along",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "Aye Chan Maung",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                            },
                            modifier = Modifier.testTag("along_navigation_rail")
                        ) {
                            Column(
                                modifier = Modifier.fillMaxHeight(),
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                            ) {
                                Column {
                                    MainDestination.values().forEach { dest ->
                                        val isSelected = currentDest == dest
                                        NavigationRailItem(
                                            selected = isSelected,
                                            onClick = { viewModel.navigateTo(dest) },
                                            icon = {
                                                Text(
                                                    text = dest.iconEmoji,
                                                    fontSize = if (isSelected) 22.sp else 18.sp
                                                )
                                            },
                                            label = {
                                                Text(
                                                    text = dest.title,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                    fontSize = 11.sp
                                                )
                                            },
                                            modifier = Modifier.testTag("nav_rail_${dest.name.lowercase()}")
                                        )
                                    }
                                }

                                // Bottom Rail quick utilities
                                Column(
                                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(bottom = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    IconButton(
                                        onClick = { viewModel.navigateToMyWorld(MyWorldDestination.PEACE_MODE) },
                                        modifier = Modifier.testTag("rail_peace_mode_btn")
                                    ) {
                                        Text(text = "🕊️", fontSize = 20.sp)
                                    }
                                    IconButton(
                                        onClick = { viewModel.navigateToMyWorld(MyWorldDestination.VAULT) },
                                        modifier = Modifier.testTag("rail_vault_btn")
                                    ) {
                                        Text(text = "🔐", fontSize = 20.sp)
                                    }
                                }
                            }
                        }

                        // Right Pane Content
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            PeacefulBackground(
                                worldTheme = settings.selectedTheme,
                                animationQuality = settings.animationQuality
                            )

                            Column(modifier = Modifier.fillMaxSize()) {
                                // Tablet Top Bar Header
                                Surface(
                                    tonalElevation = 2.dp,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 24.dp, vertical = 12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Text(
                                                text = "${currentDest.iconEmoji} ${currentDest.title}",
                                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                                            )
                                            Text(
                                                text = "• ${settings.selectedTheme.emoji} ${settings.selectedTheme.title}",
                                                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
                                            )
                                        }

                                        Row(
                                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            FilledTonalButton(
                                                onClick = { viewModel.navigateToMyWorld(MyWorldDestination.PEACE_MODE) },
                                                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                            ) {
                                                Text("🕊️ Peace Mode", fontSize = 12.sp)
                                            }
                                            FilledTonalButton(
                                                onClick = { viewModel.navigateToMyWorld(MyWorldDestination.VAULT) },
                                                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                            ) {
                                                Text("🔐 Vault", fontSize = 12.sp)
                                            }
                                        }
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .weight(1f)
                                ) {
                                    AnimatedContent(
                                        targetState = currentDest,
                                        label = "tablet_nav_transition"
                                    ) { target ->
                                        when (target) {
                                            MainDestination.HOME -> HomeScreen(
                                                viewModel = viewModel,
                                                onTalkClick = { viewModel.navigateTo(MainDestination.COMPANION) },
                                                onAddMomentClick = { preset ->
                                                    addMomentPresetCategory = preset
                                                    showAddMomentDialog = true
                                                },
                                                onAddTaskClick = { showAddTaskDialog = true },
                                                onAddExpenseClick = { showAddExpenseDialog = true },
                                                onAddGoalClick = { showAddGoalDialog = true }
                                            )

                                            MainDestination.COMPANION -> CompanionScreen(
                                                viewModel = viewModel,
                                                onCustomizeCompanionClick = { viewModel.navigateToMyWorld(MyWorldDestination.COMPANION_CUSTOMIZE) }
                                            )

                                            MainDestination.TIMELINE -> TimelineScreen(
                                                viewModel = viewModel,
                                                onAddMomentClick = {
                                                    addMomentPresetCategory = LifeMomentCategory.MEMORY
                                                    showAddMomentDialog = true
                                                }
                                            )

                                            MainDestination.PLANNER -> PlannerScreen(
                                                viewModel = viewModel,
                                                onAddTaskClick = { showAddTaskDialog = true }
                                            )

                                            MainDestination.MY_WORLD -> MyWorldScreen(
                                                viewModel = viewModel,
                                                onAddExpenseClick = { showAddExpenseDialog = true },
                                                onAddGoalClick = { showAddGoalDialog = true },
                                                onAddHabitClick = { showAddHabitDialog = true },
                                                onAddPersonClick = { showAddPersonDialog = true },
                                                onAddTimeCapsuleClick = { showAddTimeCapsuleDialog = true }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // --- Phone / Compact Layout with Bottom Bar ---
                    Scaffold(
                        contentWindowInsets = WindowInsets.systemBars,
                        topBar = {
                            AlongTopBar(
                                currentTheme = settings.selectedTheme,
                                onPeaceModeClick = { viewModel.navigateToMyWorld(MyWorldDestination.PEACE_MODE) },
                                onVaultClick = { viewModel.navigateToMyWorld(MyWorldDestination.VAULT) }
                            )
                        },
                        bottomBar = {
                            NavigationBar(
                                tonalElevation = 6.dp,
                                modifier = Modifier.testTag("along_bottom_nav")
                            ) {
                                MainDestination.values().forEach { dest ->
                                    val isSelected = currentDest == dest
                                    NavigationBarItem(
                                        selected = isSelected,
                                        onClick = { viewModel.navigateTo(dest) },
                                        icon = {
                                            Text(
                                                text = dest.iconEmoji,
                                                fontSize = if (isSelected) 22.sp else 18.sp
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = dest.title,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                fontSize = 11.sp
                                            )
                                        },
                                        modifier = Modifier.testTag("nav_${dest.name.lowercase()}")
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .consumeWindowInsets(innerPadding)
                        ) {
                            // Ambient peaceful canvas
                            PeacefulBackground(
                                worldTheme = settings.selectedTheme,
                                animationQuality = settings.animationQuality
                            )

                            // Destination routing
                            AnimatedContent(
                                targetState = currentDest,
                                label = "main_nav_transition"
                            ) { target ->
                                when (target) {
                                    MainDestination.HOME -> HomeScreen(
                                        viewModel = viewModel,
                                        onTalkClick = { viewModel.navigateTo(MainDestination.COMPANION) },
                                        onAddMomentClick = { preset ->
                                            addMomentPresetCategory = preset
                                            showAddMomentDialog = true
                                        },
                                        onAddTaskClick = { showAddTaskDialog = true },
                                        onAddExpenseClick = { showAddExpenseDialog = true },
                                        onAddGoalClick = { showAddGoalDialog = true }
                                    )

                                    MainDestination.COMPANION -> CompanionScreen(
                                        viewModel = viewModel,
                                        onCustomizeCompanionClick = { viewModel.navigateToMyWorld(MyWorldDestination.COMPANION_CUSTOMIZE) }
                                    )

                                    MainDestination.TIMELINE -> TimelineScreen(
                                        viewModel = viewModel,
                                        onAddMomentClick = {
                                            addMomentPresetCategory = LifeMomentCategory.MEMORY
                                            showAddMomentDialog = true
                                        }
                                    )

                                    MainDestination.PLANNER -> PlannerScreen(
                                        viewModel = viewModel,
                                        onAddTaskClick = { showAddTaskDialog = true }
                                    )

                                    MainDestination.MY_WORLD -> MyWorldScreen(
                                        viewModel = viewModel,
                                        onAddExpenseClick = { showAddExpenseDialog = true },
                                        onAddGoalClick = { showAddGoalDialog = true },
                                        onAddHabitClick = { showAddHabitDialog = true },
                                        onAddPersonClick = { showAddPersonDialog = true },
                                        onAddTimeCapsuleClick = { showAddTimeCapsuleDialog = true }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Dialogs
            if (showAddMomentDialog) {
                AddLifeMomentDialog(
                    presetCategory = addMomentPresetCategory,
                    onDismiss = { showAddMomentDialog = false },
                    onConfirm = { title, desc, cat, emo, emoInt, imp, tags, fav, priv, sens, res ->
                        viewModel.addLifeMoment(title, desc, cat, emo, emoInt, imp, tags, fav, priv, sens, res)
                        showAddMomentDialog = false
                    }
                )
            }

            if (showAddTaskDialog) {
                AddPlannerItemDialog(
                    onDismiss = { showAddTaskDialog = false },
                    onConfirm = { title, type, day, time, priority, reminder, notes ->
                        viewModel.addPlannerItem(title, type, day, time, priority, reminder, notes)
                        showAddTaskDialog = false
                    }
                )
            }

            if (showAddExpenseDialog) {
                AddTransactionDialog(
                    onDismiss = { showAddExpenseDialog = false },
                    onConfirm = { type, amount, cat, note ->
                        viewModel.addTransaction(type, amount, cat, note)
                        showAddExpenseDialog = false
                    }
                )
            }

            if (showAddGoalDialog) {
                AddGoalDialog(
                    onDismiss = { showAddGoalDialog = false },
                    onConfirm = { title, cat, target, unit, notes ->
                        viewModel.addGoal(title, cat, target, unit, notes)
                        showAddGoalDialog = false
                    }
                )
            }

            if (showAddHabitDialog) {
                AddHabitDialog(
                    onDismiss = { showAddHabitDialog = false },
                    onConfirm = { title, targetDays, icon ->
                        viewModel.addHabit(title, targetDays, icon)
                        showAddHabitDialog = false
                    }
                )
            }

            if (showAddPersonDialog) {
                AddPersonDialog(
                    onDismiss = { showAddPersonDialog = false },
                    onConfirm = { name, rel, bday, notes ->
                        viewModel.addPerson(name, rel, bday, notes)
                        showAddPersonDialog = false
                    }
                )
            }

            if (showAddTimeCapsuleDialog) {
                AddTimeCapsuleDialog(
                    onDismiss = { showAddTimeCapsuleDialog = false },
                    onConfirm = { title, message, unlockMillis ->
                        viewModel.addTimeCapsule(title, message, unlockMillis)
                        showAddTimeCapsuleDialog = false
                    }
                )
            }
        }
    }
}
