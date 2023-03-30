package com.civileva.table.example.presentation.legend.base

abstract class BaseLegendPanel(
	override val direction: ILegendPanel.Direction,
	override val id: Int,
	override val legend: ILegend,
	override val panelSize: ILegendPanel.Size = ILegendPanel.Size.Undefined()
) : ILegendPanel