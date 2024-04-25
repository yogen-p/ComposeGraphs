package com.mryogip.compose_graphs.utils

import androidx.compose.ui.geometry.Offset
import com.mryogip.compose_graphs.models.Point

/**
 *
 * returns the max scrollable distance based on the points to be drawn along with padding etc.
 * @param columnWidth : Width of the Y-Axis.
 * @param xMax : Max X-Axis value.
 * @param xMin: Min X-Axis value.
 * @param xOffset: Total distance between two X-Axis points.
 * @param xLeft: Total Left padding.
 * @param paddingRight : Padding at the end of the canvas.
 * @param canvasWidth : Total available canvas width.
 */
internal fun getMaxScrollDistance(
    columnWidth: Float,
    xMax: Float,
    xMin: Float,
    xOffset: Float,
    xLeft: Float,
    paddingRight: Float,
    canvasWidth: Float
): Float {
    val xLastPoint = (xMax - xMin) * xOffset + xLeft + columnWidth + paddingRight
    return if (xLastPoint > canvasWidth) {
        xLastPoint - canvasWidth
    } else 0f
}

/**
 * returns the draw offset for bar graph.
 * @param point : bar point
 * @param xMin: Minimum value on the x axis
 * @param yMin: Minimum value on the y axis
 * @param xOffset: Distance between bars
 * @param yOffset: Distance between y axis points
 * @param xLeft: X starting point of bar graph
 * @param scrollOffset: Scroll offset
 * @param yBottom: Y starting point of bar graph
 */
internal fun getDrawOffset(
    point: Point,
    xMin: Float,
    xOffset: Float,
    xLeft: Float,
    scrollOffset: Float,
    yBottom: Float,
    yOffset: Float,
    yMin: Float,
    firstItemOffset: Float,
    zoomScale: Float,
    barWidth: Float
): Offset {
    val (x, y) = point
    val x1 =
        ((x - xMin) * xOffset) + xLeft + (firstItemOffset * zoomScale) - barWidth / 2 - scrollOffset
    val y1 = yBottom - ((y - yMin) * yOffset)
    return Offset(x1, y1)
}

/**
 * Returns the maximum value of Y axis
 * @param yMax: Maximum value in the Y axis
 * @param yStepSize: Size of one step in the Y axis
 */
fun getMaxElementInYAxis(yMax: Float, yStepSize: Int): Int {
    var reqYLabelsQuo =
        (yMax / yStepSize)
    val reqYLabelsRem = yMax.rem(yStepSize)
    if (reqYLabelsRem > 0f) {
        reqYLabelsQuo += 1
    }
    return reqYLabelsQuo.toInt() * yStepSize
}