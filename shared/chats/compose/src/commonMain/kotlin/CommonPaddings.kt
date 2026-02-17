import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.safeContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp

object CommonPaddings {
    val horizontalBottomBarPadding: Dp = 20.dp
    val horizontalTopBarPadding: Dp = horizontalBottomBarPadding
    val startDrawerPadding: Dp = 15.dp

    val messagesPadding: Dp = 5.dp

    @Composable
    fun calculateBottomPadding(): Dp =
        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 20.dp

    @Composable
    fun calculateTopPadding(): Dp {
        val topPadding = WindowInsets.safeContent.asPaddingValues().calculateTopPadding()
        return calculateBottomPadding().coerceAtLeast(topPadding)
    }

    @Composable
    fun calculateImeBottomPadding(): Dp =
        (WindowInsets.ime.asPaddingValues()
            .calculateBottomPadding() + 10.dp).coerceAtLeast(calculateBottomPadding())

    @Composable
    fun calculateKeyboardOffset(): Dp {
        val ime = WindowInsets.ime.asPaddingValues().calculateBottomPadding()
        val nav = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

        return (ime - nav).coerceAtLeast(0.dp)
    }
}