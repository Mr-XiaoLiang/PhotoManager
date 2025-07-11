package com.lollipop.photo

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lollipop.photo.state.WindowConfig
import com.lollipop.photo.values.Strings
import com.lollipop.photo.values.StringsKey
import com.lollipop.photo.widget.ColumnIconMenuButton
import com.lollipop.photo.widget.ColumnMenuGroup

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppSettings() {

    Spacer(modifier = Modifier.height(12.dp))

    var useCustomWindow by remember { WindowConfig.isUseCustomWindow }

    if (useCustomWindow) {
        ColumnIconMenuButton(
            modifier = Modifier.fillMaxWidth(),
            label = StringsKey.UseDefaultWindow
        ) {
            useCustomWindow = false
        }
    } else {
        ColumnIconMenuButton(
            modifier = Modifier.fillMaxWidth(),
            label = StringsKey.UseCustomWindow
        ) {
            useCustomWindow = true
        }
    }

    ColumnMenuGroup(
        label = StringsKey.Language
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())
        ) {
            val currentLanguage by remember { Strings.currentLanguage }
            val selectedColor = MaterialTheme.colors.primary
            val unselectedColor = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
            for (language in Strings.languageArray) {
                Text(
                    text = language.name,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                        .onClick(
                            onClick = {
                                Strings.setLanguage(language)
                            }
                        ),
                    color = if (currentLanguage == language) {
                        selectedColor
                    } else {
                        unselectedColor
                    }
                )
            }

        }
    }

}