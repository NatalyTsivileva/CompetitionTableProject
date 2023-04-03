package com.civileva.table.example.presentation.implementations.adapter

import android.util.Log
import android.view.View
import com.civileva.table.example.data.ITableCell
import com.civileva.table.example.data.Table
import com.civileva.table.example.presentation.base.adapter.ILegendPanelAdapter
import com.civileva.table.example.presentation.base.holders.ILegendTableViewHolder
import com.civileva.table.example.presentation.base.holders.ITableViewHolder
import com.civileva.table.example.presentation.base.legends.ILegendPanel
import kotlin.math.ceil

open class LegendTableAdapter<T : Comparable<T>, C : ITableCell<T>>(
	private val table: Table<T, C>,
	cellHolders:List<ITableViewHolder<T,C>>,
	legendHolderMap: Map<ILegendPanel, ILegendTableViewHolder>,
) : TableAdapter<T, C>(table,cellHolders), ILegendPanelAdapter {

	private var legendHoldersMap: MutableMap<ILegendPanel, ILegendTableViewHolder> = legendHolderMap.toMutableMap()

	init {
		legendHolderMap.forEach { (panel, holder) ->
			bindLegendPanel(holder, panel)
		}

	}


	override fun getLegendPanels(): List<ILegendPanel> {
		return legendHoldersMap.toList().map { it.first }
	}

	override fun getLegendPanels(direction: ILegendPanel.Direction): List<ILegendPanel> {
		return getLegendPanels().filter { it.direction == direction }
	}

	private fun bindLegendPanel(holder: ILegendTableViewHolder, panel: ILegendPanel) {
		holder.bindPanelData(panel.legend.data)

		holder.getPanelViews().forEach {
			measureWrapContentSize(it)
		}
		val newSize = getMeasuredPanelSize(panel)
		updateHolderMap(panel, newSize)

		getLegendPanels().forEach {
			Log.d(
				"SIZE",
				"NEW SIZE ${it.direction}${it.id} width=${it.panelSize.width}, height=${it.panelSize.height}"
			)
		}
	}

	override fun getLegendPanel(panelId: Int): ILegendPanel? {
		return legendHoldersMap.keys.find { it.id == panelId }
	}

	override fun getLegendPanelViewsHolder(panelId: Int): ILegendTableViewHolder? {
		return legendHoldersMap.filter { it.key.id == panelId }.values.firstOrNull()
	}


	override fun findLegendPanel(legendClass: Class<*>): List<ILegendPanel>{
		return getLegendPanels().filter { it.legend.javaClass == legendClass }
	}

	override fun measureLegendPanelSize(panel: ILegendPanel, size: ILegendPanel.Size) {
		val holder = getLegendPanelViewsHolder(panel.id)

		holder?.getPanelViews()?.map { view ->
			measureExactlySize(view, panel, size)
		}

		val newSize = getMeasuredPanelSize(panel)
		updateHolderMap(panel, newSize)
	}


	protected fun measureExactlySize(
		view: View,
		panel: ILegendPanel,
		size: ILegendPanel.Size
	) {
		var height = 0
		var width = 0

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


	protected fun measureWrapContentSize(
		view: View
	): ILegendPanel.Size {
		val specHeight = View.MeasureSpec.makeMeasureSpec(
			View.MeasureSpec.UNSPECIFIED,
			View.MeasureSpec.UNSPECIFIED
		)
		val specWidth = View.MeasureSpec.makeMeasureSpec(
			View.MeasureSpec.UNSPECIFIED,
			View.MeasureSpec.UNSPECIFIED
		)
		view.measure(specWidth, specHeight)

		Log.d(
			"measureWrapContentSize",
			"Вьюха высота =${view.measuredHeight},Ширина=${view.measuredWidth}"
		)
		return ILegendPanel.Size(width = view.measuredWidth, height = view.measuredHeight)
	}

	protected fun getMeasuredPanelSize(panel: ILegendPanel): ILegendPanel.Size {
		var panelWidth = ILegendPanel.Size.UNDEFINED_SIZE
		var panelHeight = ILegendPanel.Size.UNDEFINED_SIZE

		val views = getLegendPanelViewsHolder(panel.id)?.getPanelViews()

		views?.forEachIndexed { index, view ->
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
		Log.d(
			"getMeasuredPanelSize",
			"${panel}${panel.id} h=${panelHeight}, w=${panelWidth}"
		)
		return ILegendPanel.Size(width = panelWidth, height = panelHeight)
	}

	protected fun updateHolderMap(panel: ILegendPanel, size: ILegendPanel.Size) {
		val newMap = mutableMapOf<ILegendPanel, ILegendTableViewHolder>()
		legendHoldersMap.forEach {
			val updatedPanel = if (it.key == panel) it.key.updateSize(size) else it.key
			val oldHolder = getLegendPanelViewsHolder(it.key.id)
			if (oldHolder != null)
				newMap[updatedPanel] = oldHolder

		}
		legendHoldersMap = newMap
	}

}