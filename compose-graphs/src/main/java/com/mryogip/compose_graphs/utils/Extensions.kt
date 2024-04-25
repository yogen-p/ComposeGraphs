package com.mryogip.compose_graphs.utils

import android.graphics.Paint
import android.graphics.Rect
import androidx.compose.ui.geometry.Offset
import com.mryogip.compose_graphs.models.Point

fun List<Point>.getXMinAndMax(): Pair<Float, Float> {
    return minOf { it.x } to maxOf { it.x }
}

fun List<Point>.getYMinAndMax(): Pair<Float, Float> {
    return minOf { it.y } to maxOf { it.y }
}

/**
return the height of text in canvas drawn text
 */
fun String.getTextHeight(paint: Paint): Int {
    val bounds = Rect()
    paint.getTextBounds(
        this,
        0,
        this.length,
        bounds
    )
    return bounds.height()
}

/**
return the width of text in canvas drawn text
 */
fun String.getTextWidth(paint: Paint): Float {
    return paint.measureText(this)
}

/**
 * Return true if the point is selected
 * @param tapOffset Tapped offset
 * @param xOffset in the X axis
 * @param bottom bottom Value
 */
fun Offset.isTapped(tapOffset: Offset, xOffset: Float, bottom: Float, tapPadding: Float) =
    ((tapOffset.x) > x - (xOffset + tapPadding) / 2) && ((tapOffset.x) < x + (xOffset + tapPadding) / 2) &&
            ((tapOffset.plus(Offset(0f, tapPadding))).y > y) && ((tapOffset.y) < bottom)