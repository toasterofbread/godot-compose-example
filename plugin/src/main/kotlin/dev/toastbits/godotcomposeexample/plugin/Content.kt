package dev.toastbits.godotcomposeexample.plugin

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import kotlinx.coroutines.CoroutineScope
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.ui.unit.sp

@Composable
fun AndroidViewContent(
    activity: Activity,
    data: String?,
    modifier: Modifier = Modifier
) {
    // val coroutine_scope: CoroutineScope = rememberCoroutineScope()

    Column(
        modifier.border(2.dp, Color.Red),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var count: Int by remember { mutableStateOf(0) }

        CompositionLocalProvider(LocalTextStyle provides LocalTextStyle.current.copy(fontSize = 25.sp)) {
            Text("Jetpack Compose UI (data=$data)")

            Button({ count++ }) {
                Text("Pressed $count times")
            }
        }
    }
}
