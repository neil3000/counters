package rahmouni.neil.counters

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.room.Room
import com.google.accompanist.insets.*
import kotlinx.coroutines.launch
import rahmouni.neil.counters.counter_card.CounterCard
import rahmouni.neil.counters.new_counter.NewCounter
import rahmouni.neil.counters.ui.theme.CountersTheme

var db: CountersDatabase? = null
var counterDao: CounterDao? = null

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        db = Room.databaseBuilder(
            applicationContext,
            CountersDatabase::class.java, "countersDatabase"
        ).build()
        counterDao = db!!.counterDao()

        setContent {
            CountersTheme {
                ProvideWindowInsets {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Home()
                    }
                }
            }
        }
    }
}

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    androidx.compose.material.ExperimentalMaterialApi::class
)
@Composable
fun Home() {
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }

    val scope = rememberCoroutineScope()
    val localHapticFeedback = LocalHapticFeedback.current

    var countersList by remember { mutableStateOf(emptyList<Counter>()) }
    LaunchedEffect(Unit) {
        countersList = counterDao?.getAll() ?: emptyList()
    }
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )

    RoundedBottomSheet(bottomSheetState, false, { NewCounter {
        scope.launch {
            bottomSheetState.hide()
        }
        countersList = countersList+it
    } }) {
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .statusBarsPadding(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Counters"
                        )
                    },
                    actions = {
                        IconButton(onClick = { /* doSomething() */ }) { //TODO add settings
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "Settings" //TODO i18n
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text(text = "New counter") }, //TODO i18n
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "New counter"
                        )
                    }, //TODO i18n
                    onClick = {
                        scope.launch {
                            localHapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)

                            bottomSheetState.show()
                        }
                    },
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        ) { _ ->

            LazyVerticalGrid(
                cells = GridCells.Adaptive(minSize = 150.dp),
                contentPadding = rememberInsetsPaddingValues(
                    insets = LocalWindowInsets.current.navigationBars,
                    applyBottom = true,
                    additionalStart = 8.dp,
                    additionalEnd = 8.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(countersList.size) {
                    CounterCard(countersList[it])
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    CountersTheme {
        Home()
    }
}