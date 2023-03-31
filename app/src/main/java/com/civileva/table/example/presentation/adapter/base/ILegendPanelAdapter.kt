package com.civileva.table.example.presentation.adapter.base

import android.view.View
import com.civileva.table.example.presentation.legend.base.ILegendPanel

interface ILegendPanelAdapter {
	fun getLegendPanels(): List<ILegendPanel>
	fun getLegendPanel(panelId:Int): ILegendPanel?
	fun getLegendPanels(direction: ILegendPanel.Direction): List<ILegendPanel>
	fun getLegendViews(panelId: Int): List<View>
	fun findLegendPanel(legendClass: Class<*>): ILegendPanel?
	fun destroyLegendViews()

	fun updateLegendPanelSize(panel: ILegendPanel, size: ILegendPanel.Size)

	data class MeasuredPanelsBorders(
		val topPanelCount: Int = UNDEFINED_SIZE,
		val leftPanelCount: Int = UNDEFINED_SIZE,
		val rightPanelCount: Int = UNDEFINED_SIZE,
		val bottomPanelCount: Int = UNDEFINED_SIZE,

		var topPanelsHeight: Int = UNDEFINED_SIZE,
		var bottomPanelsHeight: Int = UNDEFINED_SIZE,
		var leftPanelsWidth: Int = UNDEFINED_SIZE,
		var rightPanelsWidth: Int = UNDEFINED_SIZE
	)

	companion object {
		const val UNDEFINED_SIZE = 0
	}
}