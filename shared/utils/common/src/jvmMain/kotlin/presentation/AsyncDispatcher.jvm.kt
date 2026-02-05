package presentation

import kotlinx.coroutines.Dispatchers

actual val AsyncDispatcher: kotlinx.coroutines.CoroutineDispatcher
    get() = Dispatchers.IO