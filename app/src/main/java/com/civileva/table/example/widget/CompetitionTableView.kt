package com.civileva.table.example.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.civileva.table.example.data.CellInteger
import com.civileva.table.example.presentation.adapter.base.ILegendPanelAdapter
import com.civileva.table.example.presentation.legend.base.ILegendPanel
import com.civileva.table.example.presentation.legend.base.LabeledListLegend

open class CompetitionTableView(
	context: Context,
	attrs: AttributeSet,
) : TableView<Int, CellInteger>(context, attrs) {


	override fun resizePanelsViews(
		adapter: ILegendPanelAdapter,
		availableWidth: Int,
		availableHeight: Int
	) {
		super.resizePanelsViews(adapter, availableWidth, availableHeight)
		val topPanelSize = getTopPanelsHeight()

		adapter.getLegendPanels().forEachIndexed { index, panel ->
			if (panel.legend is LabeledListLegend) {
				val firstView = adapter.getLegendViews(panel.id).firstOrNull()
				val firstHeight = firstView?.measuredHeight ?: 0

				if (topPanelSize > 0) {
					val wSpec = MeasureSpec.makeMeasureSpec(panel.panelSize.width, MeasureSpec.EXACTLY)
					val hSpec = MeasureSpec.makeMeasureSpec( topPanelSize , MeasureSpec.EXACTLY)
					firstView?.measure(wSpec,hSpec)
				} else {
					panel.updateSize(ILegendPanel.Size(availableWidth,availableHeight-firstHeight))
				}
			}
		}
	}

	override fun layoutRightLegends(adapter: ILegendPanelAdapter) {
		var startX = measuredWidth - getRightPanelsWidth()
		var startY = paddingTop

		adapter.getLegendPanels().forEach { panel ->

			if (panel.direction == ILegendPanel.Direction.RIGHT) {

				startY = if (panel.legend is LabeledListLegend) paddingTop else getTopPanelsHeight()

				val panelWidth = panel.panelSize.width

				adapter
					.getLegendViews(panel.id)
					.forEach { view ->
						view.layout(
							startX,
							startY,
							startX + panelWidth,
							startY + view.measuredHeight
						)
						startY += view.measuredHeight
						if (!view.isAttachedToWindow) addView(view)
					}
				startX += panelWidth
				startY = if (panel.legend is LabeledListLegend) paddingTop else getTopPanelsHeight()
			}

		}
	}


	override fun layoutLeftLegends(adapter: ILegendPanelAdapter) {
		var startX = paddingStart
		var startY = paddingTop

		adapter.getLegendPanels().forEach { panel ->

			if (panel.direction == ILegendPanel.Direction.LEFT) {

				startY = if (panel.legend is LabeledListLegend) paddingTop else getTopPanelsHeight()
				val panelWidth = panel.panelSize.width

				adapter
					.getLegendViews(panel.id)
					.forEach { view ->
						view.layout(
							startX,
							startY,
							startX + view.measuredWidth,
							startY + view.measuredHeight
						)
						startY += view.measuredHeight
						if (!view.isAttachedToWindow) addView(view)
					}

				startX += panelWidth
				startY = if (panel.legend is LabeledListLegend) paddingTop else getTopPanelsHeight()
			}
		}
	}


	private fun testMeasure(view: View, height: Int) {
		val measureWidth = MeasureSpec.makeMeasureSpec(view.measuredWidth, MeasureSpec.EXACTLY)
		val measureHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
		view.measure(measureWidth, measureHeight)

	}


}