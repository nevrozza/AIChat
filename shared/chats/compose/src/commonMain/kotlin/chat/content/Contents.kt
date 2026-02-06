package chat.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign

@Composable
fun NewChatContent() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Привет!\nС чем я могу помочь?", textAlign = TextAlign.Center)
    }
}

@Composable
fun LoadingContent() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun LoadingErrorContent(
    error: Exception,
    onTryAgainClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // for centering error message
        TextButton(
            onClick = {},
            modifier = Modifier.alpha(0f),
            enabled = false
        ) { Text("Попробовать ещё раз") }

        Text(error.message ?: "Unknown error", textAlign = TextAlign.Center)
        TextButton(onClick = onTryAgainClick) {
            Text("Попробовать ещё раз")
        }
    }
}