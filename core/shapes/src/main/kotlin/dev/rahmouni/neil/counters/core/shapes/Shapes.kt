package dev.rahmouni.neil.counters.core.shapes

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import kotlin.math.max

class MorphableShape(
    private val start: RoundedPolygon,
    private val end: RoundedPolygon,
    private val percentage: Float,
) : Shape {

    private val matrix = Matrix()
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        matrix.scale(size.width, size.height)

        val path = Morph(start, end).toPath(progress = percentage).asComposePath()
        path.transform(matrix)

        return Outline.Generic(path)
    }
}

class Shape(
    private val polygon: RoundedPolygon,
) : Shape {

    private val matrix = Matrix()
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        matrix.scale(size.width, size.height)

        val path = polygon.toPath().asComposePath()
        path.transform(matrix)

        return Outline.Generic(path)
    }
}