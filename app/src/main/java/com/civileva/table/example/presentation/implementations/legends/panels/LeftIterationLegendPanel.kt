package com.civileva.table.example.presentation.implementations.legends.panels

import com.civileva.table.example.presentation.base.legends.BaseLegendPanel
import com.civileva.table.example.presentation.base.legends.ILegendPanel
import com.civileva.table.example.presentation.implementations.legends.items.IterationLegend

data class LeftIterationLegendPanel(
	val iterdLegend: IterationLegend,
	val size: ILegendPanel.Size = ILegendPanel.Size.Undefined()
) : BaseLegendPanel(
	id = ILegendPanel.LEFT_ITER,
	direction = ILegendPanel.Direction.LEFT,
	legend = iterdLegend,
	panelSize = size
) {
	override fun updateSize(size: ILegendPanel.Size): LeftIterationLegendPanel {
		return this.copy(size=size)
	}
}