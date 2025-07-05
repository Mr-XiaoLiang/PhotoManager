package com.lollipop.photo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lollipop.photo.data.PhotoManager
import com.lollipop.photo.widget.ContentBox
import com.lollipop.photo.widget.ContentMenuIcon
import org.jetbrains.compose.resources.painterResource
import photomanager.composeapp.generated.resources.Res
import photomanager.composeapp.generated.resources.icon_add_24

@Composable
fun DrawerPanel(
    modifier: Modifier,
    topInsets: Dp
) {

    ContentBox(
        modifier = modifier,
        menuBar = {
            ContentMenuIcon(
                painter = painterResource(Res.drawable.icon_add_24),
                contentDescription = "添加",
                onClick = {
                    PhotoManager.openFileChooser()
                }
            )
        },
        menuPanel = null
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(color = Color.Green),
        ) {
            Spacer(modifier = Modifier.height(topInsets))

            Box(modifier = Modifier.fillMaxWidth().height(500.dp).background(color = Color.Red))

        }


    }

}