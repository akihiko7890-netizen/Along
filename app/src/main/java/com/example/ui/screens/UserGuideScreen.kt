package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AlongViewModel
import com.example.ui.MyWorldDestination

data class GuideTopic(
    val id: String,
    val title: String,
    val category: String,
    val iconEmoji: String,
    val summary: String,
    val steps: List<String>,
    val tips: List<String>,
    val privacyNote: String = "All related entries and records stay 100% on your device."
)

object GuideRepository {
    val topics = listOf(
        GuideTopic(
            id = "getting_started",
            title = "Getting Started with Along",
            category = "Basics",
            iconEmoji = "🌿",
            summary = "Along is your calm, private digital sanctuary where you can record life moments, reflect peacefully, plan your days, and spend time with a caring companion.",
            steps = listOf(
                "When you first launch Along, choose a name for your companion and select your relationship style.",
                "Explore the 5 main sections: Home, Companion, Timeline, Planner, and My World.",
                "Record your first life moment or journal entry to start growing your personal story.",
                "Everything you do is stored 100% offline and privately on your device."
            ),
            tips = listOf(
                "You can tap the top right sun/moon icon anytime to customize your environment theme.",
                "Along has zero advertisements, zero tracking, and never sells your data."
            )
        ),
        GuideTopic(
            id = "creating_companion",
            title = "Creating & Customizing Your Companion",
            category = "Companion",
            iconEmoji = "🌱",
            summary = "Your companion is a personalized presence designed to listen, remember milestones, and provide thoughtful companionship.",
            steps = listOf(
                "Go to Companion tab or My World → Companion Studio.",
                "Choose a name (like Sprout, Luna, or any name that feels cozy to you).",
                "Select a relationship style: Close Companion, Best Friend, Mentor, Coach, or Calm Presence.",
                "Adjust personality sliders: Gentle vs Direct, Calm vs Energetic, Quiet vs Talkative."
            ),
            tips = listOf(
                "You can change your companion's personality or style at any time without losing any conversation history.",
                "Your companion adapts its greeting based on the time of day and how you're feeling."
            )
        ),
        GuideTopic(
            id = "custom_companion_photos",
            title = "Custom Companion Photos & Avatars",
            category = "Companion",
            iconEmoji = "📸",
            summary = "Give your companion a unique visual face using custom avatar styles or photo picker.",
            steps = listOf(
                "Open Companion tab and tap the companion avatar or the 'Customize' button.",
                "Browse through crafted visual presets or upload a personal drawing or picture.",
                "Preview how it appears in both day and night themes before saving."
            ),
            tips = listOf(
                "Your photos never leave your device storage; they remain strictly private."
            )
        ),
        GuideTopic(
            id = "companion_mood_photos",
            title = "Companion Expressions & Moods",
            category = "Companion",
            iconEmoji = "✨",
            summary = "Your companion expresses 15+ organic emotions: Peaceful, Happy, Thinking, Concerned, Proud, and more.",
            steps = listOf(
                "Notice your companion's expression shift based on your reflections.",
                "When you share happy moments, your companion lights up with pride and excitement.",
                "During difficult times, your companion offers gentle, peaceful listening."
            ),
            tips = listOf(
                "Tap on the companion's expression badge in chat to manually preview different expressions."
            )
        ),
        GuideTopic(
            id = "talking_companion",
            title = "Talking With Your Companion",
            category = "Companion",
            iconEmoji = "💬",
            summary = "Have relaxed, supportive conversations without internet connection or external servers.",
            steps = listOf(
                "Go to the Companion tab.",
                "Type anything on your mind in the message input bar and tap Send.",
                "Use the Quick Suggestion buttons for instant check-ins like 'How are you?', 'Need to vent', or 'Tell me something nice'.",
                "Tap 'Why did you mention this?' on any companion memory card to inspect why it was brought up."
            ),
            tips = listOf(
                "Your companion's responses are completely offline and private."
            )
        ),
        GuideTopic(
            id = "memories",
            title = "Recording Life Memories",
            category = "Memories",
            iconEmoji = "📖",
            summary = "Capture precious life events, small joys, milestones, and meaningful lessons learned.",
            steps = listOf(
                "Tap the '+' button from Home or Timeline.",
                "Enter a title (e.g. 'Morning coffee in the garden') and write your thoughts.",
                "Select a category: Memory, Milestone, Celebration, Gratitude, or Lesson.",
                "Pick an emotion and emotion intensity (1 to 5).",
                "Tap 'Save Moment' to add it to your story."
            ),
            tips = listOf(
                "Mark difficult or sensitive moments as 'Sensitive' to ensure Along will never unexpectedly resurface them."
            )
        ),
        GuideTopic(
            id = "journal",
            title = "Daily Journaling & Reflections",
            category = "Memories",
            iconEmoji = "✍️",
            summary = "Express yourself freely in a safe, unjudged offline diary.",
            steps = listOf(
                "Go to Timeline and filter by 'Journal' or tap 'Add Moment' with Journal category.",
                "Write as little or as much as you want.",
                "Add tags to easily organize your writings by theme or project."
            ),
            tips = listOf(
                "Writing even one line a day helps relieve stress and build lasting mindfulness."
            )
        ),
        GuideTopic(
            id = "achievements",
            title = "Achievements & Milestones",
            category = "Memories",
            iconEmoji = "🏆",
            summary = "Celebrate small and large victories in your personal journey.",
            steps = listOf(
                "Add a moment with the 'Achievement' category.",
                "View your accomplishments anytime from Home → Celebrations card or Timeline → Achievements filter.",
                "Your companion will celebrate with you whenever you reach a new milestone!"
            ),
            tips = listOf(
                "Don't hesitate to record tiny wins—waking up early or drinking water counts!"
            )
        ),
        GuideTopic(
            id = "mood",
            title = "Mood Tracking & Emotional Trends",
            category = "Wellbeing",
            iconEmoji = "💖",
            summary = "Understand your emotional patterns with gentle mood tracking.",
            steps = listOf(
                "Open My World → Mood & Peace.",
                "Tap your current feeling from the emotion grid (Peaceful, Grateful, Stressed, etc.).",
                "View your emotional distribution chart and calming advice."
            ),
            tips = listOf(
                "Checking in with your feelings takes less than 10 seconds and promotes mental clarity."
            )
        ),
        GuideTopic(
            id = "timeline",
            title = "Your Personal Timeline",
            category = "Memories",
            iconEmoji = "🌿",
            summary = "A visual chronological tapestry of your life moments and memories.",
            steps = listOf(
                "Tap the Timeline tab at the bottom navigation.",
                "Browse through your history sorted by date.",
                "Use the category filter row to focus on Gratitude, Milestones, or Journal.",
                "Tap any moment card to view complete notes or delete."
            ),
            tips = listOf(
                "Favorite your most treasured memories with the heart icon to highlight them."
            )
        ),
        GuideTopic(
            id = "planner",
            title = "Day Planner & Tasks",
            category = "Productivity",
            iconEmoji = "📅",
            summary = "A calm schedule and task manager designed without guilt or overwhelming deadlines.",
            steps = listOf(
                "Tap the Planner tab.",
                "Select any date from the top calendar strip.",
                "Tap '+' to add a Task, Event, Meeting, Deadline, or Birthday.",
                "Check off tasks as you finish them with satisfying feedback."
            ),
            tips = listOf(
                "Unfinished tasks don't penalize you; they gracefully roll over without stress."
            )
        ),
        GuideTopic(
            id = "reminders",
            title = "Gentle Reminders & Notifications",
            category = "Productivity",
            iconEmoji = "🔔",
            summary = "Stay gently mindful of key priorities with kind, warm reminders.",
            steps = listOf(
                "When creating a planner task or event, choose a reminder time (e.g. 15 or 30 min before).",
                "In My World → Settings, customize the notification personality (Warm, Simple, or Minimal)."
            ),
            tips = listOf(
                "Notifications only run locally on your device; no background battery drain."
            )
        ),
        GuideTopic(
            id = "goals",
            title = "Goals & Dreams",
            category = "Productivity",
            iconEmoji = "🎯",
            summary = "Track your meaningful long-term goals step-by-step.",
            steps = listOf(
                "Go to My World → Goals & Milestones.",
                "Tap '+ New Goal' and choose a category (Health, Study, Career, Personal).",
                "Set a target value (e.g., 100 pages, 50 hours, 100%).",
                "Update your current progress anytime with the slider or +/- buttons."
            ),
            tips = listOf(
                "Breaking big goals into tiny increments makes them feel achievable."
            )
        ),
        GuideTopic(
            id = "habits",
            title = "Habits & Consistency",
            category = "Productivity",
            iconEmoji = "🌱",
            summary = "Build peaceful routines without harmful guilt streaks.",
            steps = listOf(
                "Go to My World → Habits & Routines.",
                "Tap '+ New Habit' (e.g. Morning Stretch, Reading, Drinking Water).",
                "Set a monthly target (e.g. 20 days per month).",
                "Tap the checkmark on any day you complete the habit."
            ),
            tips = listOf(
                "Along focuses on monthly consistency rather than strict consecutive day streaks."
            )
        ),
        GuideTopic(
            id = "finance",
            title = "Personal Finance & Budgets",
            category = "Finance",
            iconEmoji = "💰",
            summary = "Track cash flow, accounts, and monthly budgets with 100% privacy.",
            steps = listOf(
                "Go to My World → Personal Finance.",
                "View your total net balance across Cash, Checking, and Savings accounts.",
                "Tap '+ Expense' or '+ Income' to log transactions.",
                "Set monthly category budgets (Food, Study, Utilities) to see remaining limits."
            ),
            tips = listOf(
                "Along never links to real bank APIs. Your financial logs stay purely private on your phone."
            )
        ),
        GuideTopic(
            id = "important_people",
            title = "Important People & Connections",
            category = "Relationships",
            iconEmoji = "🤝",
            summary = "Keep track of the cherished people in your life, their birthdays, and gift ideas.",
            steps = listOf(
                "Go to My World → Important People.",
                "Tap '+ Add Person' to record a friend, family member, or mentor.",
                "Add their birthday, relationship style, and memorable notes."
            ),
            tips = listOf(
                "Your companion can remember their birthdays and remind you gently."
            )
        ),
        GuideTopic(
            id = "birthdays",
            title = "Birthdays & Special Anniversaries",
            category = "Relationships",
            iconEmoji = "🎂",
            summary = "Never forget a loved one's special day.",
            steps = listOf(
                "Add a birthday to any person in Important People or add a Birthday planner item.",
                "Upcoming birthdays show up on your Home screen and Planner."
            ),
            tips = listOf(
                "Add a gift idea or favorite flower in their notes so you are always prepared."
            )
        ),
        GuideTopic(
            id = "time_capsules",
            title = "Future Time Capsules",
            category = "Memories",
            iconEmoji = "⏳",
            summary = "Write letters to your future self that remain securely locked until a set date.",
            steps = listOf(
                "Go to My World → Time Capsules.",
                "Tap '+ New Capsule', write a message and choose an unlock date in the future.",
                "The capsule stays safely sealed until the appointed date arrives."
            ),
            tips = listOf(
                "Opening a capsule one year later is a deeply moving experience of self-growth."
            )
        ),
        GuideTopic(
            id = "peace_mode",
            title = "Peace Mode Sanctuary",
            category = "Wellbeing",
            iconEmoji = "🌿",
            summary = "A distraction-free Zen retreat for calm breathing and quiet focus.",
            steps = listOf(
                "Tap the top-right leaf icon or go to My World → Peace Mode.",
                "Sit with your companion in your chosen theme environment with gentle breeze or rain.",
                "Use the guided breathing circle (Breathe In, Hold, Breathe Out).",
                "Choose to Talk, Write, or simply 'Just Stay Here'."
            ),
            tips = listOf(
                "Turn on Theme Music & Ambient Sound in Audio Settings for a complete sensory retreat."
            )
        ),
        GuideTopic(
            id = "focus_mode",
            title = "Focus & Quiet Timer",
            category = "Wellbeing",
            iconEmoji = "⏳",
            summary = "Gentle pomodoro-style quiet sessions to work or study peacefully.",
            steps = listOf(
                "Start a Peace Mode session and set your timer duration (15, 25, or 45 mins).",
                "Along silences all non-essential prompts so you can immerse in reading or writing."
            ),
            tips = listOf(
                "Pair with the Forest Morning or Rainy Window theme for deep concentration."
            )
        ),
        GuideTopic(
            id = "games",
            title = "Companion Game Room & Chess",
            category = "Play",
            iconEmoji = "🎮",
            summary = "Relax and play cozy offline games with your companion or a friend on the same device.",
            steps = listOf(
                "Go to Companion tab and tap 'Play Together 🎮' or My World → Game Room.",
                "Choose Chess, Tic-Tac-Toe, Connect Four, Memory Match, Doodle Canvas, and more.",
                "For Chess, select 'You vs Companion' (with Relaxed, Easy, Normal, or Hard difficulty) or 'Two Players on This Device'.",
                "Watch your companion make live playful facial expressions and comments as you play!"
            ),
            tips = listOf(
                "Turn Companion Reactions ON/OFF using the game controls toggle.",
                "All games work 100% offline with zero advertisements or competitive stress."
            )
        ),
        GuideTopic(
            id = "themes",
            title = "Themes & Immersive Environments",
            category = "Customization",
            iconEmoji = "🎨",
            summary = "Transform Along into 10 unique living landscapes with animated particles and color palettes.",
            steps = listOf(
                "Go to My World → Theme & Atmosphere or tap the sun/moon icon at the top of the screen.",
                "Tap any theme to see a live preview of its animation, companion environment, and color palette.",
                "Tap 'Use This Theme' to apply it, or 'Cancel' to keep your current theme."
            ),
            tips = listOf(
                "Available themes: Sakura Garden 🌸, Forest Morning 🌿, Quiet Ocean 🌊, Cloud Dream ☁️, Cozy Room 🏡, Moonlit Night 🌙, Starry Sky ⭐, Cozy Autumn 🍂, Rainy Window 🌧️, Lavender Field 💜.",
                "Themes feature gentle falling petals, drifting clouds, swaying leaves, and rain droplets."
            )
        ),
        GuideTopic(
            id = "music_sounds",
            title = "Peaceful Lo-Fi & Nature Soundscapes",
            category = "Customization",
            iconEmoji = "🎵",
            summary = "Optional procedural Lo-Fi chords and soothing nature ambience.",
            steps = listOf(
                "Open the audio panel from My World → Theme & Atmosphere or the Top Bar audio icon.",
                "Toggle Music ON/OFF or Ambient Sound ON/OFF (OFF by default to respect your quiet time).",
                "Adjust Music Volume and Ambient Volume independently.",
                "Mix combinations: Lo-Fi + Rain, Lo-Fi + Ocean Waves, Lo-Fi + Forest, or Lo-Fi + Fireplace."
            ),
            tips = listOf(
                "All music and sounds are generated 100% procedurally on your device—zero copyright, zero internet required!"
            )
        ),
        GuideTopic(
            id = "private_vault",
            title = "Private Lockable Vault",
            category = "Privacy",
            iconEmoji = "🔒",
            summary = "Protect your sensitive journal entries and moments behind a secure 4-digit PIN.",
            steps = listOf(
                "Go to My World → Private Vault or tap the lock icon in the top bar.",
                "Set a private 4-digit PIN.",
                "Any moment or journal entry marked 'Private' is hidden until you enter your PIN."
            ),
            tips = listOf(
                "Lock the vault immediately with one tap from the top bar before letting someone hold your phone."
            )
        ),
        GuideTopic(
            id = "privacy",
            title = "Privacy & Local-First Security",
            category = "Privacy",
            iconEmoji = "🛡️",
            summary = "Your data belongs exclusively to you. Along never transmits your entries anywhere.",
            steps = listOf(
                "Inspect your data anytime at My World → Privacy Dashboard.",
                "Review permissions: along requires zero unnecessary permissions.",
                "Toggle granular companion memory access for Memories, Journal, Finance, or Schedule."
            ),
            tips = listOf(
                "You can turn off companion memory for any specific area in the Privacy Dashboard."
            )
        ),
        GuideTopic(
            id = "backup",
            title = "Creating an Encrypted Offline Backup",
            category = "Data",
            iconEmoji = "💾",
            summary = "Export a complete backup of your journey to your phone's storage.",
            steps = listOf(
                "Go to My World → Backup & Restore.",
                "Tap 'Create Backup' to generate a single offline backup file.",
                "Save the file to your Google Drive, USB drive, or computer."
            ),
            tips = listOf(
                "We recommend creating a backup once a month or before upgrading your phone."
            )
        ),
        GuideTopic(
            id = "restore",
            title = "Restoring Your Journey to a New Phone",
            category = "Data",
            iconEmoji = "📦",
            summary = "Seamlessly transfer all your memories, companion, and settings to a new device.",
            steps = listOf(
                "Install Along on your new phone.",
                "Copy your backup file to the new phone.",
                "Open My World → Backup & Restore, tap 'Restore Journey', and select your backup file.",
                "Along will restore all your moments, goals, companion conversations, and settings instantly."
            ),
            tips = listOf(
                "Restoring replaces the existing local data with your backup file."
            )
        ),
        GuideTopic(
            id = "search",
            title = "Searching Your Entire Journey",
            category = "Basics",
            iconEmoji = "🔍",
            summary = "Quickly search across memories, journal entries, goals, planner items, and finance.",
            steps = listOf(
                "Go to My World → Universal Search.",
                "Type any keyword (e.g. 'coffee', 'birthday', 'paris', 'project').",
                "Results are grouped by category with direct links."
            ),
            tips = listOf(
                "Search works instantaneously because your database is entirely local."
            )
        ),
        GuideTopic(
            id = "accessibility",
            title = "Accessibility & Responsive Scaling",
            category = "Settings",
            iconEmoji = "👁️",
            summary = "Along is designed to be accessible for everyone with full dynamic scaling.",
            steps = listOf(
                "Along supports Android Large Font scaling without clipping buttons or cutting off text.",
                "All interactive elements have comfortable 48dp+ touch targets.",
                "Reduce animations under My World → Settings → Animation Quality (Full, Reduced, or Minimal)."
            ),
            tips = listOf(
                "High-contrast color modes are supported automatically with your device's Dark Mode."
            )
        ),
        GuideTopic(
            id = "troubleshooting",
            title = "Troubleshooting & FAQ",
            category = "Support",
            iconEmoji = "🛠️",
            summary = "Quick solutions to common questions.",
            steps = listOf(
                "Q: How do I change my companion? → Go to Companion tab and tap 'Customize'.",
                "Q: How do I change the Sakura theme? → Tap the theme icon in the top bar and select another theme.",
                "Q: Can my companion talk without Wi-Fi? → Yes! Along runs 100% offline.",
                "Q: What if I forget my Vault PIN? → Use the Backup file to restore, or contact your trusted backup."
            ),
            tips = listOf(
                "If you ever need to reset tutorial hints, tap 'Reset Tutorial Hints' at the bottom of the User Guide."
            )
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserGuideScreen(
    viewModel: AlongViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var selectedTopic by remember { mutableStateOf<GuideTopic?>(null) }

    val categories = remember {
        listOf("All") + GuideRepository.topics.map { it.category }.distinct()
    }

    val filteredTopics = remember(searchQuery, selectedCategory) {
        GuideRepository.topics.filter { topic ->
            val matchesCategory = selectedCategory == null || selectedCategory == "All" || topic.category == selectedCategory
            val matchesSearch = searchQuery.isBlank() ||
                    topic.title.contains(searchQuery, ignoreCase = true) ||
                    topic.summary.contains(searchQuery, ignoreCase = true) ||
                    topic.steps.any { it.contains(searchQuery, ignoreCase = true) } ||
                    topic.tips.any { it.contains(searchQuery, ignoreCase = true) }
            matchesCategory && matchesSearch
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Help & User Guide 🌿",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Everything you need to enjoy your world",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        if (selectedTopic != null) {
            // Topic Detail View
            TopicDetailView(
                topic = selectedTopic!!,
                onBack = { selectedTopic = null },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            )
        } else {
            // Main Guide Explorer
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    // Welcome Header Card
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Welcome to Along 🌿",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "\"Everything you need to enjoy your little world.\"",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                            )
                            Text(
                                text = "100% Offline • Private • Created by Mr. Aye Chan Maung from Myanmar",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // Search Bar
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("guide_search_input"),
                        placeholder = { Text("Search the guide (e.g. chess, sakura, pin)...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Search Guide")
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )
                }

                // Quick Category Filters
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories) { cat ->
                            val isSelected = (selectedCategory == null && cat == "All") || selectedCategory == cat
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedCategory = if (cat == "All") null else cat },
                                label = { Text(cat) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }

                // Topics List
                items(filteredTopics, key = { it.id }) { topic ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedTopic = topic }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                modifier = Modifier.size(46.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = topic.iconEmoji, fontSize = 22.sp)
                                }
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = topic.title,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                                    )
                                }
                                Text(
                                    text = topic.summary,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2
                                )
                            }

                            Icon(
                                Icons.Default.ChevronRight,
                                contentDescription = "Open",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Reset Tutorial Hints Action
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Tutorial Hints & Tips",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Did you dismiss any first-time feature tips earlier? You can re-enable all hints anytime.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            OutlinedButton(
                                onClick = {
                                    viewModel.resetTutorialHints()
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Reset All Feature Tutorial Hints")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun TopicDetailView(
    topic: GuideTopic,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onBack,
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Back to Guide")
            }
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(topic.iconEmoji, fontSize = 34.sp)
                Column {
                    Text(
                        text = topic.title,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Category: ${topic.category}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        item {
            Text(
                text = topic.summary,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp
            )
        }

        item {
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
                    Text(
                        text = "Step-by-Step Instructions",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    topic.steps.forEachIndexed { index, step ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${index + 1}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                            Text(
                                text = step,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        if (topic.tips.isNotEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "💡 Helpful Tips",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        topic.tips.forEach { tip ->
                            Text(
                                text = "• $tip",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🛡️", fontSize = 20.sp)
                    Text(
                        text = topic.privacyNote,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

/**
 * Contextual Help Banner for First-Time Users
 * Reusable across Timeline, Planner, Game Room, Finance, etc.
 */
@Composable
fun ContextualHelpBanner(
    hintKey: String,
    title: String,
    description: String,
    emoji: String = "🌿",
    isDismissed: Boolean,
    onGotIt: () -> Unit,
    onLearnMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isDismissed) return

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(emoji, fontSize = 20.sp)
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.9f)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onGotIt,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Got it")
                }
                OutlinedButton(
                    onClick = onLearnMore,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Learn More")
                }
            }
        }
    }
}
