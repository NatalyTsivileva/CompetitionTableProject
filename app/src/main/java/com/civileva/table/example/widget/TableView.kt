package com.civileva.table.example.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.civileva.table.example.data.ITableCell
import com.civileva.table.example.presentation.adapter.base.ILegendAdapter
import com.civileva.table.example.presentation.adapter.base.ITableAdapter
import com.civileva.table.example.presentation.legend.ILegendPanel

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


	private var panelsParams = ILegendAdapter.PanelsParams()


	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
		val width = MeasureSpec.getSize(widthMeasureSpec)
		val height = MeasureSpec.getSize(heightMeasureSpec)


		(tableAdapter as? ILegendAdapter)?.let {
			measureLegend(it, parentWidth = width, parentHeight = height)
		}

		tableAdapter?.let {
			measureCell(it, parentWidth = width, parentHeight = height)
		}
	}


	private fun measureLegend(
		adapter: ILegendAdapter,
		parentHeight: Int,
		parentWidth: Int
	) {
		panelsParams = adapter.getLegendPanelParams()

		resizePanelsViews(
			adapter,
			contentHeight = getContentAreaHeight(parentHeight, panelsParams),
			contentWidth = getContentAreaWidth(parentWidth, panelsParams)
		)
	}

	open fun resizePanelsViews(
		adapter: ILegendAdapter,
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

					widthSpec = MeasureSpec.makeMeasureSpec(
						MeasureSpec.UNSPECIFIED,
						MeasureSpec.UNSPECIFIED
					)
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
		parentHeight: Int,
		parentWidth: Int
	) {

		val cellHeight = getContentAreaHeight(parentHeight, panelsParams) / adapter.getTableSize()
		val cellWidth = getContentAreaWidth(parentWidth, panelsParams) / adapter.getTableSize()

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
		layoutLegends()
		layoutCells()
	}


	private fun layoutLegends() {
		(tableAdapter as? ILegendAdapter)?.let { adapter ->
			layoutTopLegends(panelsParams, adapter)
			layoutLeftLegends(panelsParams, adapter)
			layoutRightLegends(width, panelsParams, adapter)
		}
	}

	open fun layoutTopLegends(params: ILegendAdapter.PanelsParams, adapter: ILegendAdapter) {
		var startX = getLeftPadding(params)
		var startY = (y + paddingTop).toInt()

		var viewHeight = ILegendAdapter.UNDEFINED_SIZE

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

	open fun layoutLeftLegends(params: ILegendAdapter.PanelsParams, adapter: ILegendAdapter) {
		var startX = (x + paddingStart).toInt()
		var startY = getTopPadding(params)

		var viewWidth = ILegendAdapter.UNDEFINED_SIZE

		adapter.getLegendPanels().forEach { panel ->

			if (panel.direction == ILegendPanel.Direction.LEFT) {
				adapter
					.getLegendViews(panel.id)
					.forEach { view ->
						if (viewWidth == ILegendAdapter.UNDEFINED_SIZE) viewWidth =
							view.measuredWidth
						view.layout(startX, startY, startX + viewWidth, startY + measuredHeight)
						startY += view.measuredHeight
						if (!view.isAttachedToWindow) addView(view)
					}
				startX += viewWidth
				startY = params.topPanelsHeight + paddingTop
			}
		}
	}

	open fun layoutRightLegends(
		parentWidth: Int,
		params: ILegendAdapter.PanelsParams,
		adapter: ILegendAdapter
	) {
		var startX = parentWidth - getRightPadding(params)
		var startY = (y + paddingTop).toInt()

		var viewWidth = ILegendAdapter.UNDEFINED_SIZE

		adapter.getLegendPanels().forEach { panel ->

			if (panel.direction == ILegendPanel.Direction.RIGHT) {
				adapter
					.getLegendViews(panel.id)
					.forEach { view ->
						if (viewWidth == ILegendAdapter.UNDEFINED_SIZE) viewWidth =
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

	private fun layoutCells() {
		val adapter = tableAdapter

		if (adapter != null) {
			var startX = (x + paddingStart + panelsParams.leftPanelsWidth).toInt()
			var startY = (y + paddingTop + panelsParams.topPanelsHeight).toInt()

			val data = adapter.getTableData()
			val viewCount = adapter.getTableViews().count()

			if (data.count() == viewCount) {
				data.forEach { d ->
					val view = adapter.getTableView(d.index)
					val viewHeight = view.measuredHeight
					val viewWidth = view.measuredWidth

					if (d.isNewRow()) {
						startY += viewHeight
						startX = (x + paddingStart + panelsParams.leftPanelsWidth).toInt()
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

	fun getLeftPadding(params: ILegendAdapter.PanelsParams) =
		params.leftPanelsWidth + paddingStart


	fun getRightPadding(params: ILegendAdapter.PanelsParams) =
		params.rightPanelsWidth + paddingEnd


	fun getTopPadding(params: ILegendAdapter.PanelsParams) =
		params.topPanelsHeight + paddingTop


	fun getBottomPadding(params: ILegendAdapter.PanelsParams) =
		params.bottomPanelsHeight + paddingBottom


	fun getContentAreaWidth(parentWidth: Int, params: ILegendAdapter.PanelsParams): Int {
		return parentWidth - getLeftPadding(params) - getRightPadding(params)
	}

	fun getContentAreaHeight(parentHeight: Int, params: ILegendAdapter.PanelsParams): Int {
		return parentHeight - getTopPadding(params) - getBottomPadding(params)
	}

	fun release() {
		removeAllViews()
		tableAdapter?.destroyTableViews()
		(tableAdapter as? ILegendAdapter)?.destroyLegendViews()
	}
}