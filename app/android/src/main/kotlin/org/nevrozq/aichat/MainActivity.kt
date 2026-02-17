package org.nevrozq.aichat

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.runtime.CompositionLocalProvider
import com.arkivanov.decompose.retainedComponent
import di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.stopKoin
import overscrollEffect.rememberCupertinoOverscrollFactory
import root.RealRootComponent
import root.RootComponent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        initKoin {
            androidContext(applicationContext)
        }

        val rootComponent: RootComponent = retainedComponent { componentContext ->
            RealRootComponent(componentContext)
        }
        screenSetup()
        setContent {
            CompositionLocalProvider(
                LocalOverscrollFactory provides rememberCupertinoOverscrollFactory()
            ) {
                App(rootComponent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }
}