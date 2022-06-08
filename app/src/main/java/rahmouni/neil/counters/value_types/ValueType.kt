package rahmouni.neil.counters.value_types

import androidx.compose.animation.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import rahmouni.neil.counters.R
import rahmouni.neil.counters.utils.tiles.TileDialogRadioListEnum
import kotlin.math.absoluteValue

enum class ValueType(
    private val title: Int,
    val isValueValid: (String) -> Boolean,
    val picker: @Composable (String, (String) -> Unit) -> Unit,
    val formatAsString: (Int) -> String,
    val hasStats: Boolean,
    val hasHealthConnectIntegration: Boolean,
    val largeDisplay: @Composable (count: Int) -> Unit,
    val mediumDisplay: @Composable (count: Int) -> Unit,
    val smallDisplay: @Composable (count: Int) -> Unit,
) : TileDialogRadioListEnum {

    @OptIn(ExperimentalAnimationApi::class)
    NUMBER(
        R.string.text_number,
        { it.toIntOrNull() != null },
        { value, onChange ->
            NumberValuePicker(value = value) {
                onChange(it)
            }
        },
        { it.toString() },
        true,
        true,
        {
            AnimatedContent(
                targetState = it,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInVertically { height -> (height / 1.5f).toInt() } + fadeIn() with
                                slideOutVertically { height -> -(height / 1.5f).toInt() } + fadeOut()
                    } else {
                        slideInVertically { height -> -(height / 1.5f).toInt() } + fadeIn() with
                                slideOutVertically { height -> (height / 1.5f).toInt() } + fadeOut()
                    }.using(SizeTransform(clip = false))
                }
            )
            { targetValue ->
                androidx.compose.material3.Text(
                    text = targetValue.toString(),
                    style = MaterialTheme.typography.headlineLarge,
                )
            }
        },
        {
            AnimatedContent(
                targetState = it,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInVertically { height -> (height / 1.5f).toInt() } + fadeIn() with
                                slideOutVertically { height -> -(height / 1.5f).toInt() } + fadeOut()
                    } else {
                        slideInVertically { height -> -(height / 1.5f).toInt() } + fadeIn() with
                                slideOutVertically { height -> (height / 1.5f).toInt() } + fadeOut()
                    }.using(SizeTransform(clip = false))
                }
            )
            { targetValue ->
                androidx.compose.material3.Text(
                    targetValue.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(
                        horizontal = 18.dp,
                        vertical = 16.dp
                    )
                )
            }
        },
        {
            androidx.compose.material3.Text(
                it.toString(),
                Modifier.wrapContentHeight(),
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center
            )
        }
    ),

    @OptIn(ExperimentalAnimationApi::class)
    TASK(
        R.string.text_task,
        { it.toIntOrNull() != null && it.toInt().absoluteValue <= 1 },
        { value, onChange ->
            TaskValuePicker(value = value) {
                onChange(it)
            }
        },
        { if (it >= 1) "Done" else "Not done" }, //TODO
        false,
        false,
        {
            AnimatedContent(targetState = TASK.formatAsString(it))
            { targetValue ->
                androidx.compose.material3.Text(
                    text = targetValue,
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
        },
        {
            AnimatedContent(targetState = (if (it >= 1) androidx.compose.material.icons.Icons.Outlined.Check else androidx.compose.material.icons.Icons.Outlined.Close))
            { targetValue ->
                androidx.compose.material3.Icon(
                    targetValue,
                    contentDescription = TASK.formatAsString(it),
                    modifier = Modifier
                        .size(62.dp)
                        .padding(
                            horizontal = 18.dp,
                            vertical = 16.dp
                        )
                )
            }
        },
        {
            androidx.compose.material3.Icon(
                if (it >= 1) androidx.compose.material.icons.Icons.Outlined.Check else androidx.compose.material.icons.Icons.Outlined.Close,
                contentDescription = TASK.formatAsString(it),
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
}