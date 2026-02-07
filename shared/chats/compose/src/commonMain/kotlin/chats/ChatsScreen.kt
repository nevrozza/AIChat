package chats

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import chat.ChatScreen
import chats.drawer.AdaptiveDrawerContainer
import chats.drawer.ChatListDrawer
import chats.drawer.LocalChatDrawerState
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.launch
import pro.respawn.flowmvi.essenty.compose.subscribe

@Composable
fun ChatsScreen(
    component: ChatsComponent
) {
    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    val state by component.subscribe(component)

    LaunchedEffect(component) {
        component.uiEvents.collect { event ->
            when (event) {
                is ChatsComponent.UIEvent.SetDrawerOpened -> {
                    launch {
                        with(drawerState) {
                            if (event.isOpened) open()
                            else close()
                        }
                    }
                }
            }
        }
    }


    CompositionLocalProvider(LocalChatDrawerState provides drawerState) {
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