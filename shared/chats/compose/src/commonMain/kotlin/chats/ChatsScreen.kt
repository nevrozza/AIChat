package chats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import chat.ChatScreen
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import pro.respawn.flowmvi.compose.dsl.subscribe

@Composable
fun ChatsScreen(
    component: ChatsComponent
) {
    val state by component.subscribe()


    ChatsStack(component)

}

@Composable
private fun ChatsStack(component: ChatsComponent) {
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
            is ChatsComponent.Child.ChatChild -> ChatScreen(child.component)
        }
    }
}