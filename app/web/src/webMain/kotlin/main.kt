import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import di.initKoin
import root.RealRootComponent

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    val lifecycle = LifecycleRegistry()
    val root = RealRootComponent(
        componentContext = DefaultComponentContext(
            lifecycle = lifecycle,
            backHandler = BackDispatcher()
        )
    )
    lifecycle.resume()

    ComposeViewport {
        App(root)
    }
}