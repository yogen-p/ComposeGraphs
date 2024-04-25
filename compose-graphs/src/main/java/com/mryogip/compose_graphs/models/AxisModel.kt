package com.mryogip.compose_graphs.models

import android.graphics.Typeface
import android.text.TextUtils
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 *
 * Axis information to drawing either of the axes in any graph.
 * @param steps: Number of steps/items in the axis
 * @param labelPadding: Text label padding from Axis
 * @param axisStepSize: Size of each Axis step in Dp
 * @param axisColor: Axis line Color axis
 * @param labelColor: Text Color for axis labels
 * @param labelAngle: Angle for the axis labels if labels are long
 * @param axisThickness: Thickness of the axis line
 * @param labelMarkerWidth: Indicator width on Y axis line for showing points
 * @param typeface: The font family for label
 * @param firstItemOffset: Padding between Axis and first point on the Axis
 * @param labelFontSize: Font size of axis label data
 * @param labelData : Lambda method to provide a label for an item in the axis. The Int param is the index
 * @param axisOccupiesFullWidth : Boolean to draw axis line till end.
 */
data class AxisModel(
    val steps: Int,
    val axisOffset: Dp,
    val labelPadding: Dp,
    val axisStepSize: Dp,
    val axisColor: Color,
    val labelColor: Color,
    val labelAngle: Float,
    val axisThickness: Dp,
    val axisTopPadding: Dp,
    val axisEndPadding: Dp,
    val typeface: Typeface,
    val firstItemOffset: Dp,
    val axisStartPadding: Dp,
    val labelMarkerWidth: Dp,
    val axisBottomPadding: Dp,
    val axisLabelAngle: Float,
    val backgroundColor: Color,
    val axisConfig: AxisConfig,
    val labelFontSize: TextUnit,
    val dataOptions: DataOptions,
    val labelData: (Int) -> String,
    val axisOccupiesFullWidth: Boolean
) {
    class Builder {
        private var steps: Int = 1
        private var labelAngle: Float = 0f
        private var axisOffset: Dp = 20.dp
        private var labelPadding: Dp = 20.dp
        private var axisStepSize: Dp = 30.dp
        private var axisThickness: Dp = 2.dp
        private var axisConfig = AxisConfig()
        private var firstItemOffset: Dp = 0.dp
        private var axisTopPadding: Dp = 20.dp
        private var axisLabelAngle: Float = 0f
        private var axisEndPadding: Dp = 20.dp
        private var labelMarkerWidth: Dp = 5.dp
        private var axisStartPadding: Dp = 10.dp
        private var axisBottomPadding: Dp = 10.dp
        private var axisColor: Color = Color.Black
        private var labelFontSize: TextUnit = 14.sp
        private var labelColor: Color = Color.Black
        private var typeface: Typeface = Typeface.DEFAULT
        private var axisOccupiesFullWidth: Boolean = false
        private var backgroundColor: Color = Color.Transparent
        private var dataOptions: DataOptions = DataOptions()
        private var labelData: (Int) -> String = { _ -> "" }

        fun steps(count: Int) = apply { this.steps = count }

        fun axisOffset(offset: Dp) = apply { this.axisOffset = offset }

        fun axisStepSize(size: Dp) = apply { this.axisStepSize = size }

        fun labelColor(color: Color) = apply { this.labelColor = color }

        fun labelAngle(angle: Float) = apply { this.labelAngle = angle }

        fun topPadding(padding: Dp) = apply { this.axisTopPadding = padding }

        fun endPadding(padding: Dp) = apply { this.axisEndPadding = padding }

        fun labelPadding(padding: Dp) = apply { this.labelPadding = padding }

        fun typeFace(typeface: Typeface) = apply { this.typeface = typeface }

        fun axisColor(axisColor: Color) = apply { this.axisColor = axisColor }

        fun axisConfig(config: AxisConfig) = apply { this.axisConfig = config }

        fun labelMarkerWidth(width: Dp) = apply { this.labelMarkerWidth = width }

        fun startPadding(padding: Dp) = apply { this.axisStartPadding = padding }

        fun firstItemOffset(offset: Dp) = apply { this.firstItemOffset = offset }

        fun backgroundColor(color: Color) = apply { this.backgroundColor = color }

        fun axisLabelAngle(angle: Float) = apply { this.axisLabelAngle = angle }

        fun bottomPadding(padding: Dp) = apply { this.axisBottomPadding = padding }

        fun axisThickness(thickness: Dp) = apply { this.axisThickness = thickness }

        fun labelFontSize(fontSize: TextUnit) = apply { this.labelFontSize = fontSize }

        fun labelData(labelData: (Int) -> String) = apply { this.labelData = labelData }

        fun axisOccupiesFullWidth(flag: Boolean) = apply { this.axisOccupiesFullWidth = flag }

        fun setDataOptions(dataOptions: DataOptions) = apply { this.dataOptions = dataOptions }

        fun build() = AxisModel(
            steps = steps,
            typeface = typeface,
            axisColor = axisColor,
            labelData = labelData,
            axisConfig = axisConfig,
            labelAngle = labelAngle,
            axisOffset = axisOffset,
            labelColor = labelColor,
            dataOptions = dataOptions,
            labelPadding = labelPadding,
            axisStepSize = axisStepSize,
            labelFontSize = labelFontSize,
            axisThickness = axisThickness,
            axisLabelAngle = axisLabelAngle,
            axisTopPadding = axisTopPadding,
            axisEndPadding = axisEndPadding,
            firstItemOffset = firstItemOffset,
            backgroundColor = backgroundColor,
            axisStartPadding = axisStartPadding,
            labelMarkerWidth = labelMarkerWidth,
            axisBottomPadding = axisBottomPadding,
            axisOccupiesFullWidth = axisOccupiesFullWidth
        )
    }
}

/**
 *
 * AxisConfig data class used to mention all config related param required to draw graph.
 * @param shouldEllipsizeAxisLabel : true if should ellipsize the axis label at end  else false
 * @param minTextWidthToEllipsize : minimum width of the axis label post which label will be ellipsized
 * @param ellipsizeAt : position at which the label will be truncated or ellipsized
 */
data class AxisConfig(
    val minTextWidthToEllipsize: Dp = 40.dp,
    val shouldEllipsizeAxisLabel: Boolean = false,
    val ellipsizeAt: TextUtils.TruncateAt = TextUtils.TruncateAt.END
)

/**
 * DataCategoryOptions used to hold information about data category like where should draw the data category.
 * @param isDataInYAxis: true if data category draws in y axis, false if it draws is in x axis.
 * @param isZeroAtBottom: true when data category start from bottom of y axis, false if it start from top of y axis
 */
data class DataOptions(
    val isDataInYAxis: Boolean = false,
    val isZeroAtBottom: Boolean = true
)