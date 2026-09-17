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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Terminal
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
import com.example.ui.components.EnvironmentPill
import com.example.ui.theme.DevAccentGreen
import com.example.ui.theme.DevAccentPurple
import com.example.ui.theme.DevAccentRed
import com.example.ui.theme.DevBorderDark
import com.example.ui.theme.DevCodeFont
import com.example.ui.theme.DevPrimaryCyan
import com.example.ui.theme.DevSurfaceCardDark
import com.example.ui.theme.DevSurfaceDark
import com.example.ui.theme.DevTerminalBg
import com.example.viewmodel.DevFlowViewModel

@Composable
fun DeploymentsScreen(
    viewModel: DevFlowViewModel,
    modifier: Modifier = Modifier
) {
    val deployments by viewModel.deployments.collectAsState()
    val selectedDeployment by viewModel.selectedDeployment.collectAsState()
    val active = selectedDeployment ?: deployments.firstOrNull()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Deploy Action Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Deployments & Rollouts", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
                Text("Real-time pipeline orchestration & log telemetry", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Button(
                onClick = { viewModel.triggerNewDeployment("Production") },
                colors = ButtonDefaults.buttonColors(containerColor = DevAccentGreen),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("deploy_production_button")
            ) {
                Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Color(0xFF090D16), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Deploy Prod", color = Color(0xFF090D16), style = MaterialTheme.typography.labelMedium)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Selected Deployment Detail / Logs Console
        active?.let { dep ->
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            EnvironmentPill(dep.environment)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Build #${dep.buildNumber}",
                                fontFamily = DevCodeFont,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        CiStatusPill(dep.status)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Commit: ${dep.commitHash}", fontFamily = DevCodeFont, style = MaterialTheme.typography.labelSmall, color = DevPrimaryCyan)
                        Text("Duration: ${dep.durationSeconds}s", fontFamily = DevCodeFont, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("By: ${dep.deployedBy}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // ANSI Terminal Log Output Area
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DevTerminalBg)
                            .border(1.dp, DevBorderDark, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(dep.logs.lines()) { line ->
                                Text(
                                    text = line,
                                    fontFamily = DevCodeFont,
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp,
                                    color = when {
                                        line.contains("FAILED") || line.contains("PANIC") || line.contains("ERROR") -> DevAccentRed
                                        line.contains("SUCCESS") || line.contains("verified") -> DevAccentGreen
                                        else -> Color(0xFF94A3B8)
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Actions Row (Rollback & AI Error Diagnosis)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { viewModel.rollbackDeployment(dep.buildNumber) },
                            colors = ButtonDefaults.buttonColors(containerColor = DevSurfaceDark),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DevBorderDark),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Icon(Icons.Default.History, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Rollback Build", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface)
                        }

                        // AI Error Analysis
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(DevAccentPurple.copy(alpha = 0.15f))
                                .border(1.dp, DevAccentPurple.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                            .clickable {
                                viewModel.selectDeployment(dep)
                                viewModel.toggleAiAssistant(true)
                                viewModel.askAiAssistant(
                                    AiPromptType.EXPLAIN_ERRORS,
                                    "Explain deployment logs for ${dep.environment} #${dep.buildNumber}:\n${dep.logs}"
                                )
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = DevAccentPurple, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("AI Explain Logs", style = MaterialTheme.typography.labelSmall, color = DevAccentPurple)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Deployment Pipeline History", fontFamily = DevCodeFont, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(6.dp))

        // History List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(deployments) { dep ->
                val isSelected = dep.id == active?.id
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) DevPrimaryCyan.copy(alpha = 0.1f) else DevSurfaceCardDark)
                        .border(1.dp, if (isSelected) DevPrimaryCyan else DevBorderDark, RoundedCornerShape(8.dp))
                        .clickable { viewModel.selectDeployment(dep) }
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            EnvironmentPill(dep.environment)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Build #${dep.buildNumber}", fontFamily = DevCodeFont, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(dep.commitMessage, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                        Text("${dep.deployedAt} • by ${dep.deployedBy} • ${dep.durationSeconds}s", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    CiStatusPill(dep.status)
                }
            }
        }
    }
}
