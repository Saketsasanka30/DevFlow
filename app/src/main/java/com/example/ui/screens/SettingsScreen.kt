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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.ui.components.StatusBadge
import com.example.ui.theme.DevAccentGreen
import com.example.ui.theme.DevAccentPurple
import com.example.ui.theme.DevBorderDark
import com.example.ui.theme.DevCodeFont
import com.example.ui.theme.DevPrimaryCyan
import com.example.ui.theme.DevSurfaceCardDark
import com.example.ui.theme.DevSurfaceDark
import com.example.viewmodel.DevFlowViewModel

@Composable
fun SettingsScreen(
    viewModel: DevFlowViewModel,
    modifier: Modifier = Modifier
) {
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    var ghToken by remember { mutableStateOf(viewModel.gitHubIntegration.personalAccessToken) }
    var geminiKey by remember { mutableStateOf(viewModel.aiAssistant.userCustomApiKey) }
    var saveStatus by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("DevFlow Workspace Settings", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
            Text("Manage API credentials, GitHub integration, AI models and system appearance", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        // Appearance / Theme
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DevSurfaceCardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, DevBorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = null,
                            tint = DevPrimaryCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Developer Dark Theme", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                            Text("Monochrome high-contrast dark aesthetic", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { viewModel.toggleTheme() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = DevPrimaryCyan,
                            checkedTrackColor = DevPrimaryCyan.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.testTag("theme_toggle_switch")
                    )
                }
            }
        }

        // GitHub Integration Settings
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DevSurfaceCardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, DevBorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Code, contentDescription = null, tint = DevPrimaryCyan, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("GitHub Connection Layer", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                        }
                        StatusBadge(
                            text = if (viewModel.gitHubIntegration.isConnected) "CONFIGURED" else "OPTIONAL",
                            backgroundColor = DevAccentGreen,
                            textColor = DevAccentGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Enter a GitHub Personal Access Token (PAT with 'repo' scope) to authenticate live rate limits (5,000 req/hr) and private repositories.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = ghToken,
                        onValueChange = { ghToken = it },
                        placeholder = { Text("ghp_xxxxxxxxxxxxxxxxxxxx") },
                        label = { Text("GitHub Token (PAT)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("github_token_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DevPrimaryCyan,
                            unfocusedBorderColor = DevBorderDark,
                            focusedContainerColor = DevSurfaceDark,
                            unfocusedContainerColor = DevSurfaceDark
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            viewModel.saveGitHubToken(ghToken)
                            saveStatus = "GitHub token saved successfully!"
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DevPrimaryCyan),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("save_github_token_button")
                    ) {
                        Text("Save GitHub Config", color = Color(0xFF090D16), style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }

        // Gemini AI Assistant Settings
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DevSurfaceCardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, DevBorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = DevAccentPurple, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Gemini Developer Intelligence", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                        }
                        StatusBadge("GEMINI 2.5 FLASH", DevAccentPurple, DevAccentPurple)
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Configures live AI developer assistant capabilities (Explain Repo, Summarize PR, Find Bugs, Generate Docs, Explain Errors, Suggest Tests).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = geminiKey,
                        onValueChange = { geminiKey = it },
                        placeholder = { Text("AIzaSy...") },
                        label = { Text("Gemini API Key (Optional Override)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("gemini_api_key_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DevAccentPurple,
                            unfocusedBorderColor = DevBorderDark,
                            focusedContainerColor = DevSurfaceDark,
                            unfocusedContainerColor = DevSurfaceDark
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            viewModel.saveGeminiApiKey(geminiKey)
                            saveStatus = "Gemini AI API key updated!"
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DevAccentPurple),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("save_gemini_key_button")
                    ) {
                        Text("Save Gemini Key", color = Color.White, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }

        // Deployment Documentation
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DevSurfaceCardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, DevBorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = DevAccentGreen, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Deployment Documentation", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
### DevFlow Deployment Pipeline Runbook

1. **Continuous Integration Verification**:
   - Every commit to `main` triggers automated compiler checks and unit suites.
   - DORA health status evaluates cycle time and rollback readiness.

2. **Artifact Packaging**:
   - Container image pushed to GitHub Container Registry (`ghcr.io/devflow/core`).
   - Android APK built and verified via Robolectric & Roborazzi regression tests.

3. **Production Rollout**:
   - Zero-downtime rolling update across Kubernetes worker nodes.
   - Ingress canary routing: 10% -> 50% -> 100% over 5-minute health observation window.
   - Emergency rollback available in 1 click from Deployments console.
                        """.trimIndent(),
                        fontFamily = DevCodeFont,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFCBD5E1)
                    )
                }
            }
        }
    }
}
