package com.mryogip.compose_graphs.graphs.bar.model

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mryogip.compose_graphs.models.AxisModel
import com.mryogip.compose_graphs.models.BarGraphType

data class BarGraphModel(
    val items: List<BarModel>,
    val xAxisModel: AxisModel,
    val yAxisModel: AxisModel,
    val barStyle: BarStyle = BarStyle(),
    val horizontalExtraSpace: Dp = 0.dp,
    val backgroundColor: Color = Color.White,
    val barGraphType: BarGraphType = BarGraphType.Vertical,
    val drawBar: (DrawScope, BarModel, Offset, Float, BarGraphType, BarStyle) -> Unit = { drawScope, barModel, drawOffset, height, type, style ->
        // Default implementation
        drawBarGraph(height, barModel, style, drawOffset, drawScope, type)
    }
)

/**
 *
 * Used to draw the individual bars
 * @param drawScope : Creates a scoped drawing environment
 * @param barStyle : All meta data related to the bar styling
 * @param barModel : Data related to a single bar
 * @param drawOffset: Top left offset for drawing the bar
 * @param height : Height of the bar graph
 * @param barGraphType : Type of bar graph
 */
fun drawBarGraph(
    height: Float,
    barModel: BarModel,
    barStyle: BarStyle,
    drawOffset: Offset,
    drawScope: DrawScope,
    barGraphType: BarGraphType
) {
    with(drawScope) {
        with(barStyle) {
            // Draw bar lines
            drawRoundRect(
                style = barDrawStyle,
                topLeft = drawOffset,
                color = barModel.color,
                blendMode = barBlendMode,
                cornerRadius = CornerRadius(topLeftRadius.toPx(), topRightRadius.toPx()),
                size = if (barGraphType == BarGraphType.Vertical) {
                    Size(barWidth.toPx(), height)
                } else {
                    Size(height, barWidth.toPx())
                }
            )
        }
    }
}
