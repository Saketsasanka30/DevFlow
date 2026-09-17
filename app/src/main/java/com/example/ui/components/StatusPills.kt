package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DevAccentGreen
import com.example.ui.theme.DevAccentPurple
import com.example.ui.theme.DevAccentRed
import com.example.ui.theme.DevAccentYellow
import com.example.ui.theme.DevPrimaryCyan

@Composable
fun StatusBadge(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    hasDot: Boolean = true,
    dotColor: Color = textColor
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor.copy(alpha = 0.16f))
            .border(1.dp, backgroundColor.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (hasDot) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(dotColor)
                )
                Spacer(modifier = Modifier.width(5.dp))
            }
            Text(
                text = text,
                color = textColor,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun CiStatusPill(status: String) {
    val (bg, text) = when (status.uppercase()) {
        "SUCCESS" -> Pair(DevAccentGreen, DevAccentGreen)
        "RUNNING" -> Pair(DevPrimaryCyan, DevPrimaryCyan)
        "FAILED" -> Pair(DevAccentRed, DevAccentRed)
        else -> Pair(DevAccentYellow, DevAccentYellow)
    }
    StatusBadge(text = status, backgroundColor = bg, textColor = text)
}

@Composable
fun PrStatusPill(status: String) {
    val (bg, text) = when (status.uppercase()) {
        "MERGED" -> Pair(DevAccentPurple, DevAccentPurple)
        "OPEN" -> Pair(DevAccentGreen, DevAccentGreen)
        else -> Pair(DevAccentRed, DevAccentRed)
    }
    StatusBadge(text = status, backgroundColor = bg, textColor = text)
}

@Composable
fun PriorityPill(priority: String) {
    val (bg, text) = when (priority.uppercase()) {
        "CRITICAL" -> Pair(DevAccentRed, DevAccentRed)
        "HIGH" -> Pair(DevAccentYellow, DevAccentYellow)
        "MEDIUM" -> Pair(DevPrimaryCyan, DevPrimaryCyan)
        else -> Pair(Color(0xFF94A3B8), Color(0xFF94A3B8))
    }
    StatusBadge(text = priority, backgroundColor = bg, textColor = text, hasDot = false)
}

@Composable
fun EnvironmentPill(environment: String) {
    val (bg, text) = when (environment.lowercase()) {
        "production" -> Pair(DevAccentGreen, DevAccentGreen)
        "staging" -> Pair(DevPrimaryCyan, DevPrimaryCyan)
        "development" -> Pair(DevAccentYellow, DevAccentYellow)
        else -> Pair(DevAccentPurple, DevAccentPurple)
    }
    StatusBadge(text = environment, backgroundColor = bg, textColor = text)
}
