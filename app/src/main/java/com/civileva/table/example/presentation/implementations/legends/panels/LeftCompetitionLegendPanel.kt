package com.civileva.table.example.presentation.implementations.legends.panels

import com.civileva.table.example.presentation.base.legends.BaseLegendPanel
import com.civileva.table.example.presentation.base.legends.ILegend
import com.civileva.table.example.presentation.base.legends.ILegendPanel
import com.civileva.table.example.presentation.implementations.legends.items.IterationLabeledLegend

data class LeftCompetitionLegendPanel(
	override val legend: ILegend,
	val size: ILegendPanel.Size = ILegendPanel.Size.Undefined()
) : BaseLegendPanel(
	id = ILegendPanel.LEFT_COMPETITION,
	direction = ILegendPanel.Direction.LEFT,
	panelSize = size
) {

	override fun updateSize(size: ILegendPanel.Size): LeftCompetitionLegendPanel {
		return this.copy(size = size)
	}

	override fun updateLegend(legend: ILegend): LeftCompetitionLegendPanel {
		return copy(legend=legend)
	}




}