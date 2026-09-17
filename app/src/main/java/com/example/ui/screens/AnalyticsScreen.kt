package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
fun AnalyticsScreen(
    viewModel: DevFlowViewModel,
    modifier: Modifier = Modifier
) {
    val health by viewModel.codeHealth.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Developer & Code Health Analytics", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
                    Text("DORA benchmarks, velocity telemetry and cycle time analysis", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                StatusBadge("ELITE STATUS", DevAccentGreen, DevAccentGreen)
            }
        }

        // DORA Metrics Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "Build Success Rate",
                    value = "${health.buildSuccessRatePct}%",
                    trend = "+2.1% (30d)",
                    icon = Icons.Default.CheckCircle,
                    color = DevAccentGreen,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "PR Cycle Time",
                    value = "${health.prCycleTimeHours}h",
                    trend = "Benchmark: <4h",
                    icon = Icons.Default.Speed,
                    color = DevPrimaryCyan,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "Deploy Frequency",
                    value = "${health.deploymentFrequencyPerDay}/day",
                    trend = "Multiple / day",
                    icon = Icons.Default.CloudUpload,
                    color = DevAccentPurple,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Issue Resolution (MTTR)",
                    value = "${health.issueResolutionDays} days",
                    trend = "-18% lead time",
                    icon = Icons.Default.TrendingUp,
                    color = DevAccentYellow,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Code Quality & Coverage Breakdown
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DevSurfaceCardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, DevBorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Codebase Quality & Test Coverage", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(12.dp))

                    QualityMetricRow("Automated Unit Test Coverage", 0.88f, "88.4%", DevAccentGreen)
                    Spacer(modifier = Modifier.height(10.dp))
                    QualityMetricRow("Static Security & CodeQL Pass Rate", 0.98f, "98.2%", DevPrimaryCyan)
                    Spacer(modifier = Modifier.height(10.dp))
                    QualityMetricRow("PR First-Pass Approval Rate", 0.74f, "74.0%", DevAccentPurple)
                    Spacer(modifier = Modifier.height(10.dp))
                    QualityMetricRow("Deployment Failure Recovery (<30 min)", 0.95f, "95.0%", DevAccentYellow)
                }
            }
        }
    }
}

@Composable
fun QualityMetricRow(
    title: String,
    progress: Float,
    percentText: String,
    color: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
            Text(percentText, fontFamily = DevCodeFont, style = MaterialTheme.typography.labelSmall, color = color)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = DevSurfaceDark
        )
    }
}
