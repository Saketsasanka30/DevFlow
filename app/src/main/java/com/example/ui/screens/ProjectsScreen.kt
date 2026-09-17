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
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ProjectEntity
import com.example.ui.components.CiStatusPill
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
import com.example.viewmodel.ProjectWorkspaceTab

@Composable
fun ProjectsScreen(
    viewModel: DevFlowViewModel,
    modifier: Modifier = Modifier
) {
    val projects by viewModel.projects.collectAsState()
    val selectedProjectId by viewModel.selectedProjectId.collectAsState()
    val currentTab by viewModel.currentProjectTab.collectAsState()

    val activeProject = projects.firstOrNull { it.id == selectedProjectId } ?: projects.firstOrNull()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Project Selector Chips Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            projects.forEach { proj ->
                val isSelected = proj.id == activeProject?.id
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) DevPrimaryCyan.copy(alpha = 0.15f) else DevSurfaceCardDark)
                        .border(
                            1.dp,
                            if (isSelected) DevPrimaryCyan else DevBorderDark,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { viewModel.selectProject(proj.id) }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Folder,
                            contentDescription = null,
                            tint = if (isSelected) DevPrimaryCyan else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = proj.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (isSelected) DevPrimaryCyan else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Active Project Header Info
        activeProject?.let { proj ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DevSurfaceCardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, DevBorderDark),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = proj.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                StatusBadge(
                                    text = proj.status,
                                    backgroundColor = DevAccentGreen,
                                    textColor = DevAccentGreen
                                )
                            }
                            Text(
                                text = proj.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Health Score Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(DevAccentGreen.copy(alpha = 0.15f))
                                .border(1.dp, DevAccentGreen.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${proj.healthScore}%",
                                    fontFamily = DevCodeFont,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = DevAccentGreen
                                )
                                Text(
                                    text = "HEALTH",
                                    fontFamily = DevCodeFont,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = DevAccentGreen
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Code, contentDescription = null, tint = DevPrimaryCyan, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = proj.repoName,
                                fontFamily = DevCodeFont,
                                style = MaterialTheme.typography.labelSmall,
                                color = DevPrimaryCyan
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = DevAccentYellow, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${proj.stars} stars",
                                fontFamily = DevCodeFont,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "Tech: ${proj.techStack}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Workspace Tabs (Overview, Tasks, Repository, Deployments, Documentation, Activity, Analytics)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ProjectWorkspaceTab.values().forEach { tab ->
                val isSelected = tab == currentTab
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) DevSurfaceDark else Color.Transparent)
                        .border(
                            1.dp,
                            if (isSelected) DevPrimaryCyan else Color.Transparent,
                            RoundedCornerShape(6.dp)
                        )
                        .clickable { viewModel.setProjectWorkspaceTab(tab) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = tab.label,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isSelected) DevPrimaryCyan else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Tab Content
        Box(modifier = Modifier.weight(1f)) {
            when (currentTab) {
                ProjectWorkspaceTab.OVERVIEW -> ProjectOverviewTab(viewModel)
                ProjectWorkspaceTab.TASKS -> ProjectTasksTab(viewModel)
                ProjectWorkspaceTab.REPOSITORY -> ProjectRepoTab(viewModel)
                ProjectWorkspaceTab.DEPLOYMENTS -> ProjectDeploymentsTab(viewModel)
                ProjectWorkspaceTab.DOCUMENTATION -> ProjectDocsTab(viewModel)
                ProjectWorkspaceTab.ACTIVITY -> ProjectActivityTab(viewModel)
                ProjectWorkspaceTab.ANALYTICS -> ProjectAnalyticsTab(viewModel)
            }
        }
    }
}

@Composable
fun ProjectOverviewTab(viewModel: DevFlowViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    val deployments by viewModel.deployments.collectAsState()
    val team by viewModel.teamMembers.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DevSurfaceCardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, DevBorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Workspace Summary", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Everything configured in this workspace is linked to live git webhooks, local SQLite cache, and automated deployment pipelines.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${tasks.count { it.status != "DONE" }}", fontFamily = DevCodeFont, style = MaterialTheme.typography.titleLarge, color = DevPrimaryCyan)
                            Text("Open Tasks", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${deployments.size}", fontFamily = DevCodeFont, style = MaterialTheme.typography.titleLarge, color = DevAccentPurple)
                            Text("Deployments", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${team.size}", fontFamily = DevCodeFont, style = MaterialTheme.typography.titleLarge, color = DevAccentGreen)
                            Text("Engineers", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        item {
            Text("Recent Project Tasks", fontFamily = DevCodeFont, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        items(tasks.take(3)) { task ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(DevSurfaceCardDark)
                    .border(1.dp, DevBorderDark, RoundedCornerShape(8.dp))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(task.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                    Text("${task.assignee} • ${task.storyPoints} pts", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                PriorityPill(task.priority)
            }
        }
    }
}

@Composable
fun ProjectTasksTab(viewModel: DevFlowViewModel) {
    val tasks by viewModel.tasks.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(tasks) { task ->
            Card(
                colors = CardDefaults.cardColors(containerColor = DevSurfaceCardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, DevBorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(task.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                        PriorityPill(task.priority)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(task.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Status: ${task.status}", fontFamily = DevCodeFont, style = MaterialTheme.typography.labelSmall, color = DevPrimaryCyan)
                        Text("Assignee: ${task.assignee}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectRepoTab(viewModel: DevFlowViewModel) {
    val repos by viewModel.repositories.collectAsState()
    val branches by viewModel.branches.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DevSurfaceCardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, DevBorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Clone & Repository Setup", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(DevSurfaceDark)
                            .border(1.dp, DevBorderDark, RoundedCornerShape(6.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "git clone https://github.com/devflow/devflow-android.git",
                            fontFamily = DevCodeFont,
                            style = MaterialTheme.typography.labelSmall,
                            color = DevAccentGreen
                        )
                    }
                }
            }
        }

        item {
            Text("Active Git Branches", fontFamily = DevCodeFont, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        items(branches) { b ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(DevSurfaceCardDark)
                    .border(1.dp, DevBorderDark, RoundedCornerShape(8.dp))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(b.name, fontFamily = DevCodeFont, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                if (b.isDefault) {
                    StatusBadge("DEFAULT", DevAccentGreen, DevAccentGreen, hasDot = false)
                }
            }
        }
    }
}

@Composable
fun ProjectDeploymentsTab(viewModel: DevFlowViewModel) {
    val deployments by viewModel.deployments.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(deployments) { dep ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(DevSurfaceCardDark)
                    .border(1.dp, DevBorderDark, RoundedCornerShape(8.dp))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("${dep.environment} #${dep.buildNumber}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                    Text("${dep.commitHash} • ${dep.deployedAt}", fontFamily = DevCodeFont, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                CiStatusPill(dep.status)
            }
        }
    }
}

@Composable
fun ProjectDocsTab(viewModel: DevFlowViewModel) {
    val docs by viewModel.documents.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(docs) { doc ->
            Card(
                colors = CardDefaults.cardColors(containerColor = DevSurfaceCardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, DevBorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(doc.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                        StatusBadge(doc.category, DevPrimaryCyan, DevPrimaryCyan, hasDot = false)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Author: ${doc.author} • Updated: ${doc.updatedAt}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = doc.content.take(200) + "...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 4
                    )
                }
            }
        }
    }
}

@Composable
fun ProjectActivityTab(viewModel: DevFlowViewModel) {
    val logs by viewModel.activityLogs.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(logs) { log ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(DevSurfaceCardDark)
                    .border(1.dp, DevBorderDark, RoundedCornerShape(8.dp))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(DevPrimaryCyan)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("${log.user} ${log.action} ${log.target}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                    Text("${log.type} • ${log.timestamp}", fontFamily = DevCodeFont, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun ProjectAnalyticsTab(viewModel: DevFlowViewModel) {
    val codeHealth by viewModel.codeHealth.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = DevSurfaceCardDark),
            border = androidx.compose.foundation.BorderStroke(1.dp, DevBorderDark),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("Project Velocity & Health", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(10.dp))
                Text("• Build Success Rate: ${codeHealth.buildSuccessRatePct}%", fontFamily = DevCodeFont, color = DevAccentGreen)
                Text("• Average PR Cycle Time: ${codeHealth.prCycleTimeHours} hours", fontFamily = DevCodeFont, color = DevPrimaryCyan)
                Text("• Deployment Frequency: ${codeHealth.deploymentFrequencyPerDay} rollouts/day", fontFamily = DevCodeFont, color = DevAccentPurple)
                Text("• Issue MTTR: ${codeHealth.issueResolutionDays} days", fontFamily = DevCodeFont, color = DevAccentYellow)
            }
        }
    }
}
