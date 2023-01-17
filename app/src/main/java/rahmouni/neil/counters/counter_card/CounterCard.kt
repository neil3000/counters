package rahmouni.neil.counters.counter_card

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import rahmouni.neil.counters.counterActivity.CounterActivity
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.health_connect.HealthConnectManager
import rahmouni.neil.counters.prefs

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CounterCard(
    data: CounterAugmented,
    countersListViewModel: CountersListViewModel?,
    healthConnectManager: HealthConnectManager,
    modifier: Modifier = Modifier,
    openNewIncrementSheet: ((countersListViewModel: CountersListViewModel) -> (Unit))?,
) {
    val rc = FirebaseRemoteConfig.getInstance()
    val context = LocalContext.current
    val localHapticFeedback = LocalHapticFeedback.current

    if (rc.getBoolean("issue196__patch")) {
        Card(
            colors = CardDefaults.cardColors(containerColor = data.style.getBackGroundColor()),
            modifier = modifier
        ) {
            Column(modifier = Modifier.combinedClickable(
                onClick = {
                    if (openNewIncrementSheet != null) {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        context.startActivity(
                            Intent(context, CounterActivity::class.java).putExtra(
                                "counterID",
                                data.uid
                            )
                        )
                    }
                },
                onLongClick = {
                    if (openNewIncrementSheet != null && countersListViewModel != null) {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        openNewIncrementSheet(
                            countersListViewModel
                        )

                        prefs.preferences.edit().putBoolean("LONG_PRESS_TIP_DISMISSED", true)
                            .apply()
                    }
                }
            )) {
                if (prefs.debugMode) Text("id:" + data.uid.toString())
                Text(
                    text = data.displayName,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(8.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val buttons =
                        data.valueType.getButtons().filter { it.isEnabled(data.toCounter()) }
                            .toMutableList()
                    val end = buttons.removeFirstOrNull()

                    buttons.forEach {
                        it.CardButton(data.toCounter(), countersListViewModel, healthConnectManager)
                    }
                    if (arrayOf(
                            "mon amour pour maï",
                            "my love for maï"
                        ).contains(data.displayName.lowercase().removeSurrounding(" "))
                    ) {
                        Text(
                            "∞",
                            Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.headlineLarge
                        )
                    } else {
                        data.valueType.largeDisplay(
                            this,
                            data.getCount(),
                            context,
                            true
                        )
                    }
                    end?.CardButton(data.toCounter(), countersListViewModel, healthConnectManager)
                }
            }
        }
    } else {
        Card(
            colors = CardDefaults.cardColors(containerColor = data.style.getBackGroundColor()),
            modifier = Modifier.combinedClickable(
                onClick = {
                    if (openNewIncrementSheet != null) {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        context.startActivity(
                            Intent(context, CounterActivity::class.java).putExtra(
                                "counterID",
                                data.uid
                            )
                        )
                    }
                },
                onLongClick = {
                    if (openNewIncrementSheet != null && countersListViewModel != null) {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        openNewIncrementSheet(
                            countersListViewModel
                        )

                        prefs.preferences.edit().putBoolean("LONG_PRESS_TIP_DISMISSED", true)
                            .apply()
                    }
                }
            )
        ) {
            Column {
                if (prefs.debugMode) Text("id:" + data.uid.toString())
                Text(
                    text = data.displayName,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(8.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val buttons =
                        data.valueType.getButtons().filter { it.isEnabled(data.toCounter()) }
                            .toMutableList()
                    val end = buttons.removeFirstOrNull()

                    buttons.forEach {
                        it.CardButton(data.toCounter(), countersListViewModel, healthConnectManager)
                    }
                    if (arrayOf(
                            "mon amour pour maï",
                            "my love for maï"
                        ).contains(data.displayName.lowercase().removeSurrounding(" "))
                    ) {
                        Text(
                            "∞",
                            Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.headlineLarge
                        )
                    } else {
                        data.valueType.largeDisplay(
                            this,
                            data.getCount(),
                            context,
                            true
                        )
                    }
                    end?.CardButton(data.toCounter(), countersListViewModel, healthConnectManager)
                }
            }
        }
    }
}