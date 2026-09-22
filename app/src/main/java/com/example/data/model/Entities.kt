package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class CompanionExpression(val displayName: String, val emoji: String) {
    HAPPY("Happy", "😊"),
    VERY_HAPPY("Very Happy", "😄"),
    EXCITED("Excited", "🤩"),
    PROUD("Proud", "🥹"),
    LAUGHING("Laughing", "😂"),
    PEACEFUL("Peaceful", "😌"),
    CONCERNED("Concerned", "🥺"),
    SAD("Sad", "😢"),
    THINKING("Thinking", "🤔"),
    DETERMINED("Determined", "😤"),
    SURPRISED("Surprised", "😳"),
    SLEEPY("Sleepy", "😴"),
    ENERGETIC("Energetic", "⚡"),
    CELEBRATING("Celebrating", "🥳"),
    NEUTRAL("Neutral", "😐")
}

enum class RelationshipStyle(val title: String) {
    FRIEND("Friend"),
    BEST_FRIEND("Best Friend"),
    CLOSE_COMPANION("Close Companion"),
    MENTOR("Mentor"),
    COACH("Coach"),
    SIBLING_LIKE("Sibling-like"),
    CALM_COMPANION("Calm Companion"),
    MOTIVATOR("Motivator"),
    CUSTOM("Custom")
}

enum class WorldTheme(val title: String, val emoji: String, val description: String) {
    SAKURA_GARDEN("Sakura Garden", "🌸", "Soft pink cherry blossoms & gentle warmth"),
    FOREST_MORNING("Forest Morning", "🌿", "Fresh evergreen pines & morning dew"),
    QUIET_OCEAN("Quiet Ocean", "🌊", "Gentle ocean breeze & calming tides"),
    CLOUD_DREAM("Cloud Dream", "☁️", "Dreamy pastel skies & weightless peace"),
    COZY_ROOM("Cozy Room", "🏡", "Warm golden light, books & wooden hearth"),
    MOONLIT_NIGHT("Moonlit Night", "🌙", "Serene midnight glow & calm shadows"),
    STARRY_SKY("Starry Sky", "⭐", "Quiet twinkling starlight across infinity"),
    COZY_AUTUMN("Cozy Autumn", "🍂", "Golden maple leaves & brisk amber breeze"),
    RAINY_WINDOW("Rainy Window", "🌧️", "Gentle raindrops tapping on warm glass"),
    LAVENDER_FIELD("Lavender Field", "💜", "Fragrant purple blossoms & dusk calm")
}

enum class LifeMomentCategory(val displayName: String, val icon: String) {
    MEMORY("Memory", "🌱"),
    ACHIEVEMENT("Achievement", "🏆"),
    CELEBRATION("Celebration", "🎉"),
    MILESTONE("Milestone", "🚩"),
    JOURNAL("Journal", "📖"),
    TRAVEL("Travel", "✈️"),
    RELATIONSHIP("Relationship", "💌"),
    STUDY("Study", "📚"),
    CAREER("Career", "💼"),
    GRATITUDE("Gratitude", "🙏"),
    LESSON("Lesson Learned", "💡"),
    DIFFICULT("Difficult Experience", "🌧️"),
    THOUGHT("Personal Thought", "💭")
}

enum class EmotionType(val displayName: String, val emoji: String) {
    HAPPY("Happy", "😊"),
    VERY_HAPPY("Very Happy", "😄"),
    PROUD("Proud", "🥹"),
    EXCITED("Excited", "🤩"),
    PEACEFUL("Peaceful", "😌"),
    GRATITUDE("Grateful", "🙏"),
    LOVED("Loved", "🥰"),
    NEUTRAL("Neutral", "😐"),
    TIRED("Tired", "🥱"),
    CONFUSED("Confused", "🤔"),
    LONELY("Lonely", "🍂"),
    STRESSED("Stressed", "😣"),
    ANXIOUS("Anxious", "😰"),
    ANGRY("Angry", "😤"),
    SAD("Sad", "😢"),
    VERY_SAD("Very Sad", "😭")
}

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val preferredName: String = "Traveler",
    val nickname: String = "",
    val birthday: String = "",
    val pronouns: String = "",
    val interests: String = "Reading, Nature, Walking",
    val hobbies: String = "Gardening, Photography",
    val favoriteThings: String = "Warm tea, rain sounds, quiet mornings",
    val routineNotes: String = "Morning walk, evening reflection"
)

@Entity(tableName = "companion_profile")
data class CompanionProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "Sprout",
    val relationshipStyle: RelationshipStyle = RelationshipStyle.CLOSE_COMPANION,
    val preset: String = "Cozy Friend",
    val playfulToSerious: Int = 30, // 0 = Playful, 100 = Serious
    val gentleToDirect: Int = 20,   // 0 = Gentle, 100 = Direct
    val calmToEnergetic: Int = 35,  // 0 = Calm, 100 = Energetic
    val quietToTalkative: Int = 45, // 0 = Quiet, 100 = Talkative
    val reflectiveToMotivational: Int = 40, // 0 = Reflective, 100 = Motivational
    val cuteToMature: Int = 25,     // 0 = Cute, 100 = Mature
    val currentExpression: CompanionExpression = CompanionExpression.PEACEFUL,
    val worldTheme: WorldTheme = WorldTheme.FOREST_MORNING
)

@Entity(tableName = "life_moments")
data class LifeMoment(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val category: LifeMomentCategory,
    val timestamp: Long = System.currentTimeMillis(),
    val emotion: EmotionType = EmotionType.PEACEFUL,
    val emotionIntensity: Int = 3, // 1 to 5
    val importance: Int = 3,       // 1 to 5
    val tags: String = "",         // Comma-separated
    val isFavorite: Boolean = false,
    val isPrivate: Boolean = false,
    val isSensitive: Boolean = false, // Trauma/Grief -> auto-resurfacing = OFF
    val allowResurfacing: Boolean = true,
    val notes: String = ""
)

enum class GoalCategory(val displayName: String, val icon: String) {
    HEALTH("Health", "🏃"),
    STUDY("Study", "📚"),
    CAREER("Career", "💼"),
    FINANCE("Finance", "💰"),
    RELATIONSHIPS("Relationships", "🤝"),
    SKILLS("Skills", "🎨"),
    HABITS("Habits", "🌱"),
    PERSONAL("Personal", "✨"),
    PROJECTS("Projects", "🛠️")
}

enum class GoalStatus(val displayName: String) {
    PLANNED("Planned"),
    ACTIVE("Active"),
    PAUSED("Paused"),
    COMPLETED("Completed"),
    ARCHIVED("Archived")
}

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: GoalCategory,
    val startDate: Long = System.currentTimeMillis(),
    val targetDate: Long = System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000,
    val targetValue: Double = 100.0,
    val currentValue: Double = 0.0,
    val unit: String = "%",
    val priority: Int = 2,
    val status: GoalStatus = GoalStatus.ACTIVE,
    val notes: String = ""
)

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val targetDaysPerMonth: Int = 20,
    val icon: String = "🌱",
    val active: Boolean = true
)

@Entity(tableName = "habit_logs")
data class HabitLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val habitId: Long,
    val epochDay: Long, // LocalDate.toEpochDay()
    val completed: Boolean = true
)

enum class PlannerItemType(val displayName: String, val icon: String) {
    TASK("Task", "✅"),
    EVENT("Event", "📅"),
    MEETING("Meeting", "👥"),
    DEADLINE("Deadline", "⏳"),
    BIRTHDAY("Birthday", "🎂"),
    BILL("Bill", "💳")
}

@Entity(tableName = "planner_items")
data class PlannerItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val type: PlannerItemType,
    val epochDay: Long,
    val timeMinutes: Int = 9 * 60, // e.g. 09:00 = 540
    val isCompleted: Boolean = false,
    val priority: Int = 2, // 1 low, 2 medium, 3 high
    val recurringDaily: Boolean = false,
    val reminderMinutesBefore: Int = 30,
    val notes: String = ""
)

enum class TransactionType {
    INCOME, EXPENSE, TRANSFER
}

@Entity(tableName = "finance_accounts")
data class FinanceAccount(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: String = "Cash", // Cash, Checking, Savings
    val balance: Double = 0.0
)

@Entity(tableName = "finance_transactions")
data class FinanceTransaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: TransactionType,
    val amount: Double,
    val category: String,
    val accountId: Long = 1,
    val timestamp: Long = System.currentTimeMillis(),
    val note: String = ""
)

@Entity(tableName = "finance_budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: String,
    val monthlyLimit: Double
)

@Entity(tableName = "important_people")
data class ImportantPerson(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val relationship: String,
    val birthday: String = "",
    val importantDates: String = "",
    val notes: String = ""
)

@Entity(tableName = "time_capsules")
data class TimeCapsule(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val message: String,
    val unlockEpochMillis: Long,
    val isOpened: Boolean = false,
    val createdMillis: Long = System.currentTimeMillis()
)

enum class ChatSender {
    USER, COMPANION
}

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sender: ChatSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val expression: CompanionExpression = CompanionExpression.PEACEFUL,
    val cardType: String = "NONE", // NONE, MEMORY, ACHIEVEMENT, FINANCE, SCHEDULE, GOAL
    val cardTitle: String = "",
    val cardSnippet: String = "",
    val memoryExplanation: String = "" // For "Why did you mention this?" transparency!
)

@Entity(tableName = "app_settings")
data class AppSettings(
    @PrimaryKey val id: Int = 1,
    val hasOnboarded: Boolean = false,
    val peaceModeActive: Boolean = false,
    val vaultPin: String = "",
    val vaultUnlocked: Boolean = false,
    val notificationPersonality: String = "Warm", // Warm, Simple, Minimal
    val companionCanRememberMemories: Boolean = true,
    val companionCanRememberJournal: Boolean = true,
    val companionCanRememberMood: Boolean = true,
    val companionCanRememberGoals: Boolean = true,
    val companionCanRememberSchedule: Boolean = true,
    val companionCanRememberFinance: Boolean = true,
    val companionCanRememberPeople: Boolean = true,
    val selectedTheme: WorldTheme = WorldTheme.FOREST_MORNING
)
