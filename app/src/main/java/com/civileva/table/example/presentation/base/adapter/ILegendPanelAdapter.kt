package com.civileva.table.example.presentation.base.adapter

import com.civileva.table.example.presentation.base.holders.ILegendTableViewHolder
import com.civileva.table.example.presentation.base.legends.ILegendPanel

interface ILegendPanelAdapter {
	fun getLegendPanels(): List<ILegendPanel>
	fun getLegendPanels(direction:ILegendPanel.Direction): List<ILegendPanel>

	fun getLegendPanel(panelId: Int): ILegendPanel?

	fun getLegendPanelViewsHolder(panelId: Int): ILegendTableViewHolder?
	fun findLegendPanel(legendClass: Class<*>): List<ILegendPanel>

	fun measureLegendPanelSize(panel: ILegendPanel, size: ILegendPanel.Size)

}