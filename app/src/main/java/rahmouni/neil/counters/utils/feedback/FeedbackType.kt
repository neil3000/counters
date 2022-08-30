package rahmouni.neil.counters.utils.feedback

import rahmouni.neil.counters.R
import rahmouni.neil.counters.utils.tiles.TileDialogRadioListEnum

enum class FeedbackType(
    private val title: Int,
    val location: Int?,
    val subject: String,
    val describe: Int
) : TileDialogRadioListEnum {
    BUG(
        R.string.feedbackType_bug_title,
        R.string.feedbackType_bug_location,
        "Bug report",
        R.string.feedbackType_bug_describe
    ),
    FEATURE(
        R.string.feedbackType_feature_title,
        null,
        "Feature request",
        R.string.feedbackType_feature_describe
    ),
    TRANSLATION(
        R.string.feedbackType_translation_title,
        R.string.feedbackType_translation_location,
        "Translation error",
        R.string.feedbackType_translation_describe
    );

    override fun title(): Int {
        return this.title
    }

    override fun formatted(): Int {
        return this.title
    }
}