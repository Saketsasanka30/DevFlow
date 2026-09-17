package com.example.ui.components

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.DevAccentGreen
import com.example.ui.theme.DevAccentPurple
import com.example.ui.theme.DevBorderDark
import com.example.ui.theme.DevCodeFont
import com.example.ui.theme.DevPrimaryCyan
import com.example.ui.theme.DevSurfaceCardDark
import com.example.ui.theme.DevSurfaceDark
import com.example.viewmodel.DevFlowModule

data class PaletteAction(
    val title: String,
    val subtitle: String,
    val category: String,
    val icon: ImageVector,
    val shortcut: String? = null,
    val onExecute: () -> Unit
)

@Composable
fun CommandPaletteDialog(
    onDismiss: () -> Unit,
    onNavigate: (DevFlowModule) -> Unit,
    onTriggerDeploy: () -> Unit,
    onOpenTerminal: () -> Unit,
    onOpenAi: () -> Unit,
    onToggleTheme: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val allActions = listOf(
        PaletteAction("Go to Dashboard", "Project overview, health & metrics", "Navigation", Icons.Default.Dashboard, "⌘1") {
            onNavigate(DevFlowModule.DASHBOARD)
            onDismiss()
        },
        PaletteAction("Go to Projects", "Workspace projects & repositories", "Navigation", Icons.Default.Folder, "⌘2") {
            onNavigate(DevFlowModule.PROJECTS)
            onDismiss()
        },
        PaletteAction("Go to Repositories", "Git tree, commits, branches & releases", "Navigation", Icons.Default.Code, "⌘3") {
            onNavigate(DevFlowModule.REPOSITORIES)
            onDismiss()
        },
        PaletteAction("Go to Issues", "Bug tracking and Kanban status", "Navigation", Icons.Default.FormatListBulleted, "⌘4") {
            onNavigate(DevFlowModule.ISSUES)
            onDismiss()
        },
        PaletteAction("Go to Pull Requests", "Code review diffs & CI checks", "Navigation", Icons.Default.Hub, "⌘5") {
            onNavigate(DevFlowModule.PULL_REQUESTS)
            onDismiss()
        },
        PaletteAction("Go to Deployments", "Release history, builds & live logs", "Navigation", Icons.Default.CloudUpload, "⌘6") {
            onNavigate(DevFlowModule.DEPLOYMENTS)
            onDismiss()
        },
        PaletteAction("Go to Code Health Analytics", "DORA metrics, PR cycle time & velocity", "Navigation", Icons.Default.Speed, "⌘7") {
            onNavigate(DevFlowModule.ANALYTICS)
            onDismiss()
        },
        PaletteAction("Go to API Documentation", "Interactive OpenAPI & Swagger tester", "Navigation", Icons.Default.MenuBook, "⌘8") {
            onNavigate(DevFlowModule.API_DOCS)
            onDismiss()
        },
        PaletteAction("Go to Team Directory", "Engineers, roles & PR workloads", "Navigation", Icons.Default.People, "⌘9") {
            onNavigate(DevFlowModule.TEAM)
            onDismiss()
        },
        PaletteAction("Go to Settings", "GitHub credentials & AI API keys", "Navigation", Icons.Default.Settings, "⌘,") {
            onNavigate(DevFlowModule.SETTINGS)
            onDismiss()
        },
        PaletteAction("Ask AI Developer Assistant", "Explain repos, summarize PRs, find bugs", "AI", Icons.Default.AutoAwesome, "⌘I") {
            onOpenAi()
            onDismiss()
        },
        PaletteAction("Open Terminal Console", "Interactive bash runner and live logs", "Tools", Icons.Default.Terminal, "Ctrl+`") {
            onOpenTerminal()
            onDismiss()
        },
        PaletteAction("Trigger Production Deployment", "Dispatches pipeline rollout", "Action", Icons.Default.CloudUpload, "⌘D") {
            onTriggerDeploy()
            onDismiss()
        },
        PaletteAction("Toggle Dark/Light Mode", "Switch workspace color theme", "Preference", Icons.Default.Settings) {
            onToggleTheme()
            onDismiss()
        }
    )

    val filteredActions = remember(searchQuery) {
        if (searchQuery.isBlank()) allActions
        else allActions.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
            it.subtitle.contains(searchQuery, ignoreCase = true) ||
            it.category.contains(searchQuery, ignoreCase = true)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, DevBorderDark, RoundedCornerShape(16.dp)),
            color = DevSurfaceDark,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Search Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("command_palette_search_input"),
                    placeholder = { Text("Type a command or search workspace... (Esc to close)") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = DevPrimaryCyan)
                    },
                    trailingIcon = {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(DevSurfaceCardDark)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "ESC",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DevPrimaryCyan,
                        unfocusedBorderColor = DevBorderDark,
                        focusedContainerColor = DevSurfaceCardDark,
                        unfocusedContainerColor = DevSurfaceCardDark
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Actions List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 380.dp)
                ) {
                    items(filteredActions) { action ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { action.onExecute() }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(DevSurfaceCardDark),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        action.icon,
                                        contentDescription = action.title,
                                        modifier = Modifier.size(18.dp),
                                        tint = when (action.category) {
                                            "AI" -> DevAccentPurple
                                            "Action" -> DevAccentGreen
                                            else -> DevPrimaryCyan
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = action.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = action.subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            if (action.shortcut != null) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(DevSurfaceCardDark)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = action.shortcut,
                                        fontFamily = DevCodeFont,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
