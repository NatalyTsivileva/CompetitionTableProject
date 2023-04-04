package com.civileva.table.example.presentation.implementations.adapter

import com.civileva.table.example.data.CellInteger
import com.civileva.table.example.data.ITableCell
import com.civileva.table.example.data.Table
import com.civileva.table.example.presentation.base.holders.ILegendTableViewHolder
import com.civileva.table.example.presentation.base.holders.ITableViewHolder
import com.civileva.table.example.presentation.base.legends.ILegendPanel
import com.civileva.table.example.presentation.base.listener.ITableClickListener


class CompetitionLegendTableAdapter(
	private val table: Table<Int, CellInteger>,
	cellistener: ITableClickListener<Int, CellInteger>,
	cellHolders: List<ITableViewHolder<Int, CellInteger>>,
	legendHolderMap: Map<ILegendPanel, ILegendTableViewHolder>,
) : LegendTableAdapter<Int, CellInteger>(table, cellistener, cellHolders, legendHolderMap) {


}


/*	override fun measureLegendPanelSize(panel: ILegendPanel, size: ILegendPanel.Size) {
		if (panel.legend !is LabeledListLegend) {
			super.measureLegendPanelSize(panel, size)
		} else {
			val holder = getLegendPanelViewsHolder(panel.id)
			val views = holder?.getPanelViews()
			val firstCellView = views?.firstOrNull()
			val firstCellHeight = firstCellView?.measuredHeight


			val newItemCount = panel.legend.itemsCount-1

			views?.forEachIndexed { index, view ->
				if (index != 0) {
					measureExactlySize(view, panel, size)
				}
			}

			val newSize = getMeasuredPanelSize(panel)
			updateHolderMap(panel, newSize)
		}
	}
}*/
/*init {
		(0 until table.elementCount).forEach { index ->
			tableViews.add(createView(index))
		}
	}*//*



	*/
/*private fun createView(cellIndex: Int): View {
		val cell = table.getCell(cellIndex)
		*//*
*/
/*	return if (cell.isEnabledForInput()) {
				createInputView(cell)
			} else
				createPlaceholderView()*//*
*/
/*
	}*//*



	private fun createInputView(cellInteger: CellInteger): AppCompatEditText {
*/
/*
		val view = AppCompatEditText(context, attr)
		view.hint = "X"
		view.isFocusableInTouchMode = true
		view.gravity = Gravity.CENTER
		view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.border))
*//*


*/
/*
		view.addTextChangedListener {
			processInput(cellInteger, it)
		}

		return view*//*

	//}

	*/
/*private fun processInput(cell: CellInteger, ed: Editable?) {
		val newScore = InputUtils.safeInt(ed)

		val isFailedInput = newScore == InputUtils.FAILED_INT_INPUT

		if (!isFailedInput) {
			table.updateCellData(cell, newScore)
			updateScoreLegend(cell)
			updateRewards()
		}

		setupTextColor(cell.index, isFailedInput, ed)
	}*//*


	*/
/*private fun updateScoreLegend(cell: CellInteger) {
		val cursor = aggregateRowSum(cell.rowNumber)
		val legendViews = getLegendPanelViewsHolders(ILegendPanel.RIGHT_SCORE)
		val view = legendViews[cell.rowNumber + 1]

		val legendText = if (cursor.isRowFullFilled) {
			cursor.dataSum.toString()
		} else {
			""
		}

		(view as? AppCompatTextView)?.apply {
			text = legendText
			requestLayout()
			invalidate()
		}
	}*//*



	*/
/*fun updateRewards() {
		val legendViews = getLegendPanelViewsHolders(ILegendPanel.RIGHT_PLACE)

		val rewards = table.sortTableRows(Sorting.Direction.DESC)

		rewards?.forEach {
			val index = it.cursor.rowNumber + 1
			val view = legendViews[index]

			(view as? AppCompatTextView)?.apply {
				val place = (it.order + 1).toString()
				text = place
				requestLayout()
				invalidate()
			}
		}
	}


*/
/*
	override fun destroyTableViews() {
		tableViews.clear()
	}*//*



}*/
