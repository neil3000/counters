package rahmouni.neil.counters.counterActivity.goal

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Pin
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import rahmouni.neil.counters.CountersApplication
import rahmouni.neil.counters.R
import rahmouni.neil.counters.ResetType
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.CountersListViewModelFactory
import rahmouni.neil.counters.options.ValueOption
import rahmouni.neil.counters.ui.theme.CountersTheme
import rahmouni.neil.counters.utils.SettingsDots
import rahmouni.neil.counters.utils.header.HeaderSwitch
import rahmouni.neil.counters.utils.tiles.TileDialogRadioButtons
import rahmouni.neil.counters.utils.tiles.TileDialogRadioListEnum
import rahmouni.neil.counters.utils.tiles.TileHeader
import rahmouni.neil.counters.value_types.ValueType

class CounterGoalSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        Firebase.dynamicLinks.getDynamicLink(intent)

        val counterID: Int = intent.getIntExtra("counterID", 0)

        val countersListViewModel by viewModels<CountersListViewModel> {
            CountersListViewModelFactory((this.applicationContext as CountersApplication).countersListRepository)
        }

        setContent {
            CountersTheme {
                androidx.compose.material.Surface {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        tonalElevation = 1.dp,
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        HealthConnectSettingsPage(
                            counterID,
                            countersListViewModel
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun HealthConnectSettingsPage(
    counterID: Int,
    countersListViewModel: CountersListViewModel,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val activity = (LocalContext.current as Activity)
    val localHapticFeedback = LocalHapticFeedback.current

    val counter: CounterAugmented? by countersListViewModel.getCounter(counterID).observeAsState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.counterGoalSettingsActivity_topbar_title)) },
                actions = {
                    SettingsDots(screenName = "CounterGoalSettingsActivity") {}
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                            activity.finish()
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.counterGoalSettingsActivity_topbar_icon_back_contentDescription)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding, modifier = Modifier.fillMaxHeight()) {
            item {
                Surface(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = -LocalAbsoluteTonalElevation.current,
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_goal_full),
                            null,
                            Modifier.fillMaxWidth(.7f),
                            Color.Unspecified
                        )
                    }
                }
            }
            item {
                HeaderSwitch(
                    title = stringResource(R.string.counterGoalSettingsActivity_header_title),
                    checked = counter?.isGoalSettingEnabled() ?: false
                ) {
                    countersListViewModel.updateCounter(
                        counter!!.copy(
                            goalEnabled = it
                        ).toCounter()
                    )
                }
            }

            item {
                TileHeader(stringResource(R.string.counterGoalSettingsActivity_tile_general_headerTitle))
            }

            //GoalValue
            item {
                ValueOption(
                    title = stringResource(R.string.counterGoalSettingsActivity_tile_goalValue_title),
                    dialogTitle = stringResource(R.string.counterGoalSettingsActivity_tile_goalValue_dialogTitle),
                    icon = Icons.Outlined.Pin,
                    valueType = counter?.valueType ?: ValueType.NUMBER,
                    value = counter?.goalValue ?: 0,
                ) {
                    if (counter != null) {
                        countersListViewModel.updateCounter(
                            counter!!.copy(
                                goalValue = it
                            ).toCounter()
                        )
                    }
                }
            }

            if (remoteConfig.getBoolean("i_253")) { // Per entry goal

                // GoalType
                item {
                    TileDialogRadioButtons(
                        title = stringResource(R.string.counterGoalSettingsActivity_tile_goalType_title),
                        icon = Icons.Outlined.EmojiEvents,
                        values = GoalType.values().toList(),
                        selected = counter?.goalType ?: GoalType.TIME_PERIOD
                    ) {
                        if (counter != null) {
                            countersListViewModel.updateCounter(
                                counter!!.copy(
                                    goalType = it as GoalType
                                ).toCounter()
                            )
                        }
                //GoalReset
                item {
                    TileDialogRadioButtons(
                        title = stringResource(R.string.counterGoalSettingsActivity_tile_goalReset_title),
                        icon = Icons.Outlined.Event,
                        values = listOf(GoalResetType.FollowCounter).plus(ResetType.values().toList()),
                        selected = counter?.goalReset ?: GoalResetType.FollowCounter
                    ) {
                        if (counter != null) {
                            countersListViewModel.updateCounter(
                                counter!!.copy(
                                    goalReset = if (it == GoalResetType.FollowCounter) null else (it as ResetType)
                                ).toCounter()
                            )
                    }
                }
            }
            item { androidx.compose.material.Divider() }
        }
    }
}

enum class GoalResetType : TileDialogRadioListEnum {
    FollowCounter;

    override fun title(): Int {
        return R.string.counterGoalSettingsActivity_followCounter_title
    }

    override fun formatted(): Int {
        return R.string.counterGoalSettingsActivity_followCounter_formatted
    }
}