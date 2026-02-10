package chats.drawer

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
internal fun DrawerListButton(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    icon: Painter?,
    onClick: () -> Unit
) {
    val animatedSelectColor by animateColorAsState(
        if (isSelected) MaterialTheme.colorScheme.secondaryContainer
        else Color.Transparent
    )

    Row(
        modifier
            .clip(RoundedCornerShape(15.dp))
            .background(animatedSelectColor)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        icon?.let {
            Icon(icon, contentDescription = null, modifier = Modifier.scale(.8f))
            Spacer(Modifier.width(5.dp))
        }
        Text(text, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }

}