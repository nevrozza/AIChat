package chat.bottomBar

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
internal fun ChatTextField(
    modifier: Modifier,
    text: String,
    onSendClick: () -> Unit,
    onTextChange: (String) -> Unit
) {

    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = text)) }
    var textFieldValue = textFieldValueState.copy(text = text)

    SideEffect {
        if (
            textFieldValue.selection != textFieldValueState.selection ||
            textFieldValue.composition != textFieldValueState.composition
        ) {
            textFieldValueState = textFieldValue
        }
    }

    var lastTextValue by remember(text) { mutableStateOf(text) }

    val updateText: (TextFieldValue) -> Unit = { newTextFieldValueState ->
        textFieldValueState = newTextFieldValueState

        val stringChangedSinceLastInvocation =
            lastTextValue != newTextFieldValueState.text
        lastTextValue = newTextFieldValueState.text

        if (stringChangedSinceLastInvocation) {
            onTextChange(newTextFieldValueState.text)
        }
    }

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
                value = textFieldValue,
                onValueChange = { newTextFieldValueState ->
                    updateText(newTextFieldValueState)
                },
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
                },
                keyboardActions = KeyboardActions(onSend = { onSendClick() }),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
                modifier = Modifier.onPreviewKeyEvent { event ->
                    if (event.key == Key.Enter && event.type == KeyEventType.KeyDown) {
                        if (event.isShiftPressed) {
                            val newText =
                                (textFieldValue.text.substring(0 until textFieldValue.selection.start)
                                        + "\n"
                                        + textFieldValue.text.substring(textFieldValue.selection.end until textFieldValue.text.length))
                            textFieldValue = TextFieldValue(
                                newText,
                                TextRange(textFieldValue.selection.end + 1)
                            )
                            updateText(textFieldValue)
                            false
                        } else {
                            onSendClick()
                            true
                        }
                    } else {
                        false
                    }
                }
            )
        }
    }
}