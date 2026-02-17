package chats.drawer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import chat.locals.LocalParentHeight
import kotlinx.coroutines.launch
import kotlin.math.abs

val LocalChatDrawerState = staticCompositionLocalOf<DrawerState> {
    error("No DrawerState provided")
}

@Composable
internal fun AdaptiveDrawerContainer(
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    isMobileMode: (Boolean) -> Unit,
    drawerContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current

    BoxWithConstraints {
        val isWide = maxWidth >= 600.dp
        isMobileMode(!isWide)
        val mobileDrawerWidth = (maxWidth * 0.9f).coerceAtLeast(150.dp)
        val mobileDrawerWidthPx = with(LocalDensity.current) { mobileDrawerWidth.toPx() }

        val mobileDrawerProgress =
            if (mobileDrawerWidthPx > 0) ((mobileDrawerWidthPx + drawerState.currentOffset) / mobileDrawerWidthPx)
                .coerceIn(0f, 1f) else 0f

        val darkenProgressEpsilon = 0.01f

        val mobileDarkenBackground: Modifier.() -> Modifier = {
            this.drawBehind {
                drawRect(Color.Black.copy(alpha = abs(((mobileDrawerProgress - darkenProgressEpsilon) * .4f))))
            }
        }

        CompositionLocalProvider(
            LocalParentHeight provides this@BoxWithConstraints.maxHeight
        ) {
            if (isWide) {
                Row(Modifier.fillMaxSize()) {
                    AnimatedVisibility(
                        visible = drawerState.targetValue == DrawerValue.Open,
                        enter = expandHorizontally(),
                        exit = shrinkHorizontally()
                    ) {
                        Box(
                            Modifier
                                .width(300.dp)
                                .fillMaxHeight()
                        ) {
                            drawerContent()
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
                                .width(mobileDrawerWidth),
                            windowInsets = WindowInsets()

                        ) {
                            Box(
                                Modifier.fillMaxSize().mobileDarkenBackground()
                            ) {
                                drawerContent()
                            }
                        }
                    }
                ) {
                    content()
                    if (mobileDrawerProgress > darkenProgressEpsilon) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .mobileDarkenBackground()
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

    LaunchedEffect(drawerState.isOpen) {
        focusManager.clearFocus()
    }
}