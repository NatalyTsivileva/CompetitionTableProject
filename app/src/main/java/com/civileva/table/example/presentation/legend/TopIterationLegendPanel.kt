package com.civileva.table.example.presentation.legend

import com.civileva.table.example.presentation.legend.base.BaseLegendPanel
import com.civileva.table.example.presentation.legend.base.ILegendPanel
import com.civileva.table.example.presentation.legend.base.IterationLabeledLegend
import com.civileva.table.example.presentation.legend.base.IterationLegend

data class TopIterationLegendPanel(
	val iterdLegend: IterationLegend,
	val size: ILegendPanel.Size = ILegendPanel.Size.Undefined()
) : BaseLegendPanel<IterationLabeledLegend>(
	id = ILegendPanel.TOP_ITER,
	direction = ILegendPanel.Direction.TOP,
	legend = iterdLegend,
	panelSize = size
) {
	override fun updateSize(size: ILegendPanel.Size): TopIterationLegendPanel {
		return this.copy(size=size)
	}
}