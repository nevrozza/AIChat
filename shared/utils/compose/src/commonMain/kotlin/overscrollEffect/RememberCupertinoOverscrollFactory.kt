package overscrollEffect


import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.OverscrollFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity

@Composable
fun rememberCupertinoOverscrollFactory(): OverscrollFactory {
    val density = LocalDensity.current
    val factory =
        remember {
            object : OverscrollFactory {
                override fun createOverscrollEffect(): OverscrollEffect =
                    CupertinoOverscrollEffect(density.density, false)

                override fun equals(other: Any?): Boolean = other === this

                override fun hashCode(): Int = -1
            }
        }

    return factory
}