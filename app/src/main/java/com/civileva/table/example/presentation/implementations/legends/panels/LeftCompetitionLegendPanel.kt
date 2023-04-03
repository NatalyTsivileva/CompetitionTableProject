package com.civileva.table.example.presentation.implementations.legends.panels

import com.civileva.table.example.presentation.base.legends.BaseLegendPanel
import com.civileva.table.example.presentation.base.legends.ILegendPanel
import com.civileva.table.example.presentation.implementations.legends.items.IterationLabeledLegend

data class LeftCompetitionLegendPanel(
	val iterLabeledLegend: IterationLabeledLegend,
	val size: ILegendPanel.Size = ILegendPanel.Size.Undefined()
) : BaseLegendPanel(
	id = ILegendPanel.LEFT_COMPETITION,
	direction = ILegendPanel.Direction.LEFT,
	legend = iterLabeledLegend,
	panelSize = size
) {

	override fun updateSize(size: ILegendPanel.Size): LeftCompetitionLegendPanel {
		return this.copy(size = size)
	}

}