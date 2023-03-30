package com.civileva.table.example.presentation.legend

import com.civileva.table.example.presentation.legend.base.BaseLegendPanel
import com.civileva.table.example.presentation.legend.base.ILegendPanel
import com.civileva.table.example.presentation.legend.base.IterationLabeledLegend

data class LeftCompetitionLegendPanel(
	val iterLabeledLegend: IterationLabeledLegend,
	val size: ILegendPanel.Size = ILegendPanel.Size.Undefined()
) : BaseLegendPanel<IterationLabeledLegend>(
	id = ILegendPanel.LEFT_COMPETITION,
	direction = ILegendPanel.Direction.LEFT,
	legend = iterLabeledLegend,
	panelSize = size
) {

	override fun updateSize(size: ILegendPanel.Size): LeftCompetitionLegendPanel {
		return this.copy(size = size)
	}

}