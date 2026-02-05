import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import di.initKoin
import root.RealRootComponent

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()

    val root = RealRootComponent(
        componentContext = DefaultComponentContext(
            lifecycle = LifecycleRegistry(),
            backHandler = BackDispatcher()
        )
    )

    ComposeViewport {
        App(root)
    }
}