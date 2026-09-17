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
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Commit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
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

enum class RepoViewTab(val label: String) {
    REPOSITORIES("Repositories"),
    COMMITS("Commits"),
    BRANCHES("Branches"),
    RELEASES("Releases"),
    CONTRIBUTORS("Contributors")
}

@Composable
fun RepositoriesScreen(
    viewModel: DevFlowViewModel,
    modifier: Modifier = Modifier
) {
    val repositories by viewModel.repositories.collectAsState()
    val commits by viewModel.commits.collectAsState()
    val branches by viewModel.branches.collectAsState()
    val releases by viewModel.releases.collectAsState()
    val teamMembers by viewModel.teamMembers.collectAsState()

    var activeTab by remember { mutableStateOf(RepoViewTab.REPOSITORIES) }
    var isSyncing by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // GitHub Connection & Sync Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DevSurfaceCardDark),
            border = androidx.compose.foundation.BorderStroke(1.dp, DevBorderDark),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "GitHub Integration Layer",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        StatusBadge(
                            text = if (viewModel.gitHubIntegration.isConnected) "AUTHENTICATED" else "SYNCED",
                            backgroundColor = DevAccentGreen,
                            textColor = DevAccentGreen
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Synchronized with organization: ${viewModel.gitHubIntegration.currentConnectedOrg}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = {
                        isSyncing = true
                        viewModel.syncWithGitHub()
                        isSyncing = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DevSurfaceDark),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DevBorderDark),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("github_sync_button")
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Sync", tint = DevPrimaryCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Sync", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tab Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            RepoViewTab.values().forEach { tab ->
                val isSelected = tab == activeTab
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) DevSurfaceDark else Color.Transparent)
                        .border(
                            1.dp,
                            if (isSelected) DevPrimaryCyan else Color.Transparent,
                            RoundedCornerShape(6.dp)
                        )
                        .clickable { activeTab = tab }
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
            when (activeTab) {
                RepoViewTab.REPOSITORIES -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(repositories) { repo ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = DevSurfaceCardDark),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DevBorderDark),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${repo.owner}/${repo.name}",
                                            fontFamily = DevCodeFont,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = DevPrimaryCyan
                                        )
                                        StatusBadge(
                                            text = if (repo.isPrivate) "PRIVATE" else "PUBLIC",
                                            backgroundColor = if (repo.isPrivate) DevAccentYellow else DevAccentGreen,
                                            textColor = if (repo.isPrivate) DevAccentYellow else DevAccentGreen,
                                            hasDot = false
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(repo.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("★ ${repo.stars}", fontFamily = DevCodeFont, style = MaterialTheme.typography.labelSmall, color = DevAccentYellow)
                                        Text("⑂ ${repo.forks}", fontFamily = DevCodeFont, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text("Lang: ${repo.language}", style = MaterialTheme.typography.labelSmall, color = DevAccentPurple)
                                        Text("Branch: ${repo.defaultBranch}", fontFamily = DevCodeFont, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                }

                RepoViewTab.COMMITS -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(commits) { commit ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = DevSurfaceCardDark),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DevBorderDark),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(DevPrimaryCyan.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = commit.authorAvatar,
                                            fontFamily = DevCodeFont,
                                            fontSize = 12.sp,
                                            color = DevPrimaryCyan
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(commit.message, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(commit.shortHash, fontFamily = DevCodeFont, style = MaterialTheme.typography.labelSmall, color = DevAccentPurple)
                                            Text(commit.author, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            Text(commit.timestamp, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            Text("+${commit.additions} / -${commit.deletions}", fontFamily = DevCodeFont, style = MaterialTheme.typography.labelSmall, color = DevAccentGreen)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                RepoViewTab.BRANCHES -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(branches) { branch ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DevSurfaceCardDark)
                                    .border(1.dp, DevBorderDark, RoundedCornerShape(8.dp))
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AltRoute, contentDescription = null, tint = DevPrimaryCyan, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(branch.name, fontFamily = DevCodeFont, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                                }
                                if (branch.isDefault) {
                                    StatusBadge("DEFAULT", DevAccentGreen, DevAccentGreen, hasDot = false)
                                }
                            }
                        }
                    }
                }

                RepoViewTab.RELEASES -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(releases) { rel ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = DevSurfaceCardDark),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DevBorderDark),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(rel.name, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                                        StatusBadge(rel.tag, DevPrimaryCyan, DevPrimaryCyan, hasDot = false)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Published: ${rel.publishedAt} by ${rel.author} • Downloads: ${rel.downloadCount}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(rel.changelog, fontFamily = DevCodeFont, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                                }
                            }
                        }
                    }
                }

                RepoViewTab.CONTRIBUTORS -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(teamMembers) { member ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DevSurfaceCardDark)
                                    .border(1.dp, DevBorderDark, RoundedCornerShape(8.dp))
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(DevAccentPurple.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(member.avatarInitials, fontFamily = DevCodeFont, color = DevAccentPurple)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(member.name, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                                        Text("@${member.githubHandle} • ${member.role}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                                Text("${member.commitsCount} commits", fontFamily = DevCodeFont, style = MaterialTheme.typography.labelSmall, color = DevPrimaryCyan)
                            }
                        }
                    }
                }
            }
        }
    }
}
