package dev.rahmouni.neil.counters.feature.aboutme

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import androidx.graphics.shapes.toPath
import coil.compose.rememberAsyncImagePainter
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewScreen
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3IconButton
import kotlin.math.floor

@Composable
internal fun AboutMeRoute(
    modifier: Modifier = Modifier,
    onBackIconButtonClicked: () -> Unit,
) {
    AboutMeScreen(
        modifier,
        onBackIconButtonClicked,
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AboutMeScreen(
    modifier: Modifier = Modifier,
    onBackIconButtonClicked: () -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("About me") },
                navigationIcon = {
                    Rn3IconButton(
                        icon = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "",
                        onClick = onBackIconButtonClicked,
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
        contentWindowInsets = WindowInsets.displayCutout,
    ) { paddingValues ->
        AboutMePanel(paddingValues)
    }
}

@Composable
private fun AboutMePanel(paddingValues: PaddingValues) {
    Box(
        Modifier
            .padding(paddingValues)
            .fillMaxSize(),
    ) {
        val shape1 = RoundedPolygon.star(
            numVerticesPerRadius = 12,
            innerRadius = 0.892F,
            rounding = CornerRounding(1.0F, 0.0F),
            innerRounding = CornerRounding(1.0F, 0.0F),
        )
        val shape2 = RoundedPolygon.star(
            numVerticesPerRadius = 8,
            innerRadius = 0.884f,
            rounding = CornerRounding(0.16f, 0f),
            innerRounding = CornerRounding(0.16f, 0f),
        )
        val shape3 = RoundedPolygon.star(
            numVerticesPerRadius = 15,
            innerRadius = 0.892f,
            rounding = CornerRounding(1.0f, 0.0f),
            innerRounding = CornerRounding(1.0f, 0.0f),
        )

        val infiniteAnimation = rememberInfiniteTransition("infinite animation")

        Column(
            Modifier
                .padding(24.dp)
                .fillMaxSize(),
        ) {
            val morphs = remember {
                listOf(
                    Morph(shape1, shape2),
                    Morph(shape2, shape3),
                    Morph(shape3, shape1),
                )
            }
            val animatedProgress = infiniteAnimation.animateFloat(
                initialValue = 0f,
                targetValue = 3f,
                animationSpec = infiniteRepeatable(
                    tween(3000, easing = FastOutLinearInEasing),
                    repeatMode = RepeatMode.Reverse,
                ),
                label = "animatedMorphProgress",
            )
            val animatedRotation = infiniteAnimation.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    tween(30000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
                label = "animatedMorphProgress",
            )

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    "Hi",
                    fontSize = TextUnit(12f, TextUnitType.Em),
                )
                Box(
                    Modifier
                        .fillMaxWidth(.5f)
                        .aspectRatio(1f)
                        .padding(bottom = 8.dp)
                        .clip(
                            PfpShape(
                                morphs[floor(animatedProgress.value).toInt()],
                                animatedProgress.value % 1,
                                animatedRotation.value,
                            ),
                        )
                        .background(MaterialTheme.colorScheme.primary),
                ) {
                    Image(
                        painter = rememberAsyncImagePainter("https://lh3.googleusercontent.com/pw/AP1GczN5SMNjm_3K-WhJLL_0GCpgEn_XiB61-oVfsR1iObwuFtUGejnhK1FtOpGCb_weyj7AamPJKhOt1dcV6pRx6lM-z3ktd2BFdBZ7AOsMd3Tv4YrEGejWko7BZ_zpWwBnOC8VEIK9dk9AeuOJkmfvEQZlkA=w637-h637-s-no-gm?authuser=0"),
                        contentDescription = "Neïl Rahmouni",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
            Row {
                Text(
                    "I'm ",
                    fontSize = TextUnit(6f, TextUnitType.Em),
                )
                Text(
                    "Neïl Rahmouni",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = TextUnit(6f, TextUnitType.Em),
                )
            }
            Text(
                "I'm a software engineer",
                fontSize = TextUnit(6f, TextUnitType.Em),
            )
        }

        val rotBg1 = infiniteAnimation.animateFloat(
            initialValue = 0f,
            targetValue = 120f,
            animationSpec = infiniteRepeatable(
                tween(30000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "animatedMorphProgress",
        )
        Box(
            Modifier
                .align(Alignment.BottomEnd)
                .offset(150.dp, (-200).dp)
                .size(300.dp)
                .clip(BgShape(shape1, rotBg1.value))
                .background(MaterialTheme.colorScheme.primaryContainer),
        )
    }
}

class PfpShape(
    private val morph: Morph,
    private val percentage: Float,
    private val rotation: Float,
) : Shape {

    private val matrix = Matrix()
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        matrix.scale(size.width / 2f, size.height / 2f)
        matrix.translate(1f, 1f)
        matrix.rotateZ(rotation)

        val path = morph.toPath(progress = percentage).asComposePath()
        path.transform(matrix)

        return Outline.Generic(path)
    }
}

class BgShape(
    private val polygon: RoundedPolygon,
    private val rotation: Float,
) : Shape {

    private val matrix = Matrix()
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        matrix.scale(size.width / 2f, size.height / 2f)
        matrix.translate(1f, 1f)
        matrix.rotateZ(rotation)

        val path = polygon.toPath().asComposePath()
        path.transform(matrix)

        return Outline.Generic(path)
    }
}

@Rn3PreviewScreen
@Composable
private fun Default() {
    Rn3Theme {
        AboutMeScreen()
    }
}