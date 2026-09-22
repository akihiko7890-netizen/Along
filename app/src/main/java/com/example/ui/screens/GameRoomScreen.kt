package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CompanionExpression
import com.example.data.model.WorldTheme
import com.example.ui.AlongViewModel
import com.example.ui.components.CompanionVisual
import com.example.util.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class GameCategory(val title: String) {
    PLAY_TOGETHER("Play Together"),
    RELAX("Relax"),
    HAVE_FUN("Have Fun")
}

data class GameItem(
    val id: String,
    val title: String,
    val category: GameCategory,
    val iconEmoji: String,
    val description: String
)

val gameList = listOf(
    // Play Together
    GameItem("chess", "Chess", GameCategory.PLAY_TOGETHER, "♟️", "Classic strategy. Play vs companion or 2 players."),
    GameItem("tic_tac_toe", "Tic-Tac-Toe", GameCategory.PLAY_TOGETHER, "❌", "Cozy three-in-a-row with your companion."),
    GameItem("connect_four", "Connect Four", GameCategory.PLAY_TOGETHER, "🔴", "Four-in-a-row disc dropping game."),
    GameItem("rock_paper_scissors", "Rock Paper Scissors", GameCategory.PLAY_TOGETHER, "✊", "Quick playful rounds of fun."),

    // Relax
    GameItem("memory_match", "Memory Match", GameCategory.RELAX, "🧠", "Flip and match peaceful nature symbols."),
    GameItem("doodle", "Peaceful Doodle", GameCategory.RELAX, "✏️", "A tranquil canvas to sketch, paint and relax."),

    // Have Fun
    GameItem("word_guess", "Cozy Word Guess", GameCategory.HAVE_FUN, "🔤", "Guess soothing words of comfort and nature."),
    GameItem("would_you_rather", "Would You Rather", GameCategory.HAVE_FUN, "🎭", "Thoughtful cozy questions to ponder together.")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameRoomScreen(
    viewModel: AlongViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var activeGameId by remember { mutableStateOf<String?>(null) }
    val companionProfile by viewModel.companionProfile.collectAsState()
    val appSettings by viewModel.appSettings.collectAsState()

    var selectedCategory by remember { mutableStateOf(GameCategory.PLAY_TOGETHER) }
    var companionReactionsEnabled by remember { mutableStateOf(true) }

    // Live companion dialogue & expression during games
    var liveReactionQuote by remember { mutableStateOf("Ready whenever you are! 🌿") }
    var liveExpression by remember { mutableStateOf(CompanionExpression.HAPPY) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (activeGameId == null) "Let's Play 🎮" else (gameList.find { it.id == activeGameId }?.title ?: "Game"),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (activeGameId != null) activeGameId = null else onBack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (activeGameId != null) {
                        // Companion Reactions Toggle
                        IconButton(onClick = { companionReactionsEnabled = !companionReactionsEnabled }) {
                            Icon(
                                if (companionReactionsEnabled) Icons.Default.ChatBubble else Icons.Default.ChatBubbleOutline,
                                contentDescription = "Toggle Companion Reactions",
                                tint = if (companionReactionsEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        BoxWithConstraints(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val isTablet = maxWidth >= 600.dp

            if (activeGameId != null) {
                // Active Game Screen
                Row(modifier = Modifier.fillMaxSize()) {
                    // Left/Main: Game Board
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        when (activeGameId) {
                            "chess" -> ChessGameView(
                                companionName = companionProfile.name,
                                reactionsEnabled = companionReactionsEnabled,
                                worldTheme = appSettings.selectedTheme,
                                onCompanionReact = { quote, expr ->
                                    liveReactionQuote = quote
                                    liveExpression = expr
                                }
                            )
                            "tic_tac_toe" -> TicTacToeView(
                                companionName = companionProfile.name,
                                reactionsEnabled = companionReactionsEnabled,
                                onCompanionReact = { quote, expr ->
                                    liveReactionQuote = quote
                                    liveExpression = expr
                                }
                            )
                            "connect_four" -> ConnectFourView(
                                companionName = companionProfile.name,
                                reactionsEnabled = companionReactionsEnabled,
                                onCompanionReact = { quote, expr ->
                                    liveReactionQuote = quote
                                    liveExpression = expr
                                }
                            )
                            "memory_match" -> MemoryMatchView(
                                companionName = companionProfile.name,
                                onCompanionReact = { quote, expr ->
                                    liveReactionQuote = quote
                                    liveExpression = expr
                                }
                            )
                            "rock_paper_scissors" -> RockPaperScissorsView(
                                companionName = companionProfile.name,
                                onCompanionReact = { quote, expr ->
                                    liveReactionQuote = quote
                                    liveExpression = expr
                                }
                            )
                            "doodle" -> DoodleCanvasView()
                            "word_guess" -> WordGuessView(
                                companionName = companionProfile.name,
                                onCompanionReact = { quote, expr ->
                                    liveReactionQuote = quote
                                    liveExpression = expr
                                }
                            )
                            "would_you_rather" -> WouldYouRatherView(
                                companionName = companionProfile.name
                            )
                        }
                    }

                    // Tablet: Companion presence beside the game board
                    if (isTablet && companionReactionsEnabled) {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                            ),
                            modifier = Modifier
                                .width(280.dp)
                                .fillMaxHeight()
                                .padding(12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CompanionVisual(
                                    name = companionProfile.name,
                                    expression = liveExpression,
                                    size = 110.dp
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = companionProfile.name,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    tonalElevation = 2.dp,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = liveReactionQuote,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(12.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Game Room Lobby
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        // Friendly Companion Greeting Card
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                CompanionVisual(
                                    name = companionProfile.name,
                                    expression = CompanionExpression.HAPPY,
                                    size = 64.dp
                                )
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "${companionProfile.name}'s Game Room 🌿",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "\"Want to play something together? No pressure, no internet, just pure relaxation.\"",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.85f)
                                    )
                                }
                            }
                        }
                    }

                    // Category Tabs
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            GameCategory.values().forEach { category ->
                                val isSelected = selectedCategory == category
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedCategory = category },
                                    label = { Text(category.title) },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // Games Grid (1-2 columns phone, 2-3 columns tablet)
                    val gamesInCategory = gameList.filter { it.category == selectedCategory }
                    item {
                        val columns = if (maxWidth >= 720.dp) 3 else if (maxWidth >= 420.dp) 2 else 1
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(columns),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 800.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            userScrollEnabled = false
                        ) {
                            items(gamesInCategory, key = { it.id }) { game ->
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { activeGameId = game.id }
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(text = game.iconEmoji, fontSize = 32.sp)
                                        Text(
                                            text = game.title,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = game.description,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            minLines = 2
                                        )
                                        Button(
                                            onClick = { activeGameId = game.id },
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("Play")
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
}

// ============================================================================
// 1. CHESS GAME VIEW (REQUIRED)
// ============================================================================
@Composable
fun ChessGameView(
    companionName: String,
    reactionsEnabled: Boolean,
    worldTheme: WorldTheme,
    onCompanionReact: (String, CompanionExpression) -> Unit
) {
    val chess = remember { ChessGame() }
    var boardRefreshTrigger by remember { mutableStateOf(0) }
    var selectedCell by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var validMoves by remember { mutableStateOf<List<Pair<Int, Int>>>(emptyList()) }

    var isTwoPlayerMode by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf(ChessDifficulty.NORMAL) }
    var isCompanionThinking by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    // Board theme colors
    val lightSquareColor = when (worldTheme) {
        WorldTheme.SAKURA_GARDEN -> Color(0xFFF9ECEF)
        WorldTheme.FOREST_MORNING -> Color(0xFFEAF1EB)
        else -> Color(0xFFF0E8D8)
    }
    val darkSquareColor = when (worldTheme) {
        WorldTheme.SAKURA_GARDEN -> Color(0xFFDCAEBA)
        WorldTheme.FOREST_MORNING -> Color(0xFF90B59B)
        else -> Color(0xFFB58863)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Mode & Settings Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                FilterChip(
                    selected = !isTwoPlayerMode,
                    onClick = {
                        isTwoPlayerMode = false
                        chess.resetGame()
                        boardRefreshTrigger++
                        onCompanionReact("Good luck! I'm ready ♟️", CompanionExpression.HAPPY)
                    },
                    label = { Text("vs $companionName") },
                    shape = RoundedCornerShape(10.dp)
                )
                FilterChip(
                    selected = isTwoPlayerMode,
                    onClick = {
                        isTwoPlayerMode = true
                        chess.resetGame()
                        boardRefreshTrigger++
                    },
                    label = { Text("2 Players") },
                    shape = RoundedCornerShape(10.dp)
                )
            }

            if (!isTwoPlayerMode) {
                var showDiffMenu by remember { mutableStateOf(false) }
                Box {
                    OutlinedButton(
                        onClick = { showDiffMenu = true },
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(difficulty.name.lowercase().replaceFirstChar { it.uppercase() }, fontSize = 12.sp)
                    }
                    DropdownMenu(
                        expanded = showDiffMenu,
                        onDismissRequest = { showDiffMenu = false }
                    ) {
                        ChessDifficulty.values().forEach { diff ->
                            DropdownMenuItem(
                                text = { Text(diff.name.lowercase().replaceFirstChar { it.uppercase() }) },
                                onClick = {
                                    difficulty = diff
                                    showDiffMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Status Card
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (chess.isGameOver) {
                        "Winner: ${if (chess.winner == PieceColor.WHITE) "White (You)" else "Black ($companionName)"}! 🎉"
                    } else {
                        "Turn: ${if (chess.currentTurn == PieceColor.WHITE) "White (You)" else "Black ($companionName)"}"
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                OutlinedButton(
                    onClick = {
                        chess.resetGame()
                        selectedCell = null
                        validMoves = emptyList()
                        boardRefreshTrigger++
                        onCompanionReact("Fresh board! Let's play 🌿", CompanionExpression.PEACEFUL)
                    },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                ) {
                    Text("Restart", fontSize = 11.sp)
                }
            }
        }

        // 8x8 Chess Board View
        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .border(2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            val tileSize = maxWidth / 8

            Column(modifier = Modifier.fillMaxSize()) {
                for (r in 0..7) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        for (c in 0..7) {
                            val isDark = (r + c) % 2 == 1
                            val isSelected = selectedCell?.first == r && selectedCell?.second == c
                            val isValidTarget = validMoves.contains(r to c)
                            val piece = chess.getPiece(r, c)

                            val bgColor = when {
                                isSelected -> Color(0xFFF6E58D)
                                isValidTarget -> Color(0xFFB8E994)
                                isDark -> darkSquareColor
                                else -> lightSquareColor
                            }

                            Box(
                                modifier = Modifier
                                    .size(tileSize)
                                    .background(bgColor)
                                    .clickable {
                                        if (chess.isGameOver || isCompanionThinking) return@clickable

                                        if (selectedCell == null) {
                                            if (piece != null && piece.color == chess.currentTurn) {
                                                selectedCell = r to c
                                                validMoves = chess.getValidMovesFor(r, c)
                                            }
                                        } else {
                                            val (fromR, fromC) = selectedCell!!
                                            if (fromR == r && fromC == c) {
                                                selectedCell = null
                                                validMoves = emptyList()
                                            } else if (validMoves.contains(r to c)) {
                                                val move = chess.makeMove(fromR, fromC, r, c)
                                                selectedCell = null
                                                validMoves = emptyList()
                                                boardRefreshTrigger++

                                                if (reactionsEnabled && move != null) {
                                                    if (move.capturedPiece != null) {
                                                        onCompanionReact("Oh! That was clever 😳 Nice capture.", CompanionExpression.SURPRISED)
                                                    } else {
                                                        onCompanionReact("Hmm, interesting move 🤔", CompanionExpression.THINKING)
                                                    }
                                                }

                                                // Companion's Turn
                                                if (!isTwoPlayerMode && !chess.isGameOver && chess.currentTurn == PieceColor.BLACK) {
                                                    isCompanionThinking = true
                                                    onCompanionReact("Let me see... 🤔", CompanionExpression.THINKING)
                                                    coroutineScope.launch {
                                                        delay(700)
                                                        val compMove = chess.computeCompanionMove(difficulty)
                                                        isCompanionThinking = false
                                                        boardRefreshTrigger++

                                                        if (chess.isGameOver) {
                                                            if (chess.winner == PieceColor.BLACK) {
                                                                onCompanionReact("Good game! That was really fun 😄", CompanionExpression.VERY_HAPPY)
                                                            } else {
                                                                onCompanionReact("Okay... that was actually really good 😳 Rematch?", CompanionExpression.PROUD)
                                                            }
                                                        } else if (compMove?.capturedPiece != null) {
                                                            onCompanionReact("Found an opening! 😊", CompanionExpression.HAPPY)
                                                        } else {
                                                            onCompanionReact("Your turn! 🌿", CompanionExpression.PEACEFUL)
                                                        }
                                                    }
                                                }
                                            } else if (piece != null && piece.color == chess.currentTurn) {
                                                selectedCell = r to c
                                                validMoves = chess.getValidMovesFor(r, c)
                                            } else {
                                                selectedCell = null
                                                validMoves = emptyList()
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (piece != null) {
                                    val symbol = if (piece.color == PieceColor.WHITE) piece.type.symbolWhite else piece.type.symbolBlack
                                    val pieceColor = if (piece.color == PieceColor.WHITE) Color.White else Color(0xFF1E272E)
                                    Text(
                                        text = symbol,
                                        fontSize = (tileSize.value * 0.65f).sp,
                                        color = pieceColor,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                if (isValidTarget && piece == null) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFF44BD32).copy(alpha = 0.6f),
                                        modifier = Modifier.size(tileSize * 0.3f)
                                    ) {}
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ============================================================================
// 2. TIC-TAC-TOE VIEW
// ============================================================================
@Composable
fun TicTacToeView(
    companionName: String,
    reactionsEnabled: Boolean,
    onCompanionReact: (String, CompanionExpression) -> Unit
) {
    val board = remember { mutableStateListOf("", "", "", "", "", "", "", "", "") }
    var currentTurn by remember { mutableStateOf("X") } // X = User, O = Companion
    var winner by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    fun checkWinner(): String? {
        val wins = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6)
        )
        for (w in wins) {
            if (board[w[0]].isNotEmpty() && board[w[0]] == board[w[1]] && board[w[1]] == board[w[2]]) {
                return board[w[0]]
            }
        }
        if (board.none { it.isEmpty() }) return "Tie"
        return null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = when (winner) {
                "X" -> "You won! 🎉"
                "O" -> "$companionName won! 😄"
                "Tie" -> "It's a cozy tie! 🌿"
                else -> "Turn: ${if (currentTurn == "X") "You (X)" else "$companionName (O)"}"
            },
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 3x3 Grid
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.size(300.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                for (r in 0..2) {
                    Row(modifier = Modifier.weight(1f)) {
                        for (c in 0..2) {
                            val idx = r * 3 + c
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant)
                                    .clickable {
                                        if (board[idx].isEmpty() && winner == null && currentTurn == "X") {
                                            board[idx] = "X"
                                            winner = checkWinner()
                                            if (winner == null) {
                                                currentTurn = "O"
                                                coroutineScope.launch {
                                                    delay(450)
                                                    val empties = board.indices.filter { board[it].isEmpty() }
                                                    if (empties.isNotEmpty()) {
                                                        board[empties.random()] = "O"
                                                        winner = checkWinner()
                                                        currentTurn = "X"
                                                        if (winner == "O" && reactionsEnabled) {
                                                            onCompanionReact("Yay! That was a close game 😄", CompanionExpression.VERY_HAPPY)
                                                        }
                                                    }
                                                }
                                            } else if (winner == "X" && reactionsEnabled) {
                                                onCompanionReact("Nice win! You're really good 🥹", CompanionExpression.PROUD)
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = board[idx],
                                    fontSize = 42.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (board[idx] == "X") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                for (i in 0..8) board[i] = ""
                currentTurn = "X"
                winner = null
            },
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Play Again")
        }
    }
}

// ============================================================================
// 3. CONNECT FOUR VIEW
// ============================================================================
@Composable
fun ConnectFourView(
    companionName: String,
    reactionsEnabled: Boolean,
    onCompanionReact: (String, CompanionExpression) -> Unit
) {
    // 6 rows x 7 cols
    val grid = remember { mutableStateListOf(*Array(42) { 0 }) } // 0 = empty, 1 = User (Red), 2 = Comp (Yellow)
    var currentTurn by remember { mutableStateOf(1) }
    var winner by remember { mutableStateOf<Int?>(null) }
    val coroutineScope = rememberCoroutineScope()

    fun checkWin(p: Int): Boolean {
        // Horiz, vert, diag
        for (r in 0..5) {
            for (c in 0..3) {
                if (grid[r * 7 + c] == p && grid[r * 7 + c + 1] == p && grid[r * 7 + c + 2] == p && grid[r * 7 + c + 3] == p) return true
            }
        }
        for (r in 0..2) {
            for (c in 0..6) {
                if (grid[r * 7 + c] == p && grid[(r + 1) * 7 + c] == p && grid[(r + 2) * 7 + c] == p && grid[(r + 3) * 7 + c] == p) return true
            }
        }
        return false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = when (winner) {
                1 -> "You Connected Four! 🔴🎉"
                2 -> "$companionName won! 🟡"
                else -> "Turn: ${if (currentTurn == 1) "You (Red)" else "$companionName (Yellow)"}"
            },
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 7x6 Board
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2980B9)),
            modifier = Modifier
                .width(320.dp)
                .height(280.dp)
                .padding(8.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                for (r in 0..5) {
                    Row(modifier = Modifier.weight(1f)) {
                        for (c in 0..6) {
                            val idx = r * 7 + c
                            val cell = grid[idx]
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .padding(3.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (cell) {
                                            1 -> Color(0xFFE74C3C)
                                            2 -> Color(0xFFF1C40F)
                                            else -> Color.White
                                        }
                                    )
                                    .clickable {
                                        if (winner == null && currentTurn == 1) {
                                            // drop into lowest row in column c
                                            for (row in 5 downTo 0) {
                                                val target = row * 7 + c
                                                if (grid[target] == 0) {
                                                    grid[target] = 1
                                                    if (checkWin(1)) {
                                                        winner = 1
                                                        if (reactionsEnabled) onCompanionReact("Awesome connect! 🥹", CompanionExpression.PROUD)
                                                    } else {
                                                        currentTurn = 2
                                                        coroutineScope.launch {
                                                            delay(500)
                                                            // companion picks random valid column
                                                            val validCols = (0..6).filter { col -> grid[col] == 0 }
                                                            if (validCols.isNotEmpty()) {
                                                                val compCol = validCols.random()
                                                                for (compRow in 5 downTo 0) {
                                                                    val ct = compRow * 7 + compCol
                                                                    if (grid[ct] == 0) {
                                                                        grid[ct] = 2
                                                                        if (checkWin(2)) {
                                                                            winner = 2
                                                                            if (reactionsEnabled) onCompanionReact("I got four! Good match 😊", CompanionExpression.HAPPY)
                                                                        }
                                                                        currentTurn = 1
                                                                        break
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    break
                                                }
                                            }
                                        }
                                    }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                for (i in 0..41) grid[i] = 0
                currentTurn = 1
                winner = null
            },
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Reset Board")
        }
    }
}

// ============================================================================
// 4. MEMORY MATCH
// ============================================================================
@Composable
fun MemoryMatchView(
    companionName: String,
    onCompanionReact: (String, CompanionExpression) -> Unit
) {
    val icons = remember { listOf("🌸", "🌿", "⭐", "🍃", "💖", "☕", "🌙", "🦋") }
    val cards = remember { (icons + icons).shuffled().toMutableStateList() }
    val revealed = remember { mutableStateListOf(*Array(16) { false }) }
    val matched = remember { mutableStateListOf(*Array(16) { false }) }

    var selectedFirst by remember { mutableStateOf<Int?>(null) }
    var turns by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Turns: $turns  •  Matched: ${matched.count { it } / 2} / 8",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 4x4 Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.size(300.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = false
        ) {
            items(16) { idx ->
                val isVisible = revealed[idx] || matched[idx]
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (matched[idx]) Color(0xFFE8F5E9) else if (revealed[idx]) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clickable {
                            if (!revealed[idx] && !matched[idx]) {
                                if (selectedFirst == null) {
                                    selectedFirst = idx
                                    revealed[idx] = true
                                } else {
                                    val first = selectedFirst!!
                                    revealed[idx] = true
                                    selectedFirst = null
                                    turns++

                                    if (cards[first] == cards[idx]) {
                                        matched[first] = true
                                        matched[idx] = true
                                        onCompanionReact("Matched a pair! 🌸", CompanionExpression.HAPPY)
                                    } else {
                                        coroutineScope.launch {
                                            delay(700)
                                            revealed[first] = false
                                            revealed[idx] = false
                                        }
                                    }
                                }
                            }
                        }
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = if (isVisible) cards[idx] else "🌱",
                            fontSize = 28.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val newShuffled = (icons + icons).shuffled()
                cards.clear()
                cards.addAll(newShuffled)
                for (i in 0..15) {
                    revealed[i] = false
                    matched[i] = false
                }
                selectedFirst = null
                turns = 0
            },
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("New Cards")
        }
    }
}

// ============================================================================
// 5. ROCK PAPER SCISSORS
// ============================================================================
@Composable
fun RockPaperScissorsView(
    companionName: String,
    onCompanionReact: (String, CompanionExpression) -> Unit
) {
    val options = listOf("Rock ✊", "Paper ✋", "Scissors ✌️")
    var userChoice by remember { mutableStateOf<String?>(null) }
    var compChoice by remember { mutableStateOf<String?>(null) }
    var resultText by remember { mutableStateOf("Choose your play!") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = resultText,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (compChoice != null) {
            Text("You: $userChoice  vs  $companionName: $compChoice", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            options.forEach { opt ->
                Button(
                    onClick = {
                        userChoice = opt
                        val comp = options.random()
                        compChoice = comp

                        val u = opt.take(4)
                        val c = comp.take(4)
                        if (u == c) {
                            resultText = "It's a tie! 🌿"
                            onCompanionReact("A peaceful tie! 😊", CompanionExpression.PEACEFUL)
                        } else if ((u == "Rock" && c == "Scis") || (u == "Pape" && c == "Rock") || (u == "Scis" && c == "Pape")) {
                            resultText = "You won this round! 🎉"
                            onCompanionReact("Good job! 🥹", CompanionExpression.PROUD)
                        } else {
                            resultText = "$companionName won this round! 😄"
                            onCompanionReact("Haha I got this one! Rematch? 😊", CompanionExpression.HAPPY)
                        }
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(opt, fontSize = 15.sp)
                }
            }
        }
    }
}

// ============================================================================
// 6. PEACEFUL DOODLE CANVAS
// ============================================================================
data class DoodlePath(
    val path: Path,
    val color: Color,
    val strokeWidth: Float
)

@Composable
fun DoodleCanvasView() {
    val paths = remember { mutableStateListOf<DoodlePath>() }
    var currentColor by remember { mutableStateOf(Color(0xFF2C3E50)) }
    var currentStrokeWidth by remember { mutableStateOf(8f) }
    var currentPath by remember { mutableStateOf<Path?>(null) }

    val palette = listOf(
        Color(0xFF2C3E50), // Charcoal
        Color(0xFFE74C3C), // Warm Red
        Color(0xFFF0932B), // Amber
        Color(0xFF6AB04C), // Forest Green
        Color(0xFF22A6B3), // Teal
        Color(0xFF4834D4), // Deep Blue
        Color(0xFFBE2EDD), // Lavender
        Color(0xFFF8A5C2)  // Sakura Pink
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // Color Palette Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                palette.forEach { col ->
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(col)
                            .border(
                                width = if (currentColor == col) 3.dp else 0.dp,
                                color = MaterialTheme.colorScheme.onSurface,
                                shape = CircleShape
                            )
                            .clickable { currentColor = col }
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = { if (paths.isNotEmpty()) paths.removeAt(paths.size - 1) }) {
                    Icon(Icons.Default.Undo, contentDescription = "Undo")
                }
                IconButton(onClick = { paths.clear() }) {
                    Icon(Icons.Default.Delete, contentDescription = "Clear")
                }
            }
        }

        // Drawing Canvas
        Canvas(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFFFAF7F2))
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            val p = Path().apply { moveTo(offset.x, offset.y) }
                            currentPath = p
                            paths.add(DoodlePath(p, currentColor, currentStrokeWidth))
                        },
                        onDrag = { change, _ ->
                            currentPath?.lineTo(change.position.x, change.position.y)
                        },
                        onDragEnd = {
                            currentPath = null
                        }
                    )
                }
        ) {
            for (dp in paths) {
                drawPath(
                    path = dp.path,
                    color = dp.color,
                    style = Stroke(
                        width = dp.strokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }
    }
}

// ============================================================================
// 7. COZY WORD GUESS
// ============================================================================
@Composable
fun WordGuessView(
    companionName: String,
    onCompanionReact: (String, CompanionExpression) -> Unit
) {
    val cozyWords = listOf("PEACE", "SAKURA", "BREEZE", "FOREST", "SUNLIGHT", "MEMORY", "GARDEN", "TRANQUIL")
    var targetWord by remember { mutableStateOf(cozyWords.random()) }
    val guessedLetters = remember { mutableStateListOf<Char>() }
    var remainingAttempts by remember { mutableStateOf(6) }

    val isWon = targetWord.all { guessedLetters.contains(it) }
    val isLost = remainingAttempts <= 0 && !isWon

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isWon) "You found the word! 🌸" else if (isLost) "Word was: $targetWord 🌿" else "Guess the peaceful word",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text("Attempts left: $remainingAttempts", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(24.dp))

        // Display Word with underlines
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            targetWord.forEach { ch ->
                val shown = guessedLetters.contains(ch) || isLost
                Text(
                    text = if (shown) "$ch" else "_",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Letter Keyboard
        val alphabet = ('A'..'Z').toList()
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .width(320.dp)
                .height(180.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            userScrollEnabled = false
        ) {
            items(alphabet) { ch ->
                val used = guessedLetters.contains(ch)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (used) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f) else MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier
                        .size(36.dp)
                        .clickable(enabled = !used && !isWon && !isLost) {
                            guessedLetters.add(ch)
                            if (!targetWord.contains(ch)) {
                                remainingAttempts--
                                if (remainingAttempts <= 0) {
                                    onCompanionReact("Good try! That was a tough one 🌿", CompanionExpression.PEACEFUL)
                                }
                            } else if (targetWord.all { guessedLetters.contains(it) || it == ch }) {
                                onCompanionReact("You guessed it! Beautiful word 🌸", CompanionExpression.VERY_HAPPY)
                            }
                        }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "$ch", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                targetWord = cozyWords.random()
                guessedLetters.clear()
                remainingAttempts = 6
            },
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("New Word")
        }
    }
}

// ============================================================================
// 8. WOULD YOU RATHER
// ============================================================================
@Composable
fun WouldYouRatherView(companionName: String) {
    val questions = listOf(
        Pair("Sit beside a quiet rainy window with warm tea ☕", "Walk under falling cherry blossoms in the morning sun 🌸"),
        Pair("Read an enchanting old book in a cozy library 📚", "Gaze at a shooting star in a deep midnight sky ⭐"),
        Pair("Spend a quiet day tending a gentle garden 🌱", "Listen to calm ocean waves rolling onto the sand 🌊"),
        Pair("Write a letter to your future self 10 years away ⏳", "Revisit your fondest childhood memory for one hour 🏡")
    )
    var qIndex by remember { mutableStateOf(0) }
    var selectedOpt by remember { mutableStateOf<Int?>(null) }

    val currentQ = questions[qIndex % questions.size]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Would You Rather...", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(24.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (selectedOpt == 1) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { selectedOpt = 1 }
        ) {
            Text(currentQ.first, modifier = Modifier.padding(20.dp), style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("— OR —", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.outline)
        Spacer(modifier = Modifier.height(12.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (selectedOpt == 2) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { selectedOpt = 2 }
        ) {
            Text(currentQ.second, modifier = Modifier.padding(20.dp), style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {
                qIndex++
                selectedOpt = null
            },
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Next Question")
        }
    }
}
