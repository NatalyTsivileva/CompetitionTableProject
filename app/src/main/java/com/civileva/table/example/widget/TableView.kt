package com.civileva.table.example.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import com.civileva.table.example.data.ITableCell
import com.civileva.table.example.presentation.adapter.base.ILegendPanelAdapter
import com.civileva.table.example.presentation.adapter.base.ITableAdapter
import com.civileva.table.example.presentation.legend.base.ILegendPanel

open class TableView<T : Comparable<T>, C : ITableCell<T>>(
	context: Context,
	val attrs: AttributeSet,
) : ViewGroup(context, attrs) {

	var tableAdapter: ITableAdapter<T, C>? = null
		set(value) {
			release()
			field = value
			requestLayout()
			invalidate()
		}


	private var panelsOffsets: ILegendPanelAdapter.PanelsOffsets? = null


	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
		val width = MeasureSpec.getSize(widthMeasureSpec)
		val height = MeasureSpec.getSize(heightMeasureSpec)

		(tableAdapter as? ILegendPanelAdapter)?.let { adapter ->
			if (panelsOffsets == null) {
				val offsets = adapter.measureAllPanels()
				adapter.updateLegendPanels(offsets.measuredPanels)
				panelsOffsets = offsets
			}

			measureLegend(
				adapter,
				panelsOffsets = panelsOffsets,
				measuredParentWidth = width,
				measuredParentHeight = height
			)

		}

		tableAdapter?.let { adapter ->
			measureCell(
				adapter,
				panelsOffsets = panelsOffsets,
				measuredParentWidth = width,
				measuredParentHeight = height
			)
		}
	}


	private fun measureLegend(
		adapter: ILegendPanelAdapter,
		panelsOffsets: ILegendPanelAdapter.PanelsOffsets?,
		measuredParentHeight: Int,
		measuredParentWidth: Int
	) {
		resizePanelsViews(
			adapter,
			contentHeight = getContentAreaHeight(measuredParentHeight, panelsOffsets),
			contentWidth = getContentAreaWidth(measuredParentWidth, panelsOffsets)
		)
	}

	open fun resizePanelsViews(
		adapter: ILegendPanelAdapter,
		contentWidth: Int,
		contentHeight: Int
	) {

		adapter.getLegendPanels().forEach { panel ->
			var widthSpec: Int = MeasureSpec.UNSPECIFIED
			var heightSpec: Int = MeasureSpec.UNSPECIFIED

			when (panel.direction) {
				ILegendPanel.Direction.TOP, ILegendPanel.Direction.BOTTOM -> {
					val newWidth = contentWidth / panel.legend.itemsCount

					widthSpec = MeasureSpec.makeMeasureSpec(newWidth, MeasureSpec.EXACTLY)
					heightSpec = MeasureSpec.makeMeasureSpec(
						MeasureSpec.UNSPECIFIED,
						MeasureSpec.UNSPECIFIED
					)
				}

				ILegendPanel.Direction.LEFT, ILegendPanel.Direction.RIGHT -> {
					val newHeight = contentHeight / panel.legend.itemsCount
					widthSpec = MeasureSpec.makeMeasureSpec(panel.panelSize.width, MeasureSpec.EXACTLY)
					heightSpec = MeasureSpec.makeMeasureSpec(newHeight, MeasureSpec.EXACTLY)
				}
			}

			adapter.getLegendViews(panel.id).forEach { view ->
				view.measure(widthSpec, heightSpec)
			}
		}
	}

	private fun measureCell(
		adapter: ITableAdapter<T, C>,
		panelsOffsets: ILegendPanelAdapter.PanelsOffsets?,
		measuredParentHeight: Int,
		measuredParentWidth: Int
	) {

		val cellHeight =
			getContentAreaHeight(measuredParentHeight, panelsOffsets) / adapter.getTableSize()
		val cellWidth =
			getContentAreaWidth(measuredParentWidth, panelsOffsets) / adapter.getTableSize()

		val matrixSize = adapter.getTableSize() * adapter.getTableSize()

		if (adapter.getTableViews().count() == matrixSize) {
			for (i: Int in 0 until matrixSize) {
				val widthSpec = MeasureSpec.makeMeasureSpec(cellWidth, MeasureSpec.EXACTLY)
				val heightSpec = MeasureSpec.makeMeasureSpec(cellHeight, MeasureSpec.EXACTLY)
				adapter.getTableView(i).measure(widthSpec, heightSpec)
			}
		}
	}


	override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
		layoutLegends(panelsOffsets)
		layoutCells(panelsOffsets)
	}


	private fun layoutLegends(panelsOffsets: ILegendPanelAdapter.PanelsOffsets?) {
		(tableAdapter as? ILegendPanelAdapter)?.let { adapter ->
			layoutTopLegends(panelsOffsets, adapter)
			layoutLeftLegends(panelsOffsets, adapter)
			layoutRightLegends(panelsOffsets, adapter)
		}
	}

	open fun layoutTopLegends(
		panelsOffsets: ILegendPanelAdapter.PanelsOffsets?,
		adapter: ILegendPanelAdapter
	) {
		var startX = getLeftPadding(panelsOffsets)
		var startY = (y + paddingTop).toInt()

		var viewHeight = ILegendPanelAdapter.UNDEFINED_SIZE

		adapter.getLegendPanels().forEach { panel ->

			if (panel.direction == ILegendPanel.Direction.TOP) {
				adapter
					.getLegendViews(panel.id)
					.forEach { view ->
						viewHeight = view.measuredHeight
						view.layout(
							startX,
							startY,
							startX + view.measuredWidth,
							startY + viewHeight
						)
						startX += view.measuredWidth

						if (!view.isAttachedToWindow) addView(view)
					}
			}
			startY += viewHeight
		}
	}

	open fun layoutLeftLegends(
		panelsOffsets: ILegendPanelAdapter.PanelsOffsets?,
		adapter: ILegendPanelAdapter
	) {
		var startX = (x + paddingStart).toInt()
		var startY = getTopPadding(panelsOffsets)

		adapter.getLegendPanels().forEach { panel ->
			if (panel.direction == ILegendPanel.Direction.LEFT) {
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
				startY = getTopPadding(panelsOffsets)
			}
		}
	}

	open fun layoutRightLegends(
		panelsOffsets: ILegendPanelAdapter.PanelsOffsets?,
		adapter: ILegendPanelAdapter
	) {
		var startX = width - getRightPadding(panelsOffsets)
		var startY = (y + paddingTop).toInt()

		var viewWidth = ILegendPanelAdapter.UNDEFINED_SIZE

		adapter.getLegendPanels().forEach { panel ->

			if (panel.direction == ILegendPanel.Direction.RIGHT) {
				adapter
					.getLegendViews(panel.id)
					.forEach { view ->
						if (viewWidth == ILegendPanelAdapter.UNDEFINED_SIZE) viewWidth =
							view.measuredWidth
						view.layout(startX, startY, startX + viewWidth, startY + measuredHeight)
						startY += view.measuredHeight
						if (!view.isAttachedToWindow) addView(view)
					}
				startX += viewWidth
				startY = (y + paddingTop).toInt()
			}

		}
	}

	private fun layoutCells(panelsOffsets: ILegendPanelAdapter.PanelsOffsets?) {
		val adapter = tableAdapter

		if (adapter != null) {
			var startX = getLeftPadding(panelsOffsets)
			var startY = getTopPadding(panelsOffsets)

			val data = adapter.getTableData()
			val viewCount = adapter.getTableViews().count()

			if (data.count() == viewCount) {
				data.forEach { d ->
					val view = adapter.getTableView(d.index)
					val viewHeight = view.measuredHeight
					val viewWidth = view.measuredWidth

					if (d.isNewRow()) {
						startY += viewHeight
						startX = getLeftPadding(panelsOffsets)
					} else {
						if (!d.isFirst())
							startX += viewWidth
					}

					view.layout(startX, startY, startX + viewWidth, startY + viewHeight)

					if (!view.isAttachedToWindow) {
						addView(view)
					}
				}
			}
		}
	}

	fun getLeftPadding(panelsOffsets: ILegendPanelAdapter.PanelsOffsets?) =
		(panelsOffsets?.leftCommonWidth ?: 0) + paddingStart


	fun getRightPadding(panelsOffsets: ILegendPanelAdapter.PanelsOffsets?) =
		(panelsOffsets?.rightCommonWidth ?: 0) + paddingEnd


	fun getTopPadding(panelsOffsets: ILegendPanelAdapter.PanelsOffsets?) =
		(panelsOffsets?.topCommonHeight ?: 0) + paddingTop


	fun getBottomPadding(panelsOffsets: ILegendPanelAdapter.PanelsOffsets?) =
		(panelsOffsets?.bottomCommonHeight ?: 0) + paddingBottom


	fun getContentAreaWidth(
		parentWidth: Int,
		panelsOffsets: ILegendPanelAdapter.PanelsOffsets?
	): Int {
		return parentWidth - getLeftPadding(panelsOffsets) - getRightPadding(panelsOffsets)
	}

	fun getContentAreaHeight(
		parentHeight: Int,
		panelsOffsets: ILegendPanelAdapter.PanelsOffsets?
	): Int {
		return parentHeight - getTopPadding(panelsOffsets) - getBottomPadding(panelsOffsets)
	}

	fun release() {
		removeAllViews()
		tableAdapter?.destroyTableViews()
		(tableAdapter as? ILegendPanelAdapter)?.destroyLegendViews()
		panelsOffsets = null
	}
}