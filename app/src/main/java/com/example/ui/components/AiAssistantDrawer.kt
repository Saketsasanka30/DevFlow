package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ai.AiMessage
import com.example.data.ai.AiPromptType
import com.example.ui.theme.DevAccentGreen
import com.example.ui.theme.DevAccentPurple
import com.example.ui.theme.DevAccentRed
import com.example.ui.theme.DevAccentYellow
import com.example.ui.theme.DevBorderDark
import com.example.ui.theme.DevCodeFont
import com.example.ui.theme.DevPrimaryCyan
import com.example.ui.theme.DevSurfaceCardDark
import com.example.ui.theme.DevSurfaceDark

@Composable
fun AiAssistantDrawer(
    messages: List<AiMessage>,
    isLoading: Boolean,
    onAskPrompt: (AiPromptType, String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var customQuery by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .border(1.dp, DevBorderDark, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        color = DevSurfaceDark,
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(480.dp)
                .padding(12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(DevAccentPurple.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = DevAccentPurple,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "DevFlow AI Assistant",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Gemini Developer Intelligence Engine",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(onClick = onClose) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Preset Prompts Chips Bar (All 6 requested prompts!)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                PromptChip(
                    label = "Explain Repository",
                    icon = Icons.Default.FolderOpen,
                    color = DevPrimaryCyan
                ) { onAskPrompt(AiPromptType.EXPLAIN_REPO, "Explain the active repository architecture") }

                PromptChip(
                    label = "Summarize PR",
                    icon = Icons.Default.Hub,
                    color = DevAccentPurple
                ) { onAskPrompt(AiPromptType.SUMMARIZE_PR, "Summarize active PR changes and risk level") }

                PromptChip(
                    label = "Find Bugs",
                    icon = Icons.Default.Code,
                    color = DevAccentRed
                ) { onAskPrompt(AiPromptType.IDENTIFY_BUGS, "Identify possible bugs, memory leaks, and concurrency issues") }

                PromptChip(
                    label = "Generate Docs",
                    icon = Icons.Default.Description,
                    color = DevAccentGreen
                ) { onAskPrompt(AiPromptType.GENERATE_DOCS, "Generate technical documentation and OpenAPI runbooks") }

                PromptChip(
                    label = "Explain Errors",
                    icon = Icons.Default.ErrorOutline,
                    color = DevAccentYellow
                ) { onAskPrompt(AiPromptType.EXPLAIN_ERRORS, "Explain deployment errors and suggest patch") }

                PromptChip(
                    label = "Suggest Tests",
                    icon = Icons.Default.Science,
                    color = DevPrimaryCyan
                ) { onAskPrompt(AiPromptType.SUGGEST_TESTS, "Suggest unit and integration tests with edge cases") }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Messages Timeline
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages) { msg ->
                    val isAi = msg.sender == "AI"
                    val bubbleBg = if (isAi) DevSurfaceCardDark else DevPrimaryCyan.copy(alpha = 0.15f)
                    val bubbleBorder = if (isAi) DevBorderDark else DevPrimaryCyan.copy(alpha = 0.4f)

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = if (isAi) Alignment.Start else Alignment.End
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 2.dp)
                        ) {
                            Text(
                                text = if (isAi) "DevFlow AI" else "You",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isAi) DevAccentPurple else DevPrimaryCyan
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = msg.timestamp,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth(if (isAi) 0.98f else 0.85f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(bubbleBg)
                                .border(1.dp, bubbleBorder, RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = msg.content,
                                fontFamily = if (msg.content.contains("```") || msg.content.contains("{")) DevCodeFont else MaterialTheme.typography.bodyMedium.fontFamily,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }

                if (isLoading) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = DevAccentPurple,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "DevFlow AI is analyzing codebase & crafting response...",
                                style = MaterialTheme.typography.bodySmall,
                                color = DevAccentPurple
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Query Input
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = customQuery,
                    onValueChange = { customQuery = it },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ai_assistant_query_input"),
                    placeholder = { Text("Ask anything about code, PRs, or architecture...") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DevAccentPurple,
                        unfocusedBorderColor = DevBorderDark,
                        focusedContainerColor = DevSurfaceCardDark,
                        unfocusedContainerColor = DevSurfaceCardDark
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (customQuery.isNotBlank()) {
                            onAskPrompt(AiPromptType.FREE_CHAT, customQuery)
                            customQuery = ""
                        }
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(DevAccentPurple)
                        .testTag("ai_assistant_send_button")
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PromptChip(
    label: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.12f))
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}
