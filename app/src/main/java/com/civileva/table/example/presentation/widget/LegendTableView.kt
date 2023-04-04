package com.civileva.table.example.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.civileva.table.example.data.ITableCell
import com.civileva.table.example.presentation.base.adapter.ILegendPanelAdapter
import com.civileva.table.example.presentation.base.adapter.ITableAdapter
import com.civileva.table.example.presentation.base.legends.ILegendPanel

open class LegendTableView<T : Comparable<T>, C : ITableCell<T>>(
	context: Context,
	attrs: AttributeSet,
) : TableView<T, C>(context, attrs) {

	private var legendAdapter: ILegendPanelAdapter? = null

	fun setLegendAdapter(adapter: ILegendPanelAdapter) {
		legendAdapter = adapter
		requestLayout()
		invalidate()
	}

	fun getLegendAdapter() = legendAdapter

	fun setCellAdapter(adapter: ITableAdapter<T, C>) {
		super.setTableAdapter(adapter)
	}

	fun getCellAdapter() = super.getTableAdapter()

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
		val width = MeasureSpec.getSize(widthMeasureSpec)
		val height = MeasureSpec.getSize(heightMeasureSpec)
		getLegendAdapter()?.let { adapter ->
			measureLegend(
				adapter,
				measuredParentWidth = width,
				measuredParentHeight = height
			)
		}
	}

	override fun measureCell(
		adapter: ITableAdapter<T, C>,
		measuredParentHeight: Int,
		measuredParentWidth: Int
	) {
		val width = getContentAreaWidth(measuredParentWidth)
		val height = getContentAreaHeight(measuredParentHeight)
		super.measureCell(adapter, height, width)
	}

	open fun measureLegend(
		adapter: ILegendPanelAdapter,
		measuredParentHeight: Int,
		measuredParentWidth: Int
	) {
		adapter.getLegendPanels().forEach { panel ->
			val panelSize = resizePanelsViews(
				panel,
				availableHeight = getContentAreaHeight(measuredParentHeight),
				availableWidth = getContentAreaWidth(measuredParentWidth)
			)
			adapter.measureLegendPanelSize(panel, panelSize)
		}

	}

	open fun resizePanelsViews(
		panel: ILegendPanel,
		availableHeight: Int,
		availableWidth: Int
	): ILegendPanel.Size {
		return when (panel.direction) {
			ILegendPanel.Direction.TOP, ILegendPanel.Direction.BOTTOM -> {
				ILegendPanel.Size(width = availableWidth, height = panel.panelSize.height)
			}

			ILegendPanel.Direction.LEFT, ILegendPanel.Direction.RIGHT -> {
				ILegendPanel.Size(height = availableHeight, width = panel.panelSize.width)
			}
		}
	}


	override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
		super.onLayout(p0, getLeftPanelsWidth(), getTopPanelsHeight(), p3, p4)
		layoutLegends()
	}


	private fun layoutLegends() {
		getLegendAdapter()?.let { adapter ->
			layoutTopLegends(adapter)
			layoutLeftLegends(adapter)
			layoutRightLegends(adapter)
			layoutBottomLegends(adapter)
		}
	}

	open fun layoutTopLegends(
		adapter: ILegendPanelAdapter
	) {
		var startX = getLeftPanelsWidth()
		var startY = paddingTop


		adapter.getLegendPanels().forEach { panel ->
			if (panel.direction == ILegendPanel.Direction.TOP) {
				val panelHeight = panel.panelSize.height


				adapter
					.getLegendPanelViewsHolder(panel.id)
					?.getPanelViews()
					?.forEach { view ->
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
				startX = getLeftPanelsWidth()
			}
		}
	}


	open fun layoutLeftLegends(
		adapter: ILegendPanelAdapter
	) {
		var startX = paddingStart
		var startY = getTopPanelsHeight()

		adapter.getLegendPanels().forEach { panel ->
			if (panel.direction == ILegendPanel.Direction.LEFT) {
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
				startY = getTopPanelsHeight()
			}
		}
	}

	protected open fun layoutRightLegends(
		adapter: ILegendPanelAdapter
	) {
		var startX = measuredWidth - getRightPanelsWidth()
		var startY = getTopPanelsHeight()

		adapter.getLegendPanels().forEach { panel ->
			if (panel.direction == ILegendPanel.Direction.RIGHT) {
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
				startY = getTopPanelsHeight()
			}
		}
	}

	protected open fun layoutBottomLegends(
		adapter: ILegendPanelAdapter
	) {
		var startX = getLeftPanelsWidth()
		var startY = measuredHeight - getBottomPanelsHeight()

		adapter.getLegendPanels().forEach { panel ->
			if (panel.direction == ILegendPanel.Direction.BOTTOM) {
				val panelHeight = panel.panelSize.height

				adapter
					.getLegendPanelViewsHolder(panel.id)
					?.getPanelViews()
					?.forEach { view ->
						view.layout(
							startX,
							startY,
							startX + view.measuredWidth,
							startY + panelHeight
						)
						startX += view.measuredWidth
						if (!view.isAttachedToWindow) addView(view)

					}
				startX = getLeftPanelsWidth()
				startY += panelHeight
			}

		}
	}

	open fun getLeftPanelsWidth() = getLegendAdapter()?.getLegendPanels(
		ILegendPanel.Direction.LEFT
	)?.sumOf { it.panelSize.width } ?: 0

	open fun getRightPanelsWidth() = getLegendAdapter()?.getLegendPanels(
		ILegendPanel.Direction.RIGHT
	)?.sumOf { it.panelSize.width } ?: 0

	open fun getTopPanelsHeight() = getLegendAdapter()?.getLegendPanels(
		ILegendPanel.Direction.TOP
	)?.sumOf { it.panelSize.height } ?: 0

	open fun getBottomPanelsHeight() = getLegendAdapter()?.getLegendPanels(
		ILegendPanel.Direction.BOTTOM
	)?.sumOf { it.panelSize.height } ?: 0

	open fun getTopPanelItemCount() = getLegendAdapter()?.getLegendPanels(
		ILegendPanel.Direction.TOP
	)?.count()

	private fun getContentAreaWidth(
		parentWidth: Int
	): Int {
		return parentWidth - getLeftPanelsWidth() - getRightPanelsWidth()
	}

	private fun getContentAreaHeight(
		parentHeight: Int
	): Int {
		return parentHeight - getTopPanelsHeight() - getBottomPanelsHeight()
	}

	override fun clear() {
		super.clear()
		legendAdapter?.releaseLegendPanelViewHolders()
		removeAllViews()
	}

}
