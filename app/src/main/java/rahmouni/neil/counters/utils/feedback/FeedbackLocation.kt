package rahmouni.neil.counters.utils.feedback

import rahmouni.neil.counters.R
import rahmouni.neil.counters.utils.tiles.TileDialogRadioListEnum

enum class FeedbackLocation(private val title: Int, private val formatted: Int) :
    TileDialogRadioListEnum {

    PREVIOUS(R.string.text_previousScreen, R.string.text_onThePreviousScreen),
    OTHER_SCREEN(R.string.text_otherScreen, R.string.text_onAnotherScreen),
    WIDGET(R.string.text_widget, R.string.text_inAWidget),
    NOTIFICATION(R.string.text_notification, R.string.text_inANotification);

    override fun title(): Int {
        return this.title
    }

    override fun formatted(): Int {
        return this.formatted
    }
}