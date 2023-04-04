package com.civileva.table.example.presentation.implementations.legends.panels

import com.civileva.table.example.presentation.base.legends.BaseLegendPanel
import com.civileva.table.example.presentation.base.legends.ILegend
import com.civileva.table.example.presentation.base.legends.ILegendPanel
import com.civileva.table.example.presentation.implementations.legends.items.IterationLabeledLegend

data class TestIterationLegendPanel(
	override val legend: ILegend,
	val size: ILegendPanel.Size = ILegendPanel.Size.Undefined()
) : BaseLegendPanel(
	id = ILegendPanel.TEST,
	direction = ILegendPanel.Direction.TOP,
	panelSize = size
) {
	override fun updateSize(size: ILegendPanel.Size): TestIterationLegendPanel {
		return this.copy(size=size)
	}

	override fun updateLegend(legend: ILegend): TestIterationLegendPanel {
		return this.copy(legend=legend)
	}
}