package com.civileva.table.example.presentation.implementations.legends.panels

import com.civileva.table.example.presentation.base.legends.BaseLegendPanel
import com.civileva.table.example.presentation.base.legends.ILegend
import com.civileva.table.example.presentation.base.legends.ILegendPanel

data class BottomGameLegendPanel(
	override val legend: ILegend,
	val size: ILegendPanel.Size = ILegendPanel.Size.Undefined()
) : BaseLegendPanel(
	id = ILegendPanel.GAME,
	direction = ILegendPanel.Direction.BOTTOM,
	panelSize = size
) {
	override fun updateSize(size: ILegendPanel.Size): BottomGameLegendPanel {
		return this.copy(size=size)
	}

	override fun updateLegend(legend: ILegend): BottomGameLegendPanel {
		return this.copy(legend=legend)
	}
}