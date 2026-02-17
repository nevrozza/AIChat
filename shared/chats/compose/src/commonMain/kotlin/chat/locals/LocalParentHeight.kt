package chat.locals

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp

val LocalParentHeight = compositionLocalOf<Dp> {
    error("No ParentHeight provided")
}