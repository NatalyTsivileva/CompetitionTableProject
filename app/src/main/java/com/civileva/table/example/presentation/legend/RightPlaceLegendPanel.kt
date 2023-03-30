package com.civileva.table.example.presentation.legend

import com.civileva.table.example.presentation.legend.base.*

data class RightPlaceLegendPanel(
	val labeledLegend: LabeledListLegend,
	val size: ILegendPanel.Size = ILegendPanel.Size.Undefined()
) : BaseLegendPanel(
	id = ILegendPanel.RIGHT_PLACE,
	direction = ILegendPanel.Direction.RIGHT,
	legend = labeledLegend,
	panelSize = size
) {

	override fun updateSize(size: ILegendPanel.Size): RightPlaceLegendPanel {
		return this.copy(size = size)
	}
}