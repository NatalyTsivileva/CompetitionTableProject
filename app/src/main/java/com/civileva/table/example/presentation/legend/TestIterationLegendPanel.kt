package com.civileva.table.example.presentation.legend

import com.civileva.table.example.presentation.legend.base.BaseLegendPanel
import com.civileva.table.example.presentation.legend.base.ILegendPanel
import com.civileva.table.example.presentation.legend.base.IterationLabeledLegend
import com.civileva.table.example.presentation.legend.base.IterationLegend

data class TestIterationLegendPanel(
	val iterdLegend: IterationLabeledLegend,
	val size: ILegendPanel.Size = ILegendPanel.Size.Undefined()
) : BaseLegendPanel(
	id = ILegendPanel.TEST,
	direction = ILegendPanel.Direction.BOTTOM,
	legend = iterdLegend,
	panelSize = size
) {
	override fun updateSize(size: ILegendPanel.Size): TestIterationLegendPanel {
		return this.copy(size=size)
	}
}