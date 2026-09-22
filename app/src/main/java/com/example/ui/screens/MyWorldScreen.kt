package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
import com.example.data.model.*
import com.example.ui.AlongViewModel
import com.example.ui.MyWorldDestination
import com.example.ui.components.CompanionVisual
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

private data class WorldModuleItem(
    val destination: MyWorldDestination,
    val title: String,
    val description: String,
    val iconEmoji: String
)

@Composable
fun MyWorldScreen(
    viewModel: AlongViewModel,
    onAddExpenseClick: () -> Unit,
    onAddGoalClick: () -> Unit,
    onAddHabitClick: () -> Unit,
    onAddPersonClick: () -> Unit,
    onAddTimeCapsuleClick: () -> Unit
) {
    val myWorldDest by viewModel.myWorldDestination.collectAsState()

    when (myWorldDest) {
        MyWorldDestination.MENU -> MyWorldMenuView(viewModel)
        MyWorldDestination.FINANCE -> FinanceModuleView(viewModel, onAddExpenseClick)
        MyWorldDestination.GOALS -> GoalsModuleView(viewModel, onAddGoalClick)
        MyWorldDestination.HABITS -> HabitsModuleView(viewModel, onAddHabitClick)
        MyWorldDestination.MOOD -> MoodModuleView(viewModel)
        MyWorldDestination.PEOPLE -> PeopleModuleView(viewModel, onAddPersonClick)
        MyWorldDestination.TIME_CAPSULES -> TimeCapsulesModuleView(viewModel, onAddTimeCapsuleClick)
        MyWorldDestination.SEARCH -> SearchModuleView(viewModel)
        MyWorldDestination.YEAR_IN_REVIEW -> YearInReviewModuleView(viewModel)
        MyWorldDestination.PEACE_MODE -> PeaceModeModuleView(viewModel)
        MyWorldDestination.VAULT -> VaultModuleView(viewModel)
        MyWorldDestination.PRIVACY_DASHBOARD -> PrivacyDashboardView(viewModel)
        MyWorldDestination.BACKUP_RESTORE -> BackupRestoreView(viewModel)
        MyWorldDestination.ABOUT_ALONG -> AboutAlongView(viewModel)
        MyWorldDestination.COMPANION_CUSTOMIZE -> CompanionCustomizeView(viewModel)
    }
}

@Composable
private fun MyWorldMenuView(viewModel: AlongViewModel) {
    val modules = listOf(
        WorldModuleItem(MyWorldDestination.FINANCE, "Personal Finance", "Income, expenses, cash flow & budget", "💰"),
        WorldModuleItem(MyWorldDestination.GOALS, "Goals & Milestones", "Long-term dreams & achievements", "🎯"),
        WorldModuleItem(MyWorldDestination.HABITS, "Habits & Routines", "Consistency without guilt", "🌱"),
        WorldModuleItem(MyWorldDestination.MOOD, "Mood & Peace", "Emotional trends & reflections", "💖"),
        WorldModuleItem(MyWorldDestination.PEOPLE, "Important People", "Relationships, birthdays & dates", "👥"),
        WorldModuleItem(MyWorldDestination.TIME_CAPSULES, "Time Capsules", "Letters to your future self", "⏳"),
        WorldModuleItem(MyWorldDestination.SEARCH, "Search Life", "Find memories, notes & moments", "🔍"),
        WorldModuleItem(MyWorldDestination.YEAR_IN_REVIEW, "Year in Review", "Reflect on accomplishments & growth", "📊"),
        WorldModuleItem(MyWorldDestination.PEACE_MODE, "Peace Mode", "Calm, minimal & uncluttered view", "🕊️"),
        WorldModuleItem(MyWorldDestination.VAULT, "Private Vault", "PIN-protected sacred space", "🔐"),
        WorldModuleItem(MyWorldDestination.PRIVACY_DASHBOARD, "Privacy & Companion Memory", "Full control over what Along remembers", "🛡️"),
        WorldModuleItem(MyWorldDestination.BACKUP_RESTORE, "Backup & Restore", "Encrypted .along export & import", "💾"),
        WorldModuleItem(MyWorldDestination.COMPANION_CUSTOMIZE, "Companion & Worlds", "Customize personality & peaceful themes", "🎨"),
        WorldModuleItem(MyWorldDestination.ABOUT_ALONG, "About Along", "Created by Mr. Aye Chan Maung", "🌿")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)) {
            Text(
                text = "My World",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Your private sanctuary for life, dreams, memories, and finance.",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.secondary)
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 280.dp),
            contentPadding = PaddingValues(bottom = 96.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(modules) { module ->
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .clickable { viewModel.navigateToMyWorld(module.destination) }
                        .testTag("menu_item_${module.destination.name.lowercase()}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = module.iconEmoji, fontSize = 22.sp)
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = module.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                text = module.description,
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 1. Finance Module View
// ==========================================
@Composable
private fun FinanceModuleView(viewModel: AlongViewModel, onAddExpenseClick: () -> Unit) {
    val summary by viewModel.financeSummary.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val accounts by viewModel.accounts.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        ModuleTopBar("Personal Finance 💰") { viewModel.navigateToMyWorld(MyWorldDestination.MENU) }

        LazyColumn(
            contentPadding = PaddingValues(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Net Flow Summary Card
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text("Net Cash Flow (This Period)", style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.primary))
                        Text(
                            text = "$%.2f".format(summary.netCashFlow),
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("Total Income", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.secondary))
                                Text("+$%.2f".format(summary.totalIncome), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                            }
                            Column {
                                Text("Total Expenses", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.secondary))
                                Text("-$%.2f".format(summary.totalExpenses), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                            }
                            Column {
                                Text("Total Balances", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.secondary))
                                Text("$%.2f".format(summary.totalAccountBalances), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                            }
                        }
                    }
                }
            }

            // Quick Add Transaction Button
            item {
                Button(
                    onClick = onAddExpenseClick,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Record Income / Expense")
                }
            }

            // Recent Transactions List
            item {
                Text("Recent Transactions", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }

            if (transactions.isEmpty()) {
                item {
                    Text("No transactions logged yet. Tap above to track your spending.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                items(transactions) { tx ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(tx.category, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                if (tx.note.isNotBlank()) {
                                    Text(tx.note, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val isIncome = tx.type == TransactionType.INCOME
                                Text(
                                    text = (if (isIncome) "+" else "-") + "$%.2f".format(tx.amount),
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isIncome) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(onClick = { viewModel.deleteTransaction(tx.id) }, modifier = Modifier.size(24.dp)) {
                                    Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Delete", modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 2. Goals Module View
// ==========================================
@Composable
private fun GoalsModuleView(viewModel: AlongViewModel, onAddGoalClick: () -> Unit) {
    val goals by viewModel.goals.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        ModuleTopBar("Goals & Milestones 🎯") { viewModel.navigateToMyWorld(MyWorldDestination.MENU) }

        Button(
            onClick = onAddGoalClick,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Create New Goal")
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            contentPadding = PaddingValues(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (goals.isEmpty()) {
                item {
                    Text("No goals yet. Set a peaceful target for your journey!", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                items(goals) { goal ->
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(goal.category.icon, fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(goal.title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                }
                                IconButton(onClick = { viewModel.deleteGoal(goal.id) }, modifier = Modifier.size(24.dp)) {
                                    Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Delete", modifier = Modifier.size(16.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            val progress = (goal.currentValue / goal.targetValue).toFloat().coerceIn(0f, 1f)
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp))
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "%.0f / %.0f %s".format(goal.currentValue, goal.targetValue, goal.unit),
                                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary)
                                )
                                Text(
                                    goal.status.displayName,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FilledTonalButton(
                                    onClick = { viewModel.updateGoalProgress(goal, goal.currentValue + 1.0) },
                                    modifier = Modifier.height(34.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp)
                                ) {
                                    Text("+1 Step")
                                }
                                if (goal.currentValue < goal.targetValue) {
                                    FilledTonalButton(
                                        onClick = { viewModel.updateGoalProgress(goal, goal.targetValue) },
                                        modifier = Modifier.height(34.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp)
                                    ) {
                                        Text("Mark Complete 🎉")
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

// ==========================================
// 3. Habits Module View (Consistency without guilt)
// ==========================================
@Composable
private fun HabitsModuleView(viewModel: AlongViewModel, onAddHabitClick: () -> Unit) {
    val habits by viewModel.habits.collectAsState()
    val logs by viewModel.habitLogs.collectAsState()
    val todayEpoch = remember { LocalDate.now().toEpochDay() }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        ModuleTopBar("Habits & Consistency 🌱") { viewModel.navigateToMyWorld(MyWorldDestination.MENU) }

        Text(
            text = "Gentle consistency over rigid perfection. Celebrate every day you show up.",
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.secondary)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onAddHabitClick,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Nurture a New Habit")
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            contentPadding = PaddingValues(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(habits) { habit ->
                val isDoneToday = logs.any { it.habitId == habit.id && it.epochDay == todayEpoch }
                val completedThisMonth = logs.count { it.habitId == habit.id }

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isDoneToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .clickable { viewModel.toggleHabitToday(habit.id) }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(if (isDoneToday) "✓" else habit.icon, fontSize = 22.sp)
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(habit.title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            Text(
                                "$completedThisMonth days completed (Target: ${habit.targetDaysPerMonth}/mo)",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.secondary)
                            )
                        }

                        IconButton(onClick = { viewModel.deleteHabit(habit.id) }) {
                            Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Delete", modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 4. Mood & Peace Module View
// ==========================================
@Composable
private fun MoodModuleView(viewModel: AlongViewModel) {
    val moments by viewModel.lifeMoments.collectAsState()
    val emotionsList = remember(moments) {
        moments.map { it.emotion }
    }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        ModuleTopBar("Mood & Peace 💖") { viewModel.navigateToMyWorld(MyWorldDestination.MENU) }

        Text(
            text = "Total Emotional Check-Ins: ${emotionsList.size}",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            contentPadding = PaddingValues(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            EmotionType.values().forEach { emotion ->
                val count = emotionsList.count { it == emotion }
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(emotion.emoji, fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(emotion.displayName, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium))
                            }
                            Text("$count times", style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary))
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 5. Important People Module View
// ==========================================
@Composable
private fun PeopleModuleView(viewModel: AlongViewModel, onAddPersonClick: () -> Unit) {
    val people by viewModel.importantPeople.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        ModuleTopBar("Important People 👥") { viewModel.navigateToMyWorld(MyWorldDestination.MENU) }

        Button(
            onClick = onAddPersonClick,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Important Person")
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            contentPadding = PaddingValues(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (people.isEmpty()) {
                item {
                    Text("No people recorded yet. Keep notes on family, mentors, and close friends.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                items(people) { person ->
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(person.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                Text(person.relationship, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary))
                                if (person.birthday.isNotBlank()) {
                                    Text("Birthday: ${person.birthday}", style = MaterialTheme.typography.bodySmall)
                                }
                                if (person.notes.isNotBlank()) {
                                    Text(person.notes, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                                }
                            }
                            IconButton(onClick = { viewModel.deletePerson(person.id) }) {
                                Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Delete", modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 6. Time Capsules Module View
// ==========================================
@Composable
private fun TimeCapsulesModuleView(viewModel: AlongViewModel, onAddTimeCapsuleClick: () -> Unit) {
    val capsules by viewModel.timeCapsules.collectAsState()
    val now = System.currentTimeMillis()

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        ModuleTopBar("Time Capsules ⏳") { viewModel.navigateToMyWorld(MyWorldDestination.MENU) }

        Text(
            text = "Seal a letter or memory for your future self to open on a meaningful date.",
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.secondary)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onAddTimeCapsuleClick,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Seal a New Time Capsule")
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            contentPadding = PaddingValues(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(capsules) { capsule ->
                val isUnlocked = now >= capsule.unlockEpochMillis || capsule.isOpened
                val dateStr = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(capsule.unlockEpochMillis))

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(capsule.title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            Text(if (isUnlocked) "Unlocked ✨" else "Locked 🔒", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.primary))
                        }
                        Text("Unlock Date: $dateStr", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.secondary))
                        Spacer(modifier = Modifier.height(8.dp))

                        if (isUnlocked) {
                            Text(capsule.message, style = MaterialTheme.typography.bodyMedium)
                        } else {
                            Text("This capsule is securely sealed until $dateStr. Be patient with yourself.", style = MaterialTheme.typography.bodySmall.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic))
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 7. Search Module View
// ==========================================
@Composable
private fun SearchModuleView(viewModel: AlongViewModel) {
    var query by remember { mutableStateOf("") }
    val moments by viewModel.lifeMoments.collectAsState()
    val searchResults = remember(moments, query) {
        if (query.isBlank()) emptyList()
        else moments.filter {
            it.title.contains(query, ignoreCase = true) ||
            it.description.contains(query, ignoreCase = true) ||
            it.tags.contains(query, ignoreCase = true)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        ModuleTopBar("Search Life 🔍") { viewModel.navigateToMyWorld(MyWorldDestination.MENU) }

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Search memories, tags, or thoughts...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            contentPadding = PaddingValues(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(searchResults) { moment ->
                LifeMomentCard(moment = moment, onDelete = { viewModel.deleteLifeMoment(moment.id) })
            }
        }
    }
}

// ==========================================
// 8. Year in Review Module View
// ==========================================
@Composable
private fun YearInReviewModuleView(viewModel: AlongViewModel) {
    val moments by viewModel.lifeMoments.collectAsState()
    val achievements by viewModel.achievements.collectAsState()
    val goals by viewModel.goals.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        ModuleTopBar("Year in Review 📊") { viewModel.navigateToMyWorld(MyWorldDestination.MENU) }

        LazyColumn(
            contentPadding = PaddingValues(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text("Looking Back Along Your Path 🌿", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${userProfile.preferredName}, you have preserved ${moments.size} moments, achieved ${achievements.size} milestones, and nurtured ${goals.size} life goals. Every gentle step counts.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            item {
                Text("Celebrated Milestones", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }

            items(achievements) { ach ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(ach.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        if (ach.description.isNotBlank()) {
                            Text(ach.description, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 9. Peace Mode Module View
// ==========================================
@Composable
private fun PeaceModeModuleView(viewModel: AlongViewModel) {
    val settings by viewModel.appSettings.collectAsState()
    val companion by viewModel.companionProfile.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CompanionVisual(name = companion.name, expression = CompanionExpression.PEACEFUL, size = 110.dp)
        Spacer(modifier = Modifier.height(24.dp))
        Text("Peace Mode 🕊️", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Declutter the world. No alerts, no pressure, just quiet presence.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { viewModel.togglePeaceMode() },
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(if (settings.peaceModeActive) "Turn Off Peace Mode" else "Enter Peace Mode 🌿")
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = { viewModel.navigateToMyWorld(MyWorldDestination.MENU) }) {
            Text("Back to My World")
        }
    }
}

// ==========================================
// 10. Private Vault Module View
// ==========================================
@Composable
private fun VaultModuleView(viewModel: AlongViewModel) {
    val unlocked by viewModel.vaultUnlocked.collectAsState()
    val moments by viewModel.lifeMoments.collectAsState()
    val privateMoments = remember(moments) { moments.filter { it.isPrivate } }
    var enteredPin by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        ModuleTopBar("Private Vault 🔐") { viewModel.navigateToMyWorld(MyWorldDestination.MENU) }

        if (!unlocked) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(54.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Enter Vault PIN", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                Text("Your private vault keeps sensitive thoughts protected.", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.secondary))
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = enteredPin,
                    onValueChange = { enteredPin = it },
                    label = { Text("4-digit PIN") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )
                if (pinError != null) {
                    Text(pinError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (viewModel.unlockVault(enteredPin)) {
                            pinError = null
                        } else {
                            pinError = "Incorrect PIN. Default is empty (tap unlock directly)."
                        }
                    },
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Unlock Vault")
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Vault Unlocked 🔓", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                OutlinedButton(onClick = { viewModel.lockVault() }) { Text("Lock Now") }
            }
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(
                contentPadding = PaddingValues(bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (privateMoments.isEmpty()) {
                    item { Text("No private moments stored. Mark any moment as 'Private' to secure it here.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                } else {
                    items(privateMoments) { moment ->
                        LifeMomentCard(moment = moment, onDelete = { viewModel.deleteLifeMoment(moment.id) })
                    }
                }
            }
        }
    }
}

// ==========================================
// 11. Privacy Dashboard & Companion Memory Control
// ==========================================
@Composable
private fun PrivacyDashboardView(viewModel: AlongViewModel) {
    val settings by viewModel.appSettings.collectAsState()

    var memMemories by remember(settings) { mutableStateOf(settings.companionCanRememberMemories) }
    var memJournal by remember(settings) { mutableStateOf(settings.companionCanRememberJournal) }
    var memMood by remember(settings) { mutableStateOf(settings.companionCanRememberMood) }
    var memGoals by remember(settings) { mutableStateOf(settings.companionCanRememberGoals) }
    var memSchedule by remember(settings) { mutableStateOf(settings.companionCanRememberSchedule) }
    var memFinance by remember(settings) { mutableStateOf(settings.companionCanRememberFinance) }
    var memPeople by remember(settings) { mutableStateOf(settings.companionCanRememberPeople) }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        ModuleTopBar("Privacy & Companion Memory 🛡️") { viewModel.navigateToMyWorld(MyWorldDestination.MENU) }

        LazyColumn(
            contentPadding = PaddingValues(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Zero Cloud • 100% Offline", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        Text(
                            "Along does not send your memories, finances, or personal data to external servers. Your world lives only on this device.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            item {
                Text("Companion Memory Toggles", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text("Choose what your companion is allowed to reference in conversation.", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.secondary))
            }

            item { MemoryToggleItem("Memories & Milestones", memMemories) { memMemories = it; saveToggles(viewModel, memMemories, memJournal, memMood, memGoals, memSchedule, memFinance, memPeople) } }
            item { MemoryToggleItem("Journal Reflections", memJournal) { memJournal = it; saveToggles(viewModel, memMemories, memJournal, memMood, memGoals, memSchedule, memFinance, memPeople) } }
            item { MemoryToggleItem("Mood Trends", memMood) { memMood = it; saveToggles(viewModel, memMemories, memJournal, memMood, memGoals, memSchedule, memFinance, memPeople) } }
            item { MemoryToggleItem("Goals & Milestones", memGoals) { memGoals = it; saveToggles(viewModel, memMemories, memJournal, memMood, memGoals, memSchedule, memFinance, memPeople) } }
            item { MemoryToggleItem("Daily Schedule", memSchedule) { memSchedule = it; saveToggles(viewModel, memMemories, memJournal, memMood, memGoals, memSchedule, memFinance, memPeople) } }
            item { MemoryToggleItem("Financial Records", memFinance) { memFinance = it; saveToggles(viewModel, memMemories, memJournal, memMood, memGoals, memSchedule, memFinance, memPeople) } }
            item { MemoryToggleItem("Important People", memPeople) { memPeople = it; saveToggles(viewModel, memMemories, memJournal, memMood, memGoals, memSchedule, memFinance, memPeople) } }
        }
    }
}

private fun saveToggles(
    viewModel: AlongViewModel,
    mem: Boolean, jrn: Boolean, mood: Boolean, goals: Boolean, sch: Boolean, fin: Boolean, ppl: Boolean
) {
    viewModel.updateCompanionMemoryPermissions(mem, jrn, mood, goals, sch, fin, ppl)
}

@Composable
private fun MemoryToggleItem(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Card(shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}

// ==========================================
// 12. Backup & Restore (.along format)
// ==========================================
@Composable
private fun BackupRestoreView(viewModel: AlongViewModel) {
    var exportPayload by remember { mutableStateOf("") }
    var importPayload by remember { mutableStateOf("") }
    val backupStatus by viewModel.backupStatus.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        ModuleTopBar("Backup & Restore 💾") { viewModel.navigateToMyWorld(MyWorldDestination.MENU) }

        LazyColumn(
            contentPadding = PaddingValues(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text("Export Complete World (.along)", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text("Generates an encrypted backup payload with integrity checksum.", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.secondary))
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { viewModel.exportBackup { exportPayload = it } },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Export .along Backup")
                }
                if (exportPayload.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = exportPayload,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Exported Backup Payload") },
                        modifier = Modifier.fillMaxWidth().height(140.dp),
                        shape = RoundedCornerShape(14.dp)
                    )
                }
            }

            item {
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                Text("Restore from .along Backup", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text("Paste your backup JSON below to restore your complete world.", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.secondary))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = importPayload,
                    onValueChange = { importPayload = it },
                    placeholder = { Text("Paste .along backup JSON here...") },
                    modifier = Modifier.fillMaxWidth().height(140.dp),
                    shape = RoundedCornerShape(14.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (importPayload.isNotBlank()) {
                            viewModel.restoreBackup(importPayload) {}
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Restore World")
                }

                if (backupStatus != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(backupStatus!!, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary))
                }
            }
        }
    }
}

// ==========================================
// 13. Companion & Peaceful Worlds Customization
// ==========================================
@Composable
private fun CompanionCustomizeView(viewModel: AlongViewModel) {
    val companion by viewModel.companionProfile.collectAsState()
    var name by remember(companion) { mutableStateOf(companion.name) }
    var relationship by remember(companion) { mutableStateOf(companion.relationshipStyle) }
    var preset by remember(companion) { mutableStateOf(companion.preset) }
    var playful by remember(companion) { mutableFloatStateOf(companion.playfulToSerious.toFloat()) }
    var gentle by remember(companion) { mutableFloatStateOf(companion.gentleToDirect.toFloat()) }
    var calm by remember(companion) { mutableFloatStateOf(companion.calmToEnergetic.toFloat()) }
    var quiet by remember(companion) { mutableFloatStateOf(companion.quietToTalkative.toFloat()) }
    var reflective by remember(companion) { mutableFloatStateOf(companion.reflectiveToMotivational.toFloat()) }
    var cute by remember(companion) { mutableFloatStateOf(companion.cuteToMature.toFloat()) }
    var theme by remember(companion) { mutableStateOf(companion.worldTheme) }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        ModuleTopBar("Customize Companion & Worlds 🎨") { viewModel.navigateToMyWorld(MyWorldDestination.MENU) }

        LazyColumn(
            contentPadding = PaddingValues(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CompanionVisual(name = name, expression = companion.currentExpression, size = 90.dp)
                }
            }

            item {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Companion Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )
            }

            item {
                Text("Peaceful World Environment", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(8.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.height(280.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(WorldTheme.values()) { wTheme ->
                        val isSelected = theme == wTheme
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                            ),
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
                            modifier = Modifier.clickable {
                                theme = wTheme
                                viewModel.updateWorldTheme(wTheme)
                            }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(wTheme.emoji, fontSize = 24.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(wTheme.title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                            }
                        }
                    }
                }
            }

            item {
                Text("Personality Sliders", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }

            item { PersonalitySliderRow("Playful", "Serious", playful) { playful = it } }
            item { PersonalitySliderRow("Gentle", "Direct", gentle) { gentle = it } }
            item { PersonalitySliderRow("Calm", "Energetic", calm) { calm = it } }
            item { PersonalitySliderRow("Quiet", "Talkative", quiet) { quiet = it } }
            item { PersonalitySliderRow("Reflective", "Motivational", reflective) { reflective = it } }
            item { PersonalitySliderRow("Cute", "Mature", cute) { cute = it } }

            item {
                Button(
                    onClick = {
                        viewModel.updateCompanionSettings(
                            name = name,
                            relationship = relationship,
                            preset = preset,
                            playfulToSerious = playful.toInt(),
                            gentleToDirect = gentle.toInt(),
                            calmToEnergetic = calm.toInt(),
                            quietToTalkative = quiet.toInt(),
                            reflectiveToMotivational = reflective.toInt(),
                            cuteToMature = cute.toInt(),
                            theme = theme
                        )
                    },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Save Companion Settings")
                }
            }
        }
    }
}

@Composable
private fun PersonalitySliderRow(leftLabel: String, rightLabel: String, value: Float, onValueChange: (Float) -> Unit) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(leftLabel, style = MaterialTheme.typography.labelSmall)
            Text(rightLabel, style = MaterialTheme.typography.labelSmall)
        }
        Slider(value = value, onValueChange = onValueChange, valueRange = 0f..100f)
    }
}

// ==========================================
// 14. About Along (Created by Mr. Aye Chan Maung from Myanmar)
// ==========================================
@Composable
private fun AboutAlongView(viewModel: AlongViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        ModuleTopBar("About Along 🌿") { viewModel.navigateToMyWorld(MyWorldDestination.MENU) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(80.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("🌿", fontSize = 40.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text("Along", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold))
            Text("Grow. Remember. Keep going.", style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary))
            Text("Walk through life, Along.", style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.secondary))

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Official Creator Credit",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Created by Mr. Aye Chan Maung from Myanmar",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Along is crafted as a peaceful, private digital world where a person can preserve their memories, record achievements and difficult moments, manage their goals and schedules, understand their personal finances, and interact with a customizable expressive digital companion that grows alongside their personal history.",
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Core Principles", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    Text("• Privacy First: Completely local on your device. No cloud surveillance.")
                    Text("• Offline First: Runs seamlessly without an internet connection.")
                    Text("• User Ownership: Full portable .along backup and restore.")
                    Text("• Calm Technology: Peaceful notifications, zero dopamine addiction loops.")
                    Text("• Companion-not-Robot: A living presence that respects your boundaries.")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ModuleTopBar(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}
