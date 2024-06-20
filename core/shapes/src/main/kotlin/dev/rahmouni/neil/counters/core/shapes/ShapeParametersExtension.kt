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

val loadingShapeParameters = listOf(
    Rn3ShapeParameters.Scallop,
    // Triangle
    ShapeParameters(
        innerRadius = 0.1f,
        roundness = 0.22f,
        shapeId = ShapeParameters.ShapeId.Triangle,
    ),
    Rn3ShapeParameters.ScallopPointy,
    // Clover
    ShapeParameters(
        sides = 4,
        innerRadius = .352f,
        roundness = .32f,
        rotation = 45f,
        shapeId = ShapeParameters.ShapeId.Star,
    ),
)

object Rn3ShapeParameters {
    val Scallop = ShapeParameters(
        sides = 15,
        innerRadius = .892f,
        roundness = 1f,
        shapeId = ShapeParameters.ShapeId.Star,
    )
    val ScallopPointy = ShapeParameters(
        sides = 12,
        innerRadius = .928f,
        roundness = .1f,
        shapeId = ShapeParameters.ShapeId.Star,
    )
}

object Rn3Shapes {
    val Scallop = Rn3ShapeParameters.Scallop.genShape().normalized()
    val ScallopPointy = Rn3ShapeParameters.ScallopPointy.genShape().normalized()
}