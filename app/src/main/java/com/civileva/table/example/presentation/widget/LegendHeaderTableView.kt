package com.civileva.table.example.presentation.widget

import android.content.Context
import android.util.AttributeSet
import com.civileva.table.example.data.CellInteger
import com.civileva.table.example.presentation.base.adapter.ILegendPanelAdapter
import com.civileva.table.example.presentation.base.legends.ILegendPanel
import com.civileva.table.example.presentation.implementations.legends.items.LabeledListLegend

open class LegendHeaderTableView(
	context: Context,
	attrs: AttributeSet,
) : LegendTableView<Int, CellInteger>(context, attrs) {

	override fun measureLegend(
		adapter: ILegendPanelAdapter,
		measuredParentHeight: Int,
		measuredParentWidth: Int
	) {
		super.measureLegend(adapter, measuredParentHeight, measuredParentWidth)
		adapter.findLegendPanel(LabeledListLegend::class.java).forEach { panel ->
			val topPanelCount = getTopPanelItemCount() ?: 0
			if (topPanelCount > 0) {
				val firstPanelView = getLegendAdapter()
					?.getLegendPanelViewsHolder(panel.id)
					?.getPanelViews()
					?.firstOrNull()
				val wSpec = MeasureSpec.makeMeasureSpec(panel.panelSize.width, MeasureSpec.EXACTLY)
				val hSpec = MeasureSpec.makeMeasureSpec(getTopPanelsHeight(), MeasureSpec.EXACTLY)
				firstPanelView?.measure(wSpec, hSpec)

			}
		}
	}

	override fun resizePanelsViews(
		panel: ILegendPanel,
		availableHeight: Int,
		availableWidth: Int
	): ILegendPanel.Size {
		return if (panel.legend is LabeledListLegend) {
			val firstCell = getCellAdapter()?.getTableData()?.firstOrNull()
			var firstDataCellHeight = 0

			if (firstCell != null) {
				val firstCellView =
					getCellAdapter()?.getTableHolders()?.firstOrNull()?.getTableView(firstCell)
				firstDataCellHeight = firstCellView?.measuredHeight ?: 0
			}

			val newHeight = firstDataCellHeight * (panel.legend.itemsCount)
			super.resizePanelsViews(panel, newHeight, availableWidth)

		} else {
			return super.resizePanelsViews(panel, availableHeight, availableWidth)
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
					.getLegendPanelViewsHolder(panel.id)
					?.getPanelViews()
					?.forEach { view ->
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
					.getLegendPanelViewsHolder(panel.id)
					?.getPanelViews()
					?.forEach { view ->
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

	override fun getTopPanelsHeight(): Int {
		var topPanelsHeight = super.getTopPanelsHeight()
		if (topPanelsHeight <= 0) {
			val headerLegendPanels =
				getLegendAdapter()?.findLegendPanel(LabeledListLegend::class.java)
			var maxHeaderHeight = 0

			headerLegendPanels?.forEach {
				val headerCell =
					getLegendAdapter()?.getLegendPanelViewsHolder(it.id)?.getPanelViews()
						?.firstOrNull()
				val headerCellHeight = headerCell?.measuredHeight ?: 0
				if (headerCellHeight > maxHeaderHeight) maxHeaderHeight = headerCellHeight
			}
			topPanelsHeight += maxHeaderHeight
		}
		return topPanelsHeight
	}


}
