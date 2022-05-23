package rahmouni.neil.counters.counter_card.activity

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.TouchApp
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
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import rahmouni.neil.counters.CounterStyle
import rahmouni.neil.counters.CountersApplication
import rahmouni.neil.counters.IncrementType
import rahmouni.neil.counters.R
import rahmouni.neil.counters.counter_card.CounterCard
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.CountersListViewModelFactory
import rahmouni.neil.counters.ui.theme.CountersTheme
import rahmouni.neil.counters.utils.SettingsDots
import rahmouni.neil.counters.utils.tiles.TileColorSelection
import rahmouni.neil.counters.utils.tiles.TileDialogRadioButtons
import rahmouni.neil.counters.utils.tiles.TileHeader
import rahmouni.neil.counters.utils.tiles.TileSwitch

class CardSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val counterID: Int = intent.getIntExtra("counterID", 0)

        val countersListViewModel by viewModels<CountersListViewModel> {
            CountersListViewModelFactory((this.applicationContext as CountersApplication).countersListRepository)
        }

        val remoteConfig = FirebaseRemoteConfig.getInstance()

        setContent {
            CountersTheme {
                ProvideWindowInsets {
                    androidx.compose.material.Surface {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            tonalElevation = 1.dp,
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            if (remoteConfig.getBoolean("issue120__card_preview_tablet")) {
                                CardSettingsPageNew(
                                    counterID, countersListViewModel
                                )
                            } else {
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
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun CardSettingsPage(counterID: Int, countersListViewModel: CountersListViewModel) {
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }
    val activity = (LocalContext.current as Activity)
    val localHapticFeedback = LocalHapticFeedback.current

    val counter: CounterAugmented? by countersListViewModel.getCounter(counterID).observeAsState()

    var edited by rememberSaveable { mutableStateOf(false) }
    var counterStyle by rememberSaveable { mutableStateOf(counter?.style ?: CounterStyle.DEFAULT) }
    var incrementType by rememberSaveable {
        mutableStateOf(
            counter?.incrementType ?: IncrementType.ASK_EVERY_TIME
        )
    }
    var minusEnabled by rememberSaveable { mutableStateOf(counter?.hasMinus ?: false) }

    fun checkEdited() {
        edited = (counterStyle != counter?.style)
                || (incrementType != counter?.incrementType)
                || (minusEnabled != counter?.hasMinus)
    }

    fun finalCounter(): CounterAugmented? {
        return counter?.copy(
            style = counterStyle,
            incrementType = incrementType,
            hasMinus = minusEnabled
        )
    }

    LaunchedEffect(counter) {
        counterStyle = counter?.style ?: CounterStyle.DEFAULT
        incrementType = counter?.incrementType ?: IncrementType.ASK_EVERY_TIME
        minusEnabled = counter?.hasMinus ?: false
        checkEdited()
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .statusBarsPadding(),
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.text_homeScreenSettings)) },
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
                            contentDescription = stringResource(R.string.action_back_short)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                edited,
                enter = slideInVertically { 300 },
                exit = slideOutVertically { 300 }
            ) {
                ExtendedFloatingActionButton(
                    icon = { Icon(Icons.Outlined.Check, null) },
                    text = { Text(stringResource(R.string.action_save_short)) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.navigationBarsPadding(),
                    onClick = {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        if (counter != null) {
                            edited = false
                            countersListViewModel.updateCounter(finalCounter()!!.toCounter())
                        }
                    },
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding, modifier = Modifier.fillMaxHeight()) {
            item {
                Surface(
                    Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = -LocalAbsoluteTonalElevation.current,
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(Modifier.padding(24.dp), Arrangement.spacedBy(16.dp)) {
                        Text(
                            stringResource(R.string.header_cardPreview),
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
            }

            item {
                LazyRow(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CounterStyle.values().forEach {
                        item {
                            TileColorSelection(
                                color = it.getBackGroundColor(),
                                selected = counterStyle == it
                            ) {
                                counterStyle = it
                                checkEdited()
                            }
                        }
                    }
                }
            }

            item {
                TileHeader(stringResource(R.string.text_buttons))
            }
            item {
                TileDialogRadioButtons(
                    title = stringResource(R.string.text_buttonBehavior),
                    icon = Icons.Outlined.TouchApp,
                    values = IncrementType.values().toList(),
                    selected = incrementType
                ) {
                    incrementType = it as IncrementType
                    if (incrementType != IncrementType.VALUE) minusEnabled = false
                    checkEdited()
                }
            }
            item {
                TileSwitch(
                    title = stringResource(R.string.action_showMinusButton),
                    icon = Icons.Outlined.Remove,
                    checked = minusEnabled,
                    enabled = incrementType == IncrementType.VALUE
                ) {
                    minusEnabled = it
                    checkEdited()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun CardSettingsPageNew(counterID: Int, countersListViewModel: CountersListViewModel) {
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }
    val activity = (LocalContext.current as Activity)
    val localHapticFeedback = LocalHapticFeedback.current

    val counter: CounterAugmented? by countersListViewModel.getCounter(counterID).observeAsState()

    var edited by rememberSaveable { mutableStateOf(false) }
    var counterStyle by rememberSaveable { mutableStateOf(counter?.style ?: CounterStyle.DEFAULT) }
    var incrementType by rememberSaveable {
        mutableStateOf(
            counter?.incrementType ?: IncrementType.ASK_EVERY_TIME
        )
    }
    var minusEnabled by rememberSaveable { mutableStateOf(counter?.hasMinus ?: false) }

    fun checkEdited() {
        edited = (counterStyle != counter?.style)
                || (incrementType != counter?.incrementType)
                || (minusEnabled != counter?.hasMinus)
    }

    fun finalCounter(): CounterAugmented? {
        return counter?.copy(
            style = counterStyle,
            incrementType = incrementType,
            hasMinus = minusEnabled
        )
    }

    LaunchedEffect(counter) {
        counterStyle = counter?.style ?: CounterStyle.DEFAULT
        incrementType = counter?.incrementType ?: IncrementType.ASK_EVERY_TIME
        minusEnabled = counter?.hasMinus ?: false
        checkEdited()
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .statusBarsPadding(),
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.text_homeScreenSettings)) },
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
                            contentDescription = stringResource(R.string.action_back_short)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                edited,
                enter = slideInVertically { 300 },
                exit = slideOutVertically { 300 }
            ) {
                ExtendedFloatingActionButton(
                    icon = { Icon(Icons.Outlined.Check, null) },
                    text = { Text(stringResource(R.string.action_save_short)) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.navigationBarsPadding(),
                    onClick = {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        if (counter != null) {
                            edited = false
                            countersListViewModel.updateCounter(finalCounter()!!.toCounter())
                        }
                    },
                )
            }
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
                                stringResource(R.string.header_cardPreview),
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
                    LazyRow(
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CounterStyle.values().forEach {
                            item {
                                TileColorSelection(
                                    color = it.getBackGroundColor(),
                                    selected = counterStyle == it
                                ) {
                                    counterStyle = it
                                    checkEdited()
                                }
                            }
                        }
                    }
                }
            }

            item {
                Column {
                    TileHeader(stringResource(R.string.text_buttons))
                    TileDialogRadioButtons(
                        title = stringResource(R.string.text_buttonBehavior),
                        icon = Icons.Outlined.TouchApp,
                        values = IncrementType.values().toList(),
                        selected = incrementType
                    ) {
                        incrementType = it as IncrementType
                        if (incrementType != IncrementType.VALUE) minusEnabled = false
                        checkEdited()
                    }
                    TileSwitch(
                        title = stringResource(R.string.action_showMinusButton),
                        icon = Icons.Outlined.Remove,
                        checked = minusEnabled,
                        enabled = incrementType == IncrementType.VALUE
                    ) {
                        minusEnabled = it
                        checkEdited()
                    }
                }
            }
        }
    }
}