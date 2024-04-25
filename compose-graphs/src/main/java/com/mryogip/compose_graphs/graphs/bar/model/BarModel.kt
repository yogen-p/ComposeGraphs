package com.mryogip.compose_graphs.graphs.bar.model

import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mryogip.compose_graphs.models.DataOptions
import com.mryogip.compose_graphs.models.ItemModel
import com.mryogip.compose_graphs.models.Point

/**
 * Model to plot an individual bar in the graph
 * @param point: To indicate mapping point on the graph
 * @param color: The color for a bar
 * */
data class BarModel(
    val point: Point,
    val color: Color = Color.Red,
    val dataOptions: DataOptions = DataOptions(),
) : ItemModel

data class BarStyle(
    val barWidth: Dp = 30.dp,
    val topLeftRadius: Dp = 4.dp,
    val topRightRadius: Dp = 4.dp,
    val bottomLeftRadius: Dp = 4.dp,
    val bottomRightRadius: Dp = 4.dp,
    val paddingBetweenBars: Dp = 15.dp,
    val barDrawStyle: DrawStyle = Fill,
    val barBlendMode: BlendMode = DrawScope.DefaultBlendMode,
)