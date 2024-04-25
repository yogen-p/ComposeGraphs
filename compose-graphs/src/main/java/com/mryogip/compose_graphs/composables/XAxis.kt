package com.mryogip.compose_graphs.composables

import android.text.TextPaint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.mryogip.compose_graphs.utils.withRotation
import kotlin.math.ceil

/**
 *
 * XAxis compose method used for drawing xAxis in any given graph.
 * @param axisModel: All data needed to draw Yaxis
 * @see AxisModel: Data class to save all params related to axis
 * @param modifier: All modifier related property.
 * @param xStart: Start position of xAxis Points.
 * @param scrollOffset: Offset of delta scrolled position.
 * @param zoomScale: Scale at which zoom transformation being applied.
 * @param chartPoints: List of data points used in the graph.
 */
@Composable
fun XAxis(
    xStart: Float,
    zoomScale: Float,
    axisStart: Float,
    modifier: Modifier,
    scrollOffset: Float,
    axisModel: AxisModel,
    chartPoints: List<Point>
) {
    with(axisModel) {
        var xAxisHeight by remember { mutableStateOf(0.dp) }
        Row(modifier = modifier.clipToBounds()) {
            Canvas(
                modifier = modifier
                    .fillMaxWidth()
                    .height(xAxisHeight)
                    .semantics {
                        this.testTag = "x_axis"
                    }
                    .background(backgroundColor)
            ) {
                val (_, _, xAxisScale) = getXAxisScale(chartPoints, steps)
                //this is used when data category draws in Y axis and value in X axis
                val dataValueWidth = (size.width - xStart - axisEndPadding.toPx()) / steps
                var xPos = xStart + (firstItemOffset.toPx() * zoomScale) - scrollOffset

                // used in the case of barchart
                if (firstItemOffset != 0.dp) {
                    drawLine(
                        axisColor,
                        Offset(axisStart, 0f),
                        Offset(xStart + (firstItemOffset.toPx() * zoomScale), 0f),
                        strokeWidth = axisThickness.toPx()
                    )
                }

                for (index in 0..steps) {
                    xAxisHeight = drawXAxisLabel(
                        xPos = xPos,
                        index = index,
                        xStart = xStart,
                        axisModel = axisModel,
                        xAxisScale = xAxisScale,
                        dataValueWidth = dataValueWidth
                    )
                    drawAxisLineWithPointers(
                        xPos = xPos,
                        index = index,
                        xStart = xStart,
                        axisModel = axisModel,
                        zoomScale = zoomScale,
                        xAxisScale = xAxisScale,
                        canDrawEndLine = index != steps,
                        dataValueWidth = dataValueWidth,
                        startDrawPadding = firstItemOffset.toPx()
                    )
                    xPos += ((axisStepSize.toPx() * (zoomScale * xAxisScale)))
                }
            }
        }
    }
}

private fun DrawScope.drawAxisLineWithPointers(
    xPos: Float,
    axisModel: AxisModel,
    zoomScale: Float,
    xAxisScale: Float,
    canDrawEndLine: Boolean, // Added check to avoid drawing an extra line post the last point
    xStart: Float,
    index: Int,
    dataValueWidth: Float,
    startDrawPadding: Float
) {
    with(axisModel) {
        if (canDrawEndLine) {
            // Draw X-Axis
            val axisStepWidth = (axisStepSize.toPx() * (zoomScale * xAxisScale))
            drawLine(
                color = axisColor,
                cap = StrokeCap.Round,
                strokeWidth = axisThickness.toPx(),
                start = if (axisModel.dataOptions.isDataInYAxis) {
                    Offset(xStart, 0f)
                } else {
                    Offset(xStart + (startDrawPadding * zoomScale), 0f)
                },
                end = if (axisOccupiesFullWidth) {
                    Offset(
                        (xPos + (axisStepWidth / 2) + axisStepWidth) + (startDrawPadding * zoomScale), 0f
                    )
                } else {
                    if (axisModel.dataOptions.isDataInYAxis) {
                        Offset(xStart + (dataValueWidth * (index + 1)), 0f)
                    } else {
                        Offset(xPos + axisStepWidth, 0f)
                    }
                }
            )
        }

        // Draw marker lines on X-Axis
        drawLine(
            color = axisColor,
            cap = StrokeCap.Round,
            start = Offset(xPos, 0f),
            strokeWidth = axisThickness.toPx(),
            end = Offset(xPos, labelMarkerWidth.toPx()),
        )
    }
}

private fun DrawScope.drawXAxisLabel(
    axisModel: AxisModel,
    index: Int,
    xAxisScale: Float,
    xPos: Float,
    xStart: Float,
    dataValueWidth: Float
): Dp = with(axisModel) {
    val xAxisTextPaint = TextPaint().apply {
        textSize = labelFontSize.toPx()
        color = labelColor.toArgb()
        textAlign = android.graphics.Paint.Align.LEFT
        typeface = axisModel.typeface
    }
    val xLabel = if (axisModel.dataOptions.isDataInYAxis) labelData(index) else labelData((index * xAxisScale).toInt())
    val labelHeight = xLabel.getTextHeight(xAxisTextPaint)
    val labelWidth = xLabel.getTextWidth(xAxisTextPaint)
    val calculatedXAxisHeight = labelHeight.toDp() + axisThickness + labelMarkerWidth + labelPadding + axisBottomPadding

    val ellipsizedText = android.text.TextUtils.ellipsize(
        xLabel,
        xAxisTextPaint,
        axisStepSize.toPx(),
        axisConfig.ellipsizeAt
    )
    drawContext.canvas.nativeCanvas.apply {
        val x =
            if (axisModel.dataOptions.isDataInYAxis) xStart + (dataValueWidth * index) - (labelWidth / 2) else xPos - (labelWidth / 2)
        val y = labelHeight / 2 + labelMarkerWidth.toPx() + labelPadding.toPx()
        withRotation(axisLabelAngle, x, y) {
            drawText(
                if (axisConfig.shouldEllipsizeAxisLabel) ellipsizedText.toString() else xLabel,
                x,
                y,
                xAxisTextPaint
            )
        }
    }
    calculatedXAxisHeight
}

/**
 * Returns triple of XMax, XMin & scale for given list of points and steps
 * @param points: List of points in axis
 * @param steps: Total steps in axis
 */
fun getXAxisScale(
    points: List<Point>,
    steps: Int,
): Triple<Float, Float, Float> {
    val xMin = points.takeIf { it.isNotEmpty() }?.minOf { it.x } ?: 0f
    val xMax = points.takeIf { it.isNotEmpty() }?.maxOf { it.x } ?: 0f
    val totalSteps = (xMax - xMin)
    val temp = totalSteps / steps
    val scale = ceil(temp)
    return Triple(xMin, xMax, scale)
}