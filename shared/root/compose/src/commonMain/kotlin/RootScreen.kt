import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import chats.ChatsScreen
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import root.RootComponent
import root.RootComponent.Child

@Composable
internal fun RootScreen(component: RootComponent) {
    val stack by component.stack.subscribeAsState()

    Children(
        stack,
        animation = predictiveBackAnimation(
            backHandler = component.backHandler,
            fallbackAnimation = stackAnimation(fade()),
            onBack = component::onBackClicked
        )
    ) {
        when (val child = it.instance) {
            is Child.ChatsChild -> ChatsScreen(child.component)
        }
    }

}