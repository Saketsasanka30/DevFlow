package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AiAssistantDrawer
import com.example.ui.components.CodeAnalysisPanel
import com.example.ui.components.CommandPaletteDialog
import com.example.ui.components.KeyboardShortcutsDialog
import com.example.ui.components.TerminalPanel
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.ApiDocScreen
import com.example.ui.screens.CiCdScreen
import com.example.ui.screens.CodeActivityScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DeploymentsScreen
import com.example.ui.screens.DocumentationScreen
import com.example.ui.screens.EnvironmentsScreen
import com.example.ui.screens.IssuesScreen
import com.example.ui.screens.ProjectsScreen
import com.example.ui.screens.PullRequestsScreen
import com.example.ui.screens.RepositoriesScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TasksScreen
import com.example.ui.screens.TeamScreen
import com.example.ui.theme.DevAccentGreen
import com.example.ui.theme.DevAccentPurple
import com.example.ui.theme.DevBorderDark
import com.example.ui.theme.DevCodeFont
import com.example.ui.theme.DevFlowThemeProvider
import com.example.ui.theme.DevPrimaryCyan
import com.example.ui.theme.DevSurfaceCardDark
import com.example.ui.theme.DevSurfaceDark
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.DevFlowModule
import com.example.viewmodel.DevFlowViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevFlowMainApp(viewModel: DevFlowViewModel) {
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    val currentModule by viewModel.currentModule.collectAsState()
    val isPaletteOpen by viewModel.isCommandPaletteOpen.collectAsState()
    val isShortcutsOpen by viewModel.isShortcutsDialogOpen.collectAsState()
    val isTerminalOpen by viewModel.isTerminalOpen.collectAsState()
    val isAiOpen by viewModel.isAiAssistantOpen.collectAsState()
    val terminalLogs by viewModel.terminalLogs.collectAsState()
    val aiMessages by viewModel.aiMessages.collectAsState()
    val isAiGenerating by viewModel.isAiGenerating.collectAsState()

    DevFlowThemeProvider(
        themeMode = themeMode,
        onSetThemeMode = { viewModel.setThemeMode(it) },
        onToggleTheme = { viewModel.toggleTheme() }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(DevPrimaryCyan),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("DF", fontFamily = DevCodeFont, color = Color(0xFF090D16), style = MaterialTheme.typography.titleMedium)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("DevFlow", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(DevSurfaceCardDark)
                                    .border(1.dp, DevBorderDark, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 5.dp, vertical = 2.dp)
                            ) {
                                Text("v2.4", fontFamily = DevCodeFont, fontSize = 10.sp, color = DevPrimaryCyan)
                            }
                        }
                    },
                    actions = {
                        // Command Palette Trigger
                        IconButton(
                            onClick = { viewModel.toggleCommandPalette(true) },
                            modifier = Modifier.testTag("app_bar_command_palette_button")
                        ) {
                            Icon(Icons.Default.Search, contentDescription = "Command Palette", tint = DevPrimaryCyan)
                        }

                        // Dev Terminal Toggle
                        IconButton(
                            onClick = { viewModel.toggleTerminal() },
                            modifier = Modifier.testTag("app_bar_terminal_button")
                        ) {
                            Icon(
                                Icons.Default.Terminal,
                                contentDescription = "Terminal Console",
                                tint = if (isTerminalOpen) DevAccentGreen else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // AI Assistant Trigger
                        IconButton(
                            onClick = { viewModel.toggleAiAssistant() },
                            modifier = Modifier.testTag("app_bar_ai_assistant_button")
                        ) {
                            Icon(
                                Icons.Default.AutoAwesome,
                                contentDescription = "AI Assistant",
                                tint = if (isAiOpen) DevAccentPurple else DevAccentPurple.copy(alpha = 0.8f)
                            )
                        }

                        // Keyboard Shortcuts Cheatsheet
                        IconButton(onClick = { viewModel.toggleShortcutsDialog(true) }) {
                            Icon(Icons.Default.Keyboard, contentDescription = "Shortcuts", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        // Theme switch
                        IconButton(onClick = { viewModel.toggleTheme() }) {
                            Icon(
                                if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Theme",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            bottomBar = {
                // Horizontal Module Navigation Bar
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DevFlowModule.values().forEach { module ->
                            val isSelected = module == currentModule
                            val icon = getModuleIcon(module)
                            val iconColor = when {
                                isSelected -> DevPrimaryCyan
                                module == DevFlowModule.AI_CODE_ANALYSIS -> DevAccentPurple
                                module == DevFlowModule.API_DOCS -> DevAccentPurple
                                module == DevFlowModule.DEPLOYMENTS -> DevAccentGreen
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) DevPrimaryCyan.copy(alpha = 0.15f) else Color.Transparent)
                                    .border(
                                        1.dp,
                                        if (isSelected) DevPrimaryCyan.copy(alpha = 0.5f) else Color.Transparent,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { viewModel.navigateTo(module) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                    .testTag("nav_${module.name.lowercase()}")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = module.displayName,
                                        tint = iconColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = module.displayName,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (isSelected) DevPrimaryCyan else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // Active Module Screen Content
                when (currentModule) {
                    DevFlowModule.DASHBOARD -> DashboardScreen(viewModel = viewModel, onNavigate = { viewModel.navigateTo(it) })
                    DevFlowModule.PROJECTS -> ProjectsScreen(viewModel = viewModel)
                    DevFlowModule.REPOSITORIES -> RepositoriesScreen(viewModel = viewModel)
                    DevFlowModule.ISSUES -> IssuesScreen(viewModel = viewModel)
                    DevFlowModule.PULL_REQUESTS -> PullRequestsScreen(viewModel = viewModel)
                    DevFlowModule.CODE_ACTIVITY -> CodeActivityScreen(viewModel = viewModel)
                    DevFlowModule.AI_CODE_ANALYSIS -> CodeAnalysisPanel(service = viewModel.codeAnalysisService)
                    DevFlowModule.CI_CD -> CiCdScreen(viewModel = viewModel)
                    DevFlowModule.DEPLOYMENTS -> DeploymentsScreen(viewModel = viewModel)
                    DevFlowModule.ENVIRONMENTS -> EnvironmentsScreen(viewModel = viewModel)
                    DevFlowModule.API_DOCS -> ApiDocScreen(viewModel = viewModel)
                    DevFlowModule.TASKS -> TasksScreen(viewModel = viewModel)
                    DevFlowModule.DOCUMENTATION -> DocumentationScreen(viewModel = viewModel)
                    DevFlowModule.TEAM -> TeamScreen(viewModel = viewModel)
                    DevFlowModule.ANALYTICS -> AnalyticsScreen(viewModel = viewModel)
                    DevFlowModule.SETTINGS -> SettingsScreen(viewModel = viewModel)
                }

                // Docked Resizable Terminal
                AnimatedVisibility(
                    visible = isTerminalOpen,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    TerminalPanel(
                        logs = terminalLogs,
                        onExecuteCommand = { viewModel.executeTerminalCommand(it) },
                        onClose = { viewModel.toggleTerminal(false) }
                    )
                }

                // Docked AI Assistant Drawer
                AnimatedVisibility(
                    visible = isAiOpen,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    AiAssistantDrawer(
                        messages = aiMessages,
                        isLoading = isAiGenerating,
                        onAskPrompt = { promptType, query -> viewModel.askAiAssistant(promptType, query) },
                        onClose = { viewModel.toggleAiAssistant(false) }
                    )
                }
            }

            // Command Palette Modal
            if (isPaletteOpen) {
                CommandPaletteDialog(
                    onDismiss = { viewModel.toggleCommandPalette(false) },
                    onNavigate = { viewModel.navigateTo(it) },
                    onTriggerDeploy = { viewModel.triggerNewDeployment("Production") },
                    onOpenTerminal = { viewModel.toggleTerminal(true) },
                    onOpenAi = { viewModel.toggleAiAssistant(true) },
                    onToggleTheme = { viewModel.toggleTheme() }
                )
            }

            // Keyboard Shortcuts Modal
            if (isShortcutsOpen) {
                KeyboardShortcutsDialog(
                    onDismiss = { viewModel.toggleShortcutsDialog(false) }
                )
            }
        }
    }
}

fun getModuleIcon(module: DevFlowModule): ImageVector {
    return when (module) {
        DevFlowModule.DASHBOARD -> Icons.Default.Dashboard
        DevFlowModule.PROJECTS -> Icons.Default.Folder
        DevFlowModule.REPOSITORIES -> Icons.Default.Code
        DevFlowModule.ISSUES -> Icons.Default.FormatListBulleted
        DevFlowModule.PULL_REQUESTS -> Icons.Default.Hub
        DevFlowModule.CODE_ACTIVITY -> Icons.Default.Timeline
        DevFlowModule.AI_CODE_ANALYSIS -> Icons.Default.AutoAwesome
        DevFlowModule.CI_CD -> Icons.Default.Code
        DevFlowModule.DEPLOYMENTS -> Icons.Default.CloudUpload
        DevFlowModule.ENVIRONMENTS -> Icons.Default.Code
        DevFlowModule.API_DOCS -> Icons.Default.MenuBook
        DevFlowModule.TASKS -> Icons.Default.FormatListBulleted
        DevFlowModule.DOCUMENTATION -> Icons.Default.MenuBook
        DevFlowModule.TEAM -> Icons.Default.People
        DevFlowModule.ANALYTICS -> Icons.Default.Speed
        DevFlowModule.SETTINGS -> Icons.Default.Settings
    }
}
