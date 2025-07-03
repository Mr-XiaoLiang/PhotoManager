package com.lollipop.photo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lollipop.photo.widget.WindowActionWidget
import com.lollipop.photo.widget.WindowFrameScope
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun WindowFrameScope.App() {
    MaterialTheme {

        Row(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
        ) {
            Column(
                modifier = Modifier.fillMaxHeight().width(actionBarWidth ?: 240.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {

            }
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                var showContent by remember { mutableStateOf(false) }
                Column(
                    modifier = Modifier
                        .safeContentPadding()
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Button(onClick = { showContent = !showContent }) {
                        Text("Click me!")
                    }
                    AnimatedVisibility(showContent) {
                        val greeting = remember { Greeting().greet() }
                        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Compose: $greeting， actionBarHeight = $actionBarHeight")
                            WindowActionWidget()
                        }
                    }
                }
            }
        }
    }
}