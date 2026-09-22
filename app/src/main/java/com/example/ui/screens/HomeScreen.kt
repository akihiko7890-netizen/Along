package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.AlongViewModel
import com.example.ui.components.CompanionVisual
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

@Composable
fun HomeScreen(
    viewModel: AlongViewModel,
    onTalkClick: () -> Unit,
    onAddMomentClick: (presetCategory: LifeMomentCategory) -> Unit,
    onAddTaskClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onAddGoalClick: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val companionProfile by viewModel.companionProfile.collectAsState()
    val resurfaceable by viewModel.resurfaceableMoments.collectAsState()
    val goals by viewModel.goals.collectAsState()
    val plannerItems by viewModel.plannerItems.collectAsState()
    val achievements by viewModel.achievements.collectAsState()

    val todayEpoch = remember { LocalDate.now().toEpochDay() }
    val todayPlannerItems = remember(plannerItems, todayEpoch) {
        plannerItems.filter { it.epochDay == todayEpoch }
    }

    val activeGoals = remember(goals) {
        goals.filter { it.status == GoalStatus.ACTIVE }
    }

    val recentAchievement = remember(achievements) {
        achievements.firstOrNull()
    }

    val positiveMemory = remember(resurfaceable) {
        resurfaceable.filter { it.isFavorite || it.emotion == EmotionType.PEACEFUL || it.emotion == EmotionType.VERY_HAPPY || it.emotion == EmotionType.GRATITUDE }
            .firstOrNull()
    }

    val currentDateStr = remember {
        SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault()).format(Date())
    }

    val greetingTime = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when (hour) {
            in 5..11 -> "Good morning"
            in 12..17 -> "Good afternoon"
            in 18..21 -> "Good evening"
            else -> "Peaceful night"
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isTablet = maxWidth >= 600.dp

        if (isTablet) {
            // ================= TABLET 2-PANE RESPONSIVE LAYOUT =================
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Left Pane: Companion & World + Mood & Quick Actions
                Column(
                    modifier = Modifier
                        .width(360.dp)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CompanionGreetingCard(
                        currentDateStr = currentDateStr,
                        greetingTime = greetingTime,
                        preferredName = userProfile.preferredName,
                        companionProfile = companionProfile,
                        companionSize = 130.dp
                    )

                    QuickActionsBar(
                        onTalkClick = onTalkClick,
                        onAddMomentClick = onAddMomentClick,
                        onAddTaskClick = onAddTaskClick,
                        onAddExpenseClick = onAddExpenseClick,
                        onAddGoalClick = onAddGoalClick
                    )

                    MoodCheckInCard(
                        viewModel = viewModel
                    )

                    CreatorAttributionSmallCard()
                }

                // Right Pane: Tasks, Memory, Goals, Achievements
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentPadding = PaddingValues(bottom = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        TodayScheduleCard(
                            todayPlannerItems = todayPlannerItems,
                            onAddTaskClick = onAddTaskClick,
                            onToggleComplete = { viewModel.togglePlannerItemCompleted(it) }
                        )
                    }

                    if (positiveMemory != null) {
                        item {
                            PositiveMemoryCard(memory = positiveMemory)
                        }
                    }

                    item {
                        ActiveGoalsCard(
                            activeGoals = activeGoals,
                            onAddGoalClick = onAddGoalClick
                        )
                    }

                    if (recentAchievement != null) {
                        item {
                            RecentAchievementCard(achievement = recentAchievement)
                        }
                    }
                }
            }
        } else {
            // ================= PHONE SINGLE-COLUMN LAYOUT =================
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    CompanionGreetingCard(
                        currentDateStr = currentDateStr,
                        greetingTime = greetingTime,
                        preferredName = userProfile.preferredName,
                        companionProfile = companionProfile,
                        companionSize = 105.dp,
                        modifier = Modifier.widthIn(max = 540.dp)
                    )
                }

                item {
                    Box(modifier = Modifier.widthIn(max = 540.dp)) {
                        QuickActionsBar(
                            onTalkClick = onTalkClick,
                            onAddMomentClick = onAddMomentClick,
                            onAddTaskClick = onAddTaskClick,
                            onAddExpenseClick = onAddExpenseClick,
                            onAddGoalClick = onAddGoalClick
                        )
                    }
                }

                item {
                    Box(modifier = Modifier.widthIn(max = 540.dp)) {
                        MoodCheckInCard(viewModel = viewModel)
                    }
                }

                item {
                    Box(modifier = Modifier.widthIn(max = 540.dp)) {
                        TodayScheduleCard(
                            todayPlannerItems = todayPlannerItems,
                            onAddTaskClick = onAddTaskClick,
                            onToggleComplete = { viewModel.togglePlannerItemCompleted(it) }
                        )
                    }
                }

                if (positiveMemory != null) {
                    item {
                        Box(modifier = Modifier.widthIn(max = 540.dp)) {
                            PositiveMemoryCard(memory = positiveMemory)
                        }
                    }
                }

                item {
                    Box(modifier = Modifier.widthIn(max = 540.dp)) {
                        ActiveGoalsCard(
                            activeGoals = activeGoals,
                            onAddGoalClick = onAddGoalClick
                        )
                    }
                }

                if (recentAchievement != null) {
                    item {
                        Box(modifier = Modifier.widthIn(max = 540.dp)) {
                            RecentAchievementCard(achievement = recentAchievement)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CompanionGreetingCard(
    currentDateStr: String,
    greetingTime: String,
    preferredName: String,
    companionProfile: CompanionProfile,
    companionSize: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("home_companion_card")
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = currentDateStr,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$greetingTime, $preferredName 🌿",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            CompanionVisual(
                name = companionProfile.name,
                expression = companionProfile.currentExpression,
                size = companionSize,
                speechBubbleText = when (companionProfile.currentExpression) {
                    CompanionExpression.CELEBRATING -> "Celebrating with you today!"
                    CompanionExpression.SLEEPY -> "Night is quiet. Rest gently."
                    CompanionExpression.CONCERNED -> "I am right here with you."
                    CompanionExpression.THINKING -> "Reflecting on our journey..."
                    else -> "Walking gently along beside you."
                }
            )
        }
    }
}

@Composable
private fun QuickActionsBar(
    onTalkClick: () -> Unit,
    onAddMomentClick: (presetCategory: LifeMomentCategory) -> Unit,
    onAddTaskClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onAddGoalClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        QuickActionButton(
            icon = "💬",
            label = "Talk",
            tag = "quick_action_talk",
            onClick = onTalkClick
        )
        QuickActionButton(
            icon = "🌱",
            label = "Moment",
            tag = "quick_action_moment",
            onClick = { onAddMomentClick(LifeMomentCategory.MEMORY) }
        )
        QuickActionButton(
            icon = "🏆",
            label = "Achieve",
            tag = "quick_action_achieve",
            onClick = { onAddMomentClick(LifeMomentCategory.ACHIEVEMENT) }
        )
        QuickActionButton(
            icon = "✅",
            label = "Task",
            tag = "quick_action_task",
            onClick = onAddTaskClick
        )
        QuickActionButton(
            icon = "💳",
            label = "Expense",
            tag = "quick_action_expense",
            onClick = onAddExpenseClick
        )
        QuickActionButton(
            icon = "🎯",
            label = "Goal",
            tag = "quick_action_goal",
            onClick = onAddGoalClick
        )
    }
}

@Composable
private fun MoodCheckInCard(
    viewModel: AlongViewModel,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "How are you feeling right now?",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf(
                    EmotionType.PEACEFUL to CompanionExpression.PEACEFUL,
                    EmotionType.HAPPY to CompanionExpression.HAPPY,
                    EmotionType.GRATITUDE to CompanionExpression.PROUD,
                    EmotionType.TIRED to CompanionExpression.SLEEPY,
                    EmotionType.STRESSED to CompanionExpression.CONCERNED,
                    EmotionType.SAD to CompanionExpression.CONCERNED
                ).forEach { (emotion, expression) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                viewModel.setCompanionExpression(expression)
                                viewModel.addLifeMoment(
                                    title = "Checked in feeling ${emotion.displayName}",
                                    description = "Logged from daily peaceful mood check-in.",
                                    category = LifeMomentCategory.JOURNAL,
                                    emotion = emotion,
                                    emotionIntensity = 3,
                                    importance = 2,
                                    tags = "MoodCheckIn",
                                    isFavorite = false,
                                    isPrivate = false,
                                    isSensitive = false,
                                    allowResurfacing = false
                                )
                            }
                            .padding(horizontal = 4.dp, vertical = 6.dp)
                    ) {
                        Text(text = emotion.emoji, fontSize = 24.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = emotion.displayName,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TodayScheduleCard(
    todayPlannerItems: List<PlannerItem>,
    onAddTaskClick: () -> Unit,
    onToggleComplete: (PlannerItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Today's Schedule & Tasks",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                TextButton(onClick = onAddTaskClick) {
                    Text("+ Add")
                }
            }

            if (todayPlannerItems.isEmpty()) {
                Text(
                    text = "No tasks or events scheduled for today. Enjoy the calm! 🌸",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    todayPlannerItems.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = item.isCompleted,
                                onCheckedChange = { onToggleComplete(item) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Medium,
                                        textDecoration = if (item.isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                                    )
                                )
                                val timeStr = "%02d:%02d".format(item.timeMinutes / 60, item.timeMinutes % 60)
                                Text(
                                    text = "${item.type.displayName} • $timeStr",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PositiveMemoryCard(
    memory: LifeMoment,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("🌸", fontSize = 16.sp)
                Text(
                    text = "A Gentle Memory to Remember",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = memory.title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            if (memory.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = memory.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    maxLines = 3
                )
            }
        }
    }
}

@Composable
private fun ActiveGoalsCard(
    activeGoals: List<Goal>,
    onAddGoalClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Current Goals",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                TextButton(onClick = onAddGoalClick) {
                    Text("+ Add")
                }
            }

            if (activeGoals.isEmpty()) {
                Text(
                    text = "No active goals yet. What would you like to nurture?",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    activeGoals.take(3).forEach { goal ->
                        val progress = (goal.currentValue / goal.targetValue).toFloat().coerceIn(0f, 1f)
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${goal.category.icon} ${goal.title}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                                )
                                Text(
                                    text = "%.0f / %.0f %s".format(goal.currentValue, goal.targetValue, goal.unit),
                                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.primary)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentAchievementCard(
    achievement: LifeMoment,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.size(46.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "🏆", fontSize = 22.sp)
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Celebrated Moment",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = achievement.title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                if (achievement.description.isNotBlank()) {
                    Text(
                        text = achievement.description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        maxLines = 2
                    )
                }
            }
        }
    }
}

@Composable
private fun CreatorAttributionSmallCard() {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("🇲🇲", fontSize = 18.sp)
            Column {
                Text(
                    text = "Walk through life, Along.",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = "Created by Mr. Aye Chan Maung from Myanmar",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: String,
    label: String,
    tag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp, horizontal = 2.dp)
            .testTag(tag)
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(44.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = icon, fontSize = 20.sp)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}
