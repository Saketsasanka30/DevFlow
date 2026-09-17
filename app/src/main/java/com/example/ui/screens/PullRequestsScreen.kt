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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Hub
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
import com.example.data.ai.AiPromptType
import com.example.ui.components.CiStatusPill
import com.example.ui.components.PrStatusPill
import com.example.ui.theme.DevAccentGreen
import com.example.ui.theme.DevAccentPurple
import com.example.ui.theme.DevAccentRed
import com.example.ui.theme.DevBorderDark
import com.example.ui.theme.DevCodeFont
import com.example.ui.theme.DevPrimaryCyan
import com.example.ui.theme.DevSurfaceCardDark
import com.example.viewmodel.DevFlowViewModel

@Composable
fun PullRequestsScreen(
    viewModel: DevFlowViewModel,
    modifier: Modifier = Modifier
) {
    val pullRequests by viewModel.pullRequests.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Pull Requests", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
                Text("${pullRequests.count { it.status == "OPEN" }} open for review • Automated CI and AI summarization", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(pullRequests) { pr ->
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
                                text = "#${pr.number} ${pr.title}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            PrStatusPill(pr.status)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Branches info
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(DevPrimaryCyan.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(pr.sourceBranch, fontFamily = DevCodeFont, style = MaterialTheme.typography.labelSmall, color = DevPrimaryCyan)
                            }
                            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(12.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(DevSurfaceCardDark)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(pr.targetBranch, fontFamily = DevCodeFont, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = pr.summary,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
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
                                Text("@${pr.author}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                                Text("+${pr.additions}", fontFamily = DevCodeFont, style = MaterialTheme.typography.labelSmall, color = DevAccentGreen)
                                Text("-${pr.deletions}", fontFamily = DevCodeFont, style = MaterialTheme.typography.labelSmall, color = DevAccentRed)
                                CiStatusPill(pr.ciStatus)
                            }

                            // AI PR Summarizer Quick Action
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(DevAccentPurple.copy(alpha = 0.15f))
                                    .border(1.dp, DevAccentPurple.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
                                    .clickable {
                                        viewModel.selectPullRequest(pr)
                                        viewModel.toggleAiAssistant(true)
                                        viewModel.askAiAssistant(
                                            AiPromptType.SUMMARIZE_PR,
                                            "Summarize PR #${pr.number}: ${pr.title}\nBranch: ${pr.sourceBranch} -> ${pr.targetBranch}\nSummary: ${pr.summary}"
                                        )
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = DevAccentPurple, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("AI Review", style = MaterialTheme.typography.labelSmall, color = DevAccentPurple)
                            }
                        }
                    }
                }
            }
        }
    }
}
