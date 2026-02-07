import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.safeContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object CommonPaddings {
    val horizontalContentPadding: Dp = 20.dp

    @Composable
    fun calculateBottomPadding(): Dp =
       WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 20.dp

    @Composable
    fun calculateTopPadding(): Dp {
        val topPadding = WindowInsets.safeContent.asPaddingValues().calculateTopPadding()
        return calculateBottomPadding().coerceAtLeast(topPadding)
    }
}