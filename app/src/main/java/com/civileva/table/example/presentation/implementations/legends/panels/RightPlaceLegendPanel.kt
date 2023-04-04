package com.civileva.table.example.presentation.implementations.legends.panels

import com.civileva.table.example.presentation.base.legends.BaseLegendPanel
import com.civileva.table.example.presentation.base.legends.ILegend
import com.civileva.table.example.presentation.base.legends.ILegendPanel
import com.civileva.table.example.presentation.implementations.legends.items.LabeledListLegend
data class RightPlaceLegendPanel(
	override val legend: ILegend,
	val size: ILegendPanel.Size = ILegendPanel.Size.Undefined()
) : BaseLegendPanel(
	id = ILegendPanel.RIGHT_PLACE,
	direction = ILegendPanel.Direction.RIGHT,
	panelSize = size
) {

	override fun updateSize(size: ILegendPanel.Size): RightPlaceLegendPanel {
		return this.copy(size = size)
	}

	override fun updateLegend(legend: ILegend): RightPlaceLegendPanel {
		return this.copy(legend=legend)
	}
}