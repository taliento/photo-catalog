package com.taliento.catalog.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * Created by Davide Taliento on 16/11/24.
 */
@Composable
fun DialogBox(
    cornerRadius: Dp = 16.dp,
    paddingStart: Dp = 8.dp,
    paddingEnd: Dp = 8.dp,
    paddingTop: Dp = 8.dp,
    paddingBottom: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = {},
        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(cornerRadius)
        ) {
            Column(
                modifier = Modifier.padding(
                    start = paddingStart,
                    end = paddingEnd,
                    top = paddingTop,
                    bottom = paddingBottom
                )
            ) {
                content()
            }
        }
    }
}