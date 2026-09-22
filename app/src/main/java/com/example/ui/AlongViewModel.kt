package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AlongDatabase
import com.example.data.model.*
import com.example.data.repository.AlongRepository
import com.example.data.repository.FinanceSummary
import com.example.util.AlongAudioEngine
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

enum class MainDestination(val title: String, val iconEmoji: String) {
    HOME("Home", "🏡"),
    COMPANION("Companion", "🌱"),
    TIMELINE("Timeline", "🌿"),
    PLANNER("Planner", "📅"),
    MY_WORLD("My World", "✨")
}

enum class MyWorldDestination {
    MENU,
    FINANCE,
    GOALS,
    HABITS,
    MOOD,
    PEOPLE,
    TIME_CAPSULES,
    SEARCH,
    YEAR_IN_REVIEW,
    PEACE_MODE,
    VAULT,
    PRIVACY_DASHBOARD,
    BACKUP_RESTORE,
    ABOUT_ALONG,
    COMPANION_CUSTOMIZE,
    USER_GUIDE,
    GAME_ROOM
}

class AlongViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AlongRepository
    val audioEngine = AlongAudioEngine(application)

    val userProfile: StateFlow<UserProfile>
    val companionProfile: StateFlow<CompanionProfile>
    val appSettings: StateFlow<AppSettings>

    val lifeMoments: StateFlow<List<LifeMoment>>
    val resurfaceableMoments: StateFlow<List<LifeMoment>>
    val achievements: StateFlow<List<LifeMoment>>

    val goals: StateFlow<List<Goal>>
    val habits: StateFlow<List<Habit>>
    val habitLogs: StateFlow<List<HabitLog>>
    val plannerItems: StateFlow<List<PlannerItem>>

    val accounts: StateFlow<List<FinanceAccount>>
    val transactions: StateFlow<List<FinanceTransaction>>
    val budgets: StateFlow<List<Budget>>

    val importantPeople: StateFlow<List<ImportantPerson>>
    val timeCapsules: StateFlow<List<TimeCapsule>>
    val chatMessages: StateFlow<List<ChatMessage>>

    // Navigation state
    private val _currentDestination = MutableStateFlow(MainDestination.HOME)
    val currentDestination: StateFlow<MainDestination> = _currentDestination.asStateFlow()

    private val _myWorldDestination = MutableStateFlow(MyWorldDestination.MENU)
    val myWorldDestination: StateFlow<MyWorldDestination> = _myWorldDestination.asStateFlow()

    // Search query & results
    val searchQuery = MutableStateFlow("")

    // Vault PIN unlock status
    private val _vaultUnlocked = MutableStateFlow(false)
    val vaultUnlocked: StateFlow<Boolean> = _vaultUnlocked.asStateFlow()

    // Backup export / restore message
    private val _backupStatus = MutableStateFlow<String?>(null)
    val backupStatus: StateFlow<String?> = _backupStatus.asStateFlow()

    init {
        val dao = AlongDatabase.getDatabase(application).alongDao()
        repository = AlongRepository(dao)

        userProfile = repository.userProfile
            .map { it ?: UserProfile() }
            .stateIn(viewModelScope, SharingStarted.Eagerly, UserProfile())

        companionProfile = repository.companionProfile
            .map { it ?: CompanionProfile() }
            .stateIn(viewModelScope, SharingStarted.Eagerly, CompanionProfile())

        appSettings = repository.appSettings
            .map { it ?: AppSettings() }
            .stateIn(viewModelScope, SharingStarted.Eagerly, AppSettings())

        lifeMoments = repository.lifeMoments
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

        resurfaceableMoments = repository.resurfaceableMoments
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

        achievements = repository.achievements
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

        goals = repository.goals
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

        habits = repository.habits
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

        habitLogs = repository.habitLogs
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

        plannerItems = repository.plannerItems
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

        accounts = repository.accounts
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

        transactions = repository.transactions
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

        budgets = repository.budgets
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

        importantPeople = repository.importantPeople
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

        timeCapsules = repository.timeCapsules
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

        chatMessages = repository.chatMessages
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

        viewModelScope.launch {
            repository.initializeDefaultDataIfEmpty()
        }

        viewModelScope.launch {
            appSettings.collect { settings ->
                audioEngine.updateSettings(
                    musicOn = settings.musicEnabled,
                    ambientOn = settings.ambientSoundEnabled,
                    musicVol = settings.musicVolume,
                    ambientVol = settings.ambientVolume,
                    theme = settings.selectedTheme
                )
            }
        }
    }

    // Derived Finance calculations
    val financeSummary: StateFlow<FinanceSummary> = combine(
        accounts, transactions, budgets
    ) { accs, txs, bdgs ->
        val totalIncome = txs.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        val totalExpenses = txs.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        val netFlow = totalIncome - totalExpenses
        val totalBudget = bdgs.sumOf { it.monthlyLimit }
        val budgetRemaining = if (totalBudget > 0) (totalBudget - totalExpenses).coerceAtLeast(0.0) else 0.0
        val totalAccBalance = accs.sumOf { it.balance }

        FinanceSummary(
            totalIncome = totalIncome,
            totalExpenses = totalExpenses,
            netCashFlow = netFlow,
            totalBudget = totalBudget,
            budgetRemaining = budgetRemaining,
            totalAccountBalances = totalAccBalance
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, FinanceSummary(0.0, 0.0, 0.0, 0.0, 0.0, 0.0))

    // Navigation methods
    fun navigateTo(dest: MainDestination) {
        _currentDestination.value = dest
        if (dest != MainDestination.MY_WORLD) {
            _myWorldDestination.value = MyWorldDestination.MENU
        }
    }

    fun navigateToMyWorld(dest: MyWorldDestination) {
        _currentDestination.value = MainDestination.MY_WORLD
        _myWorldDestination.value = dest
    }

    fun completeOnboarding(
        userName: String,
        companionName: String,
        relationship: RelationshipStyle,
        preset: String,
        theme: WorldTheme
    ) {
        viewModelScope.launch {
            val user = userProfile.value.copy(preferredName = userName.ifBlank { "Traveler" })
            repository.saveUserProfile(user)

            val companion = companionProfile.value.copy(
                name = companionName.ifBlank { "Sprout" },
                relationshipStyle = relationship,
                preset = preset,
                worldTheme = theme,
                currentExpression = CompanionExpression.HAPPY
            )
            repository.saveCompanionProfile(companion)

            val settings = appSettings.value.copy(
                hasOnboarded = true,
                selectedTheme = theme
            )
            repository.saveAppSettings(settings)
        }
    }

    fun updateCompanionSettings(
        name: String,
        relationship: RelationshipStyle,
        preset: String,
        playfulToSerious: Int,
        gentleToDirect: Int,
        calmToEnergetic: Int,
        quietToTalkative: Int,
        reflectiveToMotivational: Int,
        cuteToMature: Int,
        theme: WorldTheme
    ) {
        viewModelScope.launch {
            val updated = companionProfile.value.copy(
                name = name,
                relationshipStyle = relationship,
                preset = preset,
                playfulToSerious = playfulToSerious,
                gentleToDirect = gentleToDirect,
                calmToEnergetic = calmToEnergetic,
                quietToTalkative = quietToTalkative,
                reflectiveToMotivational = reflectiveToMotivational,
                cuteToMature = cuteToMature,
                worldTheme = theme
            )
            repository.saveCompanionProfile(updated)
            repository.saveAppSettings(appSettings.value.copy(selectedTheme = theme))
        }
    }

    fun setCompanionExpression(expression: CompanionExpression) {
        viewModelScope.launch {
            repository.setCompanionExpression(expression)
        }
    }

    fun updateWorldTheme(theme: WorldTheme) {
        viewModelScope.launch {
            val comp = companionProfile.value.copy(worldTheme = theme)
            repository.saveCompanionProfile(comp)
            val settings = appSettings.value.copy(selectedTheme = theme)
            repository.saveAppSettings(settings)
        }
    }

    fun togglePeaceMode() {
        viewModelScope.launch {
            val current = appSettings.value.peaceModeActive
            repository.saveAppSettings(appSettings.value.copy(peaceModeActive = !current))
            if (!current) {
                repository.setCompanionExpression(CompanionExpression.PEACEFUL)
            }
        }
    }

    // Life Moments CRUD
    fun addLifeMoment(
        title: String,
        description: String,
        category: LifeMomentCategory,
        emotion: EmotionType,
        emotionIntensity: Int,
        importance: Int,
        tags: String,
        isFavorite: Boolean,
        isPrivate: Boolean,
        isSensitive: Boolean,
        allowResurfacing: Boolean
    ) {
        viewModelScope.launch {
            repository.insertLifeMoment(
                LifeMoment(
                    title = title,
                    description = description,
                    category = category,
                    emotion = emotion,
                    emotionIntensity = emotionIntensity,
                    importance = importance,
                    tags = tags,
                    isFavorite = isFavorite,
                    isPrivate = isPrivate,
                    isSensitive = isSensitive,
                    allowResurfacing = if (isSensitive) false else allowResurfacing
                )
            )
        }
    }

    fun deleteLifeMoment(id: Long) {
        viewModelScope.launch { repository.deleteLifeMoment(id) }
    }

    // Goals CRUD
    fun addGoal(
        title: String,
        category: GoalCategory,
        targetValue: Double,
        unit: String,
        notes: String
    ) {
        viewModelScope.launch {
            repository.insertGoal(
                Goal(
                    title = title,
                    category = category,
                    targetValue = targetValue,
                    unit = unit,
                    notes = notes
                )
            )
        }
    }

    fun updateGoalProgress(goal: Goal, newCurrent: Double) {
        viewModelScope.launch {
            val isCompleted = newCurrent >= goal.targetValue
            repository.updateGoal(
                goal.copy(
                    currentValue = newCurrent,
                    status = if (isCompleted) GoalStatus.COMPLETED else GoalStatus.ACTIVE
                )
            )
        }
    }

    fun deleteGoal(id: Long) {
        viewModelScope.launch { repository.deleteGoal(id) }
    }

    // Habits CRUD
    fun addHabit(title: String, targetDaysPerMonth: Int, icon: String) {
        viewModelScope.launch {
            repository.insertHabit(Habit(title = title, targetDaysPerMonth = targetDaysPerMonth, icon = icon))
        }
    }

    fun toggleHabitToday(habitId: Long) {
        viewModelScope.launch {
            val todayEpoch = LocalDate.now().toEpochDay()
            val isLogged = habitLogs.value.any { it.habitId == habitId && it.epochDay == todayEpoch }
            repository.toggleHabitLog(habitId, todayEpoch, isLogged)
        }
    }

    fun deleteHabit(id: Long) {
        viewModelScope.launch { repository.deleteHabit(id) }
    }

    // Planner CRUD
    fun addPlannerItem(
        title: String,
        type: PlannerItemType,
        epochDay: Long,
        timeMinutes: Int,
        priority: Int,
        reminderMinutesBefore: Int,
        notes: String
    ) {
        viewModelScope.launch {
            repository.insertPlannerItem(
                PlannerItem(
                    title = title,
                    type = type,
                    epochDay = epochDay,
                    timeMinutes = timeMinutes,
                    priority = priority,
                    reminderMinutesBefore = reminderMinutesBefore,
                    notes = notes
                )
            )
        }
    }

    fun togglePlannerItemCompleted(item: PlannerItem) {
        viewModelScope.launch {
            repository.updatePlannerItem(item.copy(isCompleted = !item.isCompleted))
            if (!item.isCompleted) {
                repository.setCompanionExpression(CompanionExpression.HAPPY)
            }
        }
    }

    fun deletePlannerItem(id: Long) {
        viewModelScope.launch { repository.deletePlannerItem(id) }
    }

    // Finance CRUD
    fun addTransaction(
        type: TransactionType,
        amount: Double,
        category: String,
        note: String
    ) {
        viewModelScope.launch {
            repository.insertTransaction(
                FinanceTransaction(
                    type = type,
                    amount = amount,
                    category = category,
                    note = note
                )
            )
        }
    }

    fun deleteTransaction(id: Long) {
        viewModelScope.launch { repository.deleteTransaction(id) }
    }

    // People CRUD
    fun addPerson(name: String, relationship: String, birthday: String, notes: String) {
        viewModelScope.launch {
            repository.insertImportantPerson(
                ImportantPerson(
                    name = name,
                    relationship = relationship,
                    birthday = birthday,
                    notes = notes
                )
            )
        }
    }

    fun deletePerson(id: Long) {
        viewModelScope.launch { repository.deleteImportantPerson(id) }
    }

    // Time Capsule CRUD
    fun addTimeCapsule(title: String, message: String, unlockEpochMillis: Long) {
        viewModelScope.launch {
            repository.insertTimeCapsule(
                TimeCapsule(
                    title = title,
                    message = message,
                    unlockEpochMillis = unlockEpochMillis
                )
            )
            repository.setCompanionExpression(CompanionExpression.PROUD)
        }
    }

    fun openTimeCapsule(capsule: TimeCapsule) {
        viewModelScope.launch {
            repository.updateTimeCapsule(capsule.copy(isOpened = true))
            repository.setCompanionExpression(CompanionExpression.CELEBRATING)
        }
    }

    fun deleteTimeCapsule(id: Long) {
        viewModelScope.launch { repository.deleteTimeCapsule(id) }
    }

    // Chat
    fun sendChatMessage(text: String) {
        viewModelScope.launch {
            repository.sendChatMessage(text)
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChat()
        }
    }

    // Privacy & Vault
    fun unlockVault(pin: String): Boolean {
        val configured = appSettings.value.vaultPin
        return if (configured.isBlank() || configured == pin) {
            _vaultUnlocked.value = true
            true
        } else {
            false
        }
    }

    fun setVaultPin(newPin: String) {
        viewModelScope.launch {
            repository.saveAppSettings(appSettings.value.copy(vaultPin = newPin))
            _vaultUnlocked.value = true
        }
    }

    fun lockVault() {
        _vaultUnlocked.value = false
    }

    fun updateCompanionMemoryPermissions(
        memories: Boolean,
        journal: Boolean,
        mood: Boolean,
        goals: Boolean,
        schedule: Boolean,
        finance: Boolean,
        people: Boolean
    ) {
        viewModelScope.launch {
            val updated = appSettings.value.copy(
                companionCanRememberMemories = memories,
                companionCanRememberJournal = journal,
                companionCanRememberMood = mood,
                companionCanRememberGoals = goals,
                companionCanRememberSchedule = schedule,
                companionCanRememberFinance = finance,
                companionCanRememberPeople = people
            )
            repository.saveAppSettings(updated)
        }
    }

    // Backup & Restore
    fun exportBackup(onResult: (String) -> Unit) {
        viewModelScope.launch {
            val backupJson = repository.exportAlongBackupJson()
            _backupStatus.value = "Backup created successfully! Encrypted .along format."
            onResult(backupJson)
        }
    }

    fun restoreBackup(jsonString: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.restoreAlongBackupJson(jsonString)
            _backupStatus.value = if (success) "Restored successfully. Welcome back. 🌿" else "Restore failed: Invalid .along payload"
            onComplete(success)
        }
    }

    // Audio & Atmosphere Controls
    fun updateAudioSettings(musicOn: Boolean, ambientOn: Boolean, musicVol: Float, ambientVol: Float) {
        viewModelScope.launch {
            val updated = appSettings.value.copy(
                musicEnabled = musicOn,
                ambientSoundEnabled = ambientOn,
                musicVolume = musicVol,
                ambientVolume = ambientVol
            )
            repository.saveAppSettings(updated)
            audioEngine.updateSettings(musicOn, ambientOn, musicVol, ambientVol, updated.selectedTheme)
        }
    }

    fun updateAnimationQuality(quality: String) {
        viewModelScope.launch {
            val updated = appSettings.value.copy(animationQuality = quality)
            repository.saveAppSettings(updated)
        }
    }

    // Tutorial Hints
    fun dismissTutorialHint(key: String) {
        viewModelScope.launch {
            val current = appSettings.value.dismissedTutorialHints
            val set = current.split(",").filter { it.isNotBlank() }.toMutableSet()
            set.add(key)
            val updated = appSettings.value.copy(dismissedTutorialHints = set.joinToString(","))
            repository.saveAppSettings(updated)
        }
    }

    fun resetTutorialHints() {
        viewModelScope.launch {
            val updated = appSettings.value.copy(dismissedTutorialHints = "")
            repository.saveAppSettings(updated)
        }
    }

    fun isTutorialDismissed(key: String): Boolean {
        return appSettings.value.dismissedTutorialHints.split(",").contains(key)
    }

    override fun onCleared() {
        super.onCleared()
        audioEngine.stop()
    }
}
