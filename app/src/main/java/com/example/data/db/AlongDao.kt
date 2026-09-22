package com.example.data.db

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AlongDao {

    // --- Profile & Settings ---
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(profile: UserProfile)

    @Query("SELECT * FROM companion_profile WHERE id = 1 LIMIT 1")
    fun getCompanionProfile(): Flow<CompanionProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCompanionProfile(profile: CompanionProfile)

    @Query("SELECT * FROM app_settings WHERE id = 1 LIMIT 1")
    fun getAppSettings(): Flow<AppSettings?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAppSettings(settings: AppSettings)

    // --- Life Moments ---
    @Query("SELECT * FROM life_moments ORDER BY timestamp DESC")
    fun getAllLifeMoments(): Flow<List<LifeMoment>>

    @Query("SELECT * FROM life_moments WHERE isPrivate = 0 AND isSensitive = 0 AND allowResurfacing = 1 ORDER BY timestamp DESC")
    fun getResurfaceableMoments(): Flow<List<LifeMoment>>

    @Query("SELECT * FROM life_moments WHERE category = 'ACHIEVEMENT' OR category = 'CELEBRATION' ORDER BY timestamp DESC")
    fun getAchievements(): Flow<List<LifeMoment>>

    @Query("SELECT * FROM life_moments WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR tags LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchLifeMoments(query: String): Flow<List<LifeMoment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLifeMoment(moment: LifeMoment): Long

    @Update
    suspend fun updateLifeMoment(moment: LifeMoment)

    @Query("DELETE FROM life_moments WHERE id = :id")
    suspend fun deleteLifeMoment(id: Long)

    // --- Goals ---
    @Query("SELECT * FROM goals ORDER BY priority DESC, targetDate ASC")
    fun getAllGoals(): Flow<List<Goal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: Goal): Long

    @Update
    suspend fun updateGoal(goal: Goal)

    @Query("DELETE FROM goals WHERE id = :id")
    suspend fun deleteGoal(id: Long)

    // --- Habits ---
    @Query("SELECT * FROM habits WHERE active = 1 ORDER BY id ASC")
    fun getAllHabits(): Flow<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit): Long

    @Update
    suspend fun updateHabit(habit: Habit)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabit(id: Long)

    @Query("SELECT * FROM habit_logs")
    fun getAllHabitLogs(): Flow<List<HabitLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitLog(log: HabitLog): Long

    @Query("DELETE FROM habit_logs WHERE habitId = :habitId AND epochDay = :epochDay")
    suspend fun deleteHabitLog(habitId: Long, epochDay: Long)

    // --- Planner ---
    @Query("SELECT * FROM planner_items ORDER BY epochDay ASC, timeMinutes ASC")
    fun getAllPlannerItems(): Flow<List<PlannerItem>>

    @Query("SELECT * FROM planner_items WHERE epochDay = :epochDay ORDER BY timeMinutes ASC")
    fun getPlannerItemsForDay(epochDay: Long): Flow<List<PlannerItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlannerItem(item: PlannerItem): Long

    @Update
    suspend fun updatePlannerItem(item: PlannerItem)

    @Query("DELETE FROM planner_items WHERE id = :id")
    suspend fun deletePlannerItem(id: Long)

    // --- Finance ---
    @Query("SELECT * FROM finance_accounts ORDER BY id ASC")
    fun getFinanceAccounts(): Flow<List<FinanceAccount>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinanceAccount(account: FinanceAccount): Long

    @Update
    suspend fun updateFinanceAccount(account: FinanceAccount)

    @Query("SELECT * FROM finance_transactions ORDER BY timestamp DESC")
    fun getFinanceTransactions(): Flow<List<FinanceTransaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinanceTransaction(transaction: FinanceTransaction): Long

    @Query("DELETE FROM finance_transactions WHERE id = :id")
    suspend fun deleteFinanceTransaction(id: Long)

    @Query("SELECT * FROM finance_budgets")
    fun getBudgets(): Flow<List<Budget>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: Budget): Long

    // --- Important People ---
    @Query("SELECT * FROM important_people ORDER BY name ASC")
    fun getAllImportantPeople(): Flow<List<ImportantPerson>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImportantPerson(person: ImportantPerson): Long

    @Query("DELETE FROM important_people WHERE id = :id")
    suspend fun deleteImportantPerson(id: Long)

    // --- Time Capsules ---
    @Query("SELECT * FROM time_capsules ORDER BY unlockEpochMillis ASC")
    fun getAllTimeCapsules(): Flow<List<TimeCapsule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeCapsule(capsule: TimeCapsule): Long

    @Update
    suspend fun updateTimeCapsule(capsule: TimeCapsule)

    @Query("DELETE FROM time_capsules WHERE id = :id")
    suspend fun deleteTimeCapsule(id: Long)

    // --- Chat Messages ---
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllChatMessages(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessage): Long

    @Query("DELETE FROM chat_messages")
    suspend fun clearChatMessages()

    // --- Raw fetch for Backup Export ---
    @Query("SELECT * FROM user_profile LIMIT 1")
    suspend fun fetchUserProfileOnce(): UserProfile?

    @Query("SELECT * FROM companion_profile LIMIT 1")
    suspend fun fetchCompanionProfileOnce(): CompanionProfile?

    @Query("SELECT * FROM app_settings LIMIT 1")
    suspend fun fetchAppSettingsOnce(): AppSettings?

    @Query("SELECT * FROM life_moments")
    suspend fun fetchAllLifeMomentsOnce(): List<LifeMoment>

    @Query("SELECT * FROM goals")
    suspend fun fetchAllGoalsOnce(): List<Goal>

    @Query("SELECT * FROM habits")
    suspend fun fetchAllHabitsOnce(): List<Habit>

    @Query("SELECT * FROM habit_logs")
    suspend fun fetchAllHabitLogsOnce(): List<HabitLog>

    @Query("SELECT * FROM planner_items")
    suspend fun fetchAllPlannerItemsOnce(): List<PlannerItem>

    @Query("SELECT * FROM finance_accounts")
    suspend fun fetchAllFinanceAccountsOnce(): List<FinanceAccount>

    @Query("SELECT * FROM finance_transactions")
    suspend fun fetchAllFinanceTransactionsOnce(): List<FinanceTransaction>

    @Query("SELECT * FROM finance_budgets")
    suspend fun fetchAllBudgetsOnce(): List<Budget>

    @Query("SELECT * FROM important_people")
    suspend fun fetchAllImportantPeopleOnce(): List<ImportantPerson>

    @Query("SELECT * FROM time_capsules")
    suspend fun fetchAllTimeCapsulesOnce(): List<TimeCapsule>

    @Query("SELECT * FROM chat_messages")
    suspend fun fetchAllChatMessagesOnce(): List<ChatMessage>
}
