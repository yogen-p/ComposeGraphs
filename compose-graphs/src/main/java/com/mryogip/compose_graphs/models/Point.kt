package com.mryogip.compose_graphs.models

import androidx.annotation.FloatRange

/**
 *
 * Model to indicate mapping points on the graph.
 * @param x: X co-ordinate in the graph
 * @param y: Y co-ordinate in the graph
 */
data class Point(
    val x: Float,
    @FloatRange(from = -10.0, to = 10.0) val y: Float
)