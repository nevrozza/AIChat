package chats

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import chat.ChatScreen
import chats.drawer.AdaptiveDrawerContainer
import chats.drawer.ChatListDrawer
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

    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    AdaptiveDrawerContainer(
        drawerState,
        drawerContent = {
            ChatListDrawer(component)
        },
        content = {
            ChatsStack(component)
        }
    )

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