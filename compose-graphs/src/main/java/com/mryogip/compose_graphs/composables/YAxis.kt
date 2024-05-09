package com.mryogip.compose_graphs.composables

import android.text.TextPaint
import android.text.TextUtils
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mryogip.compose_graphs.models.AxisModel
import com.mryogip.compose_graphs.models.Point
import com.mryogip.compose_graphs.utils.getTextHeight
import com.mryogip.compose_graphs.utils.getTextWidth
import kotlin.math.ceil

/**
 *
 * YAxis compose method used for drawing yAxis in any given graph.
 * @param modifier : All modifier related property.
 * @param axisModel : All data needed to draw Yaxis.
 * @param scrollOffset : Offset of delta scrolled position.
 * @param zoomScale : Scale at which zoom transformation being applied.
 * @param chartData : List of data points used in the graph.
 * @param dataCategoryWidth length of horizontal bar
 * @param yStart start position of Y axis pointer
 */

@Composable
fun YAxis(
    modifier: Modifier,
    axisModel: AxisModel,
    scrollOffset: Float = 0f,
    zoomScale: Float = 0f,
    chartData: List<Point> = emptyList(),
    dataCategoryWidth: Float = 0f,
    yStart: Float = 0f,
    barWidth: Float = 0f
) {
    with(axisModel) {
        var yAxisWidth by remember { mutableStateOf(0.dp) }
        Column(modifier = modifier.clipToBounds()) {
            val steps = steps + 1
            Canvas(
                modifier = modifier
                    .clipToBounds()
                    .width(yAxisWidth)
                    .semantics {
                        this.testTag = "y_axis"
                    }
                    .background(backgroundColor)
            ) {
                val (yAxisHeight, segmentHeight) = getAxisInitValues(
                    axisModel = axisModel,
                    canvasHeight = size.height,
                    topPadding = axisTopPadding.toPx(),
                    dataCategoryWidth = dataCategoryWidth,
                    bottomPadding = axisBottomPadding.toPx(),
                    isDataInYAxis = dataOptions.isDataInYAxis
                )
                val (_, _, yAxisScale) = getYAxisScale(chartData, axisModel.steps)
                val yPositionFromBottom = yAxisHeight - yStart + scrollOffset
                var yPos = if (dataOptions.isZeroAtBottom) {
                    yPositionFromBottom
                } else {
                    if (zoomScale < 1) yPositionFromBottom else yStart - scrollOffset
                }

                for (index in 0 until steps) {
                    // Drawing the axis labels
                    yAxisWidth = drawAxisLabel(
                        yPos = yPos,
                        index = index,
                        zoomScale = zoomScale,
                        axisModel = axisModel,
                        lastIndex = steps - 1,
                        isRightAligned = false,
                        yAxisWidth = yAxisWidth,
                        yAxisHeight = yAxisHeight,
                        segmentHeight = segmentHeight
                    )
                    drawAxisLineWithPointers(
                        yPos = yPos,
                        index = index,
                        totalSteps = steps,
                        barWidth = barWidth,
                        zoomScale = zoomScale,
                        axisModel = axisModel,
                        isRightAligned = false,
                        yAxisWidth = yAxisWidth,
                        yAxisScale = yAxisScale,
                        yAxisHeight = yAxisHeight,
                        segmentHeight = segmentHeight
                    )
                    val yPosChangeFromBottom = (axisStepSize.toPx() * (zoomScale * yAxisScale))
                    if (dataOptions.isZeroAtBottom) {
                        yPos -= yPosChangeFromBottom
                    } else {
                        if (zoomScale < 1) {
                            yPos -= yPosChangeFromBottom
                        } else {
                            yPos += ((axisStepSize.toPx() * (zoomScale * yAxisScale)))
                        }
                    }
                }
            }
        }
    }
}

fun getAxisInitValues(
    axisModel: AxisModel,
    canvasHeight: Float,
    topPadding: Float,
    bottomPadding: Float,
    isDataInYAxis: Boolean = false,
    dataCategoryWidth: Float = 0f
): Pair<Float, Float> {
    val yAxisHeight = canvasHeight - bottomPadding

    // Minus the top padding to avoid cropping at the top
    val segmentHeight = if (isDataInYAxis) {
        dataCategoryWidth
    } else {
        (yAxisHeight - topPadding) / axisModel.steps
    }
    return Pair(yAxisHeight, segmentHeight)
}

private fun DrawScope.drawAxisLineWithPointers(
    yPos: Float,
    axisModel: AxisModel,
    index: Int,
    totalSteps: Int,
    isRightAligned: Boolean,
    yAxisWidth: Dp,
    yAxisHeight: Float,
    segmentHeight: Float,
    zoomScale: Float,
    yAxisScale: Float,
    barWidth: Float
) {
    with(axisModel) {
        // Draw line only until reqXLabelsQuo -1 else will be a extra line at top with no label
        val axisStepWidth = (axisStepSize.toPx() * (zoomScale * yAxisScale))

        if (firstItemOffset != 0.dp && dataOptions.isDataInYAxis) {
            drawLine(
                color = axisColor,
                cap = StrokeCap.Round,
                strokeWidth = axisThickness.toPx(),
                start = Offset(
                    x = if (isRightAligned) 0.dp.toPx() else yAxisWidth.toPx(),
                    y = yAxisHeight - firstItemOffset.toPx() * zoomScale
                ),
                end = Offset(
                    x = if (isRightAligned) 0.dp.toPx() else yAxisWidth.toPx(),
                    y = yAxisHeight
                )
            )
        }

        if (index != (totalSteps - 1)) {
            // Draw Y-Axis line
            drawLine(
                color = axisColor,
                cap = StrokeCap.Round,
                strokeWidth = axisThickness.toPx(),
                start = Offset(
                    x = if (isRightAligned) 0.dp.toPx() else yAxisWidth.toPx(),
                    y = if (dataOptions.isDataInYAxis) {
                        if (axisOccupiesFullWidth) {
                            if (dataOptions.isZeroAtBottom) {
                                yPos
                            } else {
                                if (zoomScale < 1) yPos else yPos - axisStepWidth / 2
                            }
                        } else {
                            yPos
                        }
                    } else {
                        yAxisHeight - (segmentHeight * index)
                    }
                ),
                end = Offset(
                    x = if (isRightAligned) 0.dp.toPx() else yAxisWidth.toPx(),
                    y = if (dataOptions.isDataInYAxis) {
                        if (dataOptions.isZeroAtBottom) {
                            if (axisOccupiesFullWidth) {
                                yPos - axisStepWidth - barWidth / 2
                            } else {
                                yPos - axisStepWidth
                            }
                        } else {
                            if (zoomScale < 1) {
                                if (axisOccupiesFullWidth) {
                                    yPos - axisStepWidth - barWidth / 2
                                } else {
                                    yPos - axisStepWidth
                                }

                            } else {
                                if (axisOccupiesFullWidth) {
                                    yPos + axisStepWidth + barWidth / 2 + (firstItemOffset.toPx() * zoomScale)
                                } else {
                                    yPos + axisStepWidth + (firstItemOffset.toPx() * zoomScale)
                                }
                            }
                        }
                    } else yAxisHeight - (segmentHeight * (index + 1))

                ),
            )

            // Draw marker lines on Y-Axis
            drawLine(
                color = axisColor,
                cap = StrokeCap.Round,
                strokeWidth = axisThickness.toPx(),
                start = Offset(
                    x = if (isRightAligned) {
                        0.dp.toPx()
                    } else {
                        yAxisWidth.toPx() - labelMarkerWidth.toPx()
                    },
                    y = if (dataOptions.isDataInYAxis) {
                        yPos
                    } else {
                        yAxisHeight - (segmentHeight * index)
                    }
                ),
                end = Offset(
                    x = if (isRightAligned) labelMarkerWidth.toPx() else yAxisWidth.toPx(),
                    y = if (dataOptions.isDataInYAxis) {
                        yPos
                    } else {
                        yAxisHeight - (segmentHeight * index)
                    }
                )
            )
        }
    }
}

private fun DrawScope.drawAxisLabel(
    yPos: Float,
    index: Int,
    axisModel: AxisModel,
    yAxisWidth: Dp,
    isRightAligned: Boolean,
    yAxisHeight: Float,
    segmentHeight: Float,
    zoomScale: Float,
    lastIndex: Int
): Dp = with(axisModel) {
    var calculatedYAxisWidth = yAxisWidth
    val yAxisTextPaint = TextPaint().apply {
        textSize = labelFontSize.toPx()
        color = labelColor.toArgb()
        textAlign = if (isRightAligned) android.graphics.Paint.Align.RIGHT else android.graphics.Paint.Align.LEFT
        typeface = axisModel.typeface
    }
    val yAxisLabel = if (dataOptions.isDataInYAxis) {
        if (dataOptions.isZeroAtBottom) labelData(index) else {
            if (zoomScale < 1) labelData(lastIndex - index) else labelData(index)
        }
    } else labelData(index)
    val measuredWidth = yAxisLabel.getTextWidth(yAxisTextPaint)
    val height: Int = yAxisLabel.getTextHeight(yAxisTextPaint)
    if (measuredWidth > calculatedYAxisWidth.toPx()) {
        val width = if (axisConfig.shouldEllipsizeAxisLabel) {
            axisConfig.minTextWidthToEllipsize
        } else {
            measuredWidth.toDp()
        }
        calculatedYAxisWidth = width + labelPadding + axisOffset
    }
    val ellipsizedText = TextUtils.ellipsize(
        yAxisLabel,
        yAxisTextPaint,
        axisConfig.minTextWidthToEllipsize.toPx(),
        axisConfig.ellipsizeAt
    )
    drawContext.canvas.nativeCanvas.apply {
        drawText(
            /* text = */ if (axisConfig.shouldEllipsizeAxisLabel) ellipsizedText.toString() else yAxisLabel,
            /* x = */ if (isRightAligned) {
                calculatedYAxisWidth.toPx() - labelPadding.toPx()
            } else {
                axisStartPadding.toPx()
            },
            /* y = */ if (dataOptions.isDataInYAxis) {
                yPos + height / 2
            } else {
                yAxisHeight + height / 2 - ((segmentHeight * index))
            },
            /* paint = */ yAxisTextPaint
        )
    }
    return calculatedYAxisWidth
}

/**
 * Returns triple of YMax, YMin & scale for given list of points and steps
 * @param points: List of points in axis
 * @param steps: Total steps in axis
 */
fun getYAxisScale(
    points: List<Point>,
    steps: Int,
): Triple<Float, Float, Float> {
    val yMin = points.takeIf { it.isNotEmpty() }?.minOf { it.y } ?: 0f
    val yMax = points.takeIf { it.isNotEmpty() }?.maxOf { it.y } ?: 0f
    val totalSteps = (yMax - yMin)
    val temp = totalSteps / steps
    val scale = ceil(temp)
    return Triple(yMin, yMax, scale)
}