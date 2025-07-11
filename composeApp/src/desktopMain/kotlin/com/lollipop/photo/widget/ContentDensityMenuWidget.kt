package com.lollipop.photo.widget

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lollipop.photo.data.ContentDensityMode
import com.lollipop.photo.values.StringsKey
import org.jetbrains.compose.resources.painterResource
import photomanager.composeapp.generated.resources.*

@Composable
fun ContentDensityMenuWidget(
    currentMode: ContentDensityMode,
    modifier: Modifier = Modifier.fillMaxWidth().height(32.dp),
    onButtonClick: (ContentDensityMode) -> Unit = {},
) {
    ColumnMenuGroup(
        modifier = Modifier.fillMaxWidth(),
        label = StringsKey.Density,
    ) {
        Row(
            modifier = modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DensityModeButton(
                currentMode = currentMode,
                targetMode = ContentDensityMode.Less3,
                onButtonClick = onButtonClick,
            )
            DensityModeButton(
                currentMode = currentMode,
                targetMode = ContentDensityMode.Less2,
                onButtonClick = onButtonClick,
            )
            DensityModeButton(
                currentMode = currentMode,
                targetMode = ContentDensityMode.Less1,
                onButtonClick = onButtonClick,
            )
            DensityModeButton(
                currentMode = currentMode,
                targetMode = ContentDensityMode.Medium,
                onButtonClick = onButtonClick,
            )
            DensityModeButton(
                currentMode = currentMode,
                targetMode = ContentDensityMode.More1,
                onButtonClick = onButtonClick,
            )
            DensityModeButton(
                currentMode = currentMode,
                targetMode = ContentDensityMode.More2,
                onButtonClick = onButtonClick,
            )
            DensityModeButton(
                currentMode = currentMode,
                targetMode = ContentDensityMode.More3,
                onButtonClick = onButtonClick,
            )
        }
    }
}

@Composable
private fun DensityModeButton(
    currentMode: ContentDensityMode,
    targetMode: ContentDensityMode,
    onButtonClick: (ContentDensityMode) -> Unit,
) {
    val painter = when (targetMode) {
        ContentDensityMode.Less3 -> painterResource(Res.drawable.icon_density_down_3_24)
        ContentDensityMode.Less2 -> painterResource(Res.drawable.icon_density_down_2_24)
        ContentDensityMode.Less1 -> painterResource(Res.drawable.icon_density_down_1_24)
        ContentDensityMode.Medium -> painterResource(Res.drawable.icon_density_medium_24)
        ContentDensityMode.More1 -> painterResource(Res.drawable.icon_density_up_1_24)
        ContentDensityMode.More2 -> painterResource(Res.drawable.icon_density_up_2_24)
        ContentDensityMode.More3 -> painterResource(Res.drawable.icon_density_up_3_24)
    }
    ContentMenuIcon(
        modifier = Modifier.width(24.dp).height(24.dp),
        onClick = { onButtonClick(targetMode) },
        painter = painter,
        contentDescription = targetMode.label,
        light = currentMode == targetMode
    )
}
