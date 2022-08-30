package rahmouni.neil.counters.utils.feedback

import rahmouni.neil.counters.R
import rahmouni.neil.counters.utils.tiles.TileDialogRadioListEnum

enum class FeedbackLocation(private val title: Int, private val formatted: Int) :
    TileDialogRadioListEnum {

    PREVIOUS_SCREEN(
        R.string.feedbackLocation_previousScreen_title,
        R.string.feedbackLocation_previousScreen_formatted
    ),
    OTHER_SCREEN(
        R.string.feedbackLocation_otherScreen_title,
        R.string.feedbackLocation_otherScreen_formatted
    ),
    WIDGET(R.string.feedbackLocation_widget_title, R.string.feedbackLocation_widget_formatted),
    NOTIFICATION(
        R.string.feedbackLocation_notification_title,
        R.string.feedbackLocation_notification_formatted
    );

    override fun title(): Int {
        return this.title
    }

    override fun formatted(): Int {
        return this.formatted
    }
}