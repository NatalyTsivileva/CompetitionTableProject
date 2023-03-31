package com.civileva.table.example.presentation.adapter.base

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.civileva.table.example.data.ITableCell
import com.civileva.table.example.data.Table
import com.civileva.table.example.presentation.legend.base.ILegendPanel
import kotlin.math.ceil

open class TableLegendAdapter<T : Comparable<T>, C : ITableCell<T>>(
	context: Context,
	attr: AttributeSet,
	private val table: Table<T, C>,
	private var legendsPanels: Pair<List<ILegendPanel>, Map<Int, List<View>>>?
) : TableAdapter<T, C>(context,attr,table,legendsPanels), ILegendPanelAdapter {



	override fun getLegendPanels(): List<ILegendPanel> {
		return legendsPanels?.first ?: emptyList()
	}

	override fun getLegendPanel(panelId: Int): ILegendPanel? {
		return legendsPanels?.first?.find { it.id == panelId }
	}

	override fun getLegendPanels(direction: ILegendPanel.Direction): List<ILegendPanel> {
		return legendsPanels?.first?.filter { it.direction == direction } ?: emptyList()
	}

	override fun getLegendViews(panelId: Int): List<View> {
		return legendsPanels?.second?.get(panelId) ?: emptyList()
	}


	override fun findLegendPanel(legendClass: Class<*>): ILegendPanel? {
		return legendsPanels?.first?.find { it.legend.javaClass == legendClass }
	}

	override fun updateLegendPanelSize(panel: ILegendPanel, size: ILegendPanel.Size) {
		val panels = legendsPanels
		if (panels != null) {
			val panelsData = panels.first.toMutableList()
			val panelsViewsMaps = panels.second

			panelsData.replaceAll { panelData ->
				if (panelData.id == panel.id) {
					measureNewSize(panelsViewsMaps,panelData,size)
				} else {
					panelData
				}
			}

			val h = panelsViewsMaps.get(panel.id)?.map { it.measuredHeight }
			val w = panelsViewsMaps.get(panel.id)?.map { it.measuredWidth }
			Log.d("updateLegendPanelSize", "${panel.direction}${panel.id},WIDTH=${w}, HEIGHT=${h}")
			legendsPanels = Pair(panelsData, panelsViewsMaps)
		}
	}
	private fun measureNewSize(
		views: Map<Int, List<View>>,
		panel: ILegendPanel,
		size: ILegendPanel.Size
	): ILegendPanel {
		return if (size.isUndefined()) {
			val newSize = measureWrapContentSize(views, panel)
			panel.updateSize(newSize)
		} else {
			measureExactlySize(views, panel, size)
			panel
		}
	}


	private fun measureExactlySize(
		views: Map<Int, List<View>>,
		panel: ILegendPanel,
		size: ILegendPanel.Size
	) {
		var height = 0
		var width = 0

		val viewsList = views[panel.id]

		viewsList?.forEach { view ->
			when (panel.direction) {
				ILegendPanel.Direction.TOP, ILegendPanel.Direction.BOTTOM -> {
					width = ceil(size.width.toFloat() / panel.legend.itemsCount).toInt()
					height = panel.panelSize.height
				}

				ILegendPanel.Direction.LEFT, ILegendPanel.Direction.RIGHT -> {
					height = ceil(size.height.toFloat() / panel.legend.itemsCount).toInt()
					width = panel.panelSize.width
				}
			}


			val specHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
			val specWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
			view.measure(specWidth, specHeight)

			Log.d(
				"measureExactlySize",
				"Вьюха высота =${view.measuredHeight},Ширина=${view.measuredWidth}"
			)
		}
	}


	private fun measureWrapContentSize(
		views: Map<Int, List<View>>,
		panel: ILegendPanel
	): ILegendPanel.Size {
		val viewsList = views[panel.id]
		viewsList?.forEach { view ->
			val specHeight =
				View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
			val specWidth =
				View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
			view.measure(specWidth, specHeight)
		}
		return getMeasuredPanelSize(panel)
	}

	private fun getMeasuredPanelSize(panel: ILegendPanel): ILegendPanel.Size {
		var panelWidth = ILegendPanelAdapter.UNDEFINED_SIZE
		var panelHeight = ILegendPanelAdapter.UNDEFINED_SIZE

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


	override fun destroyLegendViews() {
		legendsPanels = null
	}
}