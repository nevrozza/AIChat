import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import di.initKoin
import root.RealRootComponent

fun main() {
    initKoin()

    val root = runOnUiThread {
        val lifecycle = LifecycleRegistry()
        RealRootComponent(componentContext = DefaultComponentContext(lifecycle))
    }

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "AI Chat",
        ) {
            App(root)
        }
    }
}