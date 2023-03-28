package com.civileva.table.example.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import com.civileva.table.example.data.ITableCell
import com.civileva.table.example.presentation.ITableAdapter
import com.civileva.table.example.presentation.dashboard.ILegendPanel

open class CompetitionsTableView<T : ITableCell>(
	context: Context,
	val attrs: AttributeSet,
) : ViewGroup(context, attrs) {

	protected data class ViewSize(
		val width: Int,
		val height: Int
	)

	var tableAdapter: ITableAdapter<T>? = null
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
			measureCell(adapter, parentWidth = width, parentHeight = height)
			measureLegend(adapter, parentWidth = width, parentHeight = height)
		}
	}


	private fun measureCell(
		adapter: ITableAdapter<T>,
		parentHeight: Int,
		parentWidth: Int
	) {
		val rowCount = adapter.getTableSize() + 1
		val columnCount = adapter.getTableSize() + 4

		val cellHeight = (parentHeight - paddingTop - paddingBottom) / rowCount
		val cellWidth = (parentWidth - paddingStart - paddingEnd) / columnCount

		val matrixSize = adapter.getTableSize() * adapter.getTableSize()

		for (i: Int in 0 until matrixSize) {
			val widthSpec = MeasureSpec.makeMeasureSpec(cellWidth, MeasureSpec.EXACTLY)
			val heightSpec = MeasureSpec.makeMeasureSpec(cellHeight, MeasureSpec.EXACTLY)
			adapter.getTableView(i).measure(widthSpec, heightSpec)
		}
	}

	private fun measureLegend(
		adapter: ITableAdapter<T>,
		parentHeight: Int,
		parentWidth: Int
	) {
		val panel = adapter.getLegendPanels()

		panel.forEach { p ->

			var panelSize = 0
			p.legends.forEach { l ->
				val views = tableAdapter?.getLegendViews(l.id)

				views?.map { view ->
					view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)

					panelSize += when (p.direction) {
						ILegendPanel.Direction.TOP, ILegendPanel.Direction.BOTTOM -> view.measuredHeight
						ILegendPanel.Direction.LEFT, ILegendPanel.Direction.RIGHT -> view.measuredWidth
					}

					view
				}/*?.map { view ->
						when (p.direction) {
							ILegendPanel.Direction.TOP, ILegendPanel.Direction.BOTTOM -> {
								if (panelSize < parentHeight) {
									val allWidth = parentWidth - paddingStart - paddingEnd
									val width = allWidth / views.count()
									Log.d("ПАНЕЛЬ","НОВАЯ ШИРИНА = $width")

									view.measure(width, MeasureSpec.UNSPECIFIED)
								}
							}

							ILegendPanel.Direction.LEFT, ILegendPanel.Direction.RIGHT -> {
								if (panelSize < parentHeight) {
									val allHeight = parentHeight - paddingTop - paddingBottom
									val height = allHeight / views.count()

									Log.d("ПАНЕЛЬ","НОВАЯ ВЫСОТА = $height")
									view.measure(MeasureSpec.UNSPECIFIED, height)
								}
							}
						}
						Log.d("ПАНЕЛЬ","размер ячейки ПОСЛЕ растягивания = h:${view.measuredHeight},w:${measuredHeight}")
					}
			}*/

			}

			Log.d(
				"ПАНЕЛЬ",
				"размер панели ${p.direction.name} = $panelSize, размер родителя: h=${parentHeight},w=${parentWidth}"
			)
		}

	}


	override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
		//layoutCells()
		layoutLegends()
	}

	private fun layoutCells() {
		val adapter = tableAdapter

		if (adapter != null) {
			var startX = (x + paddingStart).toInt()
			var startY = (y + paddingTop).toInt()

			val data = adapter.getData()

			data.forEach { d ->
				val view = adapter.getTableView(d.index)
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


	private fun layoutLegends() {
		tableAdapter?.let { adapter ->
			adapter.getLegendPanels().forEach { panel ->
				var startX = -1
				var startY = -1
				var panelHeight = 0
				var panelWidth = 0

				panel.legends.forEach { legend ->

					//с какой точки начать рисовать
					when (panel.direction) {
						ILegendPanel.Direction.LEFT -> {
							if (startX == -1) {
								startX = (x + paddingStart).toInt()
							}
							if (startY == -1) {
								startY = (y + paddingTop).toInt()
							}
						}
						else -> {}
					}

					adapter.getLegendViews(legend.id).forEach { view ->

						view.layout(startX,	startY,	startX + view.measuredWidth,startY + measuredHeight)
						if (!view.isAttachedToWindow) {
							addView(view)
						}

							//переместить курсор для строчки в списке легенд
						when (panel.direction) {
							ILegendPanel.Direction.LEFT -> {
								startY += view.measuredHeight
								panelWidth = view.measuredWidth
								panelHeight+= view.measuredHeight
							}
							else -> {}
						}
					}
					when (panel.direction) {
						ILegendPanel.Direction.LEFT -> {
							startX+= panelWidth
							startY=(y + paddingTop).toInt()
						}
						ILegendPanel.Direction.TOP -> startY+= panelHeight
						ILegendPanel.Direction.RIGHT -> startX-= panelWidth
						ILegendPanel.Direction.BOTTOM -> startY-= panelHeight
					}
				}


			}
		}
	}
}