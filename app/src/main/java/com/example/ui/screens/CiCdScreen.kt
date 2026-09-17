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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Sync
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
import androidx.compose.ui.unit.dp
import com.example.ui.components.CiStatusPill
import com.example.ui.theme.DevAccentGreen
import com.example.ui.theme.DevAccentPurple
import com.example.ui.theme.DevBorderDark
import com.example.ui.theme.DevCodeFont
import com.example.ui.theme.DevPrimaryCyan
import com.example.ui.theme.DevSurfaceCardDark
import com.example.viewmodel.DevFlowViewModel

data class PipelineWorkflow(
    val name: String,
    val trigger: String,
    val branch: String,
    val duration: String,
    val status: String,
    val steps: List<String>
)

@Composable
fun CiCdScreen(
    viewModel: DevFlowViewModel,
    modifier: Modifier = Modifier
) {
    val workflows = listOf(
        PipelineWorkflow(
            name = "Android Build & Robolectric Unit Tests",
            trigger = "push to main",
            branch = "main",
            duration = "1m 42s",
            status = "SUCCESS",
            steps = listOf("Checkout Code", "Setup JDK 17", "Assemble Debug APK", "Run Robolectric Tests", "Roborazzi Screenshot Check")
        ),
        PipelineWorkflow(
            name = "Docker Container Build & Helm Deploy",
            trigger = "tag release v2.4.0",
            branch = "main",
            duration = "3m 15s",
            status = "SUCCESS",
            steps = listOf("Docker Build", "Scan Vulnerabilities (Trivy)", "Push to GHCR", "Apply Kubernetes Manifests")
        ),
        PipelineWorkflow(
            name = "Staging Smoke & E2E Validation",
            trigger = "PR merge #402",
            branch = "staging",
            duration = "52s",
            status = "SUCCESS",
            steps = listOf("Deploy to Staging Cluster", "Run Synthetic Health Check", "Ingress Route Verification")
        ),
        PipelineWorkflow(
            name = "Nightly Security Audit & Dependency Check",
            trigger = "cron(0 2 * * *)",
            branch = "main",
            duration = "4m 10s",
            status = "SUCCESS",
            steps = listOf("OWASP Dependency Check", "Static Analysis", "CodeQL Scanning")
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("CI/CD Pipeline Automation", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
            Text("Automated workflows, continuous integration & test runners", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        items(workflows) { wf ->
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
                        Text(wf.name, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                        CiStatusPill(wf.status)
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Trigger: ${wf.trigger} • Branch: ${wf.branch} • ${wf.duration}", fontFamily = DevCodeFont, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Pipeline Execution Steps:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))

                    wf.steps.forEachIndexed { idx, step ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DevAccentGreen, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${idx + 1}. $step",
                                fontFamily = DevCodeFont,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFCBD5E1)
                            )
                        }
                    }
                }
            }
        }
    }
}
