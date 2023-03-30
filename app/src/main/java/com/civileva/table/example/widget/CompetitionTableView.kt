package com.civileva.table.example.widget

import android.content.Context
import android.util.AttributeSet
import com.civileva.table.example.data.CellInteger
import com.civileva.table.example.presentation.adapter.base.ILegendPanelAdapter
import com.civileva.table.example.presentation.legend.base.ILegendPanel
import com.civileva.table.example.presentation.legend.base.LabeledListLegend

open class CompetitionTableView(
	context: Context,
	attrs: AttributeSet,
) : TableView<Int, CellInteger>(context, attrs) {


	override fun layoutRightLegends(adapter: ILegendPanelAdapter) {
		var startX = measuredWidth - getRightOffset()
		var startY = paddingTop

		adapter.getLegendPanels().forEach { panel ->

			if (panel.direction == ILegendPanel.Direction.RIGHT) {

				startY = if (panel.legend is LabeledListLegend) paddingTop else getTopOffset()

				val panelWidth = panel.panelSize.width

				adapter
					.getLegendViews(panel.id)
					.forEach { view ->
						view.layout(startX,	startY,startX + panelWidth,startY + measuredHeight)
						startY += view.measuredHeight
						if (!view.isAttachedToWindow) addView(view)
					}
				startX += panelWidth
				startY = if (panel.legend is LabeledListLegend) paddingTop else getTopOffset()
			}

		}
	}


	override fun layoutLeftLegends(adapter: ILegendPanelAdapter) {
		var startX = paddingStart
		var startY = paddingTop

		adapter.getLegendPanels().forEach { panel ->

			if (panel.direction == ILegendPanel.Direction.LEFT) {

				startY = if(panel.legend is LabeledListLegend) paddingTop else getTopOffset()
				val panelWidth = panel.panelSize.width

				adapter
					.getLegendViews(panel.id)
					.forEach { view ->
						view.layout(
							startX,
							startY,
							startX + view.measuredWidth,
							startY + measuredHeight
						)
						startY += view.measuredHeight
						if (!view.isAttachedToWindow) addView(view)
					}

				startX += panelWidth
				startY = if(panel.legend is LabeledListLegend) paddingTop else getTopOffset()
			}
		}
	}


}