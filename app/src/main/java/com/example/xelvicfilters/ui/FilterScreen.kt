package com.example.xelvicfilters.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.xelvicfilters.core.FilterError
import com.example.xelvicfilters.engine.FilterEngine
import kotlinx.coroutines.launch

@Composable
fun FilterPanel(engine: FilterEngine, modifier: Modifier = Modifier) {
    val filters by engine.filters.collectAsState()
    val selectedId by engine.selectedId.collectAsState()
    var lastError by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(engine) { engine.refresh() }
    LaunchedEffect(engine) { engine.errors.collect { lastError = it.toMessage() } }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Selected: ${selectedId ?: "none"}")
                OutlinedButton(onClick = { engine.clear() }) { Text("Clear") }
            }
            lastError?.let { Text("Error: $it", color = MaterialTheme.colorScheme.error) }

            LazyColumn(
                modifier = Modifier.heightIn(max = 200.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(filters, key = { it.id }) { f ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${f.name} (v${f.version})")
                        if (f.installed) {
                            Button(onClick = { engine.select(f.id) }) { Text("Select") }
                        } else {
                            Button(onClick = { scope.launch { engine.install(f.id) } }) { Text("Install") }
                        }
                    }
                }
            }
        }
    }
}

private fun FilterError.toMessage(): String = when (this) {
    is FilterError.InstallFailed -> "Install failed ($filterId): $reason"
    is FilterError.Unsupported -> "Unsupported ($filterId): $reason"
    is FilterError.RenderFailed -> "Render failed: $reason"
}