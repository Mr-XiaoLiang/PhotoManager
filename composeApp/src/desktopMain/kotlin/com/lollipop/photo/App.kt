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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lollipop.photo.state.WindowStateController
import com.lollipop.photo.widget.ContentBox
import com.lollipop.photo.widget.ContentMenuIcon
import com.lollipop.photo.widget.WindowActionWidget
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import photomanager.composeapp.generated.resources.*

@Composable
@Preview
fun App(actionBarHeight: Dp) {
    Row(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier.fillMaxHeight().width(240.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {

        }
        ContentBox(
            modifier = Modifier.fillMaxSize(),
            menuBar = {
                ContentMenuIcon(
                    onClick = {},
                    painter = painterResource(Res.drawable.window_action_floating_24),
                    contentDescription = "Close",
                )
                ContentMenuIcon(
                    onClick = {},
                    painter = painterResource(Res.drawable.window_action_maximize_24),
                    contentDescription = "Close",
                )
                ContentMenuIcon(
                    onClick = {},
                    painter = painterResource(Res.drawable.window_action_minimize_24),
                    contentDescription = "Close",
                )
            },
            menuPanel = { callClose ->
                Box(modifier = Modifier.width(240.dp).height(240.dp)) {
                    ContentMenuIcon(
                        modifier = Modifier.align(Alignment.TopEnd),
                        onClick = {
                            callClose()
                        },
                        painter = painterResource(Res.drawable.window_action_close_24),
                        contentDescription = "Close",
                    )
                }
            }
        ) {
            var showContent by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier
                    .safeContentPadding()
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(onClick = {
                    WindowStateController.updateTitle("Hello World！！！Hello World！！！Hello World！！！Hello World！！！Hello World！！！Hello World！！！Hello World！！！")
                    showContent = !showContent
                }) {
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