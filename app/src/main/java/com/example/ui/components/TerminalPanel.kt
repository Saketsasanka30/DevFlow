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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DevAccentGreen
import com.example.ui.theme.DevAccentRed
import com.example.ui.theme.DevAccentYellow
import com.example.ui.theme.DevBorderDark
import com.example.ui.theme.DevCodeFont
import com.example.ui.theme.DevPrimaryCyan
import com.example.ui.theme.DevSurfaceCardDark
import com.example.ui.theme.DevTerminalBg
import com.example.ui.theme.DevTerminalDim

@Composable
fun TerminalPanel(
    logs: List<String>,
    onExecuteCommand: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var inputCommand by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty()) {
            listState.animateScrollToItem(logs.size - 1)
        }
    }

    val panelHeight = if (isExpanded) 380.dp else 220.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(panelHeight)
            .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
            .background(DevTerminalBg)
            .border(1.dp, DevBorderDark, RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
    ) {
        // Terminal Header Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DevSurfaceCardDark)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Traffic light dots
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(DevAccentRed))
                Spacer(modifier = Modifier.width(6.dp))
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(DevAccentYellow))
                Spacer(modifier = Modifier.width(6.dp))
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(DevAccentGreen))

                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "devflow-terminal (bash: zsh-5.9)",
                    fontFamily = DevCodeFont,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Resize toggle button
                IconButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandMore else Icons.Default.ExpandLess,
                        contentDescription = "Resize Panel",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Close button
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close Terminal",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Quick Command Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .background(DevTerminalBg)
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickCommandChip("git status") { onExecuteCommand("git status") }
            QuickCommandChip("git log -n 3") { onExecuteCommand("git log -n 3") }
            QuickCommandChip("deploy prod") { onExecuteCommand("deploy prod") }
            QuickCommandChip("docker ps") { onExecuteCommand("docker ps") }
            QuickCommandChip("health") { onExecuteCommand("health") }
            QuickCommandChip("clear") { onExecuteCommand("clear") }
        }

        // Terminal Output
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            items(logs) { line ->
                val (color, prefix) = when {
                    line.startsWith("$") -> Pair(DevPrimaryCyan, "")
                    line.contains("SUCCESS", ignoreCase = true) || line.contains("PASSED", ignoreCase = true) -> Pair(DevAccentGreen, "")
                    line.contains("FAILED", ignoreCase = true) || line.contains("PANIC", ignoreCase = true) || line.contains("ERROR", ignoreCase = true) -> Pair(DevAccentRed, "")
                    line.contains("WARN", ignoreCase = true) -> Pair(DevAccentYellow, "")
                    else -> Pair(Color(0xFFCBD5E1), "")
                }
                Text(
                    text = prefix + line,
                    fontFamily = DevCodeFont,
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    color = color,
                    modifier = Modifier.padding(vertical = 1.dp)
                )
            }
        }

        // Interactive Input Line
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF070B14))
                .border(1.dp, DevBorderDark)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "devflow@os:~$ ",
                fontFamily = DevCodeFont,
                fontSize = 12.sp,
                color = DevAccentGreen
            )
            BasicTextField(
                value = inputCommand,
                onValueChange = { inputCommand = it },
                modifier = Modifier
                    .weight(1f)
                    .testTag("terminal_input_field"),
                textStyle = TextStyle(
                    fontFamily = DevCodeFont,
                    fontSize = 12.sp,
                    color = Color.White
                ),
                cursorBrush = SolidColor(DevPrimaryCyan),
                singleLine = true
            )
            IconButton(
                onClick = {
                    if (inputCommand.isNotBlank()) {
                        onExecuteCommand(inputCommand)
                        inputCommand = ""
                    }
                },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Run",
                    tint = DevPrimaryCyan,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun QuickCommandChip(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(DevSurfaceCardDark)
            .border(1.dp, DevTerminalDim.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = label,
            fontFamily = DevCodeFont,
            fontSize = 10.sp,
            color = DevPrimaryCyan
        )
    }
}
