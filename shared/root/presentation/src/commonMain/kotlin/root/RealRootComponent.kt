package root

import chats.ChatsComponent
import chats.RealChatsComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import root.RootComponent.Child
import root.RootComponent.Child.ChatsChild
import root.RootComponent.Config

class RealRootComponent(
    componentContext: ComponentContext
) : RootComponent, KoinComponent, ComponentContext by componentContext {
    override val nav = StackNavigation<Config>()
    private val _stack = childStack(
        source = nav,
        serializer = Config.serializer(),
        initialConfiguration = getInitialConfig(),
        childFactory = ::child,
        handleBackButton = true
    )

    override val stack: Value<ChildStack<Config, Child>>
        get() = _stack

    private fun child(config: Config, childCtx: ComponentContext): Child {
        return when(config) {
            Config.Chats -> ChatsChild(
                RealChatsComponent(componentCtx = childCtx, container = get())
            )
        }
    }

    private fun getInitialConfig(): Config {
        return Config.Chats
    }
}