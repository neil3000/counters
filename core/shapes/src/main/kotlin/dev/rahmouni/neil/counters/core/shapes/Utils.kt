/*
 * Copyright 2024 Rahmouni Neïl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.rahmouni.neil.counters.core.shapes

import android.graphics.PointF
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Matrix
import androidx.core.graphics.plus
import androidx.core.graphics.times
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.TransformResult
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

internal fun Float.toRadians() = this * PI.toFloat() / 180f

internal val PointZero = PointF(0f, 0f)

internal fun radialToCartesian(
    radius: Float,
    angleRadians: Float,
    center: PointF = PointZero,
) = directionVectorPointF(angleRadians) * radius + center

internal fun directionVectorPointF(angleRadians: Float) =
    PointF(cos(angleRadians), sin(angleRadians))

/**
 * Transforms a [RoundedPolygon] with the given [Matrix]
 */
fun RoundedPolygon.transformed(matrix: Matrix): RoundedPolygon =
    transformed { x, y ->
        val transformedPoint = matrix.map(Offset(x, y))
        TransformResult(transformedPoint.x, transformedPoint.y)
    }

/**
 * Calculates and returns the bounds of this [RoundedPolygon] as a [Rect]
 */
fun RoundedPolygon.getBounds() = calculateBounds().let { Rect(it[0], it[1], it[2], it[3]) }
