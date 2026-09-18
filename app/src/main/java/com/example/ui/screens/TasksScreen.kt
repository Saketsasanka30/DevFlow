package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.TaskEntity
import com.example.ui.components.PriorityPill
import com.example.ui.theme.DevAccentGreen
import com.example.ui.theme.DevAccentPurple
import com.example.ui.theme.DevAccentRed
import com.example.ui.theme.DevAccentYellow
import com.example.ui.theme.DevCodeFont
import com.example.ui.theme.DevPrimaryCyan
import com.example.viewmodel.DevFlowViewModel

@Composable
fun TasksScreen(
    viewModel: DevFlowViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.tasks.collectAsState()
    var isCreateDialogOpen by remember { mutableStateOf(false) }
    var selectedStatusFilter by remember { mutableStateOf("ALL") }
    var selectedPriorityFilter by remember { mutableStateOf("ALL") }

    val statusColumns = listOf("ALL", "TODO", "IN_PROGRESS", "REVIEW", "DONE")
    val priorityLevels = listOf("ALL", "CRITICAL", "HIGH", "MEDIUM", "LOW")

    val filteredTasks = remember(tasks, selectedStatusFilter, selectedPriorityFilter) {
        tasks.filter { task ->
            val matchesStatus = (selectedStatusFilter == "ALL" || task.status.equals(selectedStatusFilter, ignoreCase = true))
            val matchesPriority = (selectedPriorityFilter == "ALL" || task.priority.equals(selectedPriorityFilter, ignoreCase = true))
            matchesStatus && matchesPriority
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header with New Task Button and Sprint Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Project Tasks & Sprint Planning",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${tasks.count { it.status != "DONE" }} active • ${tasks.count { it.priority == "CRITICAL" }} critical • Room DB Synced",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(
                onClick = { isCreateDialogOpen = true },
                colors = ButtonDefaults.buttonColors(containerColor = DevPrimaryCyan),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("add_task_button")
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Task",
                    tint = Color(0xFF090D16),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("New Task", color = Color(0xFF090D16), style = MaterialTheme.typography.labelMedium)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Status Swimlane / Filter Chips
        Text(
            text = "STATUS FILTER",
            style = MaterialTheme.typography.labelSmall,
            fontFamily = DevCodeFont,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            statusColumns.forEach { col ->
                val isSelected = col == selectedStatusFilter
                val count = if (col == "ALL") tasks.size else tasks.count { it.status.equals(col, ignoreCase = true) }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) DevPrimaryCyan.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .border(
                            1.dp,
                            if (isSelected) DevPrimaryCyan else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            RoundedCornerShape(6.dp)
                        )
                        .clickable { selectedStatusFilter = col }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = col,
                            fontFamily = DevCodeFont,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) DevPrimaryCyan else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "($count)",
                            fontFamily = DevCodeFont,
                            fontSize = 10.sp,
                            color = if (isSelected) DevPrimaryCyan.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Priority Filter Chips
        Text(
            text = "PRIORITY FILTER",
            style = MaterialTheme.typography.labelSmall,
            fontFamily = DevCodeFont,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            priorityLevels.forEach { prio ->
                val isSelected = prio == selectedPriorityFilter
                val count = if (prio == "ALL") tasks.size else tasks.count { it.priority.equals(prio, ignoreCase = true) }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) DevAccentPurple.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .border(
                            1.dp,
                            if (isSelected) DevAccentPurple else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            RoundedCornerShape(6.dp)
                        )
                        .clickable { selectedPriorityFilter = prio }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = prio,
                            fontFamily = DevCodeFont,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) DevAccentPurple else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "($count)",
                            fontFamily = DevCodeFont,
                            fontSize = 10.sp,
                            color = if (isSelected) DevAccentPurple.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tasks List
        if (filteredTasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No tasks found matching filter criteria",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            selectedStatusFilter = "ALL"
                            selectedPriorityFilter = "ALL"
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Reset Filters", color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredTasks, key = { it.id }) { task ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = task.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )

                                // Clickable Priority Pill (cycles LOW -> MEDIUM -> HIGH -> CRITICAL)
                                Box(
                                    modifier = Modifier
                                        .clickable {
                                            val nextPriority = when (task.priority.uppercase()) {
                                                "LOW" -> "MEDIUM"
                                                "MEDIUM" -> "HIGH"
                                                "HIGH" -> "CRITICAL"
                                                else -> "LOW"
                                            }
                                            viewModel.updateTaskPriority(task, nextPriority)
                                        }
                                ) {
                                    PriorityPill(task.priority)
                                }
                            }

                            if (task.description.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = task.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${task.storyPoints} pts",
                                        fontFamily = DevCodeFont,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = DevAccentPurple
                                    )
                                    Text(
                                        text = "Assignee: ${task.assignee}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = task.dueDate,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                    )
                                }

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Cycle status button
                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(MaterialTheme.colorScheme.surface)
                                            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                                            .clickable {
                                                val nextStatus = when (task.status.uppercase()) {
                                                    "TODO" -> "IN_PROGRESS"
                                                    "IN_PROGRESS" -> "REVIEW"
                                                    "REVIEW" -> "DONE"
                                                    else -> "TODO"
                                                }
                                                viewModel.updateTaskStatus(task, nextStatus)
                                            }
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = task.status,
                                            fontFamily = DevCodeFont,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = when (task.status.uppercase()) {
                                                "DONE" -> DevAccentGreen
                                                "IN_PROGRESS" -> DevPrimaryCyan
                                                "REVIEW" -> DevAccentYellow
                                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                                            }
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("→", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                                    }

                                    // Delete Task Action
                                    IconButton(
                                        onClick = { viewModel.deleteTask(task.id) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Delete Task",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (isCreateDialogOpen) {
        CreateTaskDialog(
            onDismiss = { isCreateDialogOpen = false },
            onCreate = { title, desc, priority, pts ->
                viewModel.createTask(title, desc, priority, pts)
                isCreateDialogOpen = false
            }
        )
    }
}

@Composable
fun CreateTaskDialog(
    onDismiss: () -> Unit,
    onCreate: (title: String, description: String, priority: String, storyPoints: Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("MEDIUM") }
    var storyPoints by remember { mutableStateOf("3") }

    val priorities = listOf("CRITICAL", "HIGH", "MEDIUM", "LOW")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "New Sprint Task",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("task_title_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DevPrimaryCyan,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Acceptance Criteria / Notes") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DevPrimaryCyan,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Priority Selector
                Text(
                    text = "Priority Level",
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = DevCodeFont,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    priorities.forEach { prio ->
                        val isSelected = prio == priority
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    if (isSelected) {
                                        when (prio) {
                                            "CRITICAL" -> DevAccentRed.copy(alpha = 0.2f)
                                            "HIGH" -> DevAccentYellow.copy(alpha = 0.2f)
                                            "MEDIUM" -> DevPrimaryCyan.copy(alpha = 0.2f)
                                            else -> DevAccentGreen.copy(alpha = 0.2f)
                                        }
                                    } else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) {
                                        when (prio) {
                                            "CRITICAL" -> DevAccentRed
                                            "HIGH" -> DevAccentYellow
                                            "MEDIUM" -> DevPrimaryCyan
                                            else -> DevAccentGreen
                                        }
                                    } else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable { priority = prio }
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = prio,
                                fontFamily = DevCodeFont,
                                fontSize = 11.sp,
                                color = if (isSelected) {
                                    when (prio) {
                                        "CRITICAL" -> DevAccentRed
                                        "HIGH" -> DevAccentYellow
                                        "MEDIUM" -> DevPrimaryCyan
                                        else -> DevAccentGreen
                                    }
                                } else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = storyPoints,
                    onValueChange = { storyPoints = it },
                    label = { Text("Story Points (1, 2, 3, 5, 8)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DevPrimaryCyan,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

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
                                onCreate(title, description, priority, storyPoints.toIntOrNull() ?: 3)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DevPrimaryCyan),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Add Task", color = Color(0xFF090D16))
                    }
                }
            }
        }
    }
}
