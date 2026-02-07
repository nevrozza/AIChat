package chats

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
internal fun AdaptiveDrawerContainer(
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    BoxWithConstraints {
        val customDrawerWidth = (maxWidth * 0.5f).coerceAtLeast(150.dp)
        val drawerWidthPx = with(LocalDensity.current) { customDrawerWidth.toPx() }

        val isWide = maxWidth >= 800.dp

        if (isWide) {
            Row(Modifier.fillMaxSize()) {
                AnimatedVisibility(
                    visible = drawerState.isOpen,
                    enter = expandHorizontally(),
                    exit = shrinkHorizontally()
                ) {
                    Box(
                        Modifier
                            .width(300.dp)
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                    }
                }

                Box(Modifier.weight(1f)) {
                    content()
                }
            }
        } else {
            DismissibleNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    DismissibleDrawerSheet(
                        Modifier
                            .width(customDrawerWidth)
                    ) {
                        Box(
                            Modifier.fillMaxWidth().fillMaxHeight()
                                .background(MaterialTheme.colorScheme.primaryContainer)
                        )
                    }
                }
            ) {
                content()


                val progress =
                    if (drawerWidthPx > 0) ((drawerWidthPx + drawerState.currentOffset) / drawerWidthPx).coerceIn(
                        0f,
                        1f
                    ) else 0f

                if (progress > 0f) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .graphicsLayer { alpha = progress * 0.6f }
                            .background(Color.Black)
                            .pointerInput(Unit) {
                                detectTapGestures {
                                    scope.launch { drawerState.close() }
                                }
                            }
                    )
                }
            }
        }
    }
}