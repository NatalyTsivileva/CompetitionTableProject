package com.civileva.table.example.presentation.adapter.base

import android.view.View
import com.civileva.table.example.presentation.legend.base.ILegendPanel

interface ILegendPanelAdapter {
	fun getLegendPanels(): List<ILegendPanel>
	fun getLegendPanels(direction: ILegendPanel.Direction): List<ILegendPanel>
	fun getLegendViews(panelId: Int): List<View>
	fun findLegendPanel(legendClass: Class<*>): ILegendPanel?
	fun destroyLegendViews()


	fun updateLegendPanel(panel: ILegendPanel)
	fun updateLegendPanel(panel: ILegendPanel, views:List<View>)
	fun updateLegendPanels(panels: List<ILegendPanel>)


	fun measureAllPanels(): PanelsOffsets {
		val measuredPanels = mutableListOf<ILegendPanel>()
		var params = PanelsOffsets()

		getLegendPanels().forEach { panel ->
			val mPanel = measurePanel(panel)
			measuredPanels.add(mPanel)

			when (panel.direction) {
				ILegendPanel.Direction.LEFT -> {
					params = params.copy(
						leftPanelCount = params.leftPanelCount + 1,
						leftCommonWidth = params.leftCommonWidth + mPanel.panelSize.width
					)
				}

				ILegendPanel.Direction.TOP -> {
					params = params.copy(
						topPanelCount = params.topPanelCount + 1,
						topCommonHeight = params.topCommonHeight + mPanel.panelSize.height
					)
				}

				ILegendPanel.Direction.RIGHT -> {
					params = params.copy(
						rightPanelCount = params.rightPanelCount + 1,
						rightCommonWidth = params.rightCommonWidth + mPanel.panelSize.width
					)
				}

				ILegendPanel.Direction.BOTTOM -> {
					params = params.copy(
						bottomPanelCount = params.bottomPanelCount + 1,
						bottomCommonHeight = params.bottomCommonHeight + mPanel.panelSize.height
					)
				}
			}
		}

		return params.copy(measuredPanels = measuredPanels)
	}


	fun measurePanel(panel: ILegendPanel): ILegendPanel {
		var panelWidth = UNDEFINED_SIZE
		var panelHeight = UNDEFINED_SIZE

		val viewsList = getLegendViews(panel.id)

		viewsList.forEach { view ->
			view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

			when (panel.direction) {
				ILegendPanel.Direction.TOP, ILegendPanel.Direction.BOTTOM -> {
					if (view.measuredHeight>panelHeight) panelHeight = view.measuredHeight
					panelWidth += view.measuredWidth
				}

				ILegendPanel.Direction.LEFT, ILegendPanel.Direction.RIGHT -> {
					if (view.measuredWidth>panelWidth) panelWidth = view.measuredWidth
					panelHeight += view.measuredHeight
				}
			}
		}

		val size = ILegendPanel.Size(width = panelWidth, height = panelHeight)
		return panel.updateSize(size)
	}

	data class PanelsOffsets(
		val measuredPanels: List<ILegendPanel> = emptyList(),

		val topPanelCount: Int = UNDEFINED_SIZE,
		val leftPanelCount: Int = UNDEFINED_SIZE,
		val rightPanelCount: Int = UNDEFINED_SIZE,
		val bottomPanelCount: Int = UNDEFINED_SIZE,

		var topCommonHeight: Int = UNDEFINED_SIZE,
		var bottomCommonHeight: Int = UNDEFINED_SIZE,
		var leftCommonWidth: Int = UNDEFINED_SIZE,
		var rightCommonWidth: Int = UNDEFINED_SIZE
	)

	companion object {
		const val UNDEFINED_SIZE = 0
	}
}