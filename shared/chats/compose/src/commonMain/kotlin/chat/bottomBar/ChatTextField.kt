package chat.bottomBar

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun ChatTextField(
    modifier: Modifier,
    text: String,
    onTextChange: (String) -> Unit
) {
    val textStyle = LocalTextStyle.current

    val colors = ChatBottomBarDefaults.TextFieldColors

    Surface(
        modifier = modifier,
        color = ChatBottomBarDefaults.color,
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            Modifier
                .padding(vertical = 10.dp, horizontal = 15.dp)
                .animateContentSize().width(IntrinsicSize.Max),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = text,
                onValueChange = onTextChange,
                textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onSurface),
                cursorBrush = SolidColor(colors.cursor),
                decorationBox = { innerTextField ->
                    Box(
                        Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {

                        Text(
                            text = "Введите запрос",
                            style = textStyle.copy(fontWeight = FontWeight.Medium),
                            color = colors.placeholder,
                            maxLines = 1,
                            modifier = Modifier.fillMaxWidth().alpha(if (text.isEmpty()) 1f else 0f)
                        )

                        innerTextField()
                    }
                }
            )
        }
    }
}