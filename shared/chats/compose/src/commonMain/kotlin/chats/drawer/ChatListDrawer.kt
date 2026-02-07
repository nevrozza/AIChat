package chats.drawer

import CommonPaddings
import CommonPaddings.calculateBottomPadding
import CommonPaddings.calculateTopPadding
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.unit.dp
import chats.ChatsComponent

@Composable
internal fun ChatListDrawer(
    component: ChatsComponent
) {
    val shape = RoundedCornerShape(30.dp)
    val backgroundColor = MaterialTheme.colorScheme.surface
    Box(
        Modifier
            .fillMaxSize()
            .padding(
                start = CommonPaddings.horizontalContentPadding,
                bottom = calculateBottomPadding(),
                top = calculateTopPadding()
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = shape
            )

            .clip(shape)
            .drawBehind {
                drawRect(backgroundColor)
            }
    ) {

    }
}