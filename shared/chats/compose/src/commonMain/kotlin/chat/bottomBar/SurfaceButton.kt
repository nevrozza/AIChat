package chat.bottomBar

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SurfaceButton(
    modifier: Modifier,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        color = ChatBottomBarDefaults.color,
        shape = CircleShape,
        onClick = onClick
    ) {
        icon()
    }
}