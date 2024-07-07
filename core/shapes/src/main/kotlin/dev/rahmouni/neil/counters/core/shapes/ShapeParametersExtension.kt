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
