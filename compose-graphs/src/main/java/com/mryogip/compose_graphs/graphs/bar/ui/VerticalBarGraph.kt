package com.mryogip.compose_graphs.graphs.bar.ui

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.mryogip.compose_graphs.composables.ScrollableCanvasContainer
import com.mryogip.compose_graphs.composables.XAxis
import com.mryogip.compose_graphs.composables.YAxis
import com.mryogip.compose_graphs.graphs.bar.model.BarGraphModel
import com.mryogip.compose_graphs.graphs.bar.model.BarModel
import com.mryogip.compose_graphs.utils.RowClip
import com.mryogip.compose_graphs.utils.drawUnderScrollMask
import com.mryogip.compose_graphs.utils.getDrawOffset
import com.mryogip.compose_graphs.utils.getMaxElementInYAxis
import com.mryogip.compose_graphs.utils.getMaxScrollDistance
import com.mryogip.compose_graphs.utils.getXMinAndMax
import com.mryogip.compose_graphs.utils.getYMinAndMax
import com.mryogip.compose_graphs.utils.isTapped

@Composable
fun VerticalBarGraph(
    barGraph: BarGraphModel,
    modifier: Modifier = Modifier
) {
    with(barGraph) {
        var barHighlightVisibility by remember { mutableStateOf(false) }
        var xOffset by remember { mutableFloatStateOf(0f) }
        var tapOffset by remember { mutableStateOf(Offset(0f, 0f)) }
        var isTapped by remember { mutableStateOf(false) }
        var columnWidth by remember { mutableFloatStateOf(0f) }
        var horizontalGap by remember { mutableFloatStateOf(0f) }
        var rowHeight by remember { mutableFloatStateOf(0f) }
        val points = items.map { it.point }
        val paddingRight = 16.dp
        val tapPadding = 10.dp
        val bgColor = MaterialTheme.colorScheme.surface

        val (xMin, xMax) = points.getXMinAndMax()
        val (_, yMax) = points.getYMinAndMax()

        val xAxisModel = xAxisModel.copy(
            axisStepSize = barStyle.barWidth + barStyle.paddingBetweenBars,
            steps = items.size - 1
        )
        val yAxisData = yAxisModel.copy(
            axisBottomPadding = LocalDensity.current.run { rowHeight.toDp() }
        )
        val maxElementInYAxis = getMaxElementInYAxis(yMax, yAxisData.steps)


        ScrollableCanvasContainer(
            containerBackgroundColor = backgroundColor,
            modifier = modifier.semantics { contentDescription = "Bar Graph" },
            calculateMaxDistance = { xZoom ->
                horizontalGap = horizontalExtraSpace.toPx()
                val xLeft = (xAxisModel.firstItemOffset.toPx() * xZoom) + horizontalGap
                xOffset = (barStyle.barWidth.toPx() + barStyle.paddingBetweenBars.toPx()) * xZoom
                getMaxScrollDistance(
                    columnWidth, xMax, xMin, xOffset, xLeft, 0.dp.toPx(), size.width
                )
            },
            onDraw = { scrollOffset, xZoom ->
                val yBottom = size.height - rowHeight
                val yOffset = ((yBottom - yAxisData.axisTopPadding.toPx()) / maxElementInYAxis)
                xOffset = ((barStyle.barWidth).toPx() + barStyle.paddingBetweenBars.toPx()) * xZoom
                val xLeft = columnWidth
                val dragLocks = mutableMapOf<Int, Pair<BarModel, Offset>>()

                // Draw bar lines
                items.forEachIndexed { _, barData ->
                    val drawOffset = getDrawOffset(
                        yMin = 0f,
                        xMin = xMin,
                        xLeft = xLeft,
                        xOffset = xOffset,
                        yBottom = yBottom,
                        yOffset = yOffset,
                        zoomScale = xZoom,
                        point = barData.point,
                        scrollOffset = scrollOffset,
                        barWidth = barStyle.barWidth.toPx(),
                        firstItemOffset = xAxisModel.firstItemOffset.toPx()
                    )
                    val height = yBottom - drawOffset.y

                    // Drawing each individual bars
                    barGraph.drawBar(this, barData, drawOffset, height, barGraphType, barStyle)

                    val middleOffset =
                        Offset(drawOffset.x + barStyle.barWidth.toPx() / 2, drawOffset.y)

                    // store the tap points for selection
                    if (
                        isTapped
                        && middleOffset.isTapped(
                            tapOffset, barStyle.barWidth.toPx(), yBottom, tapPadding.toPx()
                        )
                    ) {
                        dragLocks[0] = barData to drawOffset
                    }
                }

                drawUnderScrollMask(columnWidth, paddingRight, bgColor)
            },
            drawXAndYAxis = { scrollOffset, xZoom ->
                XAxis(
                    zoomScale = xZoom,
                    xStart = columnWidth,
                    chartPoints = points,
                    axisModel = xAxisModel,
                    axisStart = columnWidth,
                    scrollOffset = scrollOffset,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(
                            RowClip(
                                columnWidth, paddingRight
                            )
                        )
                        .onGloballyPositioned {
                            rowHeight = it.size.height.toFloat()
                        }
                )

                YAxis(
                    zoomScale = xZoom,
                    chartData = points,
                    axisModel = yAxisData,
                    scrollOffset = scrollOffset,
                    modifier = modifier
                        .align(Alignment.TopStart)
                        .fillMaxHeight()
                        .wrapContentWidth()
                        .onGloballyPositioned {
                            columnWidth = it.size.width.toFloat()
                        }
                )
            },
            onPointClicked = { offset: Offset, _: Float ->
                isTapped = true
                barHighlightVisibility = true
                tapOffset = offset
            },
            onScroll = {
                isTapped = false
                barHighlightVisibility = false
            }
        )
    }
}