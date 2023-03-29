package com.civileva.table.example.presentation.adapter.base

import android.view.View
import com.civileva.table.example.presentation.legend.ILegendPanel

interface ILegendAdapter {
	fun getLegendPanels(): List<ILegendPanel>
	fun getLegendPanels(direction: ILegendPanel.Direction): List<ILegendPanel>
	fun getLegendViews(panelId: Int): List<View>
	fun findLegendPanel(legendClass: Class<*>): ILegendPanel?

	fun destroyLegendViews()

	/**
	 * Возвращает пару [Ширина-Высота] для панели
	 */
	fun getLegendPanelSize(panel: ILegendPanel): Pair<Int, Int> {
		var panelWidth = UNDEFINED_SIZE
		var panelHeight = UNDEFINED_SIZE

		val viewsList = getLegendViews(panel.id)

		viewsList.forEach { view ->
			view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

			when (panel.direction) {
				ILegendPanel.Direction.TOP, ILegendPanel.Direction.BOTTOM -> {
					if (panelHeight == UNDEFINED_SIZE) panelHeight = view.measuredHeight
					panelWidth += view.measuredWidth
				}

				ILegendPanel.Direction.LEFT, ILegendPanel.Direction.RIGHT -> {
					if (panelWidth == UNDEFINED_SIZE) panelWidth = view.measuredWidth
					panelHeight += view.measuredHeight
				}
			}
		}


		return Pair(panelWidth, panelHeight)
	}


	fun getLegendPanelParams(): PanelsParams {
		var params = PanelsParams()

		getLegendPanels().forEach { panel ->
			val size = getLegendPanelSize(panel)

			when (panel.direction) {
				ILegendPanel.Direction.LEFT -> {
					params = params.copy(
						leftPanelCount = params.leftPanelCount + 1,
						leftPanelsWidth = params.leftPanelsWidth + size.first
					)
				}

				ILegendPanel.Direction.TOP -> {
					params = params.copy(
						topPanelCount = params.topPanelCount + 1,
						topPanelsHeight = params.topPanelsHeight + size.second
					)
				}

				ILegendPanel.Direction.RIGHT -> {
					params = params.copy(
						rightPanelCount = params.rightPanelCount + 1,
						rightPanelsWidth = params.rightPanelsWidth + size.first
					)
				}

				ILegendPanel.Direction.BOTTOM -> {
					params = params.copy(
						bottomPanelCount = params.bottomPanelCount + 1,
						bottomPanelsHeight = params.bottomPanelsHeight + size.second
					)
				}
			}
		}

		return params
	}

	data class PanelsParams(
		val topPanelCount: Int = UNDEFINED_SIZE,
		val leftPanelCount: Int = UNDEFINED_SIZE,
		val rightPanelCount: Int = UNDEFINED_SIZE,
		val bottomPanelCount: Int = UNDEFINED_SIZE,

		var topPanelsHeight: Int = UNDEFINED_SIZE,
		var bottomPanelsHeight: Int = UNDEFINED_SIZE,

		var leftPanelsWidth: Int = UNDEFINED_SIZE,
		var rightPanelsWidth: Int = UNDEFINED_SIZE,
	)

	companion object {
		const val UNDEFINED_SIZE = 0
	}
}