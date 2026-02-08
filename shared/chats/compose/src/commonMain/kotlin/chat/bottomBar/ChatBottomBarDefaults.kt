package chat.bottomBar

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object ChatBottomBarDefaults {
    val color: Color
        @Composable get() = MaterialTheme.colorScheme.surfaceContainer

    val active: Color
        @Composable get() = MaterialTheme.colorScheme.primary

    val inactive: Color
        @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .5f)


    object TextFieldColors {
        val cursor: Color
            @Composable get() = active

        val placeholder: Color
            @Composable get() = inactive

    }

    object SendButtonColors {
        val disabled: Color
            @Composable get() = inactive
        val enabled_send: Color
            @Composable get() = active
        val enabled_block: Color
            @Composable get() = MaterialTheme.colorScheme.onSurface
    }
}