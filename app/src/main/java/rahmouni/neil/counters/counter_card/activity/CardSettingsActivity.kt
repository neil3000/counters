package rahmouni.neil.counters.counter_card.activity

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import rahmouni.neil.counters.CounterStyle
import rahmouni.neil.counters.CountersApplication
import rahmouni.neil.counters.R
import rahmouni.neil.counters.counter_card.CounterCard
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.CountersListViewModelFactory
import rahmouni.neil.counters.options.CounterStyleOption
import rahmouni.neil.counters.ui.theme.CountersTheme
import rahmouni.neil.counters.utils.SettingsDots
import rahmouni.neil.counters.utils.tiles.TileHeader

class CardSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val counterID: Int = intent.getIntExtra("counterID", 0)

        val countersListViewModel by viewModels<CountersListViewModel> {
            CountersListViewModelFactory((this.applicationContext as CountersApplication).countersListRepository)
        }

        setContent {
            CountersTheme {
                ProvideWindowInsets {
                    androidx.compose.material.Surface {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            tonalElevation = 1.dp,
                            color = MaterialTheme.colorScheme.surface
                        ) {

                            CardSettingsPage(
                                counterID, countersListViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun CardSettingsPage(counterID: Int, countersListViewModel: CountersListViewModel) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val activity = (LocalContext.current as Activity)
    val localHapticFeedback = LocalHapticFeedback.current

    val counter: CounterAugmented? by countersListViewModel.getCounter(counterID).observeAsState()

    var counterStyle by rememberSaveable { mutableStateOf(counter?.style ?: CounterStyle.DEFAULT) }

    fun finalCounter(): CounterAugmented? {
        return counter?.copy(
            style = counterStyle,
        )
    }

    LaunchedEffect(counter) {
        counterStyle = counter?.style ?: CounterStyle.DEFAULT
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.cardSettingsActivity_topbar_title)) },
                actions = {
                    SettingsDots(screenName = "CardSettingsActivity") {}
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                            activity.finish()
                        }
                    ) {
                        Icon(
                            Icons.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.cardSettingsActivity_topbar_icon_back_contentDescription)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 400.dp),
            Modifier.padding(innerPadding)
        ) {
            item {
                Column {
                    Surface(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = -LocalAbsoluteTonalElevation.current,
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(Modifier.padding(24.dp), Arrangement.spacedBy(24.dp)) {
                            Text(
                                stringResource(R.string.cardSettingsActivity_surface_cardPreview_text),
                                style = MaterialTheme.typography.headlineSmall,
                            )
                            if (counter != null) {
                                Row(
                                    Modifier
                                        .requiredWidth(250.dp)
                                        .align(Alignment.CenterHorizontally)
                                ) {
                                    CounterCard(
                                        data = finalCounter()!!,
                                        countersListViewModel = null,
                                        openNewIncrementSheet = null
                                    )
                                }
                            }
                        }
                    }
                    if (counter != null) {
                        CounterStyleOption(counterStyle) {
                            counterStyle = it
                            countersListViewModel.updateCounter(
                                counter!!.copy(style = counterStyle).toCounter()
                            )
                        }
                    }
                }
            }

            item {
                Column {
                    TileHeader(stringResource(R.string.cardSettingsActivity_tile_buttons_headerTitle))
                    counter?.valueType?.getButtons()?.forEach {
                        it.Tile(counter!!.toCounter(), countersListViewModel)
                    }
                }
            }
        }
    }
}