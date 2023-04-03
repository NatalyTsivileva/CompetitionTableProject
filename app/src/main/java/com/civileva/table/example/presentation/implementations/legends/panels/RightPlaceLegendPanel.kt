package com.civileva.table.example.presentation.implementations.legends.panels

import com.civileva.table.example.presentation.base.legends.BaseLegendPanel
import com.civileva.table.example.presentation.base.legends.ILegendPanel
import com.civileva.table.example.presentation.implementations.legends.items.LabeledListLegend
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