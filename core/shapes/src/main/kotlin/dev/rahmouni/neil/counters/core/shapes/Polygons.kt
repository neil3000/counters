package dev.rahmouni.neil.counters.core.shapes

val loadingShapeParameters = listOf(
    // Scallop (smooth)
    ShapeParameters(
        sides = 15,
        innerRadius = .892f,
        roundness = 1f,
        shapeId = ShapeParameters.ShapeId.Star,
    ),
    // Triangle
    ShapeParameters(
        innerRadius = 0.1f,
        roundness = 0.22f,
        shapeId = ShapeParameters.ShapeId.Triangle,
    ),
    // scallop (pointy)
    ShapeParameters(
        sides = 12,
        innerRadius = .928f,
        roundness = .1f,
        shapeId = ShapeParameters.ShapeId.Star,
    ),
    // Clover
    ShapeParameters(
        sides = 4,
        innerRadius = .352f,
        roundness = .32f,
        rotation = 45f,
        shapeId = ShapeParameters.ShapeId.Star,
    ),
)