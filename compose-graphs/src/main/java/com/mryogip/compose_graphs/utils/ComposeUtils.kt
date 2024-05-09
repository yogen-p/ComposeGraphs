package com.mryogip.compose_graphs.utils

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

/**
 * Return the shape that is used to mask a particular area for given leftPadding & rightPadding
 */
internal class RowClip(
    private val leftPadding: Float,
    private val rightPadding: Dp,
    private val topPadding: Float = 0f
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(
            androidx.compose.ui.geometry.Rect(
                leftPadding,
                topPadding,
                size.width - rightPadding.value * density.density,
                size.height
            )
        )
    }
}