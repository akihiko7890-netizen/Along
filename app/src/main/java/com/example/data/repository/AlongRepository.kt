package com.example.data.repository

import android.content.Context
import com.example.data.db.AlongDao
import com.example.data.db.AlongDatabase
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import org.json.JSONArray
import org.json.JSONObject
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

data class FinanceSummary(
    val totalIncome: Double,
    val totalExpenses: Double,
    val netCashFlow: Double,
    val totalBudget: Double,
    val budgetRemaining: Double,
    val totalAccountBalances: Double
)

class AlongRepository(private val dao: AlongDao) {

    val userProfile: Flow<UserProfile?> = dao.getUserProfile()
    val companionProfile: Flow<CompanionProfile?> = dao.getCompanionProfile()
    val appSettings: Flow<AppSettings?> = dao.getAppSettings()

    val lifeMoments: Flow<List<LifeMoment>> = dao.getAllLifeMoments()
    val resurfaceableMoments: Flow<List<LifeMoment>> = dao.getResurfaceableMoments()
    val achievements: Flow<List<LifeMoment>> = dao.getAchievements()

    val goals: Flow<List<Goal>> = dao.getAllGoals()
    val habits: Flow<List<Habit>> = dao.getAllHabits()
    val habitLogs: Flow<List<HabitLog>> = dao.getAllHabitLogs()

    val plannerItems: Flow<List<PlannerItem>> = dao.getAllPlannerItems()

    val accounts: Flow<List<FinanceAccount>> = dao.getFinanceAccounts()
    val transactions: Flow<List<FinanceTransaction>> = dao.getFinanceTransactions()
    val budgets: Flow<List<Budget>> = dao.getBudgets()

    val importantPeople: Flow<List<ImportantPerson>> = dao.getAllImportantPeople()
    val timeCapsules: Flow<List<TimeCapsule>> = dao.getAllTimeCapsules()
    val chatMessages: Flow<List<ChatMessage>> = dao.getAllChatMessages()

    suspend fun initializeDefaultDataIfEmpty() {
        val currentSettings = dao.getAppSettings().firstOrNull()
        if (currentSettings == null) {
            dao.saveAppSettings(AppSettings(id = 1, hasOnboarded = false))
        }

        val currentProfile = dao.getUserProfile().firstOrNull()
        if (currentProfile == null) {
            dao.saveUserProfile(
                UserProfile(
                    preferredName = "Traveler",
                    nickname = "",
                    birthday = "1998-05-12",
                    interests = "Nature walks, Books, Acoustic music",
                    hobbies = "Journaling, Stargazing",
                    favoriteThings = "Warm tea, morning breeze, rain tapping on window",
                    routineNotes = "Morning quiet time, evening reflection"
                )
            )
        }

        val currentCompanion = dao.getCompanionProfile().firstOrNull()
        if (currentCompanion == null) {
            dao.saveCompanionProfile(
                CompanionProfile(
                    name = "Sprout",
                    relationshipStyle = RelationshipStyle.CLOSE_COMPANION,
                    preset = "Cozy Friend",
                    currentExpression = CompanionExpression.PEACEFUL,
                    worldTheme = WorldTheme.FOREST_MORNING
                )
            )
        }

        // Add starter account if none exists
        val currentAccounts = dao.getFinanceAccounts().firstOrNull()
        if (currentAccounts.isNullOrEmpty()) {
            dao.insertFinanceAccount(FinanceAccount(name = "Main Savings", type = "Savings", balance = 1250.0))
            dao.insertFinanceAccount(FinanceAccount(name = "Daily Cash", type = "Cash", balance = 80.0))
        }

        // Add starter habits if empty
        val currentHabits = dao.getAllHabits().firstOrNull()
        if (currentHabits.isNullOrEmpty()) {
            dao.insertHabit(Habit(title = "Morning Mindful Walk", targetDaysPerMonth = 20, icon = "🌿"))
            dao.insertHabit(Habit(title = "Evening Reflection & Journal", targetDaysPerMonth = 25, icon = "📖"))
            dao.insertHabit(Habit(title = "Drink Warm Water & Hydrate", targetDaysPerMonth = 30, icon = "💧"))
        }

        // Add starter goal if empty
        val currentGoals = dao.getAllGoals().firstOrNull()
        if (currentGoals.isNullOrEmpty()) {
            dao.insertGoal(
                Goal(
                    title = "Complete 30 peaceful morning walks",
                    category = GoalCategory.HEALTH,
                    targetValue = 30.0,
                    currentValue = 8.0,
                    unit = "walks",
                    priority = 2,
                    status = GoalStatus.ACTIVE,
                    notes = "Walking quietly without rushing"
                )
            )
        }

        // Add starter moments if empty
        val currentMoments = dao.getAllLifeMoments().firstOrNull()
        if (currentMoments.isNullOrEmpty()) {
            dao.insertLifeMoment(
                LifeMoment(
                    title = "Started my quiet journey with Along",
                    description = "Opened this personal haven to record my life, thoughts, and peaceful memories.",
                    category = LifeMomentCategory.MILESTONE,
                    emotion = EmotionType.PEACEFUL,
                    importance = 5,
                    isFavorite = true,
                    tags = "NewBeginning, Peace, Journey"
                )
            )
            dao.insertLifeMoment(
                LifeMoment(
                    title = "Finished my first semester project",
                    description = "Spent countless evenings working hard and finally presented it with flying colors.",
                    category = LifeMomentCategory.ACHIEVEMENT,
                    emotion = EmotionType.PROUD,
                    importance = 4,
                    isFavorite = true,
                    tags = "Study, Milestone"
                )
            )
        }

        // Add starter chat if empty
        val currentChat = dao.getAllChatMessages().firstOrNull()
        if (currentChat.isNullOrEmpty()) {
            dao.insertChatMessage(
                ChatMessage(
                    sender = ChatSender.COMPANION,
                    text = "Welcome to Along. 🌿 I am Sprout, walking beside you through all your quiet moments, memories, and days ahead.",
                    expression = CompanionExpression.PEACEFUL
                )
            )
        }
    }

    // Mutators
    suspend fun saveUserProfile(profile: UserProfile) = dao.saveUserProfile(profile)
    suspend fun saveCompanionProfile(profile: CompanionProfile) = dao.saveCompanionProfile(profile)
    suspend fun saveAppSettings(settings: AppSettings) = dao.saveAppSettings(settings)
    suspend fun setCompanionExpression(expression: CompanionExpression) {
        val current = dao.getCompanionProfile().firstOrNull() ?: CompanionProfile()
        dao.saveCompanionProfile(current.copy(currentExpression = expression))
    }

    suspend fun insertLifeMoment(moment: LifeMoment): Long {
        val id = dao.insertLifeMoment(moment)
        // Expression engine reaction:
        if (moment.category == LifeMomentCategory.ACHIEVEMENT || moment.category == LifeMomentCategory.CELEBRATION) {
            setCompanionExpression(CompanionExpression.CELEBRATING)
        } else if (moment.emotion == EmotionType.VERY_HAPPY || moment.emotion == EmotionType.EXCITED) {
            setCompanionExpression(CompanionExpression.HAPPY)
        } else if (moment.emotion == EmotionType.SAD || moment.emotion == EmotionType.VERY_SAD) {
            setCompanionExpression(CompanionExpression.CONCERNED)
        }
        return id
    }

    suspend fun updateLifeMoment(moment: LifeMoment) = dao.updateLifeMoment(moment)
    suspend fun deleteLifeMoment(id: Long) = dao.deleteLifeMoment(id)

    suspend fun insertGoal(goal: Goal) = dao.insertGoal(goal)
    suspend fun updateGoal(goal: Goal) {
        dao.updateGoal(goal)
        if (goal.status == GoalStatus.COMPLETED) {
            setCompanionExpression(CompanionExpression.PROUD)
        }
    }
    suspend fun deleteGoal(id: Long) = dao.deleteGoal(id)

    suspend fun insertHabit(habit: Habit) = dao.insertHabit(habit)
    suspend fun updateHabit(habit: Habit) = dao.updateHabit(habit)
    suspend fun deleteHabit(id: Long) = dao.deleteHabit(id)

    suspend fun toggleHabitLog(habitId: Long, epochDay: Long, completedNow: Boolean) {
        if (completedNow) {
            dao.deleteHabitLog(habitId, epochDay)
        } else {
            dao.insertHabitLog(HabitLog(habitId = habitId, epochDay = epochDay, completed = true))
            setCompanionExpression(CompanionExpression.HAPPY)
        }
    }

    suspend fun insertPlannerItem(item: PlannerItem): Long {
        setCompanionExpression(CompanionExpression.THINKING)
        return dao.insertPlannerItem(item)
    }
    suspend fun updatePlannerItem(item: PlannerItem) = dao.updatePlannerItem(item)
    suspend fun deletePlannerItem(id: Long) = dao.deletePlannerItem(id)

    suspend fun insertTransaction(transaction: FinanceTransaction): Long {
        val id = dao.insertFinanceTransaction(transaction)
        setCompanionExpression(CompanionExpression.THINKING)
        return id
    }
    suspend fun deleteTransaction(id: Long) = dao.deleteFinanceTransaction(id)
    suspend fun insertAccount(account: FinanceAccount) = dao.insertFinanceAccount(account)
    suspend fun insertBudget(budget: Budget) = dao.insertBudget(budget)

    suspend fun insertImportantPerson(person: ImportantPerson) = dao.insertImportantPerson(person)
    suspend fun deleteImportantPerson(id: Long) = dao.deleteImportantPerson(id)

    suspend fun insertTimeCapsule(capsule: TimeCapsule) = dao.insertTimeCapsule(capsule)
    suspend fun updateTimeCapsule(capsule: TimeCapsule) = dao.updateTimeCapsule(capsule)
    suspend fun deleteTimeCapsule(id: Long) = dao.deleteTimeCapsule(id)

    suspend fun sendChatMessage(userText: String): ChatMessage {
        // Save user message
        dao.insertChatMessage(ChatMessage(sender = ChatSender.USER, text = userText))

        // Process companion response deterministically
        val reply = generateCompanionResponse(userText)
        dao.insertChatMessage(reply)
        setCompanionExpression(reply.expression)
        return reply
    }

    suspend fun clearChat() = dao.clearChatMessages()

    private suspend fun generateCompanionResponse(query: String): ChatMessage {
        val lower = query.lowercase().trim()
        val user = dao.getUserProfile().firstOrNull() ?: UserProfile()
        val companion = dao.getCompanionProfile().firstOrNull() ?: CompanionProfile()
        val settings = dao.getAppSettings().firstOrNull() ?: AppSettings()

        // Schedule / Tomorrow / Today query
        if (lower.contains("tomorrow") || lower.contains("schedule") || lower.contains("doing") || lower.contains("plan")) {
            if (!settings.companionCanRememberSchedule) {
                return ChatMessage(
                    sender = ChatSender.COMPANION,
                    text = "You requested that I keep your schedule private, so I won't access your planner here. You can check it anytime in the Planner tab! 📅",
                    expression = CompanionExpression.PEACEFUL,
                    memoryExplanation = "Respecting your Companion Memory setting for Schedule."
                )
            }
            val planner = dao.fetchAllPlannerItemsOnce().filter { !it.isCompleted }
            return if (planner.isNotEmpty()) {
                val nextItem = planner.first()
                ChatMessage(
                    sender = ChatSender.COMPANION,
                    text = "You have '${nextItem.title}' (${nextItem.type.displayName}) on your schedule. Remember to take it step by step and breathe gently. 🌿",
                    expression = CompanionExpression.THINKING,
                    cardType = "SCHEDULE",
                    cardTitle = nextItem.title,
                    cardSnippet = "${nextItem.type.displayName} • Priority ${nextItem.priority}",
                    memoryExplanation = "Retrieved from your active Planner items."
                )
            } else {
                ChatMessage(
                    sender = ChatSender.COMPANION,
                    text = "Your schedule is clear right now! A wonderful time to rest, enjoy a walk, or simply just stay here. 🌸",
                    expression = CompanionExpression.PEACEFUL
                )
            }
        }

        // Finance / Spending query
        if (lower.contains("spent") || lower.contains("spend") || lower.contains("finance") || lower.contains("money") || lower.contains("cost")) {
            if (!settings.companionCanRememberFinance) {
                return ChatMessage(
                    sender = ChatSender.COMPANION,
                    text = "Your financial records are kept strictly private per your companion memory settings. You can review them in My World → Finance. 💰",
                    expression = CompanionExpression.PEACEFUL,
                    memoryExplanation = "Respecting your Companion Memory setting for Finance."
                )
            }
            val txs = dao.fetchAllFinanceTransactionsOnce()
            val totalExpense = txs.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
            val totalIncome = txs.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
            return ChatMessage(
                sender = ChatSender.COMPANION,
                text = "Based on your records, your total spending is $%.2f and your income is $%.2f (Net flow: $%.2f). You're managing thoughtfully.".format(
                    totalExpense, totalIncome, totalIncome - totalExpense
                ),
                expression = CompanionExpression.THINKING,
                cardType = "FINANCE",
                cardTitle = "Finance Summary",
                cardSnippet = "Total Spent: $%.2f • Total Income: $%.2f".format(totalExpense, totalIncome),
                memoryExplanation = "Calculated deterministically from your logged finance transactions."
            )
        }

        // Achievements / Accomplish query
        if (lower.contains("achieve") || lower.contains("accomplish") || lower.contains("proud") || lower.contains("win")) {
            val achievementsList = dao.fetchAllLifeMomentsOnce().filter {
                it.category == LifeMomentCategory.ACHIEVEMENT || it.category == LifeMomentCategory.CELEBRATION
            }
            return if (achievementsList.isNotEmpty()) {
                val topAchieve = achievementsList.first()
                ChatMessage(
                    sender = ChatSender.COMPANION,
                    text = "I'm so proud of what you've achieved! Especially '${topAchieve.title}'. Every step you take matters. 🏆",
                    expression = CompanionExpression.CELEBRATING,
                    cardType = "ACHIEVEMENT",
                    cardTitle = topAchieve.title,
                    cardSnippet = topAchieve.description,
                    memoryExplanation = "Mentioned because you recorded this achievement in your life timeline."
                )
            } else {
                ChatMessage(
                    sender = ChatSender.COMPANION,
                    text = "Every day you keep going is an achievement in itself. When something wonderful happens, be sure to save it so we can celebrate! 🌟",
                    expression = CompanionExpression.HAPPY
                )
            }
        }

        // Good memory / Remember something good
        if (lower.contains("good") || lower.contains("memory") || lower.contains("remember") || lower.contains("past")) {
            if (!settings.companionCanRememberMemories) {
                return ChatMessage(
                    sender = ChatSender.COMPANION,
                    text = "Your memories are kept stored in your private vault and not brought up here. You can open Timeline to browse them! 🌱",
                    expression = CompanionExpression.PEACEFUL,
                    memoryExplanation = "Respecting your Companion Memory setting for Memories."
                )
            }
            val memories = dao.fetchAllLifeMomentsOnce().filter {
                it.isFavorite || it.category == LifeMomentCategory.MEMORY || it.category == LifeMomentCategory.GRATITUDE
            }
            return if (memories.isNotEmpty()) {
                val mem = memories.random()
                ChatMessage(
                    sender = ChatSender.COMPANION,
                    text = "Here is a warm memory from your journey: '${mem.title}'. ${mem.description} 🌸",
                    expression = CompanionExpression.PROUD,
                    cardType = "MEMORY",
                    cardTitle = mem.title,
                    cardSnippet = mem.description,
                    memoryExplanation = "Resurfaced from your favorite positive memories."
                )
            } else {
                ChatMessage(
                    sender = ChatSender.COMPANION,
                    text = "You're at the very beginning of filling your world with memories. Every small kind moment can become a treasure. 🌿",
                    expression = CompanionExpression.HAPPY
                )
            }
        }

        // Emotional states: Sad / Down / Tired / Stressed
        if (lower.contains("sad") || lower.contains("tired") || lower.contains("down") || lower.contains("stressed") || lower.contains("anxious") || lower.contains("hurts")) {
            return ChatMessage(
                sender = ChatSender.COMPANION,
                text = "I hear you, ${user.preferredName}. It's completely okay to feel this way. You don't have to carry everything all at once. Take a gentle breath—I'm right here beside you. 🌧️🤍",
                expression = CompanionExpression.CONCERNED
            )
        }

        // Happy / Good day
        if (lower.contains("happy") || lower.contains("great") || lower.contains("wonderful") || lower.contains("excited") || lower.contains("love")) {
            return ChatMessage(
                sender = ChatSender.COMPANION,
                text = "That warms my heart! Seeing you joyful makes this whole little world brighter. Let's cherish this moment. 😊✨",
                expression = CompanionExpression.VERY_HAPPY
            )
        }

        // Night time / Sleep
        if (lower.contains("sleep") || lower.contains("night") || lower.contains("goodnight") || lower.contains("tired")) {
            return ChatMessage(
                sender = ChatSender.COMPANION,
                text = "Rest well, ${user.preferredName}. The stars are quiet and you've done enough today. Tomorrow will welcome you softly. 🌙😴",
                expression = CompanionExpression.SLEEPY
            )
        }

        // Morning
        if (lower.contains("morning") || lower.contains("hello") || lower.contains("hi") || lower.contains("hey")) {
            return ChatMessage(
                sender = ChatSender.COMPANION,
                text = "Good day, ${user.preferredName}! The morning air is calm. How are you feeling today? 🌿",
                expression = CompanionExpression.HAPPY
            )
        }

        // General / Default conversational reflection
        return ChatMessage(
            sender = ChatSender.COMPANION,
            text = "I'm listening, ${user.preferredName}. Whatever is on your mind—thoughts, memories, or plans—I'm here walking Along with you. 🌱",
            expression = CompanionExpression.PEACEFUL,
            memoryExplanation = "Open conversational companion mode."
        )
    }

    // --- Complete Backup (.along format) & Restore ---
    suspend fun exportAlongBackupJson(password: String = ""): String {
        val root = JSONObject()
        root.put("app", "Along")
        root.put("version", "1.0.0")
        root.put("creator", "Mr. Aye Chan Maung from Myanmar")
        root.put("exportedAt", SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).format(Date()))

        val profile = dao.fetchUserProfileOnce()
        if (profile != null) {
            val userObj = JSONObject()
            userObj.put("preferredName", profile.preferredName)
            userObj.put("nickname", profile.nickname)
            userObj.put("birthday", profile.birthday)
            userObj.put("pronouns", profile.pronouns)
            userObj.put("interests", profile.interests)
            userObj.put("hobbies", profile.hobbies)
            userObj.put("favoriteThings", profile.favoriteThings)
            userObj.put("routineNotes", profile.routineNotes)
            root.put("userProfile", userObj)
        }

        val companion = dao.fetchCompanionProfileOnce()
        if (companion != null) {
            val compObj = JSONObject()
            compObj.put("name", companion.name)
            compObj.put("relationshipStyle", companion.relationshipStyle.name)
            compObj.put("preset", companion.preset)
            compObj.put("playfulToSerious", companion.playfulToSerious)
            compObj.put("gentleToDirect", companion.gentleToDirect)
            compObj.put("calmToEnergetic", companion.calmToEnergetic)
            compObj.put("quietToTalkative", companion.quietToTalkative)
            compObj.put("reflectiveToMotivational", companion.reflectiveToMotivational)
            compObj.put("cuteToMature", companion.cuteToMature)
            compObj.put("worldTheme", companion.worldTheme.name)
            root.put("companionProfile", compObj)
        }

        val momentsArr = JSONArray()
        dao.fetchAllLifeMomentsOnce().forEach { m ->
            val obj = JSONObject()
            obj.put("title", m.title)
            obj.put("description", m.description)
            obj.put("category", m.category.name)
            obj.put("timestamp", m.timestamp)
            obj.put("emotion", m.emotion.name)
            obj.put("emotionIntensity", m.emotionIntensity)
            obj.put("importance", m.importance)
            obj.put("tags", m.tags)
            obj.put("isFavorite", m.isFavorite)
            obj.put("isPrivate", m.isPrivate)
            obj.put("isSensitive", m.isSensitive)
            obj.put("allowResurfacing", m.allowResurfacing)
            obj.put("notes", m.notes)
            momentsArr.put(obj)
        }
        root.put("lifeMoments", momentsArr)

        val goalsArr = JSONArray()
        dao.fetchAllGoalsOnce().forEach { g ->
            val obj = JSONObject()
            obj.put("title", g.title)
            obj.put("category", g.category.name)
            obj.put("startDate", g.startDate)
            obj.put("targetDate", g.targetDate)
            obj.put("targetValue", g.targetValue)
            obj.put("currentValue", g.currentValue)
            obj.put("unit", g.unit)
            obj.put("priority", g.priority)
            obj.put("status", g.status.name)
            obj.put("notes", g.notes)
            goalsArr.put(obj)
        }
        root.put("goals", goalsArr)

        val habitsArr = JSONArray()
        dao.fetchAllHabitsOnce().forEach { h ->
            val obj = JSONObject()
            obj.put("id", h.id)
            obj.put("title", h.title)
            obj.put("targetDaysPerMonth", h.targetDaysPerMonth)
            obj.put("icon", h.icon)
            obj.put("active", h.active)
            habitsArr.put(obj)
        }
        root.put("habits", habitsArr)

        val logsArr = JSONArray()
        dao.fetchAllHabitLogsOnce().forEach { l ->
            val obj = JSONObject()
            obj.put("habitId", l.habitId)
            obj.put("epochDay", l.epochDay)
            obj.put("completed", l.completed)
            logsArr.put(obj)
        }
        root.put("habitLogs", logsArr)

        val plannerArr = JSONArray()
        dao.fetchAllPlannerItemsOnce().forEach { p ->
            val obj = JSONObject()
            obj.put("title", p.title)
            obj.put("type", p.type.name)
            obj.put("epochDay", p.epochDay)
            obj.put("timeMinutes", p.timeMinutes)
            obj.put("isCompleted", p.isCompleted)
            obj.put("priority", p.priority)
            obj.put("recurringDaily", p.recurringDaily)
            obj.put("reminderMinutesBefore", p.reminderMinutesBefore)
            obj.put("notes", p.notes)
            plannerArr.put(obj)
        }
        root.put("plannerItems", plannerArr)

        val txArr = JSONArray()
        dao.fetchAllFinanceTransactionsOnce().forEach { t ->
            val obj = JSONObject()
            obj.put("type", t.type.name)
            obj.put("amount", t.amount)
            obj.put("category", t.category)
            obj.put("accountId", t.accountId)
            obj.put("timestamp", t.timestamp)
            obj.put("note", t.note)
            txArr.put(obj)
        }
        root.put("transactions", txArr)

        val peopleArr = JSONArray()
        dao.fetchAllImportantPeopleOnce().forEach { p ->
            val obj = JSONObject()
            obj.put("name", p.name)
            obj.put("relationship", p.relationship)
            obj.put("birthday", p.birthday)
            obj.put("importantDates", p.importantDates)
            obj.put("notes", p.notes)
            peopleArr.put(obj)
        }
        root.put("importantPeople", peopleArr)

        val jsonStr = root.toString(2)
        // Add SHA-256 integrity hash
        val digest = MessageDigest.getInstance("SHA-256").digest(jsonStr.toByteArray(Charsets.UTF_8))
        val hash = digest.joinToString("") { "%02x".format(it) }
        val wrapper = JSONObject()
        wrapper.put("alongFormat", "v1.0")
        wrapper.put("checksumSha256", hash)
        wrapper.put("payload", jsonStr)
        return wrapper.toString(2)
    }

    suspend fun restoreAlongBackupJson(backupPayloadString: String): Boolean {
        return try {
            val wrapper = JSONObject(backupPayloadString)
            val jsonStr = if (wrapper.has("payload")) wrapper.getString("payload") else backupPayloadString
            val root = JSONObject(jsonStr)

            if (root.has("userProfile")) {
                val u = root.getJSONObject("userProfile")
                dao.saveUserProfile(
                    UserProfile(
                        preferredName = u.optString("preferredName", "Traveler"),
                        nickname = u.optString("nickname", ""),
                        birthday = u.optString("birthday", ""),
                        pronouns = u.optString("pronouns", ""),
                        interests = u.optString("interests", ""),
                        hobbies = u.optString("hobbies", ""),
                        favoriteThings = u.optString("favoriteThings", ""),
                        routineNotes = u.optString("routineNotes", "")
                    )
                )
            }

            if (root.has("companionProfile")) {
                val c = root.getJSONObject("companionProfile")
                dao.saveCompanionProfile(
                    CompanionProfile(
                        name = c.optString("name", "Sprout"),
                        relationshipStyle = RelationshipStyle.valueOf(c.optString("relationshipStyle", "CLOSE_COMPANION")),
                        preset = c.optString("preset", "Cozy Friend"),
                        playfulToSerious = c.optInt("playfulToSerious", 30),
                        gentleToDirect = c.optInt("gentleToDirect", 20),
                        calmToEnergetic = c.optInt("calmToEnergetic", 35),
                        quietToTalkative = c.optInt("quietToTalkative", 45),
                        reflectiveToMotivational = c.optInt("reflectiveToMotivational", 40),
                        cuteToMature = c.optInt("cuteToMature", 25),
                        worldTheme = WorldTheme.valueOf(c.optString("worldTheme", "FOREST_MORNING"))
                    )
                )
            }

            if (root.has("lifeMoments")) {
                val arr = root.getJSONArray("lifeMoments")
                for (i in 0 until arr.length()) {
                    val m = arr.getJSONObject(i)
                    dao.insertLifeMoment(
                        LifeMoment(
                            title = m.getString("title"),
                            description = m.optString("description", ""),
                            category = LifeMomentCategory.valueOf(m.getString("category")),
                            timestamp = m.optLong("timestamp", System.currentTimeMillis()),
                            emotion = EmotionType.valueOf(m.optString("emotion", "PEACEFUL")),
                            emotionIntensity = m.optInt("emotionIntensity", 3),
                            importance = m.optInt("importance", 3),
                            tags = m.optString("tags", ""),
                            isFavorite = m.optBoolean("isFavorite", false),
                            isPrivate = m.optBoolean("isPrivate", false),
                            isSensitive = m.optBoolean("isSensitive", false),
                            allowResurfacing = m.optBoolean("allowResurfacing", true),
                            notes = m.optString("notes", "")
                        )
                    )
                }
            }

            if (root.has("goals")) {
                val arr = root.getJSONArray("goals")
                for (i in 0 until arr.length()) {
                    val g = arr.getJSONObject(i)
                    dao.insertGoal(
                        Goal(
                            title = g.getString("title"),
                            category = GoalCategory.valueOf(g.getString("category")),
                            startDate = g.optLong("startDate", System.currentTimeMillis()),
                            targetDate = g.optLong("targetDate", System.currentTimeMillis() + 86400000L * 30),
                            targetValue = g.optDouble("targetValue", 100.0),
                            currentValue = g.optDouble("currentValue", 0.0),
                            unit = g.optString("unit", "%"),
                            priority = g.optInt("priority", 2),
                            status = GoalStatus.valueOf(g.optString("status", "ACTIVE")),
                            notes = g.optString("notes", "")
                        )
                    )
                }
            }

            // Restore complete
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
