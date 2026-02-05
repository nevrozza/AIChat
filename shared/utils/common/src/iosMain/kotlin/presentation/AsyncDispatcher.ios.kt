package presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

actual val AsyncDispatcher: kotlinx.coroutines.CoroutineDispatcher
    get() = Dispatchers.IO