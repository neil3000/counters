package rahmouni.neil.counters.utils.feedback

import rahmouni.neil.counters.R
import rahmouni.neil.counters.utils.tiles.TileDialogRadioListEnum

enum class FeedbackType(private val title: Int, val location: Int?, val subject: String, val describe: Int) : TileDialogRadioListEnum {
    BUG(R.string.text_bugReport, R.string.text_bugLocation, "Bug report", R.string.text_describeTheIssueAndStepsToReproduceIt),
    FEATURE(R.string.text_featureRequest, null, "Feature request", R.string.text_describeTheFeatureAndHowItWouldWork),
    TRANSLATION(R.string.text_translationError, R.string.text_errorLocation, "Translation error", R.string.describeWhereTranslationErrorIsAndHowShouldBeChanged);

    override fun title(): Int {
        return this.title
    }

    override fun formatted(): Int {
        return this.title
    }
}