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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.TrendingUp
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CiStatusPill
import com.example.ui.components.EnvironmentPill
import com.example.ui.theme.DevAccentGreen
import com.example.ui.theme.DevAccentPurple
import com.example.ui.theme.DevAccentRed
import com.example.ui.theme.DevAccentYellow
import com.example.ui.theme.DevBorderDark
import com.example.ui.theme.DevCodeFont
import com.example.ui.theme.DevPrimaryCyan
import com.example.ui.theme.DevSurfaceCardDark
import com.example.ui.theme.DevSurfaceDark
import com.example.viewmodel.DevFlowModule
import com.example.viewmodel.DevFlowViewModel

@Composable
fun DashboardScreen(
    viewModel: DevFlowViewModel,
    onNavigate: (DevFlowModule) -> Unit,
    modifier: Modifier = Modifier
) {
    val codeHealth by viewModel.codeHealth.collectAsState()
    val deployments by viewModel.deployments.collectAsState()
    val pullRequests by viewModel.pullRequests.collectAsState()
    val commits by viewModel.commits.collectAsState()
    val projects by viewModel.projects.collectAsState()
    val activeProject = projects.firstOrNull { it.id == viewModel.selectedProjectId.collectAsState().value }
        ?: projects.firstOrNull()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Header & Quick Action Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DevSurfaceCardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, DevBorderDark),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "DevFlow Workspace",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(DevAccentGreen.copy(alpha = 0.15f))
                                        .border(1.dp, DevAccentGreen.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "LIVE",
                                        fontFamily = DevCodeFont,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = DevAccentGreen
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Active Project: ${activeProject?.name ?: "DevFlow Core"} (${activeProject?.techStack ?: "Kotlin / Compose"})",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Quick action buttons
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { viewModel.toggleCommandPalette(true) },
                                colors = ButtonDefaults.buttonColors(containerColor = DevPrimaryCyan),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("dashboard_command_palette_button")
                            ) {
                                Text("⌘K Palette", color = Color(0xFF090D16), style = MaterialTheme.typography.labelMedium)
                            }
                            Button(
                                onClick = { viewModel.toggleTerminal(true) },
                                colors = ButtonDefaults.buttonColors(containerColor = DevSurfaceDark),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DevBorderDark),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("dashboard_terminal_button")
                            ) {
                                Icon(Icons.Default.Terminal, contentDescription = null, modifier = Modifier.size(16.dp), tint = DevAccentGreen)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Terminal", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }
            }
        }

        // DORA & Code Health Metric Cards
        item {
            Text(
                text = "CODE HEALTH & DORA METRICS",
                fontFamily = DevCodeFont,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "Build Success",
                    value = "${codeHealth.buildSuccessRatePct}%",
                    trend = "+1.8%",
                    icon = Icons.Default.CheckCircle,
                    color = DevAccentGreen,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "PR Cycle Time",
                    value = "${codeHealth.prCycleTimeHours}h",
                    trend = "-45m",
                    icon = Icons.Default.Speed,
                    color = DevPrimaryCyan,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "Deploy Frequency",
                    value = "${codeHealth.deploymentFrequencyPerDay}/d",
                    trend = "Elite",
                    icon = Icons.Default.CloudUpload,
                    color = DevAccentPurple,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Issue Resolution",
                    value = "${codeHealth.issueResolutionDays}d",
                    trend = "-0.4d",
                    icon = Icons.Default.TrendingUp,
                    color = DevAccentYellow,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Active Environments Summary Card
        item {
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
                        Text(
                            text = "Environments & Deployments",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "View All →",
                            style = MaterialTheme.typography.labelSmall,
                            color = DevPrimaryCyan,
                            modifier = Modifier.clickable { onNavigate(DevFlowModule.DEPLOYMENTS) }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    deployments.take(3).forEach { dep ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(DevSurfaceDark)
                                .border(1.dp, DevBorderDark, RoundedCornerShape(8.dp))
                                .clickable {
                                    viewModel.selectDeployment(dep)
                                    onNavigate(DevFlowModule.DEPLOYMENTS)
                                }
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    EnvironmentPill(dep.environment)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Build #${dep.buildNumber}",
                                        fontFamily = DevCodeFont,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = dep.commitMessage,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                            }
                            CiStatusPill(dep.status)
                        }
                    }
                }
            }
        }

        // Open Pull Requests & Review Status
        item {
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
                        Text(
                            text = "Active Pull Requests",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "PRs Board →",
                            style = MaterialTheme.typography.labelSmall,
                            color = DevPrimaryCyan,
                            modifier = Modifier.clickable { onNavigate(DevFlowModule.PULL_REQUESTS) }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    pullRequests.take(2).forEach { pr ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(DevSurfaceDark)
                                .border(1.dp, DevBorderDark, RoundedCornerShape(8.dp))
                                .clickable {
                                    viewModel.selectPullRequest(pr)
                                    onNavigate(DevFlowModule.PULL_REQUESTS)
                                }
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "#${pr.number} ${pr.title}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    modifier = Modifier.weight(1f)
                                )
                                CiStatusPill(pr.ciStatus)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "@${pr.author}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = DevPrimaryCyan
                                )
                                Text(
                                    text = "+${pr.additions} / -${pr.deletions}",
                                    fontFamily = DevCodeFont,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = DevAccentGreen
                                )
                                Text(
                                    text = pr.reviewStatus,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Recent Commits Feed
        item {
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
                        Text(
                            text = "Recent Git Commits",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Code Activity →",
                            style = MaterialTheme.typography.labelSmall,
                            color = DevPrimaryCyan,
                            modifier = Modifier.clickable { onNavigate(DevFlowModule.CODE_ACTIVITY) }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    commits.take(3).forEach { commit ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(DevPrimaryCyan.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = commit.authorAvatar,
                                    fontFamily = DevCodeFont,
                                    fontSize = 11.sp,
                                    color = DevPrimaryCyan
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = commit.message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1
                                )
                                Row {
                                    Text(
                                        text = commit.shortHash,
                                        fontFamily = DevCodeFont,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = DevAccentPurple
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${commit.author} • ${commit.timestamp}",
                                        style = MaterialTheme.typography.bodySmall,
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

@Composable
fun MetricCard(
    title: String,
    value: String,
    trend: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(DevSurfaceCardDark)
            .border(1.dp, DevBorderDark, RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = value,
                    fontFamily = DevCodeFont,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = trend,
                    fontFamily = DevCodeFont,
                    style = MaterialTheme.typography.labelSmall,
                    color = color
                )
            }
        }
    }
}
