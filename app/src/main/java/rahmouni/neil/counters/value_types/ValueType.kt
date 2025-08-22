package rahmouni.neil.counters.value_types

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue
import rahmouni.neil.counters.R
import rahmouni.neil.counters.utils.tiles.TileDialogRadioListEnum

enum class ValueType(
    private val title: Int,
    val isValueValid: (String) -> Boolean,
    val picker: @Composable (String, (String) -> Unit) -> Unit,
    val formatAsString: (count: Int, context: Context) -> String,
    val largeDisplay: @Composable RowScope.(count: Int, context: Context, padding: Boolean) -> Unit,
    val mediumDisplay: @Composable (count: Int, context: Context) -> Unit,
    val smallDisplay: @Composable (count: Int, context: Context) -> Unit,
) : TileDialogRadioListEnum {

    NUMBER(
        R.string.valueType_number,
        { it.toIntOrNull() != null },
        { value, onChange ->
            NumberValuePicker(value = value) {
                onChange(it)
            }
        },
        { count, _ -> count.toString() },
        { count, _, padding ->
            AnimatedContent(
                targetState = count,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInVertically { height -> (height / 1.5f).toInt() } + fadeIn()).togetherWith(
                            slideOutVertically { height -> -(height / 1.5f).toInt() } + fadeOut())
                    } else {
                        (slideInVertically { height -> -(height / 1.5f).toInt() } + fadeIn()).togetherWith(
                            slideOutVertically { height -> (height / 1.5f).toInt() } + fadeOut())
                    }.using(SizeTransform(clip = false))
                }
            )
            { targetValue ->
                androidx.compose.material3.Text(
                    targetValue.toString(),
                    Modifier.padding(start = if (padding) 8.dp else 0.dp),
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center
                )
            }
        },
        { count, _ ->
            AnimatedContent(
                targetState = count,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInVertically { height -> (height / 1.5f).toInt() } + fadeIn()).togetherWith(
                            slideOutVertically { height -> -(height / 1.5f).toInt() } + fadeOut())
                    } else {
                        (slideInVertically { height -> -(height / 1.5f).toInt() } + fadeIn()).togetherWith(
                            slideOutVertically { height -> (height / 1.5f).toInt() } + fadeOut())
                    }.using(SizeTransform(clip = false))
                }
            )
            { targetValue ->
                androidx.compose.material3.Text(
                    targetValue.toString(),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        { count, _ ->
            androidx.compose.material3.Text(
                count.toString(),
                Modifier.wrapContentHeight(),
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center
            )
        }
    ),

    TASK(
        R.string.valueType_task,
        { it.toIntOrNull() != null && it.toInt().absoluteValue <= 1 },
        { value, onChange ->
            TaskValuePicker(value = value) {
                onChange(it)
            }
        },
        { count, context ->
            if (count >= 1) context.getString(R.string.valueType_task_done) else context.getString(
                R.string.valueType_task_notDone
            )
        },
        { count, context, padding ->
            AnimatedContent(targetState = TASK.formatAsString(count, context))
            { targetValue ->
                androidx.compose.material3.Text(
                    targetValue,
                    Modifier.padding(start = if (padding) 8.dp else 0.dp),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            }
        },
        { count, context ->
            AnimatedContent(targetState = (if (count >= 1) Icons.Outlined.Check else Icons.Outlined.Close))
            { targetValue ->
                androidx.compose.material3.Icon(
                    targetValue,
                    contentDescription = TASK.formatAsString(count, context),
                    modifier = Modifier
                        .size(62.dp)
                        .padding(
                            horizontal = 18.dp,
                            vertical = 16.dp
                        )
                )
            }
        },
        { count, context ->
            androidx.compose.material3.Icon(
                if (count >= 1) Icons.Outlined.Check else Icons.Outlined.Close,
                contentDescription = TASK.formatAsString(count, context),
                Modifier.requiredSize(24.dp)
            )
        }
    );

    override fun title(): Int {
        return this.title
    }

    override fun formatted(): Int {
        return this.title
    }

    fun getButtons(): List<ValueTypeButton> {
        return listOf(
            ValueTypeButton(
                "Plus", //TODO i18n
                Icons.Outlined.Add,
                NUMBER,
                { it.plusButtonEnabled },
                { it.plusButtonValue },
                { counter, enabled -> counter.copy(plusButtonEnabled = enabled) },
                { counter, value -> counter.copy(plusButtonValue = value) }
            ),
            ValueTypeButton(
                "Minus", //TODO i18n
                Icons.Outlined.Remove,
                NUMBER,
                { it.minusButtonEnabled },
                { it.minusButtonValue },
                { counter, enabled -> counter.copy(minusButtonEnabled = enabled) },
                { counter, value -> counter.copy(minusButtonValue = value) }

            ),
            ValueTypeButton(
                "Done", //TODO i18n
                Icons.Outlined.Check,
                TASK,
                { it.doneButtonEnabled },
                { 1 },
                { counter, enabled -> counter.copy(doneButtonEnabled = enabled) }
            ),
            ValueTypeButton(
                "Not done", //TODO i18n
                Icons.Outlined.Close,
                TASK,
                { it.notDoneButtonEnabled },
                { 0 },
                { counter, enabled -> counter.copy(notDoneButtonEnabled = enabled) }
            )
        ).filter { it.valueType == this }
    }
}