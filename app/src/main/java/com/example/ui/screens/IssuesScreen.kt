package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.data.ai.AiPromptType
import com.example.data.local.IssueEntity
import com.example.ui.components.PriorityPill
import com.example.ui.components.StatusBadge
import com.example.ui.theme.DevAccentGreen
import com.example.ui.theme.DevAccentPurple
import com.example.ui.theme.DevAccentYellow
import com.example.ui.theme.DevBorderDark
import com.example.ui.theme.DevCodeFont
import com.example.ui.theme.DevPrimaryCyan
import com.example.ui.theme.DevSurfaceCardDark
import com.example.ui.theme.DevSurfaceDark
import com.example.viewmodel.DevFlowViewModel

@Composable
fun IssuesScreen(
    viewModel: DevFlowViewModel,
    modifier: Modifier = Modifier
) {
    val issues by viewModel.issues.collectAsState()
    var selectedFilter by remember { mutableStateOf("ALL") }
    var isCreateDialogOpen by remember { mutableStateOf(false) }

    val filteredIssues = remember(issues, selectedFilter) {
        when (selectedFilter) {
            "OPEN" -> issues.filter { it.status == "OPEN" || it.status == "IN_PROGRESS" }
            "CLOSED" -> issues.filter { it.status == "CLOSED" }
            else -> issues
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Actions Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Issue Tracker", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
                Text("${issues.count { it.status != "CLOSED" }} open issues • Bug diagnosis enabled", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Button(
                onClick = { isCreateDialogOpen = true },
                colors = ButtonDefaults.buttonColors(containerColor = DevPrimaryCyan),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("new_issue_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF090D16), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("New Issue", color = Color(0xFF090D16), style = MaterialTheme.typography.labelMedium)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Chips
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("ALL", "OPEN", "CLOSED").forEach { filter ->
                val isSelected = filter == selectedFilter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) DevPrimaryCyan.copy(alpha = 0.15f) else DevSurfaceCardDark)
                        .border(1.dp, if (isSelected) DevPrimaryCyan else DevBorderDark, RoundedCornerShape(6.dp))
                        .clickable { selectedFilter = filter }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = filter,
                        fontFamily = DevCodeFont,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) DevPrimaryCyan else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Issues List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredIssues) { issue ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = DevSurfaceCardDark),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DevBorderDark),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.selectIssue(issue) }
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "#${issue.number} ${issue.title}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            PriorityPill(issue.priority)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = issue.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                StatusBadge(
                                    text = issue.status,
                                    backgroundColor = if (issue.status == "OPEN") DevAccentGreen else DevAccentYellow,
                                    textColor = if (issue.status == "OPEN") DevAccentGreen else DevAccentYellow
                                )
                                Text("Assignee: ${issue.assignee}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text("${issue.commentsCount}", fontFamily = DevCodeFont, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }

                            // AI Bug diagnosis button
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(DevAccentPurple.copy(alpha = 0.15f))
                                    .border(1.dp, DevAccentPurple.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
                                    .clickable {
                                        viewModel.toggleAiAssistant(true)
                                        viewModel.askAiAssistant(AiPromptType.IDENTIFY_BUGS, "Analyze issue #${issue.number}: ${issue.title}\n${issue.description}")
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = DevAccentPurple, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("AI Triage", style = MaterialTheme.typography.labelSmall, color = DevAccentPurple)
                            }
                        }
                    }
                }
            }
        }
    }

    if (isCreateDialogOpen) {
        CreateIssueDialog(
            onDismiss = { isCreateDialogOpen = false },
            onCreate = { title, desc, priority, labels ->
                viewModel.createIssue(title, desc, priority, labels)
                isCreateDialogOpen = false
            }
        )
    }
}

@Composable
fun CreateIssueDialog(
    onDismiss: () -> Unit,
    onCreate: (title: String, description: String, priority: String, labels: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("HIGH") }
    var labels by remember { mutableStateOf("bug, backend") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, DevBorderDark, RoundedCornerShape(16.dp)),
            color = DevSurfaceDark
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("New Developer Issue", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Issue Title") },
                    modifier = Modifier.fillMaxWidth().testTag("issue_title_input"),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DevPrimaryCyan, unfocusedBorderColor = DevBorderDark)
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description & Steps to Reproduce") },
                    modifier = Modifier.fillMaxWidth().height(100.dp).testTag("issue_desc_input"),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DevPrimaryCyan, unfocusedBorderColor = DevBorderDark)
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = labels,
                    onValueChange = { labels = it },
                    label = { Text("Labels (comma separated)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DevPrimaryCyan, unfocusedBorderColor = DevBorderDark)
                )
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onCreate(title, description, priority, labels)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DevPrimaryCyan),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("submit_issue_button")
                    ) {
                        Text("Create Issue", color = Color(0xFF090D16))
                    }
                }
            }
        }
    }
}
