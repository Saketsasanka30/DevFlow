package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.git.GitCommandResult
import com.example.data.git.LocalGitBranch
import com.example.ui.theme.DevAccentGreen
import com.example.ui.theme.DevAccentPurple
import com.example.ui.theme.DevAccentYellow
import com.example.ui.theme.DevCodeFont
import com.example.ui.theme.DevPrimaryCyan
import com.example.ui.theme.DevTerminalBg
import com.example.ui.theme.DevTerminalText
import com.example.viewmodel.DevFlowViewModel
import kotlinx.coroutines.launch

@Composable
fun GitBranchSwitcher(
    viewModel: DevFlowViewModel,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val activeBranch by viewModel.gitCliService.currentBranch.collectAsState()
    val isExecuting by viewModel.gitCliService.isExecuting.collectAsState()
    val cliLogs by viewModel.gitCliService.recentCliLogs.collectAsState()

    var localBranches by remember { mutableStateOf<List<LocalGitBranch>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var isNewBranchDialogOpen by remember { mutableStateOf(false) }
    var showCliTerminalLogs by remember { mutableStateOf(false) }
    var lastExecutedCommandResult by remember { mutableStateOf<GitCommandResult?>(null) }

    fun refreshBranches() {
        coroutineScope.launch {
            localBranches = viewModel.gitCliService.listLocalBranches()
        }
    }

    LaunchedEffect(activeBranch) {
        refreshBranches()
    }

    val filteredBranches = remember(localBranches, searchQuery) {
        if (searchQuery.isBlank()) localBranches
        else localBranches.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("git_branch_switcher"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Current Branch + Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(DevAccentGreen.copy(alpha = 0.15f))
                            .border(1.dp, DevAccentGreen.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.AltRoute,
                            contentDescription = "Git Branch",
                            tint = DevAccentGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Local Git Branches",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(DevAccentGreen.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Runtime.exec()",
                                    fontFamily = DevCodeFont,
                                    fontSize = 10.sp,
                                    color = DevAccentGreen
                                )
                            }
                        }
                        Text(
                            text = "Active HEAD: $activeBranch",
                            fontFamily = DevCodeFont,
                            style = MaterialTheme.typography.bodySmall,
                            color = DevPrimaryCyan
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { refreshBranches() },
                        modifier = Modifier.testTag("refresh_branches_button")
                    ) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = { isNewBranchDialogOpen = true },
                        colors = ButtonDefaults.buttonColors(containerColor = DevPrimaryCyan),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("create_branch_button")
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            tint = Color(0xFF090D16),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "New Branch",
                            color = Color(0xFF090D16),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search Filter + Terminal toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Filter branches...", style = MaterialTheme.typography.bodySmall) },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("branch_filter_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DevPrimaryCyan,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = { showCliTerminalLogs = !showCliTerminalLogs },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (showCliTerminalLogs) DevPrimaryCyan.copy(alpha = 0.1f) else Color.Transparent
                    ),
                    modifier = Modifier.height(50.dp)
                ) {
                    Icon(
                        Icons.Default.Terminal,
                        contentDescription = "CLI Logs",
                        tint = if (showCliTerminalLogs) DevPrimaryCyan else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "CLI Log",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (showCliTerminalLogs) DevPrimaryCyan else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Collapsible CLI Output Viewer
            AnimatedVisibility(
                visible = showCliTerminalLogs,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(DevTerminalBg)
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Terminal CLI Log (Runtime.exec())",
                            fontFamily = DevCodeFont,
                            fontSize = 11.sp,
                            color = DevAccentPurple
                        )
                        if (isExecuting) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(12.dp),
                                    strokeWidth = 2.dp,
                                    color = DevPrimaryCyan
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Executing...", fontSize = 10.sp, color = DevPrimaryCyan)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    val latest = cliLogs.firstOrNull()
                    if (latest != null) {
                        Text(
                            text = "$ ${latest.command}",
                            fontFamily = DevCodeFont,
                            fontSize = 12.sp,
                            color = DevPrimaryCyan
                        )
                        if (latest.stdout.isNotBlank()) {
                            Text(
                                text = latest.stdout,
                                fontFamily = DevCodeFont,
                                fontSize = 11.sp,
                                color = DevTerminalText
                            )
                        }
                        if (latest.stderr.isNotBlank()) {
                            Text(
                                text = latest.stderr,
                                fontFamily = DevCodeFont,
                                fontSize = 11.sp,
                                color = Color(0xFFF87171)
                            )
                        }
                        Text(
                            text = "Exit Code: ${latest.exitCode} (${latest.durationMs}ms) • ${latest.timestamp}",
                            fontFamily = DevCodeFont,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = "Ready to execute standard git commands (git branch, git checkout).",
                            fontFamily = DevCodeFont,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Branch List
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filteredBranches.forEach { branch ->
                    BranchRowItem(
                        branch = branch,
                        isCurrent = branch.name == activeBranch,
                        isExecuting = isExecuting,
                        onSwitchBranch = {
                            coroutineScope.launch {
                                val res = viewModel.gitCliService.switchBranch(branch.name)
                                lastExecutedCommandResult = res
                                refreshBranches()
                            }
                        }
                    )
                }

                if (filteredBranches.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No branches match '$searchQuery'",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    // Dialog for creating a new local branch
    if (isNewBranchDialogOpen) {
        NewBranchDialog(
            onDismiss = { isNewBranchDialogOpen = false },
            onCreate = { branchName ->
                coroutineScope.launch {
                    val res = viewModel.gitCliService.createAndSwitchBranch(branchName)
                    lastExecutedCommandResult = res
                    refreshBranches()
                    isNewBranchDialogOpen = false
                }
            }
        )
    }
}

@Composable
private fun BranchRowItem(
    branch: LocalGitBranch,
    isCurrent: Boolean,
    isExecuting: Boolean,
    onSwitchBranch: () -> Unit
) {
    val borderColor = if (isCurrent) DevAccentGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    val bgColor = if (isCurrent) DevAccentGreen.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .padding(12.dp)
            .testTag("branch_item_${branch.name}"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AltRoute,
                contentDescription = null,
                tint = if (isCurrent) DevAccentGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = branch.name,
                        fontFamily = DevCodeFont,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isCurrent) DevAccentGreen else MaterialTheme.colorScheme.onSurface
                    )

                    if (isCurrent) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(DevAccentGreen.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "CURRENT HEAD",
                                fontFamily = DevCodeFont,
                                fontSize = 9.sp,
                                color = DevAccentGreen
                            )
                        }
                    }

                    if (branch.isProtected) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(DevAccentYellow.copy(alpha = 0.2f))
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = DevAccentYellow, modifier = Modifier.size(10.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("PROTECTED", fontFamily = DevCodeFont, fontSize = 9.sp, color = DevAccentYellow)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = branch.lastCommitHash,
                        fontFamily = DevCodeFont,
                        style = MaterialTheme.typography.labelSmall,
                        color = DevPrimaryCyan
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = branch.lastCommitMessage,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        if (isCurrent) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(DevAccentGreen.copy(alpha = 0.15f))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = DevAccentGreen, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Checked Out", color = DevAccentGreen, style = MaterialTheme.typography.labelSmall)
                }
            }
        } else {
            Button(
                onClick = onSwitchBranch,
                enabled = !isExecuting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(6.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                modifier = Modifier.testTag("switch_to_${branch.name}")
            ) {
                Text("Switch", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun NewBranchDialog(
    onDismiss: () -> Unit,
    onCreate: (String) -> Unit
) {
    var branchName by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AltRoute, contentDescription = null, tint = DevPrimaryCyan)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Create & Checkout Branch", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Executes: git checkout -b <branch-name> via Runtime.exec()",
                    fontFamily = DevCodeFont,
                    fontSize = 11.sp,
                    color = DevAccentGreen
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = branchName,
                    onValueChange = {
                        branchName = it.replace(" ", "-")
                        errorMessage = ""
                    },
                    label = { Text("Branch Name") },
                    placeholder = { Text("e.g. feature/ai-code-lens") },
                    modifier = Modifier.fillMaxWidth().testTag("new_branch_name_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DevPrimaryCyan,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                )

                if (errorMessage.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(errorMessage, color = Color(0xFFF87171), style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val cleanName = branchName.trim()
                            if (cleanName.isBlank()) {
                                errorMessage = "Please provide a valid branch name"
                            } else {
                                onCreate(cleanName)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DevPrimaryCyan),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("confirm_create_branch_button")
                    ) {
                        Text("Create & Switch", color = Color(0xFF090D16))
                    }
                }
            }
        }
    }
}
