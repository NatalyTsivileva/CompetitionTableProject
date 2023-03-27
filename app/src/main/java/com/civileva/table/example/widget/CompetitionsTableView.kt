package com.civileva.table.example.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.civileva.table.example.data.ICompetitionTableCell
import com.civileva.table.example.presentation.ITableAdapter

open class CompetitionsTableView(
	context: Context,
	val attrs: AttributeSet,
) : ViewGroup(context, attrs) {

	var tableAdapter: ITableAdapter<ICompetitionTableCell>? = null
		set(value) {
			field = value
			requestLayout()
			invalidate()
	}


	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)

		val adapter = tableAdapter
		if (adapter != null) {
			val width = MeasureSpec.getSize(widthMeasureSpec)
			val height = MeasureSpec.getSize(heightMeasureSpec)

			val cellHeight = (height - paddingTop - paddingBottom) / adapter.getTableSize()
			val cellWidth = (width - paddingStart - paddingEnd) / adapter.getTableSize()

			val matrixSize= adapter.getTableSize() * adapter.getTableSize()

			for (i: Int in 0 until matrixSize) {
				val widthSpec = MeasureSpec.makeMeasureSpec(cellWidth, MeasureSpec.EXACTLY)
				val heightSpec = MeasureSpec.makeMeasureSpec(cellHeight, MeasureSpec.EXACTLY)
				adapter.getView(i).measure(widthSpec, heightSpec)
			}
		}
	}


	override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
		val adapter = tableAdapter

		if (adapter != null) {
			var startX = (x + paddingStart).toInt()
			var startY = (y + paddingTop).toInt()

			val data = adapter.getData()

			data.forEach { d ->
				val view = adapter.getView(d.index)
				val viewHeight = view.measuredHeight
				val viewWidth = view.measuredWidth

				if (d.isNewRow()) {
					startY += viewHeight
					startX = (x + paddingStart).toInt()
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