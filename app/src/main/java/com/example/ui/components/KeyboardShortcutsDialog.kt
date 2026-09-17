package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.DevBorderDark
import com.example.ui.theme.DevCodeFont
import com.example.ui.theme.DevPrimaryCyan
import com.example.ui.theme.DevSurfaceCardDark
import com.example.ui.theme.DevSurfaceDark

data class ShortcutItem(
    val keyCombo: String,
    val description: String,
    val category: String
)

@Composable
fun KeyboardShortcutsDialog(onDismiss: () -> Unit) {
    val shortcuts = listOf(
        ShortcutItem("⌘ K / Ctrl+K", "Open Command Palette", "Navigation"),
        ShortcutItem("Ctrl + `", "Toggle Dev Terminal Console", "Tools"),
        ShortcutItem("⌘ I / Ctrl+I", "Open AI Developer Assistant", "AI"),
        ShortcutItem("⌘ D", "Trigger Production Deployment", "Actions"),
        ShortcutItem("⌘ B", "Toggle Side Navigation Rail", "View"),
        ShortcutItem("⌘ 1", "Jump to Developer Dashboard", "Quick Jump"),
        ShortcutItem("⌘ 2", "Jump to Project Workspace", "Quick Jump"),
        ShortcutItem("⌘ 3", "Jump to Git Repositories", "Quick Jump"),
        ShortcutItem("⌘ 4", "Jump to Issues Board", "Quick Jump"),
        ShortcutItem("⌘ 5", "Jump to Pull Requests", "Quick Jump"),
        ShortcutItem("⌘ 6", "Jump to Deployments & Logs", "Quick Jump"),
        ShortcutItem("⌘ 7", "Jump to Code Health Analytics", "Quick Jump"),
        ShortcutItem("⌘ ,", "Open Workspace Settings", "System"),
        ShortcutItem("Esc", "Close overlay / dialog", "General")
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, DevBorderDark, RoundedCornerShape(16.dp)),
            color = DevSurfaceDark
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Keyboard, contentDescription = null, tint = DevPrimaryCyan)
                        Spacer(modifier = Modifier.padding(start = 8.dp))
                        Text(
                            text = "Keyboard Shortcuts",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(shortcuts) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(DevSurfaceCardDark)
                                    .border(1.dp, DevBorderDark, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = item.keyCombo,
                                    fontFamily = DevCodeFont,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = DevPrimaryCyan
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
