/*
 * Counters
 * Copyright (C) 2024 Rahmouni Neïl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.rahmouni.neil.counters.lint

import com.android.tools.lint.checks.infrastructure.TestFile
import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import dev.rahmouni.neil.counters.lint.TestMethodNameDetector.Companion.FORMAT
import dev.rahmouni.neil.counters.lint.TestMethodNameDetector.Companion.PREFIX
import org.junit.Test

class TestMethodNameDetectorTest {

    @Test
    fun `detect prefix`() {
        lint().issues(PREFIX)
            .files(
                JUNIT_TEST_STUB,
                kotlin(
                    """
                    import org.junit.Test
                    class Test {
                        @Test
                        fun foo() = Unit
                        @Test
                        fun test_foo() = Unit
                        @Test
                        fun `test foo`() = Unit
                    }
                """,
                ).indented(),
            )
            .run()
            .expect(
                """
                src/Test.kt:6: Warning: Test method starts with test [TestMethodPrefix]
                    fun test_foo() = Unit
                        ~~~~~~~~
                src/Test.kt:8: Warning: Test method starts with test [TestMethodPrefix]
                    fun `test foo`() = Unit
                        ~~~~~~~~~~
                0 errors, 2 warnings
                """.trimIndent(),
            )
            .expectFixDiffs(
                """
                Autofix for src/Test.kt line 6: Remove prefix:
                @@ -6 +6
                -     fun test_foo() = Unit
                +     fun foo() = Unit
                Autofix for src/Test.kt line 8: Remove prefix:
                @@ -8 +8
                -     fun `test foo`() = Unit
                +     fun `foo`() = Unit
                """.trimIndent(),
            )
    }

    @Test
    fun `detect format`() {
        lint().issues(FORMAT)
            .files(
                JUNIT_TEST_STUB,
                kotlin(
                    "src/androidTest/com/example/Test.kt",
                    """
                    import org.junit.Test
                    class Test {
                        @Test
                        fun when_then() = Unit
                        @Test
                        fun given_when_then() = Unit

                        @Test
                        fun foo() = Unit
                        @Test
                        fun foo_bar_baz_qux() = Unit
                        @Test
                        fun `foo bar baz`() = Unit
                    }
                """,
                ).indented(),
            )
            .run()
            .expect(
                """
                src/androidTest/com/example/Test.kt:9: Warning: Test method does not follow the given_when_then or when_then format [TestMethodFormat]
                    fun foo() = Unit
                        ~~~
                src/androidTest/com/example/Test.kt:11: Warning: Test method does not follow the given_when_then or when_then format [TestMethodFormat]
                    fun foo_bar_baz_qux() = Unit
                        ~~~~~~~~~~~~~~~
                src/androidTest/com/example/Test.kt:13: Warning: Test method does not follow the given_when_then or when_then format [TestMethodFormat]
                    fun `foo bar baz`() = Unit
                        ~~~~~~~~~~~~~
                0 errors, 3 warnings
                """.trimIndent(),
            )
    }

    private companion object {
        private val JUNIT_TEST_STUB: TestFile = kotlin(
            """
                package org.junit
                annotation class Test
                """,
        ).indented()
    }
}
