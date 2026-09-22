package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.example.data.model.ChatMessage
import com.example.data.model.ChatSender
import com.example.data.model.CompanionExpression
import com.example.ui.AlongViewModel
import com.example.ui.components.CompanionVisual
import kotlinx.coroutines.launch

@Composable
fun CompanionScreen(
    viewModel: AlongViewModel,
    onCustomizeCompanionClick: () -> Unit
) {
    val companionProfile by viewModel.companionProfile.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()

    var inputMessage by remember { mutableStateOf("") }
    var selectedExplanation by remember { mutableStateOf<String?>(null) }
    var showExpressionPicker by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Auto scroll when messages change
    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        val isTablet = maxWidth >= 600.dp

        if (isTablet) {
            // ================= TABLET 2-PANE RESPONSIVE LAYOUT =================
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Left: Chat Conversation Area
                Column(
                    modifier = Modifier
                        .weight(1.3f)
                        .fillMaxHeight()
                ) {
                    // Chat Messages List
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(chatMessages) { message ->
                            ChatMessageItem(
                                message = message,
                                companionName = companionProfile.name,
                                maxBubbleWidth = 440.dp,
                                onWhyMentionedClick = { explanation ->
                                    selectedExplanation = explanation
                                }
                            )
                        }
                    }

                    // Quick Prompts Suggestions
                    QuickSuggestionsRow(onSelectPrompt = { text ->
                        viewModel.sendChatMessage(text)
                    })

                    Spacer(modifier = Modifier.height(8.dp))

                    // Message Input Bar
                    ChatInputBar(
                        inputMessage = inputMessage,
                        companionName = companionProfile.name,
                        onMessageChange = { inputMessage = it },
                        onSend = {
                            val text = inputMessage.trim()
                            if (text.isNotBlank()) {
                                viewModel.sendChatMessage(text)
                                inputMessage = ""
                            }
                        }
                    )
                }

                // Right: Dedicated Companion Status & Context Pane
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                    ),
                    modifier = Modifier
                        .width(320.dp)
                        .fillMaxHeight()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Companion Presence",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        CompanionVisual(
                            name = companionProfile.name,
                            expression = companionProfile.currentExpression,
                            size = 120.dp,
                            speechBubbleText = when (companionProfile.currentExpression) {
                                CompanionExpression.CELEBRATING -> "Celebrating life's milestones!"
                                CompanionExpression.SLEEPY -> "Night is quiet. Sleep peacefully."
                                CompanionExpression.CONCERNED -> "I am right here with you."
                                CompanionExpression.THINKING -> "Quietly listening to your story..."
                                CompanionExpression.PROUD -> "You are doing wonderful."
                                else -> "Walking gently along beside you."
                            }
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = companionProfile.name,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "${companionProfile.relationshipStyle.title} • ${companionProfile.currentExpression.displayName} ${companionProfile.currentExpression.emoji}",
                                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
                            )
                        }

                        Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                        // Switch Companion Expressions
                        Text(
                            text = "Expressive Reaction",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        )
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                listOf(CompanionExpression.HAPPY, CompanionExpression.VERY_HAPPY, CompanionExpression.PEACEFUL, CompanionExpression.PROUD).forEach { expr ->
                                    ExpressionQuickChip(expr, companionProfile.currentExpression) {
                                        viewModel.setCompanionExpression(expr)
                                    }
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                listOf(CompanionExpression.THINKING, CompanionExpression.CONCERNED, CompanionExpression.SLEEPY, CompanionExpression.CELEBRATING).forEach { expr ->
                                    ExpressionQuickChip(expr, companionProfile.currentExpression) {
                                        viewModel.setCompanionExpression(expr)
                                    }
                                }
                            }
                        }

                        Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                        // Offline & Privacy Guarantee Card
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "🛡️ Companion Memory is Private",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "All chat history and memory associations remain 100% on this device. No servers, no tracking.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = onCustomizeCompanionClick,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Customize", fontSize = 12.sp)
                            }
                            OutlinedButton(
                                onClick = { viewModel.clearChat() },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Clear", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        } else {
            // ================= PHONE SINGLE-COLUMN LAYOUT =================
            Column(modifier = Modifier.fillMaxSize()) {
                // Companion Status Banner
                Surface(
                    tonalElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            CompanionVisual(
                                name = companionProfile.name,
                                expression = companionProfile.currentExpression,
                                size = 46.dp,
                                showExpressionBadge = false
                            )
                            Column {
                                Text(
                                    text = companionProfile.name,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "${companionProfile.relationshipStyle.title} • ${companionProfile.currentExpression.displayName} ${companionProfile.currentExpression.emoji}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary)
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            // Quick Expression Changer
                            IconButton(
                                onClick = { showExpressionPicker = !showExpressionPicker },
                                modifier = Modifier.testTag("expression_picker_toggle")
                            ) {
                                Text(text = companionProfile.currentExpression.emoji, fontSize = 20.sp)
                            }

                            // Customize Persona
                            IconButton(
                                onClick = onCustomizeCompanionClick,
                                modifier = Modifier.testTag("customize_companion_button")
                            ) {
                                Icon(imageVector = Icons.Default.Tune, contentDescription = "Customize Companion")
                            }

                            // Clear Chat
                            IconButton(
                                onClick = { viewModel.clearChat() }
                            ) {
                                Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Clear Chat")
                            }
                        }
                    }
                }

                // Expression Picker Panel (Collapsible)
                AnimatedVisibility(visible = showExpressionPicker) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LazyRow(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(CompanionExpression.values()) { expr ->
                                FilterChip(
                                    selected = companionProfile.currentExpression == expr,
                                    onClick = {
                                        viewModel.setCompanionExpression(expr)
                                        showExpressionPicker = false
                                    },
                                    label = { Text("${expr.emoji} ${expr.displayName}", fontSize = 12.sp) }
                                )
                            }
                        }
                    }
                }

                // Chat Messages List
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(chatMessages) { message ->
                        ChatMessageItem(
                            message = message,
                            companionName = companionProfile.name,
                            maxBubbleWidth = 280.dp,
                            onWhyMentionedClick = { explanation ->
                                selectedExplanation = explanation
                            }
                        )
                    }
                }

                // Quick Prompts suggestions
                QuickSuggestionsRow(onSelectPrompt = { text ->
                    viewModel.sendChatMessage(text)
                })

                // Message Input Bar
                ChatInputBar(
                    inputMessage = inputMessage,
                    companionName = companionProfile.name,
                    onMessageChange = { inputMessage = it },
                    onSend = {
                        val text = inputMessage.trim()
                        if (text.isNotBlank()) {
                            viewModel.sendChatMessage(text)
                            inputMessage = ""
                        }
                    }
                )
            }
        }
    }

    // "Why did you mention this?" Memory Transparency Dialog
    if (selectedExplanation != null) {
        AlertDialog(
            onDismissRequest = { selectedExplanation = null },
            icon = { Icon(imageVector = Icons.Default.Info, contentDescription = null) },
            title = { Text("Why Along Mentioned This") },
            text = {
                Text(
                    text = selectedExplanation!!,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = { selectedExplanation = null }) {
                    Text("Understood")
                }
            }
        )
    }
}

@Composable
private fun ExpressionQuickChip(
    expr: CompanionExpression,
    selected: CompanionExpression,
    onClick: () -> Unit
) {
    Surface(
        shape = CircleShape,
        color = if (expr == selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .size(38.dp)
            .clickable(onClick = onClick)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = expr.emoji, fontSize = 18.sp)
        }
    }
}

@Composable
private fun QuickSuggestionsRow(onSelectPrompt: (String) -> Unit) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        val suggestions = listOf(
            "What am I doing tomorrow? 📅",
            "Show me a good memory 🌸",
            "I achieved something! 🏆",
            "How much have I spent? 💰",
            "I'm feeling down today 🌧️",
            "Good night, rest well 🌙"
        )
        items(suggestions) { prompt ->
            SuggestionChip(
                onClick = {
                    val cleanText = prompt.substringBeforeLast(" ")
                    onSelectPrompt(cleanText)
                },
                label = { Text(prompt, fontSize = 12.sp) }
            )
        }
    }
}

@Composable
private fun ChatInputBar(
    inputMessage: String,
    companionName: String,
    onMessageChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Surface(
        tonalElevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputMessage,
                onValueChange = onMessageChange,
                placeholder = { Text("Talk with $companionName...") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_field"),
                shape = RoundedCornerShape(20.dp),
                maxLines = 3
            )

            FilledIconButton(
                onClick = onSend,
                modifier = Modifier
                    .size(46.dp)
                    .testTag("chat_send_button"),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send message",
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun ChatMessageItem(
    message: ChatMessage,
    companionName: String,
    maxBubbleWidth: androidx.compose.ui.unit.Dp,
    onWhyMentionedClick: (String) -> Unit
) {
    val isUser = message.sender == ChatSender.USER

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .size(32.dp)
                    .padding(top = 4.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = message.expression.emoji, fontSize = 16.sp)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier.widthIn(max = maxBubbleWidth),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            Surface(
                shape = RoundedCornerShape(
                    topStart = 18.dp,
                    topEnd = 18.dp,
                    bottomStart = if (isUser) 18.dp else 4.dp,
                    bottomEnd = if (isUser) 4.dp else 18.dp
                ),
                color = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = message.text,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 20.sp
                        )
                    )

                    // Context Card if any (Memory, Achievement, Finance, Schedule)
                    if (message.cardType != "NONE" && message.cardTitle.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = message.cardTitle,
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                )
                                if (message.cardSnippet.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = message.cardSnippet,
                                        style = MaterialTheme.typography.bodySmall,
                                        maxLines = 3
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Transparency button: "Why did you mention this?"
            if (!isUser && message.memoryExplanation.isNotBlank()) {
                TextButton(
                    onClick = { onWhyMentionedClick(message.memoryExplanation) },
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                    modifier = Modifier.height(26.dp)
                ) {
                    Text(
                        text = "Why did you mention this? 🔍",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                        )
                    )
                }
            }
        }
    }
}
