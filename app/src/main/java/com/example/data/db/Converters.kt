package com.example.data.db

import androidx.room.TypeConverter
import com.example.data.model.*

class AlongConverters {
    @TypeConverter
    fun fromCompanionExpression(value: CompanionExpression?): String = value?.name ?: CompanionExpression.PEACEFUL.name

    @TypeConverter
    fun toCompanionExpression(value: String?): CompanionExpression =
        try { CompanionExpression.valueOf(value ?: CompanionExpression.PEACEFUL.name) }
        catch (e: Exception) { CompanionExpression.PEACEFUL }

    @TypeConverter
    fun fromRelationshipStyle(value: RelationshipStyle?): String = value?.name ?: RelationshipStyle.CLOSE_COMPANION.name

    @TypeConverter
    fun toRelationshipStyle(value: String?): RelationshipStyle =
        try { RelationshipStyle.valueOf(value ?: RelationshipStyle.CLOSE_COMPANION.name) }
        catch (e: Exception) { RelationshipStyle.CLOSE_COMPANION }

    @TypeConverter
    fun fromWorldTheme(value: WorldTheme?): String = value?.name ?: WorldTheme.FOREST_MORNING.name

    @TypeConverter
    fun toWorldTheme(value: String?): WorldTheme =
        try { WorldTheme.valueOf(value ?: WorldTheme.FOREST_MORNING.name) }
        catch (e: Exception) { WorldTheme.FOREST_MORNING }

    @TypeConverter
    fun fromLifeMomentCategory(value: LifeMomentCategory?): String = value?.name ?: LifeMomentCategory.MEMORY.name

    @TypeConverter
    fun toLifeMomentCategory(value: String?): LifeMomentCategory =
        try { LifeMomentCategory.valueOf(value ?: LifeMomentCategory.MEMORY.name) }
        catch (e: Exception) { LifeMomentCategory.MEMORY }

    @TypeConverter
    fun fromEmotionType(value: EmotionType?): String = value?.name ?: EmotionType.PEACEFUL.name

    @TypeConverter
    fun toEmotionType(value: String?): EmotionType =
        try { EmotionType.valueOf(value ?: EmotionType.PEACEFUL.name) }
        catch (e: Exception) { EmotionType.PEACEFUL }

    @TypeConverter
    fun fromGoalCategory(value: GoalCategory?): String = value?.name ?: GoalCategory.PERSONAL.name

    @TypeConverter
    fun toGoalCategory(value: String?): GoalCategory =
        try { GoalCategory.valueOf(value ?: GoalCategory.PERSONAL.name) }
        catch (e: Exception) { GoalCategory.PERSONAL }

    @TypeConverter
    fun fromGoalStatus(value: GoalStatus?): String = value?.name ?: GoalStatus.ACTIVE.name

    @TypeConverter
    fun toGoalStatus(value: String?): GoalStatus =
        try { GoalStatus.valueOf(value ?: GoalStatus.ACTIVE.name) }
        catch (e: Exception) { GoalStatus.ACTIVE }

    @TypeConverter
    fun fromPlannerItemType(value: PlannerItemType?): String = value?.name ?: PlannerItemType.TASK.name

    @TypeConverter
    fun toPlannerItemType(value: String?): PlannerItemType =
        try { PlannerItemType.valueOf(value ?: PlannerItemType.TASK.name) }
        catch (e: Exception) { PlannerItemType.TASK }

    @TypeConverter
    fun fromTransactionType(value: TransactionType?): String = value?.name ?: TransactionType.EXPENSE.name

    @TypeConverter
    fun toTransactionType(value: String?): TransactionType =
        try { TransactionType.valueOf(value ?: TransactionType.EXPENSE.name) }
        catch (e: Exception) { TransactionType.EXPENSE }

    @TypeConverter
    fun fromChatSender(value: ChatSender?): String = value?.name ?: ChatSender.COMPANION.name

    @TypeConverter
    fun toChatSender(value: String?): ChatSender =
        try { ChatSender.valueOf(value ?: ChatSender.COMPANION.name) }
        catch (e: Exception) { ChatSender.COMPANION }
}
