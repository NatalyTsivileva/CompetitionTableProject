package com.civileva.table.example.presentation.implementations.legends.panels

import com.civileva.table.example.presentation.base.legends.BaseLegendPanel
import com.civileva.table.example.presentation.base.legends.ILegendPanel
import com.civileva.table.example.presentation.implementations.legends.items.IterationLabeledLegend

data class TestIterationLegendPanel(
	val iterdLegend: IterationLabeledLegend,
	val size: ILegendPanel.Size = ILegendPanel.Size.Undefined()
) : BaseLegendPanel(
	id = ILegendPanel.TEST,
	direction = ILegendPanel.Direction.TOP,
	legend = iterdLegend,
	panelSize = size
) {
	override fun updateSize(size: ILegendPanel.Size): TestIterationLegendPanel {
		return this.copy(size=size)
	}
}