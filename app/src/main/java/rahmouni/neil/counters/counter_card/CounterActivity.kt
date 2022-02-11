package rahmouni.neil.counters.counter_card

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.gson.Gson
import rahmouni.neil.counters.Counter
import rahmouni.neil.counters.ui.theme.CountersTheme

class CounterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        setContent {
            CountersTheme {
                ProvideWindowInsets {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        CounterPage(
                            Gson().fromJson(intent.getStringExtra("counter"), Counter::class.java)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterPage(counter: Counter) {
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }

    val activity = (LocalContext.current as? Activity)
    val localHapticFeedback = LocalHapticFeedback.current

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .statusBarsPadding(),
        topBar = {
            SmallTopAppBar(
                title = { Text(counter.displayName) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            localHapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)

                            activity?.finish()
                        }
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back") //TODO i18n
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Inc") },
                onClick = { /* fab click handler */ },
            )
        },
        content = { innerPadding ->
            LazyColumn(contentPadding = innerPadding) {
                items(count = 100) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    )
                }
            }
        },
        bottomBar = {
            var selectedItem by remember { mutableStateOf(0) }
            val items = listOf("Songs", "Artists", "Playlists")

            NavigationBar(modifier = Modifier.navigationBarsPadding()) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CounterPagePreview() {
    CountersTheme {
        CounterPage(Counter(displayName = "Mollets"))
    }
}