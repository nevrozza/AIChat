package chat.content.showDialog

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp

val LocalFeedTopPadding = compositionLocalOf<Dp> {
    error("No FeedTopPadding provided")
}
val LocalFeedBottomPadding = compositionLocalOf<Dp> {
    error("No FeedBottomPadding provided")
}