package com.civileva.table.example.presentation.implementations.adapter

import android.util.Log
import android.view.View
import com.civileva.table.example.data.ITableCell
import com.civileva.table.example.data.Table
import com.civileva.table.example.presentation.base.adapter.ILegendPanelAdapter
import com.civileva.table.example.presentation.base.holders.ILegendTableViewHolder
import com.civileva.table.example.presentation.base.holders.ITableViewHolder
import com.civileva.table.example.presentation.base.legends.ILegendPanel
import com.civileva.table.example.presentation.base.listener.ITableClickListener
import kotlin.math.ceil

open class LegendTableAdapter<T : Comparable<T>, C : ITableCell<T>>(
	private val table: Table<T, C>,
	cellListener: ITableClickListener<T, C>,
	cellHolders: List<ITableViewHolder<T, C>>,
	legendHolderMap: Map<ILegendPanel, ILegendTableViewHolder>,
) : TableAdapter<T, C>(table, cellHolders, cellListener), ILegendPanelAdapter {

	private var panelIdHolders: Map<Int, ILegendTableViewHolder> = legendHolderMap
		.map {
			it.key.id to it.value
		}.toMap()

	private var panels: MutableList<ILegendPanel> = legendHolderMap.keys.toMutableList()

	init {
		legendHolderMap.forEach { (panel, holder) ->
			bindLegendPanel(holder, panel)
		}
	}


	protected fun bindLegendPanel(holder: ILegendTableViewHolder, panel: ILegendPanel) {
		holder.bindPanelData(panel.legend.data)

		holder.getPanelViews().forEach {
			measureWrapContentSize(it)
		}

		val newSize = getMeasuredPanelSize(panel)

		updatePanelData(panel) {
			panel.updateSize(newSize)
		}

		getLegendPanels().forEach {
			Log.d(
				"SIZE",
				"NEW SIZE ${it.direction}${it.id} width=${it.panelSize.width}, height=${it.panelSize.height}"
			)
		}
	}



	override fun updateLegendPanel(panelId: Int, data: List<*>) {
		val holder = getLegendPanelViewsHolder(panelId)
		val panel = getLegendPanel(panelId)
		val newLegend = panel?.legend?.updateData(data)

		if (panel != null && newLegend != null && holder!=null) {
			updatePanelData(panel) {
				panel.updateLegend(newLegend)
			}
			val updatedData = getLegendPanel(panelId)
			if (updatedData != null) {
				bindLegendPanel(holder,updatedData)
			}
		}
	}

	override fun updateLegendPanel(panelId: Int, viewIndex: Int, data: Any) {
		val panel = getLegendPanel(panelId)
		if(panel!=null) {
			val dataList = panel.legend.data
			val viewListCount = getLegendPanelViewsHolder(panelId)?.getPanelViews()?.count()?:0
			if (viewListCount > viewIndex && dataList.count()>viewIndex) {
				val mutableData = dataList.toMutableList()
				mutableData[viewIndex] = data
				updateLegendPanel(panelId, mutableData)
			}else{
				Log.e("updateLegendPanel","Cant update legend panel ${panel.direction}[${panel.id}] viewListCount=$viewListCount, viewIndex=$viewIndex")
			}
		}
	}


	override fun measureLegendPanelSize(panel: ILegendPanel, size: ILegendPanel.Size) {
		val holder = getLegendPanelViewsHolder(panel.id)

		holder?.getPanelViews()?.map { view ->
			measureExactlySize(view, panel, size)
		}

		val newSize = getMeasuredPanelSize(panel)

		updatePanelData(panel) {
			panel.updateSize(newSize)
		}
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


	private fun updatePanelData(panel: ILegendPanel, newPanel: () -> ILegendPanel) {
		val panelsCopy = mutableListOf<ILegendPanel>()
		for (i: Int in 0 until panels.count()) {
			val p = if (panels[i].id == panel.id) {
				newPanel()
			} else {
				panels[i]
			}
			panelsCopy.add(p)
		}

		panels = panelsCopy
	}

	override fun getLegendPanels(): List<ILegendPanel> {
		return panels
	}

	override fun getLegendPanels(direction: ILegendPanel.Direction): List<ILegendPanel> {
		return getLegendPanels().filter { it.direction == direction }
	}
	override fun getLegendPanel(panelId: Int): ILegendPanel? {
		return getLegendPanels().find { it.id == panelId }
	}

	override fun getLegendPanelViewsHolder(panelId: Int): ILegendTableViewHolder? {
		return panelIdHolders[panelId]
	}

	override fun findLegendPanel(legendClass: Class<*>): List<ILegendPanel> {
		return getLegendPanels().filter { it.legend.javaClass == legendClass }
	}
}