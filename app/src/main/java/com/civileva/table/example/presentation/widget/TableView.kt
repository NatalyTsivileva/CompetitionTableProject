package com.civileva.table.example.presentation.widget

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ViewGroup
import com.civileva.table.example.data.ITableCell
import com.civileva.table.example.presentation.base.adapter.ITableAdapter
import kotlin.math.ceil

open class TableView<T : Comparable<T>, C : ITableCell<T>>(
	context: Context,
	val attrs: AttributeSet,
) : ViewGroup(context, attrs) {

	val p = Paint().apply {
		color = Color.BLACK
		strokeWidth = 5f
	}

	private var tableAdapter: ITableAdapter<T, C>? = null

	fun setTableAdapter(adapter: ITableAdapter<T, C>){
		tableAdapter = adapter
		requestLayout()
		invalidate()
	}

	fun getTableAdapter() = tableAdapter

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
		val width = MeasureSpec.getSize(widthMeasureSpec)
		val height = MeasureSpec.getSize(heightMeasureSpec)


		getTableAdapter()?.let { adapter ->
			measureCell(
				adapter,
				measuredParentWidth = width,
				measuredParentHeight = height
			)
		}
	}


	open fun measureCell(
		adapter: ITableAdapter<T, C>,
		measuredParentHeight: Int,
		measuredParentWidth: Int
	) {

		val cellHeight = ceil(measuredParentHeight.toFloat() / adapter.getTableSize())
		val cellWidth = ceil(measuredParentWidth.toFloat() / adapter.getTableSize())


		adapter.getTableData().forEach {
			val widthSpec = MeasureSpec.makeMeasureSpec(cellWidth.toInt(), MeasureSpec.EXACTLY)
			val heightSpec = MeasureSpec.makeMeasureSpec(cellHeight.toInt(), MeasureSpec.EXACTLY)
			val holder = adapter.getTableHolder(it)
			val view = holder?.getTableView(it)
			view?.measure(widthSpec, heightSpec)
		}
	}


	override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
		layoutCells(p1, p2)

	}


	private fun layoutCells(x: Int, y: Int) {
		val adapter = getTableAdapter()

		if (adapter != null) {
			var startX = x
			var startY = y

			adapter.getTableData().forEach { c ->
				val view = adapter.getTableHolder(c)?.getTableView(c)
				val viewHeight = view?.measuredHeight ?: 0
				val viewWidth = view?.measuredWidth ?: 0

				if (c.isNewRow()) {
					startY += viewHeight
					startX = x
				} else {
					if (!c.isFirst())
						startX += viewWidth
				}

				view?.layout(startX, startY, startX + viewWidth, startY + viewHeight)

				if (view?.isAttachedToWindow == false) {
					addView(view)
				}
			}

		}
	}
}