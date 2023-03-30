package com.civileva.table.example.presentation.legend

import com.civileva.table.example.presentation.legend.base.BaseLegendPanel
import com.civileva.table.example.presentation.legend.base.ILegendPanel
import com.civileva.table.example.presentation.legend.base.IterationLabeledLegend
import com.civileva.table.example.presentation.legend.base.IterationLegend

data class LeftIterationLegendPanel(
	val iterdLegend: IterationLegend,
	val size: ILegendPanel.Size = ILegendPanel.Size.Undefined()
) : BaseLegendPanel<IterationLabeledLegend>(
	id = ILegendPanel.LEFT_ITER,
	direction = ILegendPanel.Direction.LEFT,
	legend = iterdLegend,
	panelSize = size
) {
	override fun updateSize(size: ILegendPanel.Size): LeftIterationLegendPanel {
		return this.copy(size=size)
	}
}