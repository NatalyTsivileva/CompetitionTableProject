package com.civileva.table.example.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import com.civileva.table.example.data.ITableCell
import com.civileva.table.example.presentation.adapter.base.ILegendPanelAdapter
import com.civileva.table.example.presentation.adapter.base.ITableAdapter
import com.civileva.table.example.presentation.legend.base.ILegendPanel
import kotlin.math.ceil

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

	private var borders: ILegendPanelAdapter.MeasuredPanelsBorders? = null

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
		val width = MeasureSpec.getSize(widthMeasureSpec)
		val height = MeasureSpec.getSize(heightMeasureSpec)

		(tableAdapter as? ILegendPanelAdapter)?.let { adapter ->
			measureLegend(adapter,
				measuredParentWidth = width,
				measuredParentHeight = height
			)
		}

		tableAdapter?.let { adapter ->
			measureCell(
				adapter,
				measuredParentWidth = width,
				measuredParentHeight = height
			)
		}
	}


	private fun measureLegend(
		adapter: ILegendPanelAdapter,
		measuredParentHeight: Int,
		measuredParentWidth: Int
	) {

		borders = adapter.measureLegendPanels()
		 resizePanelsViews(
			adapter,
			availableHeight = getContentAreaHeight(measuredParentHeight),
			availableWidth = getContentAreaWidth(measuredParentWidth)
		)
	}

	open fun resizePanelsViews(
		adapter: ILegendPanelAdapter,
		availableWidth: Int,
		availableHeight: Int
	) {

		adapter.getLegendPanels().forEach { panel ->
			var widthSpec: Int = MeasureSpec.UNSPECIFIED
			var heightSpec: Int = MeasureSpec.UNSPECIFIED

			when (panel.direction) {
				ILegendPanel.Direction.TOP, ILegendPanel.Direction.BOTTOM -> {
					val newWidth = ceil(availableWidth.toFloat() / panel.legend.itemsCount)

					widthSpec = MeasureSpec.makeMeasureSpec(newWidth.toInt(), MeasureSpec.EXACTLY)
					heightSpec = MeasureSpec.makeMeasureSpec(panel.panelSize.height, MeasureSpec.EXACTLY)
				}

				ILegendPanel.Direction.LEFT, ILegendPanel.Direction.RIGHT -> {
					val newHeight = ceil(availableHeight.toFloat() / panel.legend.itemsCount)

					widthSpec = MeasureSpec.makeMeasureSpec(panel.panelSize.width, MeasureSpec.EXACTLY)
					heightSpec = MeasureSpec.makeMeasureSpec(newHeight.toInt(), MeasureSpec.EXACTLY)
				}
			}

			adapter.getLegendViews(panel.id).forEach { view ->
				view.measure(widthSpec, heightSpec)
			}
		}
	}

	private fun measureCell(
		adapter: ITableAdapter<T, C>,
		measuredParentHeight: Int,
		measuredParentWidth: Int
	) {

		val cellHeight = ceil(
			getContentAreaHeight(
				measuredParentHeight,
			).toFloat() / adapter.getTableSize()
		)
		val cellWidth = ceil(
			getContentAreaWidth(
				measuredParentWidth
			).toFloat() / adapter.getTableSize()
		)

		val matrixSize = adapter.getTableSize() * adapter.getTableSize()

		if (adapter.getTableViews().count() == matrixSize) {
			for (i: Int in 0 until matrixSize) {
				val widthSpec = MeasureSpec.makeMeasureSpec(cellWidth.toInt(), MeasureSpec.EXACTLY)
				val heightSpec =
					MeasureSpec.makeMeasureSpec(cellHeight.toInt(), MeasureSpec.EXACTLY)
				adapter.getTableView(i).measure(widthSpec, heightSpec)
			}
		}
	}


	override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
		layoutLegends()
		layoutCells()
	}


	private fun layoutLegends() {
		(tableAdapter as? ILegendPanelAdapter)?.let { adapter ->
			layoutTopLegends(adapter)
			layoutLeftLegends(adapter)
			layoutRightLegends(adapter)
			layoutBottomLegends(adapter)
		}
	}

	open fun layoutTopLegends(
		adapter: ILegendPanelAdapter
	) {
		var startX = getLeftOffset()
		var startY = paddingTop


		adapter.getLegendPanels().forEach { panel ->
			if (panel.direction == ILegendPanel.Direction.TOP) {
				val panelHeight = panel.panelSize.height

				Log.d("layoutTopLegends", "panelHeight ${panelHeight}")

				adapter
					.getLegendViews(panel.id)
					.forEach { view ->
						view.layout(
							startX,
							startY,
							startX + view.measuredWidth,
							startY + panelHeight
						)
						startX += view.measuredWidth
						if (!view.isAttachedToWindow) addView(view)
					}
				startY += panelHeight
				startX = getLeftOffset()
			}
		}
	}

	open fun layoutLeftLegends(
		adapter: ILegendPanelAdapter
	) {
		var startX = paddingStart
		var startY = getTopOffset()

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
				startY = getTopOffset()
			}
		}
	}

	protected open fun layoutRightLegends(
		adapter: ILegendPanelAdapter
	) {
		var startX = measuredWidth - getRightOffset()
		var startY = getTopOffset()

		adapter.getLegendPanels().forEach { panel ->
			if (panel.direction == ILegendPanel.Direction.RIGHT) {
				val panelWidth = panel.panelSize.width

				adapter
					.getLegendViews(panel.id)
					.forEach { view ->
						view.layout(startX, startY, startX + panelWidth, startY + measuredHeight)
						startY += view.measuredHeight
						if (!view.isAttachedToWindow) addView(view)
					}
				startX += panelWidth
				startY = getTopOffset()
			}
		}
	}

	protected open fun layoutBottomLegends(
		adapter: ILegendPanelAdapter
	) {
		var startX = getLeftOffset()
		var startY = measuredHeight - getBottomOffset()

		adapter.getLegendPanels().forEach { panel ->
			if (panel.direction == ILegendPanel.Direction.BOTTOM) {
				val panelHeight = panel.panelSize.height

				adapter
					.getLegendViews(panel.id)
					.forEach { view ->
						view.layout(startX, startY, startX + view.measuredWidth, startY + panelHeight)
						startX += view.measuredWidth
						if (!view.isAttachedToWindow) addView(view)
					}
				startX += panelHeight
				startY = measuredHeight - getBottomOffset()
			}

		}
	}
	private fun layoutCells() {
		val adapter = tableAdapter

		if (adapter != null) {
			var startX = getLeftOffset()
			var startY = getTopOffset()

			val data = adapter.getTableData()
			val viewCount = adapter.getTableViews().count()

			if (data.count() == viewCount) {
				data.forEach { d ->
					val view = adapter.getTableView(d.index)
					val viewHeight = view.measuredHeight
					val viewWidth = view.measuredWidth

					if (d.isNewRow()) {
						startY += viewHeight
						startX = getLeftOffset()
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

	open fun getLeftOffset() = (borders?.leftPanelsWidth?: 0) + paddingStart
	open fun getRightOffset() = (borders?.rightPanelsWidth?: 0) + paddingEnd
	open fun getTopOffset() = (borders?.topPanelsHeight?:0) + paddingTop
	open fun getBottomOffset() = (borders?.bottomPanelsHeight?: 0) + paddingBottom


	private fun getContentAreaWidth(
		parentWidth: Int
	): Int {
		return parentWidth - getLeftOffset() - getRightOffset()
	}

	private fun getContentAreaHeight(
		parentHeight: Int
	): Int {
		return parentHeight - getTopOffset() - getBottomOffset()
	}

	fun release() {
		removeAllViews()
		tableAdapter?.destroyTableViews()
		(tableAdapter as? ILegendPanelAdapter)?.destroyLegendViews()
	}
	fun getPanelBorders() = borders
}