import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import di.initKoin
import overscrollEffect.rememberCupertinoOverscrollFactory
import root.RealRootComponent

fun main() {

    initKoin()

    val lifecycle = LifecycleRegistry()

    val root = runOnUiThread {
        RealRootComponent(componentContext = DefaultComponentContext(lifecycle))
    }

    application {
        val windowState = rememberWindowState()

        Window(
            onCloseRequest = ::exitApplication,
            title = "AI Chat",
            state = windowState
        ) {
            LifecycleController(
                lifecycleRegistry = lifecycle,
                windowState = windowState,
                windowInfo = LocalWindowInfo.current,
            )
            CompositionLocalProvider(
                LocalOverscrollFactory provides rememberCupertinoOverscrollFactory()
            ) {
                App(root)
            }
        }
    }
}