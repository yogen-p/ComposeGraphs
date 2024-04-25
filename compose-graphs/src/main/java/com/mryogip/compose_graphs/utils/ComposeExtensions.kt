package com.mryogip.compose_graphs.utils

import android.graphics.Canvas
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.unit.Dp
import kotlin.math.abs

/**
 * Gesture support to detect and filter pointer scopes to give a zoom start callback
 * @param isZoomAllowed: True if user is allowed to zoom.
 * @param onZoom: Callback when zoom gesture is detected.
 */
internal suspend fun PointerInputScope.detectZoomGesture(
    isZoomAllowed: Boolean = true,
    onZoom: (zoom: Float) -> Unit
) {
    if (isZoomAllowed) {
        forEachGesture {
            awaitPointerEventScope {
                awaitFirstDown(requireUnconsumed = false)
            }
            awaitPointerEventScope {
                var zoom = 1f
                var pastTouchSlop = false
                val touchSlop = viewConfiguration.touchSlop

                do {
                    val event = awaitPointerEvent()
                    val canceled = event.changes.any { it.isConsumed }
                    if (event.changes.size == 1) break
                    else if (event.changes.size == 2) {
                        if (isZoomAllowed) {
                            if (!canceled) {
                                val zoomChange = event.calculateZoom()
                                if (!pastTouchSlop) {
                                    zoom *= zoomChange
                                    val centroidSize =
                                        event.calculateCentroidSize(useCurrent = false)
                                    val zoomMotion = abs(1 - zoom) * centroidSize
                                    if (zoomMotion > touchSlop) pastTouchSlop = true
                                }
                                if (pastTouchSlop) {
                                    if (zoomChange != 1f) onZoom(zoomChange)
                                    event.changes.forEach { if (it.positionChanged()) it.consume() }
                                }
                            }
                        }
                    } else break
                } while (!canceled && event.changes.any { it.pressed })
            }
        }
    }
}

/**
 *
 * DrawScope.drawUnderScrollMask extension method used  for drawing a rectangular mask to make graph scrollable under the YAxis.
 * @param columnWidth : Width of the rectangular mask here width of Y Axis is used.
 * @param paddingRight : Padding given at the end of the graph.
 * @param bgColor : Background of the rectangular mask.
 */
internal fun DrawScope.drawUnderScrollMask(columnWidth: Float, paddingRight: Dp, bgColor: Color) {
    // Draw column to make graph look scrollable under Yaxis
    drawRect(
        bgColor, Offset(0f, 0f), Size(columnWidth, size.height)
    )
    // Draw right padding
    drawRect(
        bgColor,
        Offset(size.width - paddingRight.toPx(), 0f),
        Size(paddingRight.toPx(), size.height)
    )
}

/**
 * Wrap the specified [block] in calls to [Canvas.save]/[Canvas.rotate]
 * and [Canvas.restoreToCount].
 */
public inline fun Canvas.withRotation(
    degrees: Float = 0.0f,
    pivotX: Float = 0.0f,
    pivotY: Float = 0.0f,
    block: Canvas.() -> Unit
) {
    val checkpoint = save()
    rotate(degrees, pivotX, pivotY)
    try {
        block()
    } finally {
        restoreToCount(checkpoint)
    }
}