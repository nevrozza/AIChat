package architecture

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import kotlin.reflect.KClass

interface DefaultStack<Config : Any, Child: Any> : BackHandlerOwner {
    val nav: StackNavigation<Config>

    val stack: Value<ChildStack<Config, Child>>

    fun onBackClicked() {
        popOnce(stack.active.instance::class)
    }

    fun popOnce(
        child: KClass<out Child>
    ) {
        if (child.isInstance(stack.active.instance)) {
            nav.pop()
        }
    }
}