package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ai.CodeAnalysisMode
import com.example.data.ai.CodeAnalysisResult
import com.example.data.ai.CodeAnalysisService
import com.example.data.ai.CodeFinding
import com.example.ui.theme.DevAccentGreen
import com.example.ui.theme.DevAccentPurple
import com.example.ui.theme.DevAccentYellow
import com.example.ui.theme.DevCodeFont
import com.example.ui.theme.DevPrimaryCyan
import com.example.ui.theme.DevTerminalBg
import com.example.ui.theme.DevTerminalText
import kotlinx.coroutines.launch

@Composable
fun CodeAnalysisPanel(
    service: CodeAnalysisService = remember { CodeAnalysisService() },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val coroutineScope = rememberCoroutineScope()

    var selectedPresetId by remember { mutableStateOf(service.sampleSnippets.first().id) }
    var codeSnippet by remember { mutableStateOf(service.sampleSnippets.first().code) }
    var selectedMode by remember { mutableStateOf(CodeAnalysisMode.ALL_CHECKS) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var analysisResult by remember { mutableStateOf<CodeAnalysisResult?>(null) }
    var copiedToClipboard by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("ai_code_analysis_panel"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(DevAccentPurple.copy(alpha = 0.15f))
                                .border(1.dp, DevAccentPurple.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.AutoAwesome,
                                contentDescription = "AI Analysis",
                                tint = DevAccentPurple,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "AI Code Analysis",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Firebase Genkit (Gemini 2.5 Flash) Engine",
                                fontFamily = DevCodeFont,
                                fontSize = 11.sp,
                                color = DevPrimaryCyan
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(DevAccentPurple.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "GENKIT / GEMINI",
                            fontFamily = DevCodeFont,
                            fontSize = 10.sp,
                            color = DevAccentPurple
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Select a code snippet or paste your own code to receive AI recommendations on concurrency safety, performance bottlenecks, and security audits.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Snippet Presets Selector
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Preset Code Snippets",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                service.sampleSnippets.forEach { preset ->
                    val isSelected = preset.id == selectedPresetId
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedPresetId = preset.id
                            codeSnippet = preset.code
                            analysisResult = null
                        },
                        label = {
                            Text(
                                preset.title,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        leadingIcon = {
                            if (isSelected) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DevPrimaryCyan.copy(alpha = 0.2f),
                            selectedLabelColor = DevPrimaryCyan,
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.testTag("snippet_preset_${preset.id}")
                    )
                }
            }
        }

        // Code Editor Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DevTerminalBg),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Code, contentDescription = null, tint = DevPrimaryCyan, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Source Snippet (Kotlin / Compose)",
                            fontFamily = DevCodeFont,
                            fontSize = 11.sp,
                            color = DevPrimaryCyan
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                codeSnippet = ""
                                selectedPresetId = "custom"
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = codeSnippet,
                    onValueChange = {
                        codeSnippet = it
                        selectedPresetId = "custom"
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .testTag("code_snippet_input"),
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = DevCodeFont,
                        color = DevTerminalText,
                        lineHeight = 18.sp
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DevPrimaryCyan,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        cursorColor = DevPrimaryCyan
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }

        // Analysis Mode Chips
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Analysis Objective",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CodeAnalysisMode.values().forEach { mode ->
                    val isSelected = mode == selectedMode
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedMode = mode },
                        label = { Text(mode.chipLabel, style = MaterialTheme.typography.labelSmall) },
                        leadingIcon = {
                            val icon = when (mode) {
                                CodeAnalysisMode.ALL_CHECKS -> Icons.Default.AutoAwesome
                                CodeAnalysisMode.BUGS_AND_CRASHES -> Icons.Default.BugReport
                                CodeAnalysisMode.PERFORMANCE -> Icons.Default.Speed
                                CodeAnalysisMode.SECURITY -> Icons.Default.Security
                                CodeAnalysisMode.MODERN_KOTLIN -> Icons.Default.Code
                                CodeAnalysisMode.UNIT_TESTS -> Icons.Default.CheckCircle
                            }
                            Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp))
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DevAccentPurple.copy(alpha = 0.2f),
                            selectedLabelColor = DevAccentPurple,
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.testTag("analysis_mode_${mode.name.lowercase()}")
                    )
                }
            }
        }

        // Execute Button
        Button(
            onClick = {
                if (codeSnippet.isNotBlank()) {
                    coroutineScope.launch {
                        isAnalyzing = true
                        try {
                            analysisResult = service.analyzeCodeSnippet(
                                snippet = codeSnippet,
                                mode = selectedMode
                            )
                        } finally {
                            isAnalyzing = false
                        }
                    }
                }
            },
            enabled = !isAnalyzing && codeSnippet.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("run_code_analysis_button"),
            colors = ButtonDefaults.buttonColors(containerColor = DevPrimaryCyan),
            shape = RoundedCornerShape(10.dp)
        ) {
            if (isAnalyzing) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color(0xFF090D16), strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Analyzing via Firebase Genkit...", color = Color(0xFF090D16), style = MaterialTheme.typography.labelLarge)
            } else {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFF090D16), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Analyze Code with Firebase Genkit", color = Color(0xFF090D16), style = MaterialTheme.typography.labelLarge)
            }
        }

        // Results Section
        AnimatedVisibility(
            visible = analysisResult != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            analysisResult?.let { result ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Summary & Score Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Code Quality Score",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(MaterialTheme.colorScheme.surface)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            result.providerUsed,
                                            fontFamily = DevCodeFont,
                                            fontSize = 9.sp,
                                            color = DevPrimaryCyan
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = result.summary,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Score Badge
                            val scoreColor = when {
                                result.qualityScore >= 80 -> DevAccentGreen
                                result.qualityScore >= 50 -> DevAccentYellow
                                else -> Color(0xFFF87171)
                            }

                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(scoreColor.copy(alpha = 0.15f))
                                    .border(2.dp, scoreColor, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "${result.qualityScore}",
                                        fontFamily = DevCodeFont,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = scoreColor
                                    )
                                    Text(
                                        text = "/100",
                                        fontFamily = DevCodeFont,
                                        fontSize = 9.sp,
                                        color = scoreColor.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }
                    }

                    // Key Findings List
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Detected Issues & Recommendations (${result.findings.size})",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        result.findings.forEach { finding ->
                            FindingItemRow(finding = finding)
                        }
                    }

                    // Suggested Replacement Code
                    if (result.suggestedCode.isNotBlank()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = DevTerminalBg),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DevAccentGreen.copy(alpha = 0.3f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DevAccentGreen, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Suggested Replacement Code",
                                            fontFamily = DevCodeFont,
                                            fontSize = 11.sp,
                                            color = DevAccentGreen
                                        )
                                    }

                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        OutlinedButton(
                                            onClick = {
                                                clipboardManager.setText(AnnotatedString(result.suggestedCode))
                                                Toast.makeText(context, "Code copied to clipboard!", Toast.LENGTH_SHORT).show()
                                            },
                                            shape = RoundedCornerShape(6.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = DevPrimaryCyan)
                                        ) {
                                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Copy", fontSize = 11.sp)
                                        }

                                        Button(
                                            onClick = {
                                                codeSnippet = result.suggestedCode
                                                Toast.makeText(context, "Applied suggested code to editor!", Toast.LENGTH_SHORT).show()
                                            },
                                            shape = RoundedCornerShape(6.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = DevAccentGreen)
                                        ) {
                                            Text("Apply Fix", color = Color(0xFF090D16), fontSize = 11.sp)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF090D16))
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = result.suggestedCode,
                                        fontFamily = DevCodeFont,
                                        fontSize = 12.sp,
                                        color = DevTerminalText,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }

                    // Detailed Senior Architecture Explanation
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Architectural Breakdown & Best Practices",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = result.fullExplanation,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FindingItemRow(finding: CodeFinding) {
    val (badgeBg, badgeText, badgeIcon) = when (finding.severity.uppercase()) {
        "CRITICAL" -> Triple(Color(0xFFF87171).copy(alpha = 0.15f), Color(0xFFF87171), Icons.Default.Warning)
        "WARNING" -> Triple(DevAccentYellow.copy(alpha = 0.15f), DevAccentYellow, Icons.Default.Warning)
        else -> Triple(DevAccentPurple.copy(alpha = 0.15f), DevAccentPurple, Icons.Default.AutoAwesome)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(badgeBg)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(badgeIcon, contentDescription = null, tint = badgeText, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = finding.severity,
                                fontFamily = DevCodeFont,
                                fontSize = 9.sp,
                                color = badgeText
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = finding.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                if (finding.lineHint.isNotBlank()) {
                    Text(
                        text = finding.lineHint,
                        fontFamily = DevCodeFont,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = finding.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
