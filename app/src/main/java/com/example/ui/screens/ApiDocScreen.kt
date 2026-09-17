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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.data.local.ApiDocEntity
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
fun ApiDocScreen(
    viewModel: DevFlowViewModel,
    modifier: Modifier = Modifier
) {
    val apiDocs by viewModel.apiDocs.collectAsState()
    var selectedApi by remember { mutableStateOf<ApiDocEntity?>(null) }
    val active = selectedApi ?: apiDocs.firstOrNull()

    val testResponse by viewModel.apiTestResponse.collectAsState()
    val isTesting by viewModel.isApiTestLoading.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Interactive API Documentation", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
        Text("OpenAPI 3.1 & REST endpoint explorer with interactive request execution", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(14.dp))

        // Selected Endpoint Runner
        active?.let { api ->
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
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        when (api.method) {
                                            "POST" -> DevAccentGreen.copy(alpha = 0.2f)
                                            "DELETE" -> DevAccentRed.copy(alpha = 0.2f)
                                            else -> DevPrimaryCyan.copy(alpha = 0.2f)
                                        }
                                    )
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = api.method,
                                    fontFamily = DevCodeFont,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = when (api.method) {
                                        "POST" -> DevAccentGreen
                                        "DELETE" -> DevAccentRed
                                        else -> DevPrimaryCyan
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = api.path,
                                fontFamily = DevCodeFont,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Button(
                            onClick = { viewModel.testApiEndpoint(api) },
                            colors = ButtonDefaults.buttonColors(containerColor = DevPrimaryCyan),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.testTag("execute_api_request_button")
                        ) {
                            if (isTesting) {
                                CircularProgressIndicator(modifier = Modifier.size(14.dp), color = Color(0xFF090D16), strokeWidth = 2.dp)
                            } else {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color(0xFF090D16), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Send", color = Color(0xFF090D16), style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(api.summary, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Headers: ${api.headersJson} • Status: ${api.statusCodes}", fontFamily = DevCodeFont, style = MaterialTheme.typography.labelSmall, color = DevAccentPurple)

                    Spacer(modifier = Modifier.height(10.dp))

                    // Live Response Box
                    Text("Response Payload (JSON):", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(DevTerminalBg)
                            .border(1.dp, DevBorderDark, RoundedCornerShape(6.dp))
                            .padding(8.dp)
                    ) {
                        LazyColumn {
                            item {
                                Text(
                                    text = testResponse ?: api.responseBodySample,
                                    fontFamily = DevCodeFont,
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp,
                                    color = if (testResponse != null) DevAccentGreen else Color(0xFFCBD5E1)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("All Endpoints", fontFamily = DevCodeFont, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(6.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(apiDocs) { api ->
                val isSelected = api.id == active?.id
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) DevPrimaryCyan.copy(alpha = 0.1f) else DevSurfaceCardDark)
                        .border(1.dp, if (isSelected) DevPrimaryCyan else DevBorderDark, RoundedCornerShape(8.dp))
                        .clickable { selectedApi = api }
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    when (api.method) {
                                        "POST" -> DevAccentGreen.copy(alpha = 0.15f)
                                        else -> DevPrimaryCyan.copy(alpha = 0.15f)
                                    }
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(api.method, fontFamily = DevCodeFont, style = MaterialTheme.typography.labelSmall, color = if (api.method == "POST") DevAccentGreen else DevPrimaryCyan)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(api.path, fontFamily = DevCodeFont, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                            Text(api.summary, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}
