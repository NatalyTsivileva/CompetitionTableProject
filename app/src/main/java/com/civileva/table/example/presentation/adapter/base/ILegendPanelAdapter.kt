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


	fun measureLegendPanels(): MeasuredPanelsBorders {
		var params = MeasuredPanelsBorders()
		val measuredPanelsList = mutableListOf<ILegendPanel>()

		getLegendPanels().forEach { panel ->
			val measuredPanel = measurePanel(panel)

			updateLegendPanelSize(
				measuredPanel,
				measuredPanel.panelSize
			)

			measuredPanelsList.add(measuredPanel)

			 when (panel.direction) {
				ILegendPanel.Direction.LEFT -> {
					params = params.copy(
						leftPanelCount = params.leftPanelCount + 1,
						leftPanelsWidth = params.leftPanelsWidth + measuredPanel.panelSize.width
					)
				}

				ILegendPanel.Direction.TOP -> {
					params = params.copy(
						topPanelCount = params.topPanelCount + 1,
						topPanelsHeight = params.topPanelsHeight + measuredPanel.panelSize.height
					)
				}

				ILegendPanel.Direction.RIGHT -> {
					params = params.copy(
						rightPanelCount = params.rightPanelCount + 1,
						rightPanelsWidth = params.rightPanelsWidth + measuredPanel.panelSize.width
					)
				}

				ILegendPanel.Direction.BOTTOM -> {
					params = params.copy(
						bottomPanelCount = params.bottomPanelCount + 1,
						bottomPanelsHeight = params.bottomPanelsHeight + measuredPanel.panelSize.height
					)
				}
			}
		}

		return params
	}


	 fun measurePanel(panel: ILegendPanel): ILegendPanel {
		getLegendViews(panel.id).forEach { view ->
			view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
		}

		val size = getMeasuredPanelSize(panel)
		return panel.updateSize(size)
	}


	 fun getMeasuredPanelSize(panel: ILegendPanel): ILegendPanel.Size {
		var panelWidth = UNDEFINED_SIZE
		var panelHeight = UNDEFINED_SIZE

		val viewsList = getLegendViews(panel.id)

		viewsList.forEach { view ->
			when (panel.direction) {
				ILegendPanel.Direction.TOP, ILegendPanel.Direction.BOTTOM -> {
					if (view.measuredHeight > panelHeight) panelHeight = view.measuredHeight
					panelWidth += view.measuredWidth
				}

				ILegendPanel.Direction.LEFT, ILegendPanel.Direction.RIGHT -> {
					if (view.measuredWidth > panelWidth) panelWidth = view.measuredWidth
					panelHeight += view.measuredHeight
				}
			}
		}
		return ILegendPanel.Size(width = panelWidth, height = panelHeight)
	}

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