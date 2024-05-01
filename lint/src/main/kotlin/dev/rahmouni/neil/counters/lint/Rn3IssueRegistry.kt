package dev.rahmouni.neil.counters.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import dev.rahmouni.neil.counters.lint.designsystem.DesignSystemDetector

class Rn3IssueRegistry : IssueRegistry() {

    override val issues = listOf(
        DesignSystemDetector.ISSUE,
        TestMethodNameDetector.FORMAT,
        TestMethodNameDetector.PREFIX,
    )

    override val api: Int = CURRENT_API

    override val minApi: Int = 12

    override val vendor: Vendor = Vendor(
        vendorName = "Counters",
        feedbackUrl = "counters.rahmouni.dev/gitlab",
        contact = "counters@rahmouni.dev",
    ) //TODO neil: recheck ça
}
