package com.civileva.table.example.presentation.implementations.legends.panels

import com.civileva.table.example.presentation.base.legends.BaseLegendPanel
import com.civileva.table.example.presentation.base.legends.ILegendPanel
import com.civileva.table.example.presentation.implementations.legends.items.IterationLegend

data class TopIterationLegendPanel(
	val iterdLegend: IterationLegend,
	val size: ILegendPanel.Size = ILegendPanel.Size.Undefined()
) : BaseLegendPanel(
	id = ILegendPanel.TOP_ITER,
	direction = ILegendPanel.Direction.TOP,
	legend = iterdLegend,
	panelSize = size
) {
	override fun updateSize(size: ILegendPanel.Size): TopIterationLegendPanel {
		return this.copy(size=size)
	}
}