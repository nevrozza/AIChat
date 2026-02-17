import androidx.compose.ui.uikit.OnFocusBehavior
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.ApplicationLifecycle
import di.initKoin
import platform.UIKit.UIViewController
import root.RealRootComponent
import root.RootComponent

// export JAVA_HOME="/Users/nevrozq/Code/jbr 17 for jewel/jbrsdk_jcef-17.0.11-osx-aarch64-b1207.24/Contents/Home"
// export PATH="$JAVA_HOME/bin:$PATH"
// to run from XCode

@Suppress("unused", "FunctionName")
fun MainViewController(): UIViewController {
    initKoin()

    val rootComponent: RootComponent = RealRootComponent(
        DefaultComponentContext(
            ApplicationLifecycle(),
            backHandler = BackDispatcher()
        )
    )

    return ComposeUIViewController(
        configure = {
            onFocusBehavior = OnFocusBehavior.DoNothing
        }
    ) {
        App(rootComponent)
    }
}